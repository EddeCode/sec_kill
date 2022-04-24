package com.boo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author song
 * @date 2022/4/18 11:33
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT id,prod_name,img,price,sec_flag from sys_prod")
    List<Product> getSummaryProducts();




}
