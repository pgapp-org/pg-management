package com.pgapp.response.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentCollectionResponse {
    private String tenantName;
    private String phoneNumber;
    private String roomNumber;
    private Double rentAmount;
    private String status; // "PAID"
    private LocalDateTime paidDate;
}
