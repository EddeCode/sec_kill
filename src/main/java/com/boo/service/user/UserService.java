package com.boo.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.ResponseResult;
import com.boo.entity.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author 宋
 */
public interface UserService extends IService<User> {
    User getOneByNameOfEmail(String userNameOrEmail);

    String login(User user) throws JsonProcessingException;

    String logout();

    ResponseResult register(User user, String code);

    ResponseResult getCode(String email);

}
