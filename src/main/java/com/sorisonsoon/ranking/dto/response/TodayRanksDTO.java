package com.sorisonsoon.ranking.dto.response;

import com.sorisonsoon.common.domain.type.GameCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class TodayRanksDTO {
    private final Long rankingId;
    private final Long userId;
    private final GameCategory category;
    private final Integer score;
    private final LocalDateTime createdAt;

    public TodayRanksDTO(
            Long rankingId,
            Long userId,
            GameCategory category,
            Integer score,
            LocalDateTime createdAt
    ) {
        this.rankingId = rankingId;
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.createdAt = createdAt;
    }
}
