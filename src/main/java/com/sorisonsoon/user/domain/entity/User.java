package com.sorisonsoon.user.domain.entity;

import com.sorisonsoon.user.domain.type.UserProvider;
import com.sorisonsoon.user.domain.type.UserRole;
import com.sorisonsoon.user.domain.type.UserStatus;
import com.sorisonsoon.user.domain.type.UserType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
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
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVATE;

    private String accessToken;
    private String refreshToken;

    @Enumerated(value = EnumType.STRING)
    private UserProvider provider;

    private String profileImage;


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

}
