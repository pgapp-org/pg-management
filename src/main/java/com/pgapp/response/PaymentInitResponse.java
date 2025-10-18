package com.pgapp.response;


import lombok.Data;

@Data
public class PaymentInitResponse {
    private Long tenantApplicationId;
    private Double totalAmount;
    private String type;

    // Optional breakdown fields
    private Double tokenAmount;
    private Double advanceAmount;
    private Double firstMonthRent;
    private Double rentAmount;
    private Double gstAmount;
    private Double finalAmountWithGST;
}
