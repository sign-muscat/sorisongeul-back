package com.sorisonsoon.payment.domain.repository;

import com.sorisonsoon.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
