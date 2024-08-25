package com.sorisonsoon.interest.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sorisonsoon.interest.domain.entity.Interest;
import com.sorisonsoon.user.domain.entity.User;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByUser(User user);

    void deleteByUser(User user);

}
