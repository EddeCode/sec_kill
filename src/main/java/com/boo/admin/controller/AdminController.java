package com.boo.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boo.common.ResponseResult;
import com.boo.merchant.entity.Merchant;
import com.boo.user.entity.User;
import com.boo.merchant.service.IMerchantService;
import com.boo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 可以后面解耦顺便回顾spring security
 *
 * @author song
 * @date 2022/5/22 20:47
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
@Tag(name = "AdminController", description = "系统后台管理，使用另一套前端")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    IMerchantService merchantService;

    @Operation(summary = "获取某页用户信息")
    @GetMapping("/getUsers/{pageId}")
    public ResponseResult getUsers(@PathVariable int pageId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getId, User::getUserName, User::getStatus);
        Page<User> page = userService.page(new Page<>(pageId, 10), wrapper);
        return new ResponseResult(200, "users", page);
    }
    @Operation(summary = "获取某页商店信息")
    @GetMapping("/getMerchants/{pageId}")
    public ResponseResult getMerchants(@PathVariable int pageId){
        Page<Merchant> page = merchantService.page(new Page<>(pageId, 10));
        return new ResponseResult(200,"merchants",page);
    }
}
