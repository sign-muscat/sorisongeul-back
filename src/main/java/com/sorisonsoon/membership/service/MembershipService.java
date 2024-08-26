package com.sorisonsoon.membership.service;

import com.sorisonsoon.membership.domain.entity.Membership;
import com.sorisonsoon.membership.domain.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public Membership createMembership(int userId, Long paymentId) {
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setPaymentId(paymentId);
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(1));
        membership.setActivate(true);
        return membershipRepository.save(membership);
    }

    @Transactional(readOnly = true)
    public boolean isUserSubscribed(int userId) {
        return membershipRepository.findByUserIdAndIsActivateTrue(userId).isPresent();
    }
}