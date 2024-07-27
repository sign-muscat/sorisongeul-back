package com.sorisonsoon.signsearch.domain.repository;

import com.sorisonsoon.gameChallenge.domain.entity.GameChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignSearchRepository extends JpaRepository<GameChallenge, Long> {

}
