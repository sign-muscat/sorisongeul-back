package com.sorisonsoon.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.domain.type.CustomUser;
import jakarta.servlet.FilterChain;
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
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenUtils tokenUtils;

    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/v1/login", "POST"));
        log.debug("CustomAuthenticationFilter initialized");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.debug("Attempting authentication...");
        log.debug("Attempting authentication for request: {}", request.getRequestURI());

        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            log.error("Invalid content type: {}", contentType);
            throw new AuthenticationServiceException("Content-Type not supported");
        }

        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);

        String id = bodyMap.get("id");
        String password = bodyMap.get("password");

        log.debug("ID: {}", id);
        log.debug("Password: {}", password); // 패스워드는 노출하지 않는 것이 좋지만, 일단은 디버깅을 위해 로그에 추가

        if (id == null || password == null) {
            log.error("ID or Password is missing in the request body");
            throw new AuthenticationServiceException("ID or Password is missing");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        try {
            Authentication authResult = this.getAuthenticationManager().authenticate(authenticationToken);
            log.debug("Authentication result: {}", authResult);
            return authResult;
        } catch (AuthenticationException ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            throw ex;
        }
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        log.debug("Attempting authentication...");
//        log.debug("Attempting authentication for request: {}", request.getRequestURI());  // 로그 추가
//
//        if (request.getContentType() == null || !request.getContentType().equals("application/json")) {
//            log.error("Invalid content type: {}", request.getContentType());
//            throw new AuthenticationServiceException("Content-Type not supported");
//        }
//
//        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
//        Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);
//
//        String id = bodyMap.get("id");
//        String password = bodyMap.get("password");
//
//        log.debug("ID: {}", id);
//        log.debug("Password: {}", password);
//        log.debug("Attempting authentication for ID: {}", id);
//
//        if (id == null || password == null) {
//            log.error("ID or Password is missing in the request body");
//            throw new AuthenticationServiceException("ID or Password is missing");
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
//        try {
//            Authentication authResult = this.getAuthenticationManager().authenticate(authenticationToken);
//            log.debug("Authentication result: {}", authResult);  // 로그 추가
//            return authResult;
//        } catch (AuthenticationException ex) {
//            log.error("Authentication failed: {}", ex.getMessage());  // 에러 로그 추가
//            throw ex;
//        }
////        Authentication authResult = this.getAuthenticationManager().authenticate(authenticationToken);
////
////        log.debug("Authentication result.isAuthenticated: {}", authResult.isAuthenticated());  // 로그 추가
////        log.debug("Authentication result: {}", authResult);  // 로그 추가
////
////        return authResult;
//    }

    @Override //테스트를 위한 코드 TODO: 추후에 로그인 기능 테스트가 끝나면 이 메서드를 삭제하기.
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        log.debug("Successful authentication for user: {}", customUser.getUsername());

        // ROLE 정보 추출
        String role = customUser.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining());

        // AccessToken 생성
        String accessToken = tokenUtils.createAccessToken(customUser, role);
        String refreshToken = tokenUtils.createRefreshToken();

        // 응답을 JSON 형태로 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        )));
    }
}
