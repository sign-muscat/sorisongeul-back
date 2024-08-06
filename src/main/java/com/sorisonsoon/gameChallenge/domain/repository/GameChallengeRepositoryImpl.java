package com.sorisonsoon.gameChallenge.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.sorisonsoon.gameChallenge.domain.entity.QGameChallenge.gameChallenge;
import static com.sorisonsoon.record.domain.entity.QRecord.record;

@RequiredArgsConstructor
public class GameChallengeRepositoryImpl implements GameChallengeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SoundQuestionResponse> getQuestionByDifficulty(GameDifficulty difficulty) {
        SoundQuestionResponse result = queryFactory
                .select(Projections.constructor(SoundQuestionResponse.class,
                        gameChallenge.challengeId,
                        gameChallenge.question
//                        gameChallenge.question
                ))
                .from(gameChallenge)
                .where(gameChallenge.difficulty.eq(difficulty))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<SoundRecordResponse> getSoundRecords(Long challengeId) {
        return queryFactory
                .select(Projections.constructor(SoundRecordResponse.class,
                        record.inputText,
                        record.similarity
                ))
                .from(record)
                .where(record.challengeId.eq(challengeId))
                .orderBy(record.similarity.desc())
                .fetch();
    }
}
