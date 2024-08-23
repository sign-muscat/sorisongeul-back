package com.sorisonsoon.record.domain.repository;

import java.time.LocalDateTime;

public interface RecordRepositoryCustom {

    void deleteAllByPlayerIdAndCreatedAtBetween(Long playerId, LocalDateTime start, LocalDateTime end);

}