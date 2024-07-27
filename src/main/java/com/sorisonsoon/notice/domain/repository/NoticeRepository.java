package com.sorisonsoon.notice.domain.repository;

import com.sorisonsoon.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
