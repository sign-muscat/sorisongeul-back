package com.sorisonsoon.guestbook.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
    List<Guestbook> findByOrderByCreatedAtDesc(Pageable pageable);
}
