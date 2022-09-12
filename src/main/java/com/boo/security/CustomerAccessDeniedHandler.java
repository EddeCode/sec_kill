package com.boo.security;

import com.boo.common.ResponseResult;
import com.boo.common.utils.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author song
 * @date 2022/4/17 16:40
 */
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        ResponseResult<Object> responseResult =
                new ResponseResult<Object>(HttpStatus.FORBIDDEN.value(), "Access Denied");
        WebUtils.jsonResult(response);
        response.getWriter().print(objectMapper.writeValueAsString(responseResult));
    }
}
