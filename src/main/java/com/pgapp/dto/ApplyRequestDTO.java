package com.pgapp.dto;

import lombok.Data;

@Data
public class ApplyRequestDTO {
    private Long tenantId;
    private String pgName;
    private Integer floorNumber;
    private String roomNumber;
}
