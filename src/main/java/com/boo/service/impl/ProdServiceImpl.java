package com.boo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.prod.Merchant;
import com.boo.entity.prod.PdSku;
import com.boo.entity.prod.Product;
import com.boo.entity.user.User;
import com.boo.mapper.ProductMapper;
import com.boo.service.IPdSkuService;
import com.boo.service.ProdService;
import com.boo.service.user.IOrderService;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.security.AccessControlException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author song
 * @date 2022/4/19 16:00
 */
@Service
@Slf4j
public class ProdServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProdService {

    @Override
    public List<Product> summaryList() {
        List<Map<String, Object>> list = baseMapper.getProdListAndMinPrice();
        List<Product> products = new LinkedList<>();
        list.forEach(map -> {
            products.add(
                    new Product(
                            Long.parseLong(map.get("id").toString()),
                            null,
                            map.get("prod_name").toString(),
                            true,
                            Boolean.parseBoolean(map.get("sec_flag").toString()),
                            Timestamp.valueOf(map.get("create_time").toString()),
                            Double.parseDouble(map.get("price").toString()),
                            map.get("img").toString()
                    ));
        });
        return products;
    }

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    DefaultRedisScript<Long> defaultRedisScript;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IPdSkuService skuService;
    @Autowired
    UserService service;
    @Autowired
    IOrderService orderService;
    @Autowired
    UserService userService;

    /**
     * 2 min
     */
    private static final int DELAY = 1000 * 5;


    /**
     * ??????????????????????????????redis???
     *
     * @return ???????????????
     */
    @Override
    public Integer cacheSecProduct() {
        int count = 0;
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getSecFlag, true)
                .eq(Product::getStatus, true)
                .select(Product::getId);
        List<Product> products = this.list(queryWrapper);
        for (Product pd : products) {
            List<PdSku> skus = skuService.getSkuByPid(pd.getId());
            count += skus.size();
            skus.forEach(sku -> {
                redisTemplate.opsForValue().set(
                        "commodity:" + sku.getPid() + ":" + sku.getMask(),
                        sku.getStock().toString()
                );
            });
        }
        return count;
    }




    @Override
    public List<Product> getMerchantPds(Merchant merchant) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getMid, merchant.getId());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean getSecFlagByPid(Long pid) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getId, pid).select(Product::getSecFlag);
        Product one = this.getOne(queryWrapper);
        return one.getSecFlag();
    }

    @Override
    public void setSecTime(SecTimeInfo secTimeInfo) {

        /*
        ??????????????????????????????????????????????????????
        ????????????????????????
        secFlag true ?????????????????? false ???????????????
        */
        boolean b = skuService.checkEditorByPid(secTimeInfo.getPid());
        if (!b) {
            //TODO AOP
            throw new AccessControlException("????????????");
        }
        if (secTimeInfo.isSecFlag()) {
            try {
                redisTemplate.opsForValue().set("sec_time:" + secTimeInfo.getPid(),
                        objectMapper.writeValueAsString(secTimeInfo));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Product::getId, secTimeInfo.getPid()).set(Product::getSecFlag,
                    secTimeInfo.isSecFlag());
            this.update(null, updateWrapper);
        }
    }

    @Override
    public String getSecTime(long pid) {
        return redisTemplate.opsForValue().get("sec_time:" + pid);
    }


}
