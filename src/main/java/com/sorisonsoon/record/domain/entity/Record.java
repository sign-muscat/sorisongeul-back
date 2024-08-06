package com.sorisonsoon.record.domain.entity;

import com.sorisonsoon.common.domain.type.GameCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    private Long playerId;
    private Long challengeId;
    private Long voiceId;

    @Enumerated(EnumType.STRING)
    private GameCategory category;

    private Boolean isCorrect;

    @CreatedDate
    private LocalDateTime createdAt;

    private Double similarity;
    private String inputText;
}
