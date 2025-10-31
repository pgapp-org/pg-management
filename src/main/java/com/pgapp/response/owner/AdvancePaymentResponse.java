package com.pgapp.response.owner;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.pgapp.enums.PaymentStatus;

@Data
public class AdvancePaymentResponse {
    private String tenantName;
    private String phoneNumber;
    private String roomNumber;
    private LocalDate checkInDate;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paidDate;

    public AdvancePaymentResponse(String tenantName, String phoneNumber, String roomNumber,
                                  LocalDate checkInDate, Double amount, PaymentStatus status,
                                  LocalDateTime paidDate) {
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.amount = amount;
        this.status = status;
        this.paidDate = paidDate;
    }

    public AdvancePaymentResponse() {}
}
