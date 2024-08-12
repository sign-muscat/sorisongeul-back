package com.sorisonsoon.payment.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    private int payerId;
    private int amount;

    @CreatedDate
    private LocalDateTime payedAt;

    // Getter 메서드들 (필요한 경우)
    public Long getPaymentId() {
        return paymentId;
    }

    public int getPayerId() {
        return payerId;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getPayedAt() {
        return payedAt;
    }
}