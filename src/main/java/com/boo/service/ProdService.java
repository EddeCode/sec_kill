package com.boo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.Product;
import com.boo.entity.ResponseResult;
import com.boo.entity.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface ProdService extends IService<Product> {
    List<Product> summaryList();

    ResponseResult snapUp(User user, Long pid);

    Integer cacheSecProduct() ;
}
