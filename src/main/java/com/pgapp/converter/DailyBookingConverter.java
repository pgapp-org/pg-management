package com.pgapp.converter;


import com.pgapp.response.tenant.DailyBookingResponseDTO;
import com.pgapp.response.tenant.DailyGuestDTO;
import com.pgapp.entity.DailyBookings;

import java.util.List;
import java.util.stream.Collectors;

public class DailyBookingConverter {

    public static DailyBookingResponseDTO toDTO(DailyBookings booking) {
        DailyBookingResponseDTO dto = new DailyBookingResponseDTO();
        dto.setId(booking.getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setStatus(booking.getStatus().name());
        dto.setTenantId(booking.getTenant() != null ? booking.getTenant().getId() : null);
        dto.setPgId(booking.getPg() != null ? booking.getPg().getId() : null);
        dto.setRoomId(booking.getRoom() != null ? booking.getRoom().getId() : null);

        dto.setFoodIncluded(booking.getFoodIncluded());  // ADD THIS
        dto.setDailyRate(booking.getDailyRate());        // optional if needed

        if (booking.getGuests() != null) {
            List<DailyGuestDTO> guestDTOs = booking.getGuests().stream().map(g -> {
                DailyGuestDTO guestDTO = new DailyGuestDTO();
                guestDTO.setId(g.getId()); // <-- Add this line
                guestDTO.setName(g.getName());
                guestDTO.setAge(g.getAge());
                guestDTO.setGender(g.getGender());
//                guestDTO.setPhone(g.getPhone());
                guestDTO.setBedId(g.getBed() != null ? g.getBed().getId() : null);
                guestDTO.setTenantId(g.getBooking().getTenant() != null ? g.getBooking().getTenant().getId() : null);
                guestDTO.setCheckInDate(g.getBooking().getCheckInDate());
                guestDTO.setCheckOutDate(g.getBooking().getCheckOutDate());
                return guestDTO;
            }).toList();
            dto.setGuests(guestDTOs);
        }

        return dto;
    }

}