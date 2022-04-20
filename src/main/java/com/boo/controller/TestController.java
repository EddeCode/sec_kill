package com.boo.controller;

import com.boo.entity.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author song
 * @date 2022/4/17 11:23
 */
@RestController
public class TestController {
    // @PreAuthorize("hasAnyAuthority('sys.non')")
    @RequestMapping("/hello")
    public ResponseResult hello() {
        return new ResponseResult(200, "ok", "秒杀接口测试");
    }

    @PreAuthorize("hasAnyAuthority('sys.del')")
    @RequestMapping("/test")
    public ResponseResult test() {
        return new ResponseResult(200, "test");
    }
}
