package com.sorisonsoon.report.domain.repository;

import com.sorisonsoon.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
