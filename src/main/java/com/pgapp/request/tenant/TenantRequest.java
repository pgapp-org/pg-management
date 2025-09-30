package com.pgapp.request.tenant;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TenantRequest {
    private String name;
    private String email;
    private String phone;
}
