package com.pgapp.request.tenant;

import com.pgapp.enums.ApplicationStatus;
import lombok.Data;

@Data
public class TenantApplicationRequest {
    private Long tenantId;
    private String pgName;
    private Integer floorNumber;
    private String roomNumber;
    private String bedNumber;
    private Boolean foodOpted;
    private ApplicationStatus status;
}