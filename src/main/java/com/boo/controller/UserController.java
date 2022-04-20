package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.entity.user.User;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
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
    public ResponseResult login(@RequestBody User user, HttpServletResponse response) throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();
        String jwt = userService.login(user);
        map.put("jwt", jwt);
        ResponseCookie responseCookie = ResponseCookie
                .from("token", jwt)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(((int) Duration.ofDays(7).toSeconds()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
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
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseResult(200, logout);
    }

}
