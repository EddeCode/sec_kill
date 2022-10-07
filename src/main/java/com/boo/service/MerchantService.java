package com.boo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.Merchant;
import com.boo.entity.Product;
import com.boo.entity.User;
import com.boo.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MerchantService extends ServiceImpl<MerchantMapper, Merchant> {


    @Autowired
    UserService userService;

    @Autowired
    ProdService prodService;

    public Merchant getMerchantByUser(User user) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getUid, user.getId());
        return baseMapper.selectOne(wrapper);
    }

    public List<Product> getMyPds() {
        User user = userService.getUserInSecurityContext();
        Merchant merchant = getMerchantByUser(user);
        return prodService.getMerchantPds(merchant);
    }

    public Product aplNewPd(Product product) {
        product.setStatus(false);
        User user = userService.getUserInSecurityContext();
        Merchant merchant = getMerchantByUser(user);
        product.setMid(merchant.getId());
        prodService.save(product);
        return product;
    }

    public void replaceImg(Product product) {
        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Product::getImg,product.getImg());
        updateWrapper.eq(Product::getId,product.getId());
        //第一个参数一定要设置为null，这样就只会更新需要更新的字段
        prodService.update(null,updateWrapper);
    }


}
