package com.sorisonsoon.ranking.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.domain.entity.QRanking;
import com.sorisonsoon.ranking.dto.response.RankResponse;
import lombok.RequiredArgsConstructor;


import static com.sorisonsoon.common.domain.type.GameCategory.*;
import static com.sorisonsoon.ranking.domain.entity.QRanking.ranking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Map<GameCategory, List<RankResponse>> getTodayRanks(int limit) {
        LocalDate today = LocalDate.now();

        List<RankResponse> allRankings = queryFactory
                .select(Projections.constructor(RankResponse.class,
                        ranking.rankingId,
                        ranking.userId,
                        ranking.category,
                        ranking.score,
                        ranking.createdAt
                ))
                .from(ranking)
                .where(ranking.createdAt.between(today.atStartOfDay(), today.atTime(LocalTime.MAX))
                        .and(ranking.category.in(VOICE, RIDDLE, CHALLENGE)))
                .orderBy(ranking.category.asc(), ranking.score.desc())
                .fetch();

        return allRankings.stream()
                .collect(Collectors.groupingBy(RankResponse::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(RankResponse::getScore).reversed())
                                        .limit(limit)
                                        .collect(Collectors.toList())
                        )));
    }

    @Override
    public List<RankResponse> getMyRanks(Long userId) {
        LocalDate today = LocalDate.now();

        return queryFactory
                .select(Projections.constructor(RankResponse.class,
                        ranking.rankingId,
                        ranking.userId,
                        ranking.category,
                        ranking.score,
                        ranking.createdAt,
                        Expressions.numberTemplate(Integer.class,
                                "(SELECT COUNT(*) + 1 FROM Ranking r2 " +
                                        "WHERE r2.category = ranking.category " +
                                        "AND r2.score > ranking.score " +
                                        "AND r2.createdAt BETWEEN {0} AND {1})",
                                today.atStartOfDay(), today.atTime(LocalTime.MAX)
                        ).as("myRank")
                ))
                .from(ranking)
                .where(ranking.userId.eq(userId)
                        .and(ranking.createdAt.between(today.atStartOfDay(), today.atTime(LocalTime.MAX)))
                        .and(ranking.category.in(VOICE, RIDDLE, CHALLENGE))
                )
                .orderBy(ranking.category.asc(), ranking.score.desc())
                .fetch();
    }
}
