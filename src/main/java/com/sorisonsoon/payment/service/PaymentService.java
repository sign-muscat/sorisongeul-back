// package com.sorisonsoon.payment.service;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// @Service
// @Transactional
// @RequiredArgsConstructor
// public class PaymentService {

// }
package com.sorisonsoon.payment.service;

import com.sorisonsoon.payment.domain.entity.Payment;
import com.sorisonsoon.payment.domain.repository.PaymentRepository;
import com.sorisonsoon.payment.gateway.ExamplePaymentGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExamplePaymentGatewayClient paymentGatewayClient;

    public Payment createPayment(Long payerId, int amount, LocalDateTime payedAt) {
        Payment payment = new Payment();
        payment.setPayerId(payerId);
        payment.setAmount(amount);
        payment.setPayedAt(payedAt);
        Payment savedPayment = paymentRepository.save(payment);

        boolean paymentProcessed = paymentGatewayClient.processPayment(
            savedPayment.getPaymentId().toString(), savedPayment.getAmount());

        if (!paymentProcessed) {
            throw new RuntimeException("Payment processing failed");
        }

        return savedPayment;
    }

    @Transactional(readOnly = true)
    public List<Payment> getUserPayments(Long payerId) {
        return paymentRepository.findByPayerId(payerId);
    }

    @Transactional(readOnly = true)
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}