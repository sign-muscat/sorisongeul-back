package com.sorisonsoon.gamevoice.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import lombok.RequiredArgsConstructor;

import static com.sorisonsoon.gamevoice.domain.entity.QGameVoice.gameVoice;

@RequiredArgsConstructor
public class GameVoiceRepositoryImpl implements GameVoiceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public GameVoiceQuestionResponse getRandomVoiceByDifficulty(GameDifficulty difficulty) {
        return queryFactory
                .select(Projections.constructor(GameVoiceQuestionResponse.class,
                                gameVoice.voiceId,
                                gameVoice.question,
                                gameVoice.answer,
                                gameVoice.category,
                                gameVoice.difficulty
                ))
                .from(gameVoice)
                .where(gameVoice.difficulty.eq(difficulty))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(1)
                .fetchOne();
    }

}
