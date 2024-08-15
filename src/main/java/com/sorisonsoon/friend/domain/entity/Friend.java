package com.sorisonsoon.friend.domain.entity;

import com.sorisonsoon.friend.domain.type.FriendStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE friend SET status = 'DELETED' WHERE friend_id = ?")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long friendId;

    private Long toUser;
    private Long fromUser;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    Friend(Long fromUser, Long toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public static Friend of(Long fromUser, Long toUser) {
        return new Friend(
                fromUser,
                toUser
        );
    }

    public void updateStatus(FriendStatus status) {
        this.status = status;
    }
}
