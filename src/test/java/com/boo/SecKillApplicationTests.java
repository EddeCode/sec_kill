package com.boo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boo.entity.Order;
import com.boo.service.OrderService;
import com.boo.entity.User;
import com.boo.mapper.PdClassMapper;
import com.boo.mapper.PdSkuMapper;
import com.boo.mapper.ProductMapper;
import com.boo.mapper.MenuMapper;
import com.boo.service.IPdClassService;
import com.boo.service.ProdService;
import com.boo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        System.out.println(mapper.getMenuList(1L));
    }

    @Autowired
    ProductMapper productMapper;

    @Test
    void pTest() {
        System.out.println(prodService.summaryList());
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
    void res() {
        System.out.println(defaultRedisScript.getScriptAsString());
    }


    @Test
    void parse() throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("msg", null);
        System.out.println(objectMapper.writeValueAsString(map));
    }


    @Autowired
    IPdClassService pdClassService;

    @Test
    void ll() {
        System.out.println(pdClassService.getClassListByPid(1L));
    }

    @Autowired
    OrderService orderService;

    @Test
    void testOrder() {
        System.out.println(orderService.list());
    }

    @Autowired
    PdClassMapper pdClassMapper;

    @Test
    void testGetClassJoinTagByPid() {
        System.out.println(pdClassMapper.getClassJoinTagByPid(1L));
    }

    @Autowired
    PdSkuMapper skuMapper;

    /**
     * IPdClassService.getClassListByPid
     * 测试一次访问数据库完成映射转换
     */
    @Test
    void testGetClassListByPid() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(pdClassService.getClassListByPid(1L)));
    }

    /**
     * ProdServiceImpl.summaryList
     */
    @Test
    void testSummaryList(){
        System.out.println(prodService.summaryList());
    }

    @Autowired
    MenuMapper menuMapper;
    /**
     * MenuMapper.getRoleList
     */
    @Test
    void testMenuMapper(){
        System.out.println(menuMapper.getRoleList(1L));
    }

    @Autowired
    UserService userService;
    @Test
    void testPage(){
        Page<User> page = userService.page(new Page<User>(1, 10));
        System.out.println(page);
    }

    @Test
    void testE(){
        System.out.println(Order.Type.NORMAL);
    }
}
