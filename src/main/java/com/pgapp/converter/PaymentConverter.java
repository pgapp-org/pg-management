package com.pgapp.converter;

import com.pgapp.entity.Payment;
import com.pgapp.response.PaymentResponse;

public class PaymentConverter {

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) return null;

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setType(payment.getType().name());
        response.setPaymentDate(payment.getPaymentDate());
        response.setTransactionId(payment.getTransactionId());
        response.setSuccess(payment.isSuccess());

        return response;
    }
}