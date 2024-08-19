package com.sorisonsoon.guestbook.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;
@Repository
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    List<Guestbook> findTopNByOrderByCreatedAtDesc(int limit);
}
