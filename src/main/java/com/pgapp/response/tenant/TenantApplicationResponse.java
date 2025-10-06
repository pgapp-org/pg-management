package com.pgapp.response.tenant;

import com.pgapp.enums.ApplicationStatus;
import lombok.*;

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
}