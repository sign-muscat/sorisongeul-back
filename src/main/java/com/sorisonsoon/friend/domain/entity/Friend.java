package com.sorisonsoon.friend.domain.entity;

import com.sorisonsoon.friend.domain.type.FriendStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long friendId;

    private Long toUser;
    private Long fromUser;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private FriendStatus status;
}
