package com.sorisonsoon.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.common.security.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenUtils tokenUtils;

    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/v1/login", "POST"));
        log.debug("CustomAuthenticationFilter 초기화됨");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("인증 시도 중...");
        log.debug("요청에 대한 인증 시도: {}", request.getRequestURI());

        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            log.error("잘못된 콘텐츠 타입: {}", contentType);
            throw new AuthenticationServiceException(ExceptionCode.INVALID_CREDENTIALS.getMessage());
        }

        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);

        String id = bodyMap.get("id");
        String password = bodyMap.get("password");

        log.debug("아이디: {}", id);
        log.debug("비밀번호: {}", password); // 패스워드는 노출하지 않는 것이 좋지만, 일단은 디버깅을 위해 로그에 추가

        if (id == null || password == null) {
            log.error("요청 본문에 아이디 또는 비밀번호가 없습니다.");
            throw new AuthenticationServiceException(ExceptionCode.INVALID_CREDENTIALS.getMessage());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        try {
            Authentication authResult = this.getAuthenticationManager().authenticate(authenticationToken);
            log.debug("인증 결과: {}", authResult);
            return authResult;
        } catch (AuthenticationException ex) {
            log.error("인증 실패: {}", ex.getMessage());
            throw new AuthenticationServiceException(ExceptionCode.INVALID_CREDENTIALS.getMessage());
        }
    }

}
