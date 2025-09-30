package com.pgapp.response.tenant;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyBookingResponseDTO {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private Long tenantId;
    private Long pgId;
    private Long roomId;
    private List<DailyGuestDTO> guests;
}
