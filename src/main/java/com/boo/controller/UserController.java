package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.entity.user.LoginUserDetails;
import com.boo.entity.user.User;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author song
 * @Date 2022/4/10 15:16
 * @Description
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();
        String jwt = userService.login(user);
        map.put("jwt", jwt);
        return new ResponseResult(200, "登录成功", map);
    }

    @RequestMapping("/register")
    public ResponseResult register(@RequestBody User user) {

        return userService.register(user, user.getCode());
    }

    @RequestMapping("/getCode")
    public ResponseResult code(String email) {
        return userService.getCode(email);
    }

    @RequestMapping("/logout")
    public ResponseResult logout(HttpServletResponse response) {
        String logout = userService.logout();
        return new ResponseResult(200, logout);
    }


    @RequestMapping("/avatar")
    public ResponseResult uploadAvatar(@RequestPart("headerImg") MultipartFile avatar) throws IOException {
        boolean b = userService.saveOrUpdateAvatar(avatar.getBytes());
        avatar.transferTo(new File("d:/test.jpg"));
        return new ResponseResult(200, "ok", b);
    }

}
