package com.sorisonsoon.ranking.domain.entity;

import com.sorisonsoon.common.domain.type.GameCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ranking")
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_id")
    private Long rankingId;

    private Long userId;

    @Enumerated(value = EnumType.STRING)
    private GameCategory category;

    private int score;

    @CreatedDate
    private LocalDateTime createdAt;

    private Ranking(Long userId, GameCategory category, int score) {
        this.userId = userId;
        this.category = category;
        this.score = score;
    }

    public static Ranking of(Long userId, GameCategory category, int score) {
        return new Ranking(
                userId,
                category,
                score
        );
    }
}
