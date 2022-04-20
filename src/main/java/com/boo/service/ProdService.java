package com.boo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.Param;
import com.boo.entity.Product;
import com.boo.entity.SubParam;

import java.util.List;
import java.util.Map;

public interface ProdService extends IService<Product> {
    List<Product> summaryList();
    List<Map<String,List<SubParam>>> getParamsMap(Long pid);
}
