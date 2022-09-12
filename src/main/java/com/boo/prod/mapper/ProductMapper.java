package com.boo.prod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.prod.entity.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author song
 * @date 2022/4/18 11:33
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @MapKey(value = "")
    List<Map<String, Object>> getProdListAndMinPrice();


}
