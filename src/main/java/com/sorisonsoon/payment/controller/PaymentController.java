package com.sorisonsoon.payment.controller;

import com.sorisonsoon.payment.dto.PaymentDTO;
import com.sorisonsoon.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sorisonsoon.payment.domain.entity.Payment;
import com.sorisonsoon.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @RequestParam Long payerId,
            @RequestParam int amount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime payedAt
    ) {
        logger.info("Received payment request: payerId={}, amount={}, payedAt={}", payerId, amount, payedAt);

        LocalDateTime effectivePayedAt = (payedAt != null) ? payedAt : LocalDateTime.now();

        logger.info("Effective payedAt: {}", effectivePayedAt);

        try {
            Payment payment = paymentService.createPayment(payerId, amount, effectivePayedAt);
            logger.info("Payment created: {}", payment);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error creating payment", e);
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable Long userId) {
        try {
            List<Payment> payments = paymentService.getUserPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.getPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }




}