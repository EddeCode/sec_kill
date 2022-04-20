package com.boo.filter;

import com.boo.entity.user.LoginUserDetails;
import com.boo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author song
 * @date 2022/4/17 10:01
 * 该过滤器用于前后端分离的项目，
 * 在该次请求中，解析jwt中的信息，
 * 并且去redis中读取消息，
 * 确认后添加到spring security的上下文信息中
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //1 先获取jwt，从cookie里
        Cookie[] cookies = request.getCookies();
        //如果cookie不存在，直接放行
        if (Objects.isNull(cookies)) {
            filterChain.doFilter(request, response);
            log.info("no cookies");
            return;
        }
        String jwt = "";
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                jwt = cookie.getValue();
                break;
            }
        }
        //2 如果不存在jwt 放行
        if (!StringUtils.hasText(jwt)) {
            //TODO 测试结束后删除日志
            filterChain.doFilter(request, response);
            log.info("no jwt in cookies");
            return;
        }
        //3 存在则解析 解析失败后放行打印日志
        String s;
        try {
            Claims claims = JwtUtil.parseJWT(jwt);
            s = claims.getSubject();
        } catch (ExpiredJwtException e) {
            // e.printStackTrace();
            log.info("JWT ExpiredJwtException");
            ResponseCookie responseCookie = ResponseCookie
                    .from("token", null)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            filterChain.doFilter(request, response);
            return;
        }
        //4 从redis中获取信息
        String userSerialization = redisTemplate.opsForValue().get("login:" + s);
        //如果redis中不存在 ...
        if (!StringUtils.hasText(userSerialization)) {
            log.info("user isn't login");
            filterChain.doFilter(request, response);
            return;
        }
        LoginUserDetails userDetails = objectMapper.readValue(userSerialization,
                LoginUserDetails.class);
        // TODO 添加权限
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
