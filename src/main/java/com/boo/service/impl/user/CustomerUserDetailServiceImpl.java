package com.boo.service.impl.user;

import com.boo.entity.user.LoginUserDetails;
import com.boo.entity.user.User;
import com.boo.mapper.user.MenuMapper;
import com.boo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author song
 * @date 2022/4/16 8:41
 */
@Service
@Slf4j
public class CustomerUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Autowired
    MenuMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = service.getOneByNameOfEmail(username);
        if (Objects.isNull(user)) {
            log.info("can't find user");
            throw new UsernameNotFoundException("can't find user");
        }
        log.info("get user success;username:{};password:{}", user.getUserName(),
                user.getPassword());
        List<String> list = mapper.getMenuList(user.getId());
        return new LoginUserDetails(user, list);

    }
}
