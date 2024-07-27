package com.sorisonsoon.record.domain.repository;


import com.sorisonsoon.record.domain.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

}
