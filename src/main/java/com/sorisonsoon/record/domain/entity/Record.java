package com.sorisonsoon.record.domain.entity;

import com.sorisonsoon.common.domain.type.GameCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "record")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    private Long playerId;
    private Long challengeId;
    private Long voiceId;

    @Enumerated(value = EnumType.STRING)
    private GameCategory category;

    private Boolean isCorrect;

    @CreatedDate
    private LocalDateTime createdAt;

    private Double similarity;
    private String inputText;


    private Record(
            Long playerId,
            Long gameId,
            GameCategory category,
            Boolean isCorrect,
            Double similarity,
            String inputText
    ) {
        this.playerId = playerId;
        this.category = category;
        this.isCorrect = isCorrect;
        this.similarity = similarity;
        this.inputText = inputText;

        if(category == GameCategory.CHALLENGE) {
            this.challengeId = gameId;
        } else if(category == GameCategory.VOICE) {
            this.voiceId = gameId;
        }
    }


    public static Record of(
            Long playerId, Long gameId, GameCategory category,
            Boolean isCorrect, Double similarity, String inputText
    ) {
        return new Record (
                playerId,
                gameId,
                category,
                isCorrect,
                similarity,
                inputText
        );

    }

    // isCorrect Getter 메서드 수동 추가 -> Lombok 에서 오류 나나봄
    public boolean getIsCorrect() {
        return isCorrect;
    }

}
