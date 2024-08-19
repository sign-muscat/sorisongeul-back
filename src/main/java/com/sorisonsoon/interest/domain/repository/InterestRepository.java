package com.sorisonsoon.interest.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sorisonsoon.interest.domain.entity.Interest;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

