package com.boo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * @author song
 * @Date 2022/4/10 15:16
 * @Description
 */
@RestController
public class UserController {

    @RequestMapping("/")
    public Map<String,Object> register()
    {
            return Collections.singletonMap("msg","hello");
    }

    @RequestMapping("/index")
    public String index()
    {
        return "index";
    }

    @RequestMapping("/common")
    public String common()
    {
        return "common";
    }

    @RequestMapping("/admin")
    public String admin()
    {
        return "admin";
    }

    @RequestMapping("/403")
    public Map A03(HttpServletResponse response)
    {
        response.setStatus(403);
        return  Collections.singletonMap("msg",403);
    }

    @RequestMapping("/se")
    @PreAuthorize("hasAnyAuthority('common')")
    public Map aTest()
    {
        return Collections.singletonMap("msg","se");
    }
}
