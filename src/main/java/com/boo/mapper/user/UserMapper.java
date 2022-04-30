package com.boo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.servlet.annotation.HandlesTypes;
import java.sql.Blob;

/**
 * @author song
 * @Date 2022/4/10 15:23
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {




}
