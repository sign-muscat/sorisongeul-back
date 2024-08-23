package com.sorisonsoon.record.domain.repository;


import com.sorisonsoon.record.domain.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RecordRepository extends JpaRepository<Record, Long> , RecordRepositoryCustom{

    Long countByPlayerIdAndCreatedAtBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
