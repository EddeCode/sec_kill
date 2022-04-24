package com.boo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boo.entity.user.User;
import com.boo.mapper.user.MenuMapper;
import com.boo.mapper.ProductMapper;
import com.boo.service.ProdService;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Map;

@SpringBootTest
class SecKillApplicationTests {

    @Autowired
    UserService service;

    @Test
    void contextLoads() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        User id = service.getOne(queryWrapper.eq("id", 1));
        System.out.println(id.getEmail());
    }

    @Autowired
    MenuMapper mapper;

    @Test
    void sqlTest() {
        System.out.println(mapper.getMenuList(2L));
    }

    @Autowired
    ProductMapper productMapper;

    @Test
    void pTest() {
        System.out.println(productMapper.getSummaryProducts());
    }
    @Test
    void spTest()
    {
        Map<String, String> specParams = Map.of("size","1","color","2");
        Map<String, String> stringStringMap = Map.of("size","1","color","1");
        System.out.println(specParams.equals(stringStringMap));
    }

    @Autowired
    ProdService prodService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 测试获取一个产品所有参数
     */
    @Test
    void testMaps() throws JsonProcessingException {

    }

    @Autowired
    DefaultRedisScript<Long> defaultRedisScript;
    @Test
    void res()
    {
        System.out.println(defaultRedisScript.getScriptAsString());
    }
}
