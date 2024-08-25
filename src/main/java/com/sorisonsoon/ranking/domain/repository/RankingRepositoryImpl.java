package com.sorisonsoon.ranking.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.ranking.domain.entity.QRanking;
import com.sorisonsoon.ranking.dto.response.RankResponse;
import lombok.RequiredArgsConstructor;


import static com.sorisonsoon.common.domain.type.GameCategory.*;
import static com.sorisonsoon.ranking.domain.entity.QRanking.ranking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RankResponse> getTodayRanks() {
        LocalDate today = LocalDate.now();

        QRanking rankingSub = new QRanking("rankingSub");

        // 각 카테고리별 최고 점수를 가진 레코드만 가져오는 쿼리
        return queryFactory
                .select(Projections.constructor(RankResponse.class,
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

//    @Override
//    public List<RankResponse> getMyRanks(Long userId) {
//        LocalDate today = LocalDate.now();
//
//        // 각 카테고리별로 주어진 userId의 순위를 가져오는 쿼리
//        return queryFactory
//                .select(Projections.constructor(RankResponse.class,
//                        ranking.rankingId,
//                        ranking.userId,
//                        ranking.category,
//                        ranking.score,
//                        ranking.createdAt,
//                        Expressions.numberTemplate(Integer.class,
//                                "(SELECT COUNT(*) + 1 FROM Ranking r2 " +
//                                        "WHERE r2.category = ranking.category " +
//                                        "AND r2.score > ranking.score " +
//                                        "AND r2.created_at BETWEEN {0} AND {1})",
//                                today.atStartOfDay(), today.atTime(LocalTime.MAX)
//                        ).as("myRank")
//                ))
//                .from(ranking)
//                .where(ranking.userId.eq(userId)
//                        .and(ranking.createdAt.between(today.atStartOfDay(), today.atTime(LocalTime.MAX)))
//                        .and(ranking.category.in(VOICE, RIDDLE, CHALLENGE))
//                )
//                .orderBy(ranking.category.asc(), ranking.score.desc())
//                .fetch();
//    }
    @Override
    public List<RankResponse> getMyRanks(Long userId) {
        LocalDate today = LocalDate.now();

        // 각 카테고리별로 주어진 userId의 순위를 가져오는 쿼리
        return queryFactory
                .select(Projections.constructor(RankResponse.class,
                        ranking.rankingId,
                        ranking.userId,
                        ranking.category,
                        ranking.score,
                        ranking.createdAt,  // 여기 필드 이름 수정
                        Expressions.numberTemplate(Integer.class,
                                "(SELECT COUNT(*) + 1 FROM Ranking r2 " +
                                        "WHERE r2.category = ranking.category " +
                                        "AND r2.score > ranking.score " +
                                        "AND r2.createdAt BETWEEN {0} AND {1})",  // 여기 필드 이름도 수정
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
