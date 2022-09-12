package com.boo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author song
 * @Date 2022/4/10 15:23
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
