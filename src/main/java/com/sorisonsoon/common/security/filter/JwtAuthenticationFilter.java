package com.sorisonsoon.common.security.filter;

import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String accessToken = TokenUtils.getToken(request.getHeader("Authorization")); // 기존 방식
        final String refreshToken = TokenUtils.getToken(request.getHeader("Refresh-Token")); // 추가된 부분

        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);

        String username = null;
        String jwtToken = null;

        if (accessToken != null) {
            jwtToken = accessToken;
            try {
                username = tokenUtils.getUsernameFromToken(jwtToken);
                System.out.println("추출된 회원 이름! : " + username);
            } catch (Exception e) {
                System.out.println("JWT 토큰이 유효하지 않거나 만료되었습니다.: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.authService.loadUserByUsername(username);

            if (tokenUtils.isValidToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("인증 성공한 회원! : " + username);
            } else if (refreshToken != null && tokenUtils.isValidToken(refreshToken, userDetails)) {
                // Refresh Token이 유효하면 새로운 Access Token을 발급
                String role = userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.joining());

                String newAccessToken = tokenUtils.createAccessToken(userDetails, role);
                response.setHeader("Access-Token", newAccessToken);

                // 새로운 인증 정보를 SecurityContext에 저장
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("회원에게 새로 발급된 엑세스 토큰! : " + username);
            }else {
                System.out.println("토큰 검증 실패 회원 정보  ㅠㅠ : " + username);
            }
        }

        chain.doFilter(request, response);
    }


}





