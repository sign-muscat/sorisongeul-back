package com.sorisonsoon.common.security.hadler;


import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.domain.type.CustomUser;
import com.sorisonsoon.user.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final TokenUtils tokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공: LoginSuccessHandler 의 연결 확인용 로그!");
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        // ROLE 정보 추출
        String role = customUser.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining());

        // AccessToken 생성 시 ROLE도 포함
        String accessToken = tokenUtils.createAccessToken(customUser, role);
        String refreshToken = tokenUtils.createRefreshToken(customUser);

        log.info("리프레시 토큰을 업데이트합니다: 사용자 ID = {}", customUser.getUsername());
        authService.updateRefreshToken(customUser.getUsername(), refreshToken);
        log.info("리프레시 토큰이 업데이트되었습니다: 사용자 ID = {}", customUser.getUsername());

        System.out.println("로그인 성공시의 아이디값 : " + customUser.getUsername());
        /* 응답 헤더에 발급 된 토큰을 담는다. */
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}

