package com.sorisonsoon.user.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.type.UserProvider;
import com.sorisonsoon.user.domain.type.UserRole;
import com.sorisonsoon.user.domain.type.UserStatus;
import com.sorisonsoon.user.domain.type.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDto {
    private Long userId;

    private String id; //아이디
    private String password ; //비밀번호
    private String nickname ; //닉네임

    
    private String email ; //이메일

    private LocalDateTime createdAt; //가입일시
    private LocalDateTime deletedAt; //탈퇴일시

    private UserRole role; //권한

    private UserType type; //종류

    private UserStatus status; //상태

    private UserProvider provider;

    private String accessToken;
    private String refreshToken; //리프레시 토큰

    private MultipartFile profileImage; //프로필 이미지

    public static UserFormDto fromUser(User user) {
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setUserId(user.getUserId());
        userFormDto.setId(user.getId());
        userFormDto.setNickname(user.getNickname());
        userFormDto.setEmail(user.getEmail());
        userFormDto.setCreatedAt(user.getCreatedAt());
        userFormDto.setDeletedAt(user.getDeletedAt());
        userFormDto.setRole(user.getRole());
        userFormDto.setType(user.getType());
        userFormDto.setStatus(user.getStatus());
        userFormDto.setProvider(user.getProvider());
        userFormDto.setAccessToken(user.getAccessToken());
        userFormDto.setRefreshToken(user.getRefreshToken());

        // 프로필 이미지는 String으로 설정 (경로)
        userFormDto.setProfileImage(null); // 혹은 경로를 String으로 변환 후 설정

        return userFormDto;
    }


}
