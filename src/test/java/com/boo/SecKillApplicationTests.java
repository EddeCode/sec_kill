package com.boo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boo.entity.UserInfo;
import com.boo.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecKillApplicationTests {

    @Autowired
    UserInfoService service;

    @Test
    void contextLoads() {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
        UserInfo id = service.getOne(queryWrapper.eq("id", 1));
        System.out.println(id.getName());
    }

}
