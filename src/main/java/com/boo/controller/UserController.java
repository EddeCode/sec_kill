package com.boo.controller;

import com.boo.common.ResponseResult;
import com.boo.entity.Order;
import com.boo.service.OrderService;
import com.boo.entity.LoginUserDetails;
import com.boo.entity.User;
import com.boo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * @author song
 * @Date 2022/4/10 15:16
 * @Description
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "UserController", description = "用户相关接口")
public class UserController {


    @Autowired
    UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        HashMap<String, Object> map = new HashMap<>();
        String jwt = userService.login(user);
        map.put("jwt", jwt);
        map.put("uid", user.getId());
        return new ResponseResult(200, "登录成功", map);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        //TODO 验证码 PS 不需要鉴权的且访问数据库的接口好好考虑一下
        return userService.register(user, user.getCode());
    }

    @Operation(summary = "用户注册时获取验证码接口")
    @GetMapping("/getCode")
    public ResponseResult code(String email) {
        return userService.getCode(email);
    }

    @Operation(summary = "用户注销接口")
    @GetMapping("/logout")
    public ResponseResult logout(HttpServletResponse response) {
        String logout = userService.logout();
        return new ResponseResult(200, logout);
    }


    @Value("${file-storage-directory}")
    String filePath;

    @Operation(summary = "用户上传头像")
    @RequestMapping("/avatar")
    public ResponseResult uploadAvatar(@RequestPart("headerImg") MultipartFile avatar) throws IOException {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        User user = loginUserDetails.getUser();
        String contentType = avatar.getContentType();
        log.info("{}", contentType);
        avatar.transferTo(new File(filePath + "/avatar/" + user.getId() + ".jpg"));
        return new ResponseResult(200, "ok");
    }

    @Operation(summary = "获取用户头像", description = "不需要权限")
    @GetMapping("/getAvatar/{uid}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String uid) {
        //http header的作用是告诉浏览器，这个文件是什么类型的，以及文件的名字
        HttpHeaders httpHeaders = new HttpHeaders();
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Path.of(filePath + "/avatar/" + uid + ".jpg"));
        } catch (IOException e) {
            log.warn("no avatar found");
        }
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes,
                httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public ResponseResult getUserInfo() {
        User userInfo = userService.getUserInfo();
        userInfo.setPassword(null);
        return new ResponseResult(200, "ok", userInfo);
    }

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('sys.scan')")
    @GetMapping("/getOrders")
    public ResponseResult getUserOrders() {
        List<Order> orders = orderService.orders();
        return new ResponseResult(200, "ok", orders);
    }


}
