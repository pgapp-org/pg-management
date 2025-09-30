package com.pgapp.response.tenant;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TenantResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;

}
