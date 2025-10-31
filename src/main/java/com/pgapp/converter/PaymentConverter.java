package com.pgapp.converter;

import com.pgapp.entity.Payment;
import com.pgapp.response.PaymentResponse;

import java.time.LocalDate;

public class PaymentConverter {

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) return null;

        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setType(payment.getType() != null ? payment.getType().name() : null);
        response.setPaymentDate(payment.getTimestamp() != null ? payment.getTimestamp().toLocalDate() : null);
        response.setTransactionId(payment.getTransactionRef());
        response.setSuccess(payment.getStatus() != null && payment.getStatus().name().equals("SUCCESS"));
        response.setStatus(payment.getStatus());
        return response;
    }
}
