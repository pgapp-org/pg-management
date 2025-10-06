package com.pgapp.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long tenantApplicationId;   // which tenant application this belongs to
    private double amount;              // how much is being paid
    private String type;                // TOKEN, ADVANCE, RENT, REFUND (as String -> PaymentType enum)
}