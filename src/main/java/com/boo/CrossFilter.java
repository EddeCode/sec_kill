package com.boo;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author song
 * @Date 2022/4/13 8:56
 * @Description
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class CrossFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = request.getHeader("Origin");
        if(origin!=null)
        {
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
        filterChain.doFilter(request,response);
        log.info(origin);
    }
}
