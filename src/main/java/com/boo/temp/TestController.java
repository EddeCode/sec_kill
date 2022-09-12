package com.boo.temp;

import com.boo.common.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author song
 * @date 2022/4/17 11:23
 */
@RestController
@Tag(name = "测试权限用的接口")
public class TestController {
    @PreAuthorize("hasAnyAuthority('sys.scan')")
    @GetMapping("/hello")
    public ResponseResult hello() {
        return new ResponseResult(200, "ok", "秒杀接口测试");
    }

    @PreAuthorize("hasAnyAuthority('sys.del')")
    @GetMapping("/test")
    public ResponseResult test() {
        return new ResponseResult(200, "test");
    }
}
