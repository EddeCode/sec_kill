package com.boo.consumer;

import com.boo.config.RabbitMqConfig;
import com.boo.entity.Order;
import com.boo.service.OrderService;
import com.boo.entity.PdSku;
import com.boo.service.IPdSkuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author song
 * @date 2022/4/30 11:10
 */
@Component
@Slf4j
public class OrderConsumer {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    IPdSkuService pdSkuService;

    /**
     * 查看定时订单是否失效
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMqConfig.DELAY_QUEUE)
    void watchOrderStatus(Message message) throws IOException {
        Long oid = objectMapper.readValue(message.getBody(), Long.class);
        // log.info("该订单创建时间为：{}",order.getDateTime());
        String orderKey = "OID:" + oid;
        String orderJson = redisTemplate.opsForValue().get(orderKey);
        if (!StringUtils.hasText(orderJson)) {
            log.error("get no value by key <{}>", orderKey);
            return;
        }
        redisTemplate.delete(orderKey);
        Order order = objectMapper.readValue(orderJson, Order.class);
        if (order.getIsPayed() == 0L) {
            log.info("{}:未支付",order);
            //    还未支付 设置为已过时 还原订单
            orderService.updateStatus(-1,order.getOid());
            pdSkuService.revertStock(order);
        }else{
            log.info("{}:已支付",order);
        }
    }



    private String notPayedScript;

    @PostConstruct
    public void setNotPayedScript() throws IOException {
        //加载类路径下的脚本文件
        ClassPathResource classPathResource = new ClassPathResource("not_payed.lua");
        InputStream inputStream = classPathResource.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        notPayedScript = new String(bytes);
    }

    /**
     * 秒杀项目
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMqConfig.DELAY_SEC_QUEUE)
    void watchSecOrderStatus(Message message, Channel channel) throws IOException {
        Long oid = objectMapper.readValue(message.getBody(), Long.class);
        String orderKey = "SEC_OID:" + oid;
        String orderJson = redisTemplate.opsForValue().getAndDelete(orderKey);
        //redis中不存在
        if (!StringUtils.hasText(orderJson)) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.error("get nol value by key <{}>", orderKey);
            return;
        }
        Order order = objectMapper.readValue(orderJson, Order.class);
        if (order.getIsPayed() == 0L) {
            PdSku sku = null;
            try {
                sku = objectMapper.readValue(order.getSkuSerialized(), PdSku.class);
            } catch (JsonProcessingException e) {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                log.error("transfer failed");
            }
            if (Objects.isNull(sku)) {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                throw new RuntimeException("NULL SKU");
            }
            //还未支付 设置为已过时 redis货存增加
            order.setIsPayed( -1);
            String skuInfo = sku.getPid().toString() + ":" + sku.getMask();
            redisTemplate.execute(RedisScript.of(notPayedScript),
                    List.of(order.getUid().toString(), skuInfo, order.getOid().toString()));
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}