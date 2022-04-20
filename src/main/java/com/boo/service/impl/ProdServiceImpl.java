package com.boo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.Param;
import com.boo.entity.Product;
import com.boo.entity.SubParam;
import com.boo.mapper.ProductMapper;
import com.boo.service.ProdService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map<String, List<SubParam>>> getParamsMap(Long pid) {
        List<Param> params = baseMapper.getAllParamNameByPid(pid);
        List<Map<String, List<SubParam>>> maps = new LinkedList<>();
        for (Param param : params) {
            Map<String, List<SubParam>> listMap = new HashMap<>();
            List<SubParam> allParamsById = baseMapper.getAllParamsById(param.getId());
            listMap.put(param.getParamName(), allParamsById);
            maps.add(listMap);
        }
        return maps;
    }
}
