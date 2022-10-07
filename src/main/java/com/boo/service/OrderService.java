package com.boo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.config.RabbitMqConfig;
import com.boo.common.ResponseResult;
import com.boo.entity.Order;
import com.boo.entity.PdSku;
import com.boo.entity.User;
import com.boo.mapper.OrderMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author song
 * @date 2022/5/11 12:12
 */
@Service
@Slf4j
public class OrderService extends ServiceImpl<OrderMapper, Order>{

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    UserService userService;

    public List<Order> orders() {
        Long uid = userService.getUserInSecurityContext().getId();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUid, uid);
        return baseMapper.selectList(wrapper);
    }


    @Autowired
    RabbitTemplate rabbit;

    @Autowired
    IPdSkuService skuService;

    @Autowired
    Map<String, ProdService.SecTimeInfo> secCache;

    /**
     * 1分钟
     */
    private static final int ONE_MINUTE = 1000 * 60;
    private static final int CURRENT_TIME = ONE_MINUTE;


    @Transactional
    public void addOrdinaryOrder(Order order) {
        User user = userService.getUserInSecurityContext();
        order.setUid(user.getId());
        order.setOid(null);
        order.setIsPayed(0);
        order.setType(Order.Type.NORMAL.toString());
        //保存到数据库后user中会自动写入oid
        this.save(order);
        Message message;
        try {
            message = new Message(objectMapper.writeValueAsBytes(order.getOid()));
        } catch (JsonProcessingException e) {
            message = new Message(new byte[0]);
            log.error("transfer failed");
        }
        rabbit.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE,
                RabbitMqConfig.DELAY_ROUTING_KEY, message, correlationData -> {
                    correlationData.getMessageProperties().setDelay(CURRENT_TIME);
                    return correlationData;
                });
        /*
         * sku减少货存
         * redis缓存订单状态
         */
        skuService.reduceStock(order);
        try {
            redisTemplate.opsForValue().set("OID:" + order.getOid(),
                    objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            log.error("{}", e);
        }
    }

    /**
     * 秒杀成功的
     *
     * @param order
     */
    public void addSecOrder(Order order) {
        User user = userService.getUserInSecurityContext();
        order.setUid(user.getId());
        order.setOid(null);
        order.setIsPayed(0);
        order.setType(Order.Type.SEC.toString());
        //
        Message message = null;
        PdSku sku = null;
        this.save(order);
        try {
            message = new Message(objectMapper.writeValueAsBytes(order.getOid()));
            sku = objectMapper.readValue(order.getSkuSerialized(), PdSku.class);
        } catch (JsonProcessingException e) {
            log.error("transfer failed");
        }
        if (Objects.isNull(message) || Objects.isNull(sku)) {
            throw new RuntimeException("NULL MESSAGE OR SKU");
        }
        rabbit.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE,
                RabbitMqConfig.DELAY_SEC_ROUTING_KEY, message, correlationData -> {
                    correlationData.getMessageProperties().setDelay(ONE_MINUTE);
                    return correlationData;
                });
        //添加订单状态 SEC_OID
        try {
            redisTemplate.opsForValue().set("SEC_OID:" + order.getOid(),
                    objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("writeValueAsString SEC_OID");
        }

    }

    @Autowired
    RedisScript<Long> redisScript;

    /**
     * 只秒杀，成功失败未知
     *
     * @param order
     * @return
     */
    public ResponseResult snapOrder(Order order, PdSku sku) {
        /*
         先查看是否在可购买范围内
         因为是秒杀项目
         一切在redis中操作
         避免访问数据库
         */
        boolean suitableTime = judgeTime(sku.getPid());
        if (!suitableTime) {
            return new ResponseResult(201, "非法时间操作");
        }
        User user = userService.getUserInSecurityContext();
        order.setUid(user.getId());
        //    减货存"commodity:" + sku.getPid() + ":" + sku.getMask(),
        String key1 = order.getUid().toString();
        String key2 = sku.getPid().toString() + ":" + sku.getMask();
        List<String> keys = List.of(key1, key2);
        Long execute = redisTemplate.execute(redisScript, keys);
        if (execute == 2) {
            return new ResponseResult(202, "已经秒杀成功，不可重复秒杀");
        } else if (execute == 1) {
            //秒杀成功的
            addSecOrder(order);
            return new ResponseResult(200, "秒杀成功", order);
        } else {
            return new ResponseResult(203, "秒杀结束");
        }
    }

    private boolean judgeTime(Long pid) {
        /*
        1） 先访问本地缓存 没有再访问redis并且缓存到本地
        2） 对比时间
        */
        ProdService.SecTimeInfo info = secCache.get(pid.toString());
        if (Objects.isNull(info)) {
            String s = redisTemplate.opsForValue().get("sec_time:" + pid);
            try {
                info = objectMapper.readValue(s, ProdService.SecTimeInfo.class);
                secCache.put(pid.toString(), info);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        long startStp = info.getStartStp().getTime();
        long endStp = info.getEndStp().getTime();
        long now = Timestamp.from(Instant.now()).getTime();
        return startStp <= now && now < endStp;
    }

    public String getStatus(Long oid) {
        return getOrderStatus(oid, "OID:");
    }

    /**
     * @param oid 订单id
     * @return 获取订单状态
     */
    public String getSecStatus(Long oid) {
        return getOrderStatus(oid, "SEC_OID:");
    }

    /**
     * 虚假的支付模拟，后面会删掉
     *
     * @param oid 订单id
     * @throws JsonProcessingException JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void simulatePayment(Long oid) throws JsonProcessingException {
        //支付以后redis和数据库都改
        /*todo 这个方法不好，有时间可以精简一下
        不在oid里就在sec_oid里
        */
        log.info("模拟支付 oid:{}",oid);
        String odi = redisTemplate.opsForValue().get("OID:" + oid);
        Order order = null;
        if (StringUtils.hasText(odi)) {
            order = objectMapper.readValue(odi, Order.class);
            order.setIsPayed(1);
            redisTemplate.opsForValue().set("OID:" + oid, objectMapper.writeValueAsString(order));
        } else {
            log.error("未找到相关订单数据 oid:{}",oid);
            //todo 有没有可能正好现在rabbitMq也在查看数据，数据一致性
            String sec = redisTemplate.opsForValue().get("SEC_OID:" + oid);
            if (StringUtils.hasText(sec)) {
                order = objectMapper.readValue(sec, Order.class);
                order.setIsPayed(1);
                redisTemplate.opsForValue().set("SEC_OID:" + oid,
                        objectMapper.writeValueAsString(order));
            }
        }
        //修改数据库的订单状态：实时显示 redis就不用改了
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Order::getIsPayed,1).eq(Order::getOid, oid);
        update(null,updateWrapper);
        log.info("order:{}",order);
    }

    private String getOrderStatus(Long oid, String prefix) {
        String s = redisTemplate.opsForValue().get(prefix + oid);
        if (!StringUtils.hasText(s)) {
            return "expired";
        }
        try {
            Order order = objectMapper.readValue(s, Order.class);
            if (order.getIsPayed() == 1) {
                baseMapper.updateById(order);
                return "success";
            } else {
                return "fail";
            }

        } catch (JsonProcessingException e) {
            log.error("{}", e);
            return "fail";
        }
    }

    public void updateStatus(int isPayed,long oid){
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        this.update(null,wrapper.set(Order::getIsPayed,isPayed).eq(Order::getOid,oid));
    }
}
