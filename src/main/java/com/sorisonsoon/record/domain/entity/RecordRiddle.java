package com.sorisonsoon.record.domain.entity;

import com.sorisonsoon.common.domain.type.GameCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "record_riddle")
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordRiddle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    private Long playerId;
    private Long riddleId;

    @Enumerated(EnumType.STRING)
    private GameCategory category;

    private Boolean isCorrect;

    @CreatedDate
    private LocalDateTime createdAt;


    private RecordRiddle(
            Long playerId,
            Long riddleId,
            GameCategory category,
            Boolean isCorrect
    ) {
        this.playerId = playerId;
        this.riddleId = riddleId;
        this.category = category;
        this.isCorrect = isCorrect;
    }


    public static RecordRiddle of(
            Long playerId, Long riddleId, GameCategory category, Boolean isCorrect
    ) {
        return new RecordRiddle(
                playerId,
                riddleId,
                category,
                isCorrect
        );
    }

}
