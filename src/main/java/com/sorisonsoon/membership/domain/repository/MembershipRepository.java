package com.sorisonsoon.membership.domain.repository;

import com.sorisonsoon.membership.domain.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserIdAndIsActivateTrue(int userId);
    Optional<Membership> findByUserId(int userId);
}
