package com.boo.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.user.LoginUserDetails;
import com.boo.entity.ResponseResult;
import com.boo.entity.user.User;
import com.boo.mapper.user.UserMapper;
import com.boo.service.user.UserService;
import com.boo.utils.JwtUtil;
import com.boo.utils.WebUtils;
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
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Override
    public User getOneByNameOfEmail(String userNameOrEmail) {
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

    @Override
    public String login(User user) throws JsonProcessingException {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("login failed");
        }

        LoginUserDetails loginUser = (LoginUserDetails) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        redisTemplate.opsForValue().set("login:" + id, mapper.writeValueAsString(loginUser),
                Duration.ofDays(7));
        return JwtUtil.createJWT(id.toString());
    }

    @Override
    public String logout() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        Long id = loginUserDetails.getUser().getId();
        redisTemplate.delete("login:" + id);
        return "注销成功";
    }

    @Override
    public ResponseResult register(User user, String code) {

        //参数不存在
        if (Objects.isNull(user.getUserName()) || Objects.isNull(user.getPassword())) {
            return new ResponseResult(403, "Parameter Error");
        }
        //邮箱格式错误
        if (!WebUtils.checkEmail(user.getEmail())) {
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

    @Override
    public ResponseResult getCode(String email) {
        if (Objects.isNull(email)) {
            return new ResponseResult(200, "Null Email");
        }
        if (!WebUtils.checkEmail(email)) {
            return new ResponseResult(200, "Wrong Email");
        }

        String key = "register:" + email;
        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(s)) {
            return new ResponseResult(200, "请勿重复获取验证码");
        }

        String rand = "";
        for (int i = 0; i < 6; i++) {
            rand += new Random().nextInt(9);
        }

        redisTemplate.opsForValue().set(key, rand, Duration.ofDays(1));
        //TODO 发送邮箱 不返回验证码
        return new ResponseResult(200, "ok", rand);
    }

}
