package com.pgapp.response;


import com.pgapp.response.tenant.TenantResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BedResponse {
    private Long id;
    private Integer bedNumber;
    private boolean occupied;
    private boolean shortTermOccupied;
    private LocalDate shortTermUntil;
    private TenantResponse tenant;


    private TenantResponse shortTermGuest;  // short-term guest info

}