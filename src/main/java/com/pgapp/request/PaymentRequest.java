package com.pgapp.request;

import com.pgapp.enums.PaymentType;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long tenantApplicationId;   // which tenant application this belongs to
    private double amount;              // how much is being paid
    private PaymentType type;                // TOKEN, ADVANCE, RENT, REFUND (as String -> PaymentType enum)
}