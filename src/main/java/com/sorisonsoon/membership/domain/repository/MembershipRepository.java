package com.sorisonsoon.membership.domain.repository;

import com.sorisonsoon.membership.domain.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

}
