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

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // JWT 토큰이 있는지 확인하고, 있다면 "Bearer " 접두사를 제거
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = TokenUtils.getToken(requestTokenHeader);
            try {
                username = tokenUtils.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                System.out.println("JWT Token is invalid or expired: " + e.getMessage());
            }
        }

        // 토큰이 유효하고 인증되지 않은 상태라면, 사용자 정보 설정
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.authService.loadUserByUsername(username);
            if (tokenUtils.isValidToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // Access Token이 만료된 경우
                String refreshToken = request.getHeader("Refresh-Token");
                if (refreshToken != null && tokenUtils.isValidToken(refreshToken, userDetails)) {
                    // Refresh Token이 유효한 경우, 새로운 Access Token 발급
                    String newAccessToken = tokenUtils.createAccessToken(userDetails);
                    response.setHeader("Access-Token", newAccessToken);
                    // 인증 정보 갱신
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        chain.doFilter(request, response);
    }


}





