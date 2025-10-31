package com.pgapp.response.owner;

import com.pgapp.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class RentStatusResponse {
    private String tenantName;
    private String phoneNumber;
    private String roomNumber;
    private Double rentAmount;
    private String status; // "PENDING"

    public RentStatusResponse(String tenantName, String phoneNumber, String roomNumber, Double rentAmount, PaymentStatus status) {
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.rentAmount = rentAmount;
        this.status = status.toString(); // convert enum → string
    }

}
