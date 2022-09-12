package com.boo.merchant.service;

import com.boo.merchant.entity.Merchant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.prod.entity.Product;
import com.boo.user.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
public interface IMerchantService extends IService<Merchant> {

    Merchant getMerchantByUser(User user);

    List<Product> getMyPds();

    Product aplNewPd(Product product);

    void replaceImg(Product product);
}
