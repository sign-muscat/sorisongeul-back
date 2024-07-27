package com.sorisonsoon.gamewhisper.domain.repository;

import com.sorisonsoon.gamewhisper.domain.entity.GameWhisper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameWhisperRepository extends JpaRepository<GameWhisper, Long> {

}
