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
    private Double similarity;
    private String inputText;


    private Record(
            Long playerId,
            Long voiceId,
            GameCategory category,
            Boolean isCorrect,
            Double similarity,
            String inputText
    ) {

        this.playerId = playerId;
        this.voiceId = voiceId;
        this.category = category;
        this.isCorrect = isCorrect;
        this.similarity = similarity;
        this.inputText = inputText;
    }


    public static Record of(
            Long playerId, Long voiceId, GameCategory category,
            Boolean isCorrect, Double similarity, String inputText
    ) {
        return new Record (
                playerId,
                voiceId,
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
