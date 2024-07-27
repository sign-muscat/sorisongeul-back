package com.sorisonsoon.teamrecord.domain.repository;


import com.sorisonsoon.teamrecord.domain.entity.TeamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRecordRepository extends JpaRepository<TeamRecord, Long> {

}
