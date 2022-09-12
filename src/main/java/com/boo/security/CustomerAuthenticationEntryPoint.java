package com.boo.security;

import com.boo.common.ResponseResult;
import com.boo.common.utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author song
 * @date 2022/4/17 16:47
 */
@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ResponseResult<Object> responseResult =
                new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), "Authentication Failure");
        WebUtils.jsonResult(response);
        response.getWriter().print(objectMapper.writeValueAsString(responseResult));
    }
}
