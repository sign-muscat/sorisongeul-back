package com.sorisonsoon.ranking.domain.repository;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.dto.response.RankResponse;

import java.util.List;
import java.util.Map;

public interface RankingRepositoryCustom {
    Map<GameCategory, List<RankResponse>> getTodayRanks(int limit);

    List<RankResponse> getMyRanks(Long userId);
}
