package com.pgapp.request.tenant;

import com.pgapp.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TenantApplicationRequest {
    private Long tenantId;
    private Long pgId;
    private Integer floorNumber;
    private String roomNumber;
    private String bedNumber;
    private Boolean foodOpted;
    private ApplicationStatus status;
    // new fields
    private LocalDate checkInDate;   // format "YYYY-MM-DD" in JSON
    private LocalDate checkOutDate;
}