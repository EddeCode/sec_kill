package com.boo.service;

import com.boo.entity.LoginUserDetails;
import com.boo.entity.User;
import com.boo.mapper.MenuMapper;
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

        User user = service.getOneByNameOrEmail(username);
        if (Objects.isNull(user)) {
            log.info("can't find user");
            throw new UsernameNotFoundException("can't find user");
        }
        List<String> list = mapper.getMenuList(user.getId());
        if(list.isEmpty()){
            throw new RuntimeException("无权限");
        }
        List<String> roleList = mapper.getRoleList(user.getId());
        list.addAll(roleList);
        return new LoginUserDetails(user, list);

    }

}
