package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.entity.user.LoginUserDetails;
import com.boo.entity.user.User;
import com.boo.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * @author song
 * @Date 2022/4/10 15:16
 * @Description
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();
        String jwt = userService.login(user);
        map.put("jwt", jwt);
        map.put("uid", user.getId());
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


    @Value("${file-storage-directory}")
    String filePath;

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

    @RequestMapping("/getAvatar/{uid}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String uid) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        byte[] bytes = Files.readAllBytes(Path.of(filePath + "/avatar/" + uid + ".jpg"));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes,
                httpHeaders, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("/info")
    public ResponseResult getUserInfo()
    {
        User userInfo = userService.getUserInfo();
        userInfo.setPassword(null);
        return  new ResponseResult(200,"ok",userInfo);
    }


}
