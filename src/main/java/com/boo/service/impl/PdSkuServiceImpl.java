package com.boo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.prod.Order;
import com.boo.entity.prod.PdSku;
import com.boo.entity.prod.Product;
import com.boo.entity.user.User;
import com.boo.mapper.PdSkuMapper;
import com.boo.mapper.ProductMapper;
import com.boo.service.IPdSkuService;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Service
@Slf4j
public class PdSkuServiceImpl extends ServiceImpl<PdSkuMapper, PdSku> implements IPdSkuService {

    @Override
    public List<PdSku> getSkuByPid(Long pid) {
        LambdaQueryWrapper<PdSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PdSku::getPid, pid);
        return baseMapper.selectList(wrapper);
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void reduceStock(Order order) {
        /*
        1 先查出订单 乐观锁插件
        2 修改订单
        */
        LambdaQueryWrapper<PdSku> queryWrapper = new LambdaQueryWrapper<>();
        PdSku t = null;
        try {
            t = objectMapper.readValue(order.getSkuSerialized(), PdSku.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert t != null;
        queryWrapper.eq(
                PdSku::getPid, t.getPid()).eq(
                PdSku::getMask,
                t.getMask());
        PdSku sku = baseMapper.selectOne(queryWrapper);
        sku.setStock(sku.getStock() - order.getNum());
        UpdateWrapper<PdSku> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(PdSku::getPid, sku.getPid())
                .eq(PdSku::getMask, sku.getMask());
    }

    @Override
    public void revertStock(Order order) {
        PdSku pdSku = null;
        try {
            pdSku = objectMapper.readValue(order.getSkuSerialized(), PdSku.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        pdSku.setStock(pdSku.getStock());
        UpdateWrapper<PdSku> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(PdSku::getPid, pdSku.getPid())
                .eq(PdSku::getMask, pdSku.getMask());
        int update = baseMapper.update(pdSku, updateWrapper);
    }

    @Override
    public boolean getModifiable(Long pid) {
        LambdaQueryWrapper<PdSku> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PdSku::getPid, pid);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count == 0;
    }


    @Override
    public int getSecStock(String pid, String mask) {
        String s = redisTemplate.opsForValue().get("commodity:" + pid + ":" + mask);
        if (StringUtils.hasText(s)) {
            return Integer.parseInt(s);
        }
        return 0;
    }

    @Autowired
    UserService userService;

    /**
     * 防止恶意更改
     *
     * @param pid pid
     * @return true 合法 false 不合法
     */
    @Override
    public boolean checkEditorByPid(Long pid) {
        long uid = baseMapper.getUidByPid(pid);
        User user = userService.getUserInSecurityContext();
        return uid == user.getId();
    }

    @Override
    public void updateSecurity(PdSku sku) {
        //防止恶意更改
        if (!checkEditorByPid(sku.getPid())) {
            return;
        }
        LambdaUpdateWrapper<PdSku> updateWrapper = new LambdaUpdateWrapper<>();
        //缓存在redis中
        if (getSecFlagByPid(sku.getPid())) {
            redisTemplate.opsForValue().set(
                    "commodity:" + sku.getPid() + ":" + sku.getMask(),
                    sku.getStock().toString()
            );
        } else {
            updateWrapper.set(PdSku::getStock, sku.getStock());
        }
        updateWrapper.set(PdSku::getImg, sku.getImg());
        updateWrapper.set(PdSku::getPrice, sku.getPrice());
        updateWrapper.eq(PdSku::getPid, sku.getPid());
        updateWrapper.eq(PdSku::getMask, sku.getMask());
        this.update(null, updateWrapper);
    }

    @Autowired
    ProductMapper productMapper;

    @Override
    public boolean getSecFlagByPid(long pid) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getId, pid);
        wrapper.select(Product::getSecFlag);
        return productMapper.selectOne(wrapper).getSecFlag();
    }
}
