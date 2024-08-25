package com.sorisonsoon.ranking.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import com.sorisonsoon.ranking.domain.entity.QRanking;
import com.sorisonsoon.ranking.domain.entity.Ranking;
import com.sorisonsoon.ranking.dto.response.TodayRanksDTO;
import lombok.RequiredArgsConstructor;


import static com.sorisonsoon.common.domain.type.GameCategory.*;
import static com.sorisonsoon.ranking.domain.entity.QRanking.ranking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TodayRanksDTO> getTodayRanks() {
        LocalDate today = LocalDate.now();

        QRanking rankingSub = new QRanking("rankingSub");

        // 각 카테고리별 최고 점수를 가진 레코드만 가져오는 쿼리
        return queryFactory
                .select(Projections.constructor(TodayRanksDTO.class,
                        ranking.rankingId,
                        ranking.userId,
                        ranking.category,
                        ranking.score,
                        ranking.createdAt
                ))
                .from(ranking)
                .where(ranking.createdAt.between(today.atStartOfDay(), today.atTime(LocalTime.MAX))
                        .and(ranking.category.in(VOICE, RIDDLE, CHALLENGE))
                        .and(ranking.score.eq(
                                JPAExpressions.select(rankingSub.score.max())
                                        .from(rankingSub)
                                        .where(rankingSub.category.eq(ranking.category)
                                                .and(rankingSub.createdAt.between(today.atStartOfDay(), today.atTime(LocalTime.MAX))))
                                        .groupBy(rankingSub.category)
                        ))
                )
                .fetch();
    }
}
