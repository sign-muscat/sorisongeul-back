package com.sorisonsoon.ranking.service;

import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.domain.entity.Ranking;
import com.sorisonsoon.ranking.domain.repository.RankingRepository;
import com.sorisonsoon.ranking.dto.RankingDTO;
import com.sorisonsoon.ranking.dto.response.RankResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<RankingDTO> getTop10ByCategory(GameCategory category) {
        Pageable topTen = PageRequest.of(0, 10);
        List<Ranking> rankings = rankingRepository.findTop10ByCategory(category, topTen);
        return rankings.stream()
                .map(ranking -> new RankingDTO(ranking.getUserId(), ranking.getScore()))
                .collect(Collectors.toList());
    }

    public List<RankResponse> getTodayRanks() {
        List<RankResponse> rankings = rankingRepository.getTodayRanks();
        return rankings;
    }

    public List<RankResponse> getMyRanks(Long userId) {
        List<RankResponse> myRankings = rankingRepository.getMyRanks(userId);
        return myRankings;
    }
}