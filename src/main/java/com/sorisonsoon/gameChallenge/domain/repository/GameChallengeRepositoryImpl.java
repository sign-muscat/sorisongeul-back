package com.sorisonsoon.gameChallenge.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import com.sorisonsoon.gameChallenge.dto.response.SoundCorrectResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import com.sorisonsoon.gameChallenge.dto.response.SoundRecordResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.sorisonsoon.gameChallenge.domain.entity.QGameChallenge.gameChallenge;
import static com.sorisonsoon.gameChallenge.domain.entity.QGameChallengeSchedule.gameChallengeSchedule;
import static com.sorisonsoon.record.domain.entity.QRecord.record;

@RequiredArgsConstructor
public class GameChallengeRepositoryImpl implements GameChallengeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public GameChallenge getUnscheduledQuestion() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        return queryFactory
                .selectFrom(gameChallenge)
                .leftJoin(gameChallengeSchedule).on(gameChallenge.challengeId.eq(gameChallengeSchedule.challengeId))
                .where(
                        gameChallengeSchedule.schedule.lt(oneMonthAgo)
                                .or(gameChallengeSchedule.schedule.isNull())
                )
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public SoundCorrectResponse checkCorrect(Long userId) {
        Long challengeId = queryFactory
                .select(gameChallenge.challengeId)
                .from(gameChallenge)
                .leftJoin(gameChallengeSchedule).on(gameChallenge.challengeId.eq(gameChallengeSchedule.challengeId))
                .where(gameChallengeSchedule.schedule.eq(LocalDate.now()))
                .fetchOne();

        Boolean exists = queryFactory
                .selectOne()
                .from(record)
                .where(record.playerId.eq(userId)
                        .and(record.challengeId.eq(challengeId))
                        .and(record.isCorrect.eq(Boolean.TRUE)))
                .fetchFirst() != null;

        GameDifficulty difficulty = queryFactory
                .select(gameChallenge.difficulty)
                .from(gameChallenge)
                .where(gameChallenge.challengeId.eq(challengeId))
                .fetchOne();

        return new SoundCorrectResponse(exists, difficulty);
    }

    @Override
    public Optional<SoundQuestionResponse> getTodayQuestion() {
        SoundQuestionResponse result = queryFactory
                .select(Projections.constructor(SoundQuestionResponse.class,
                        gameChallenge.challengeId,
                        gameChallenge.question,
                        gameChallenge.difficulty
                ))
                .from(gameChallenge)
                .leftJoin(gameChallengeSchedule).on(gameChallenge.challengeId.eq(gameChallengeSchedule.challengeId))
                .where(gameChallengeSchedule.schedule.eq(LocalDate.now()))
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
