package com.boo.filter;

import com.boo.entity.user.LoginUserDetails;
import com.boo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        // if(StringUtils.hasText(request.getHeader("Sec-WebSocket-Extensions")))
        // {
        //
        //     filterChain.doFilter(request, response);
        //     log.info("websocket");
        //     return;
        // }
        if (StringUtils.hasText(request.getHeader("Sec-WebSocket-Extensions")) ||
                StringUtils.hasText(request.getHeader("Sec-WebSocket-Protocol"))) {
            String header = request.getHeader("Sec-WebSocket-Protocol");
            log.info("websocket:<{}>", header);
            filterChain.doFilter(request, response);
            return;
        }
        //1 先获取header 中的 jwt
        String jwtToken = request.getHeader("token");
        if (!StringUtils.hasText(jwtToken)) {
            filterChain.doFilter(request, response);
            log.info("no token");
            return;
        }
        //2 存在则解析 解析失败后放行打印日志
        String s;
        try {
            Claims claims = JwtUtil.parseJWT(jwtToken);
            s = claims.getSubject();
        } catch (ExpiredJwtException e) {
            // e.printStackTrace();
            log.info("JWT ExpiredJwtException");
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
