package com.sorisonsoon.record.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sorisonsoon.record.domain.entity.QRecord;
import com.sorisonsoon.record.domain.entity.Record;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

}
