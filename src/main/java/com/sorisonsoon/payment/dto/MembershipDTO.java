package com.sorisonsoon.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MembershipDTO {
    private Long membershipId;
    private int userId;
    private Long paymentId;
    private String membershipType;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActivate;
}