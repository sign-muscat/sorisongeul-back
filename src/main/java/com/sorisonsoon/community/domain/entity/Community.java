package com.sorisonsoon.community.domain.entity;

import com.sorisonsoon.community.domain.type.CommunityStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "community")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE  community SET status = 'DELETED' , deleted_at = CURRENT_TIMESTAMP WHERE community_id = ? ")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    private int userId;
    private String communityTitle;
    private String communityContent;
    private int views;

    @Enumerated(value = EnumType.STRING)
    private CommunityStatus status;

    private boolean isDeleted;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
