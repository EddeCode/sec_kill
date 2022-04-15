package com.boo.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.UserInfo;
import com.boo.mapper.UserInfoMapper;
import com.boo.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author song
 * @Date 2022/4/11 20:54
 * @Description
 */
@Service
public class UserInfoServiceImpl
    extends ServiceImpl<UserInfoMapper,UserInfo>
        implements UserInfoService {
}
