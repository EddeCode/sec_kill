package com.boo.service;

import com.boo.entity.prod.Merchant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.prod.Product;
import com.boo.entity.user.User;

import javax.security.sasl.AuthenticationException;
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
