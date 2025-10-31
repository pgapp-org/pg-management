package com.pgapp.response.owner;

import com.pgapp.enums.PaymentStatus;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class RefundResponse {
    private String tenantName;
    private String tenantPhone;
    private String roomNumber;
    private LocalDate checkOutDate;
    private Double advanceAmount;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime timestamp;

    // ✅ Constructor used in the JPQL query
    public RefundResponse(String tenantName, String tenantPhone, String roomNumber,
                          LocalDate checkOutDate, Double advanceAmount,
                          Double refundAmount, PaymentStatus status, LocalDateTime timestamp) {
        this.tenantName = tenantName;
        this.tenantPhone = tenantPhone;
        this.roomNumber = roomNumber;
        this.checkOutDate = checkOutDate;
        this.advanceAmount = advanceAmount;
        this.amount = refundAmount;
        this.status = status;
        this.timestamp = timestamp;
    }
}
