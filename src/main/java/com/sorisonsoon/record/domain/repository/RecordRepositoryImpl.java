package com.sorisonsoon.record.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.sorisonsoon.record.domain.entity.QRecord;

import java.time.LocalDateTime;


@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByPlayerIdAndCreatedAtBetween(Long playerId, LocalDateTime start, LocalDateTime end) {
        QRecord record = QRecord.record;

        // 해당 플레이어의 주어진 시간 범위 내의 모든 레코드를 삭제합니다.
        queryFactory
            .delete(record)
            .where(record.playerId.eq(playerId)
                .and(record.createdAt.between(start, end)))
            .execute();
    }
}
