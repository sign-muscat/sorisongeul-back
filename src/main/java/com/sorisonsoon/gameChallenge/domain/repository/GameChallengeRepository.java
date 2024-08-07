package com.sorisonsoon.gameChallenge.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.common.domain.type.GameDifficulty;
import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import com.sorisonsoon.gameChallenge.dto.response.SoundQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameChallengeRepository extends JpaRepository<GameChallenge, Long>, GameChallengeRepositoryCustom {

}
