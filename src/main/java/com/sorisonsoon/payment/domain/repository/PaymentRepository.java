package com.sorisonsoon.payment.domain.repository;

import com.sorisonsoon.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPayerId(int payerId);
}