package com.boo.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author song
 * @date 2022/4/17 16:09
 */
@Mapper
public interface MenuMapper {
    List<String> getMenuList(Long userId);
}
