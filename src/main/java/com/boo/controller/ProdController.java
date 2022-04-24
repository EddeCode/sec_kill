package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.entity.user.LoginUserDetails;
import com.boo.service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author song
 * @date 2022/4/19 15:57
 */
@RestController
public class ProdController {

    @Autowired
    private ProdService prodService;

    @PreAuthorize("hasAuthority('sys.update')")
    @RequestMapping("/cache")
    public ResponseResult cacheSecProd()
    {
        Integer num = prodService.cacheSecProduct();
        return new ResponseResult(200,"ok",num);
    }

    @RequestMapping("/prods")
    public ResponseResult getAllProductSummary() {
        return new ResponseResult(200, "ok", prodService.summaryList());
    }

    @RequestMapping("/prods/{id}")
    public ResponseResult getSpecProd(@PathVariable("id") Long id) {
        return new ResponseResult(200, "ok", prodService.getById(id));
    }

    @RequestMapping("/prods/snap/{pid}")
    public ResponseResult snapUp(@PathVariable Long pid) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
        ResponseResult msg = prodService.snapUp(userDetails.getUser(),pid);
        return msg;
    }



}
