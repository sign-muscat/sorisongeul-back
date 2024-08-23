package com.sorisonsoon.attachedfile.domain.entity;

import com.sorisonsoon.attachedfile.domain.type.AttachedFileCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "attached_file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AttachedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attached_id")
    private Long attachedId;

    private Long noticePostId;

    private Long communityPostId;

    private Long reportId;

    @Enumerated(value = EnumType.STRING)
    private AttachedFileCategory category;

    private String fileName;

    private String originName;

    private String path;

    private String extension;

    @CreatedDate
    private LocalDateTime createdAt;

    private AttachedFile(Long attachedId, AttachedFileCategory category, String path, String fileName, String extension, String originName) {
        this.category = category;
        updateIdBasedOnCategory(attachedId, category);
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
        this.originName = originName;
    }

    public static AttachedFile of(Long attachedId, AttachedFileCategory category, String path, String fileName, String extension, String originName) {
        return new AttachedFile(attachedId, category, path, fileName, extension, originName);
    }

    private void updateIdBasedOnCategory(Long attachedId, AttachedFileCategory category) {
        switch (category) {
            case REPORT -> this.reportId = attachedId;
            case COMMUNITY -> this.communityPostId = attachedId;
            case NOTICE -> this.noticePostId = attachedId;
        }
    }

}
