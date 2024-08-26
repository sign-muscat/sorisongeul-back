package com.sorisonsoon.payment.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long payerId;
    private int amount;

    @CreatedDate
    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    // 필요한 경우 추가 필드들...
}