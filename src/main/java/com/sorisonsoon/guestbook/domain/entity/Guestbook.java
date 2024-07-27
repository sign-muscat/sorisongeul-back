package com.sorisonsoon.guestbook.domain.entity;

import com.sorisonsoon.guestbook.domain.type.GuestbookStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "guestbook")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long guestbookId;

    private int senderId;
    private int receiverId;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private GuestbookStatus status;
}
