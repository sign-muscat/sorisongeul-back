package com.sorisonsoon.user.controller;


import com.sorisonsoon.user.domain.type.CustomUser;
import com.sorisonsoon.user.service.AuthService;
import com.sorisonsoon.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 관련 API [AuthController]")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * 토큰 재발급
     */
    @GetMapping("/token/issue")
    public ResponseEntity<?> issueToken(@RequestHeader("Refresh-Token") String refreshToken,
                                        @AuthenticationPrincipal CustomUser loginUser) {

        String ReIssuedAccessToken = authService.issueToken(refreshToken, loginUser.getUsername());

        return ResponseEntity.noContent()
                .header("Access-Token", ReIssuedAccessToken)
                .build();
    }






}
