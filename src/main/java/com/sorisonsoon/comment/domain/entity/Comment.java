package com.sorisonsoon.comment.domain.entity;

import com.sorisonsoon.comment.domain.type.CommentCategory;
import com.sorisonsoon.comment.domain.type.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE  comment SET status = 'DELETED' , deleted_at = CURRENT_TIMESTAMP WHERE comment_id = ? ")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private int writerId;
    private int communityId;
    private int parentCommentId;

    @Enumerated(value = EnumType.STRING)
    private CommentCategory category;

    private String commentContent;

    @Enumerated(value = EnumType.STRING)
    private CommentStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

}
