package com.sorisonsoon.notice.domain.entity;

import com.sorisonsoon.notice.domain.type.NoticeCategory;
import com.sorisonsoon.notice.domain.type.NoticeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    private int userId;
    private String noticeTitle;
    private String noticeContent;
    private boolean isFixed;
    private int views;

    @Enumerated(EnumType.STRING)
    private NoticeStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NoticeCategory category;
}
