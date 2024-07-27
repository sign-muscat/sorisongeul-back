package com.sorisonsoon.ranking.domain.repository;

import com.sorisonsoon.ranking.domain.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

}
