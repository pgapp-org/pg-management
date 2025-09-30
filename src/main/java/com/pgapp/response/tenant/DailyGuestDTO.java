package com.pgapp.response.tenant;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyGuestDTO {
    private  Long id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private Long bedId;
    private Long tenantId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}