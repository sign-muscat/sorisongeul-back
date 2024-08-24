package com.sorisonsoon.membership.controller;

import com.sorisonsoon.membership.domain.entity.Membership;
import com.sorisonsoon.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<Membership> createMembership(@RequestParam int userId, @RequestParam Long paymentId) {
        Membership membership = membershipService.createMembership(userId, paymentId);
        return ResponseEntity.ok(membership);
    }

    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<Boolean> isUserSubscribed(@PathVariable int userId) {
        boolean isSubscribed = membershipService.isUserSubscribed(userId);
        return ResponseEntity.ok(isSubscribed);
    }
}