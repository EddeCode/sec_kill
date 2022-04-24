package com.boo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.Product;
import com.boo.entity.ResponseResult;
import com.boo.entity.user.User;
import com.boo.mapper.ProductMapper;
import com.boo.service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * @author song
 * @date 2022/4/19 16:00
 */
@Service
public class ProdServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProdService {

    @Override
    public List<Product> summaryList() {
        return baseMapper.getSummaryProducts();
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    DefaultRedisScript<Long> defaultRedisScript;


    @Override
    public ResponseResult snapUp(User user, Long pid) {
        Long execute = redisTemplate.execute(defaultRedisScript, List.of(user.getId().toString(),
                pid.toString()));
        if (execute == 0L) {
            return new ResponseResult(300,"秒杀结束");
        }
        if (execute == 1L) {
            return new ResponseResult(200,"秒杀成功");
        }
        if (execute == 2L) {
            return new ResponseResult(300,"已经购买，请勿重复点击");
        }
        return new ResponseResult(403,"网络出问题啦~");
    }

    @Override
    public Integer cacheSecProduct() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, true).eq(Product::getSecFlag, true);
        List<Product> secProducts = baseMapper.selectList(wrapper);
        secProducts.forEach(product -> {
            redisTemplate.opsForValue().set(
                    "commodity:" + product.getId(),
                    product.getStock().toString(),
                    Duration.ofDays(1)
            );
        });
        return secProducts.size();
    }
}
