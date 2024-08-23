package com.sorisonsoon.gameriddle.domain.repository;

import com.sorisonsoon.gameriddle.domain.entity.GameRiddle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRiddleRepository extends JpaRepository<GameRiddle, Long>, GameRiddleRepositoryCustom {

    GameRiddle findByRiddleId(Long riddleId);
}


