package com.sorisonsoon.ranking.service;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.domain.entity.Ranking;
import com.sorisonsoon.ranking.domain.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;

    public void save(Long userId, GameCategory category, double score) {
        final Ranking newRanking = Ranking.of(
                userId,
                category,
                (int) Math.round(score * 100)
        );
        rankingRepository.save(newRanking);
    }
}
