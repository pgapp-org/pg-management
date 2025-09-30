package com.pgapp.response.tenant;

import lombok.Data;

@Data
public class DailyGuestAllocationDTO {
    private Long guestId; // DailyBookingGuest id
    private Long roomId;
    private Long bedId;
}