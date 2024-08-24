package com.sorisonsoon.payment.controller;

import com.sorisonsoon.payment.dto.PaymentDTO;
import com.sorisonsoon.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestParam int payerId, @RequestParam int amount) {
        return ResponseEntity.ok(paymentService.createPayment(payerId, amount));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(@PathVariable int userId) {
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }


}