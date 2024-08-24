package com.sorisonsoon.payment.gateway;

import org.springframework.stereotype.Component;

@Component
public class ExamplePaymentGatewayClient {

    public boolean processPayment(String paymentId, int amount) {
        // 실제 결제 게이트웨이 연동 로직을 여기에 구현합니다.
        // 현재는 항상 true를 반환하는 것으로 가정합니다.
        System.out.println("Processing payment: " + paymentId + " for amount: " + amount);
        return true;
    }
}