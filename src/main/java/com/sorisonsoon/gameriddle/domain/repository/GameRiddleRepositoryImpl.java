package com.sorisonsoon.gameriddle.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.sorisonsoon.gameriddle.domain.entity.QGameRiddle.gameRiddle;
import static com.sorisonsoon.gameriddle.domain.entity.QGameRiddleStep.gameRiddleStep;

@RequiredArgsConstructor
public class GameRiddleRepositoryImpl implements GameRiddleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HandQuestionResponse> getRiddlesByDifficulty(GameDifficulty difficulty, Long totalQuestion) {
        return queryFactory
                .select(Projections.constructor(HandQuestionResponse.class,
                        gameRiddle.riddleId,
                        gameRiddle.question,
                        gameRiddleStep.riddleStepId.count()
                ))
                .from(gameRiddle)
                .leftJoin(gameRiddleStep).on(gameRiddle.riddleId.eq(gameRiddleStep.riddleId))
                .where(
                        gameRiddle.difficulty.eq(difficulty)
                )
                .groupBy(gameRiddle.riddleId)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(totalQuestion)
                .fetch();
    }
}
