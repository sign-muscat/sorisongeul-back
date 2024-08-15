package com.sorisonsoon.common.security.hadler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.common.exception.dto.response.ErrorResponse;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.fail(new ErrorResponse(ExceptionCode.ACCESS_DENIED)))
        );
    }
}
