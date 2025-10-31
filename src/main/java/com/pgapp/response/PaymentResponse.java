package com.pgapp.response;

import com.pgapp.enums.PaymentStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentResponse {
    private Long id;
    private double amount;
    private String type;     // TOKEN, ADVANCE, RENT, REFUND
    private LocalDate paymentDate;
    private String transactionId;
    private boolean success;
    private PaymentStatus status;
}