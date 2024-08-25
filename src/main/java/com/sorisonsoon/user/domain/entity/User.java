package com.sorisonsoon.user.domain.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.interest.domain.entity.Interest;
import com.sorisonsoon.user.domain.type.UserProvider;
import com.sorisonsoon.user.domain.type.UserRole;
import com.sorisonsoon.user.domain.type.UserStatus;
import com.sorisonsoon.user.domain.type.UserType;
import com.sorisonsoon.user.dto.UserFormDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE  users SET status = 'WITHDRAW' , deleted_at = CURRENT_TIMESTAMP WHERE user_id = ? ")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String id;
    private String password;
    private String nickname;
    private String email;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.FREE_USER;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVATE;

    private String accessToken;
    private String refreshToken;

    @Enumerated(value = EnumType.STRING)
    private UserProvider provider;

    private String profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests = new ArrayList<>();


    private User(String id, String password, String name, String email, String type) {
        this(id, password, name, email, UserType.valueOf(type));
    }

    private User(String id, String password, String name, String email, UserType type) {
        this.id = id;
        this.password = password;
        this.nickname = name;
        this.email = email;
        this.type = type;
    }

    public static User from(String id, String password, String name, String email, String type) {
        return new User(
                id,
                password,
                name,
                email,
                type
        );
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static User createUser(UserFormDto userFormDto) throws IOException {
        User user = new User();

        user.setId(userFormDto.getId());
        user.setPassword(userFormDto.getPassword());
        user.setNickname(userFormDto.getNickname());
        user.setEmail(userFormDto.getEmail());
        user.setCreatedAt(userFormDto.getCreatedAt());
        user.setDeletedAt(userFormDto.getDeletedAt());
        if (userFormDto.getRole() != null) {
            user.setRole(userFormDto.getRole());
        }
        user.setType(userFormDto.getType());
        if (userFormDto.getStatus() != null) {
            user.setStatus(userFormDto.getStatus());
        }

        user.setRefreshToken(userFormDto.getRefreshToken());

        // 프로필 이미지 처리
        if (userFormDto.getProfileImage() != null && !userFormDto.getProfileImage().isEmpty()) {
            // 프로필 이미지 파일을 저장하고 경로를 설정
            String profileImagePath = saveProfileImage(userFormDto.getProfileImage());
            user.setProfileImage(profileImagePath);
        }

        // Determine provider based on email
        user.setProvider(determineProvider(userFormDto.getEmail()));

        return user;
    }

    // 프로필 이미지를 저장하는 메서드
    public static String saveProfileImage(MultipartFile profileImage) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        Path imagePath = Paths.get("path/to/save/directory", fileName);
        Files.copy(profileImage.getInputStream(), imagePath);
        return imagePath.toString();
    }


    private static UserProvider determineProvider(String email) {
        if (email.endsWith("@gmail.com")) {
            return UserProvider.GOOGLE;
        } else {
            return UserProvider.NONE;
        }
    }

    public void update(Long userId, String nickname, String profileImage) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
