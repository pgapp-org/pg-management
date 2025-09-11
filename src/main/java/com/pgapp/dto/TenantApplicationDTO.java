package com.pgapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantApplicationDTO {
    private Long id;
    private String status;

    private Long tenantId;
    private String tenantName;

    private Long pgId;
    private String pgName;

    private Long roomId;
    private String roomNumber;
    private Integer roomCapacity;
    private Integer roomOccupiedBeds;
    private Integer roomVacantBeds;
}
