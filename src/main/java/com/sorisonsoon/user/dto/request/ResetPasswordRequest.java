package com.sorisonsoon.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordRequest {

    @NotBlank
    @Size(min = 8, max = 20)
    private final String newPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    private final String confirmPassword;

}
