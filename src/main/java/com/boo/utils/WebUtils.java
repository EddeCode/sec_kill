package com.boo.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author song
 * @date 2022/4/17 16:55
 */
public class WebUtils {
    public static void jsonResult(HttpServletResponse response) {
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

    }

    //    验证邮箱格式
    public static boolean checkEmail(String email) {
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regex);
    }
}
