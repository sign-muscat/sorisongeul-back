package com.sorisonsoon.user.controller;


import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.domain.type.CustomUser;
import com.sorisonsoon.user.service.AuthService;
import com.sorisonsoon.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "인증 관련 API " ,description = "[AuthController]")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    /**
     * 임시 로그인 요청
     * [SwaggerUI 사용을 위한 임시임. 추후 삭제 예정. ] TODO: 추후 삭제(이미 필터에 코드는 있음)후 필터로 ㄱㄱ 할 예정.
     *
     * @pram id
     * @pram password
     */

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "SwaggerUI 사용을 위한 임시 로그인")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String id = loginRequest.get("id");
        String password = loginRequest.get("password");

        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(id, password)
            );

            CustomUser customUser = (CustomUser) authentication.getPrincipal();

            // ROLE 정보 추출
            String role = customUser.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining());

            // AccessToken 및 RefreshToken 생성
            String accessToken = tokenUtils.createAccessToken(customUser, role);
            String refreshToken = tokenUtils.createRefreshToken();

            // RefreshToken 저장
            authService.updateRefreshToken(customUser.getUsername(), refreshToken);

            // 토큰 반환
            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * 토큰 재발급
     *
     * @param refreshToken
     */
    @GetMapping("/token/issue")
    @Operation(summary = "토큰 재발급", description = "토큰 재 발급용 API")
    public ResponseEntity<?> issueToken(@RequestHeader("Refresh-Token") String refreshToken,
                                        @AuthenticationPrincipal CustomUser loginUser) {

        String ReIssuedAccessToken = authService.issueToken(refreshToken, loginUser.getUsername());

        return ResponseEntity.noContent()
                .header("Access-Token", ReIssuedAccessToken)
                .build();
    }

    /**
     * 로그아웃
     *
     * @param loginUser 로그인 정보
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API")
    public ResponseEntity<ApiResponse<?>> logout(@AuthenticationPrincipal CustomUser loginUser) {

        authService.updateRefreshToken(loginUser.getUsername(), null);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다."));
    }


}
