package com.sorisonsoon.gamevoice.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.gamevoice.domain.entity.GameVoice;
import com.sorisonsoon.gamevoice.dto.response.GameVoiceQuestionResponse;
import lombok.RequiredArgsConstructor;

import static com.sorisonsoon.gamevoice.domain.entity.QGameVoice.gameVoice;

@RequiredArgsConstructor
public class GameVoiceRepositoryImpl implements GameVoiceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public GameVoiceQuestionResponse geTodayQuestion () {
        return queryFactory
                .select(Projections.constructor(GameVoiceQuestionResponse.class,
                        gameVoice.voiceId,
                        gameVoice.question,
                        gameVoice.answer,
                        gameVoice.category,
                        gameVoice.difficulty,
                        gameVoice.isTodayQuestion,
                        gameVoice.isPast))
                .from(gameVoice)
                .where(gameVoice.isTodayQuestion.eq(true))
                .fetchOne();
    }

    @Override
    public GameVoiceQuestionResponse getFixedQuestion() {
        return queryFactory
                .select(Projections.constructor(GameVoiceQuestionResponse.class, gameVoice))
                .from(gameVoice)
                .where(gameVoice.voiceId.eq(1L))
                .fetchOne();
    }

    @Override
    public GameVoiceQuestionResponse createTodayQuestion() {
        queryFactory
                .update(gameVoice)
                .set(gameVoice.isTodayQuestion, false) // false로 초기화
                .execute();

         GameVoice selectedVoice = queryFactory
                .selectFrom(gameVoice)
                .where(gameVoice.isPast.eq(false))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(1)
                .fetchOne();

        if (selectedVoice != null) {
            queryFactory
                    .update(gameVoice)
                    .set(gameVoice.isTodayQuestion, true)
                    .set(gameVoice.isPast, true)
                    .where(gameVoice.voiceId.eq(selectedVoice.getVoiceId()))
                    .execute();

            return new GameVoiceQuestionResponse(
                    selectedVoice.getVoiceId(),
                    selectedVoice.getQuestion(),
                    selectedVoice.getAnswer(),
                    selectedVoice.getCategory(),
                    selectedVoice.getDifficulty(),
                    true,
                    true
            );

        } else {
            // 문제가 없을 경우 처리 (필요시)
            return null;
        }
    }

}
