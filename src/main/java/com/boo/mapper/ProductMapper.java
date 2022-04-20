package com.boo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.Param;
import com.boo.entity.Product;
import com.boo.entity.SubParam;
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
    @Select("SELECT id,prod_name from sys_prod")
    List<Product> getSummaryProducts();

    List<Param> getAllParamNameByPid(Long pid);



    List<SubParam> getAllParamsById(Long id);
}
