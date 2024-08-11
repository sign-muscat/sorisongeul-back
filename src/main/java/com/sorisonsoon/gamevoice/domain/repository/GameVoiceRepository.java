package com.sorisonsoon.gamevoice.domain.repository;

import com.sorisonsoon.gamevoice.domain.entity.GameVoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameVoiceRepository extends JpaRepository<GameVoice, Long>, GameVoiceRepositoryCustom {

}
