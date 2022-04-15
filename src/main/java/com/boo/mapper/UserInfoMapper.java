package com.boo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author song
 * @Date 2022/4/10 15:23
 * @Description
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
