package com.sorisonsoon.common.security.dto;

import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.type.CustomUser;
import com.sorisonsoon.user.domain.type.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {

    @Schema()
    private final Long userId;

    private final String id;

    private final String password;

    private final UserRole role;

    public static LoginDto from(User user) {
        return new LoginDto(
                user.getUserId(),
                user.getId(),
                user.getPassword(),
                user.getRole()
        );
    }
    public CustomUser toCustomUser() {
        return new CustomUser(this.userId, this.id, this.password, this.role);
    }

}
