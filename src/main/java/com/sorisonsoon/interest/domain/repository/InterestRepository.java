package com.sorisonsoon.interest.domain.repository;

import com.sorisonsoon.interest.domain.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

}
