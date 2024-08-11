package com.sorisonsoon.record.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;


}
