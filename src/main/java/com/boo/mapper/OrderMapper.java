package com.boo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.prod.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
