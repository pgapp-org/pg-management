package com.pgapp.response.tenant;

import com.pgapp.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantApplicationResponse {
    private Long id;
//    private String status;

    private Long tenantId;
    private String tenantName;

    private Long pgId;
    private String pgName;

    private Long roomId;
    private String roomNumber;
    private Integer roomCapacity;
    private Integer roomOccupiedBeds;
    private Integer roomVacantBeds;

    private String bedNumber;
    private Boolean foodOpted;
    private Double finalMonthlyRent;
    private ApplicationStatus status;
    private Double advanceAmount;

    // new
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // payment summary
    private boolean tokenPaid;
    private boolean advancePaid;
    private boolean refundProcessed;
    private Double tokenAmount;
    private Double refundAmount;

}