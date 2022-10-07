package com.boo.service;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.common.ResponseResult;
import com.boo.common.utils.JwtUtil;
import com.boo.entity.LoginUserDetails;
import com.boo.entity.User;
import com.boo.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

/**
 * @author song
 * @Date 2022/4/11 20:54
 * @Description
 */
@Service
public class UserService
        extends ServiceImpl<UserMapper, User> {
    public User getOneByNameOrEmail(String userNameOrEmail) {
        //新知识
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, userNameOrEmail)
                .or()
                .eq(User::getEmail, userNameOrEmail);
        return baseMapper.selectOne(wrapper);
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PasswordEncoder encoder;

    public String login(User user)  {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("login failed");
        }

        LoginUserDetails loginUser = (LoginUserDetails) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        user.setId(id);
        try {
            redisTemplate.opsForValue().set("login:" + id, mapper.writeValueAsString(loginUser),
                    Duration.ofDays(7));
        } catch (JsonProcessingException e) {
            //登录异常处理器
            e.printStackTrace();
        }
        return JwtUtil.createJWT(id.toString());
    }

    public String logout() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        Long id = loginUserDetails.getUser().getId();
        redisTemplate.delete("login:" + id);
        return "id:" + id + "注销成功";
    }

    public ResponseResult register(User user, String code) {

        //参数不存在
        if (Objects.isNull(user.getUserName()) || Objects.isNull(user.getPassword())) {
            return new ResponseResult(403, "Parameter Error");
        }
        //邮箱格式错误
        if (!Validator.isEmail(user.getEmail())) {
            return new ResponseResult(403, "Wrong Email");
        }

        //用户名和邮箱都唯一才能注册
        LambdaQueryWrapper<User> wrapperEmail = new LambdaQueryWrapper<>();
        wrapperEmail.eq(User::getEmail, user.getEmail());
        User one = this.getOne(wrapperEmail);
        if (!Objects.isNull(one)) {
            return new ResponseResult(403, "该邮箱已注册");
        }
        LambdaQueryWrapper<User> wrapperName = new LambdaQueryWrapper<>();
        wrapperName.eq(User::getUserName, user.getUserName());
        User two = this.getOne(wrapperName);
        if (!Objects.isNull(two)) {
            return new ResponseResult(403, "该用户已存在");
        }
        //获取验证码
        String s = redisTemplate.opsForValue().get("register:" + user.getEmail());
        if (Objects.isNull(s) || !StringUtils.hasText(s)) {
            return new ResponseResult(403, "Please Get Verification " +
                    "Code");
        }
        //验证码是否正确
        if (!s.equals(code)) {
            System.out.println("s" + s);
            System.out.println("code" + code);
            return new ResponseResult(403, "Wrong Code", s);
        }
        //一切顺利
        user.setPassword(encoder.encode(user.getPassword()));
        boolean save = this.save(user);

        return new ResponseResult(200, "注册成功");
    }

    public ResponseResult getCode(String email) {
        if (Objects.isNull(email)) {
            return new ResponseResult(200, "Null Email");
        }
        if (!Validator.isEmail(email)) {
            return new ResponseResult(200, "Wrong Email");
        }

        String key = "register:" + email;
        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(s)) {
            return new ResponseResult(200, "请勿重复获取验证码");
        }

        StringBuilder rand = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            rand.append(new Random().nextInt(9));
        }

        redisTemplate.opsForValue().set(key, rand.toString(), Duration.ofDays(1));
        //TODO 发送邮箱 不返回验证码
        return new ResponseResult(200, "ok", rand.toString());
    }

    public User getUserInSecurityContext() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        return loginUserDetails.getUser();
    }

    public long getUidInSecurityContext() {
        return getUserInSecurityContext().getId();
    }


    public User getUserInfo() {
        return getUserInSecurityContext();
    }
}
