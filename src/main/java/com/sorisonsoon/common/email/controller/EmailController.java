package com.sorisonsoon.common.email.controller;

import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.common.email.service.EmailService;
import com.sorisonsoon.common.email.util.EmailValidator;
import com.sorisonsoon.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "이메일 관련 API " ,description = "[EmailController]")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailValidator emailValidator;

    /**
     * 인증 번호 전송
     *
     * @param email
     */
    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<?>> send(String email) throws Exception {

        if (!EmailValidator.isValid(email)) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("유효하지 않은 이메일 형식입니다."));
        }

        String authCode = emailService.generateAuthCode();
        String token = emailService.encryptToken(email, authCode);
        emailService.sendVerificationEmail(email, authCode);

        return ResponseEntity.ok(
                ApiResponse.success("인증 번호가 발송되었습니다.", token)
        );
    }

    /**
     * 인증 번호 검증
     *
     * @param token
     * @param email
     * @param code
     */
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<?>> verify(String token, String email, String code) {

        try {
            emailService.verifyToken(token, email, code);
            return ResponseEntity.ok(ApiResponse.success("인증 완료되었습니다."));
        } catch (NotFoundException e) {
            // 이메일이 디비에 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("해당 이메일을 가진 회원이 존재하지 않습니다."));
        } catch (IllegalArgumentException e) {
            // 인증 코드가 틀리거나, 유효 시간이 만료된 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }
}
