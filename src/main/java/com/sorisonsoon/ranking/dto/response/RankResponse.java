package com.sorisonsoon.ranking.dto.response;

import com.sorisonsoon.common.domain.type.GameCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RankResponse {
    private final Long rankingId;
    private final Long userId;
    private final GameCategory category;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final Integer myRank;
    private final String nickname;

    public RankResponse(
            Long rankingId,
            Long userId,
            GameCategory category,
            Integer score,
            LocalDateTime createdAt,
            String nickname
    ) {
        this.rankingId = rankingId;
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.createdAt = createdAt;
        this.myRank = null;
        this.nickname = nickname;
    }

    public RankResponse(
            Long rankingId,
            Long userId,
            GameCategory category,
            Integer score,
            LocalDateTime createdAt,
            Integer myRank
    ) {
        this.rankingId = rankingId;
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.createdAt = createdAt;
        this.myRank = myRank;
        this.nickname = null;
    }

    public RankResponse(
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
        this.myRank = null; // 기본값 설정
        this.nickname = null; // 기본값 설정
    }

    public RankResponse(
            Long rankingId,
            Long userId,
            GameCategory category,
            Integer score,
            LocalDateTime createdAt,
            Integer myRank,
            String nickname
    ) {
        this.rankingId = rankingId;
        this.userId = userId;
        this.category = category;
        this.score = score;
        this.createdAt = createdAt;
        this.myRank = myRank;
        this.nickname = nickname;
    }
}
