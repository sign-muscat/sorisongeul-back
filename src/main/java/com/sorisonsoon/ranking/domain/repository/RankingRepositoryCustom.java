package com.sorisonsoon.ranking.domain.repository;

import com.sorisonsoon.ranking.dto.response.TodayRanksDTO;

import java.util.List;

public interface RankingRepositoryCustom {
    List<TodayRanksDTO> getTodayRanks();
}
