package com.boo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boo.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author song
 * @Date 2022/4/10 15:23
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
