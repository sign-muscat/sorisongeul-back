package com.sorisonsoon.gameriddle.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameriddle.dto.response.HandQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sorisonsoon.gameriddle.domain.entity.QGameRiddle.gameRiddle;
import static com.sorisonsoon.gameriddle.domain.entity.QGameRiddleStep.gameRiddleStep;

@RequiredArgsConstructor
@Repository
public class GameRiddleRepositoryImpl implements GameRiddleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HandQuestionResponse> getRiddlesByDifficulty(GameDifficulty difficulty, Long totalQuestion) {
        System.out.println("getRiddlesByDifficulty called with difficulty: " + difficulty + ", totalQuestion: " + totalQuestion);

        List<HandQuestionResponse> result = queryFactory
                .select(Projections.constructor(HandQuestionResponse.class,
                        gameRiddle.riddleId,
                        gameRiddle.question,
                        gameRiddleStep.riddleStepId.count().as("stepCount")
                ))
                .from(gameRiddle)
                .leftJoin(gameRiddleStep).on(gameRiddle.riddleId.eq(gameRiddleStep.riddleId))
                .where(gameRiddle.difficulty.eq(difficulty))
                .groupBy(gameRiddle.riddleId)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(totalQuestion)
                .fetch();

        System.out.println("Fetched riddles:");
        for (HandQuestionResponse riddle : result) {
            System.out.println("Riddle ID: " + riddle.getRiddleId() + ", Question: " + riddle.getQuestion() + ", Step Count: " + riddle.getTotalStep());
        }

        return result;
    }

    public String getQuestionImage(Long riddleId, Long step) {
        System.out.println("getQuestionImage called with riddleId: " + riddleId + ", step: " + step);

        String questionImage = queryFactory
                .select(gameRiddleStep.answer)
                .from(gameRiddleStep)
                .where(gameRiddleStep.riddleId.eq(riddleId)
                        .and(gameRiddleStep.step.eq(step)))
                .fetchOne();

        if (questionImage != null) {
            System.out.println("Fetched Question Image: " + questionImage);
        } else {
            System.out.println("No Question found for riddleId: " + riddleId + ", step: " + step);
        }

        return questionImage;
    }
}
