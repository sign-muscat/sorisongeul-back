package com.sorisonsoon.guestbook.domain.repository;

import com.sorisonsoon.guestbook.domain.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

}
