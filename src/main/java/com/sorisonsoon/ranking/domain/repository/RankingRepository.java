package com.sorisonsoon.ranking.domain.repository;

import org.springframework.data.domain.Pageable;
import com.sorisonsoon.common.domain.type.GameCategory;
import com.sorisonsoon.ranking.domain.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    @Query("SELECT r FROM Ranking r WHERE r.category = :category ORDER BY r.score DESC")
    List<Ranking> findTop10ByCategory(@Param("category") GameCategory category, Pageable pageable);
}