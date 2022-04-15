package com.boo.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boo.entity.UserInfo;
import com.boo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song
 * @Date 2022/4/11 20:50
 * @Description
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoService service;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = service.getOne(new QueryWrapper<UserInfo>().eq("name", username));
        if (userInfo == null) {
            throw new UsernameNotFoundException("no user");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userInfo.getRole()));
        User userDetails = new User(userInfo.getName(),
                userInfo.getPass(), authorities);
        return userDetails;
    }
}
