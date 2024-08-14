package com.sorisonsoon.payment.service;

import com.sorisonsoon.payment.domain.entity.Payment;
import com.sorisonsoon.payment.domain.repository.PaymentRepository;
import com.sorisonsoon.payment.dto.PaymentDTO;
import com.sorisonsoon.payment.gateway.ExamplePaymentGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExamplePaymentGatewayClient paymentGatewayClient;

    public PaymentDTO createPayment(int payerId, int amount) {
        Payment payment = new Payment();
        payment.setPayerId(payerId);
        payment.setAmount(amount);
        // payedAt is automatically set by @CreatedDate
        Payment savedPayment = paymentRepository.save(payment);

        boolean paymentProcessed = paymentGatewayClient.processPayment(
            savedPayment.getPaymentId().toString(), savedPayment.getAmount());

        if (!paymentProcessed) {
            throw new RuntimeException("Payment processing failed");
        }

        return convertToPaymentDTO(savedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getUserPayments(int payerId) {
        List<Payment> payments = paymentRepository.findByPayerId(payerId);
        return payments.stream().map(this::convertToPaymentDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToPaymentDTO(payment);
    }

    private PaymentDTO convertToPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPayerId(payment.getPayerId());
        dto.setAmount(payment.getAmount());
        dto.setPayedAt(payment.getPayedAt());
        return dto;
    }
}