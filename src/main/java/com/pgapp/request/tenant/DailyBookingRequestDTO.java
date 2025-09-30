package com.pgapp.request.tenant;

import com.pgapp.response.tenant.DailyGuestDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyBookingRequestDTO {
    private Long tenantId;
    private Long pgId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<DailyGuestDTO> guests;
}