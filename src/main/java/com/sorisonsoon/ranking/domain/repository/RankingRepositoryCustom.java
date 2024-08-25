package com.sorisonsoon.ranking.domain.repository;

import com.sorisonsoon.ranking.dto.response.RankResponse;

import java.util.List;

public interface RankingRepositoryCustom {
    List<RankResponse> getTodayRanks();

    List<RankResponse> getMyRanks(Long userId);
}
