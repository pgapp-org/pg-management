package com.pgapp.controller;
//
//import com.pgapp.dto.ApproveRequestDTO;
//import com.pgapp.dto.BookingRequest;
//import com.pgapp.dto.BookingResponseDTO;
//import com.pgapp.entity.DailyBookings;
//import com.pgapp.service.DailyBookingService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/daily-bookings")
//@CrossOrigin(origins = "*")
//public class DailyBookingController {
//
//    private final DailyBookingService bookingService;
//
//    public DailyBookingController(DailyBookingService bookingService) {
//        this.bookingService = bookingService;
//    }
//
//
//
////    @PostMapping
////    public ResponseEntity<DailyBookings> createBooking(@RequestBody BookingRequest request) {
////        return ResponseEntity.ok(bookingService.createBooking(request));
////    }
////
////
////    @PutMapping("/{id}/approve")
////    public ResponseEntity<DailyBookings> approveBooking(
////            @PathVariable Long id,
////            @RequestBody ApproveRequestDTO request) {
////        return ResponseEntity.ok(bookingService.approveBooking(id, request));
////    }
////
////
////    // Owner rejects
////    @PutMapping("/{id}/reject")
////    public ResponseEntity<DailyBookings> rejectBooking(@PathVariable Long id) {
////        return ResponseEntity.ok(bookingService.rejectBooking(id));
////    }
////
////    // Get all bookings
////    @GetMapping
////    public ResponseEntity<List<DailyBookings>> getAllBookings() {
////        return ResponseEntity.ok(bookingService.getAllBookings());
////    }
////
////    // Get pending bookings
////    @GetMapping("/pending")
////    public ResponseEntity<List<DailyBookings>> getPendingBookings() {
////        return ResponseEntity.ok(bookingService.getPendingBookings());
////    }
//
//    @PostMapping
//    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequest request) {
//        return ResponseEntity.ok(bookingService.createBooking(request));
//    }
//
//    @PutMapping("/{id}/approve")
//    public ResponseEntity<BookingResponseDTO> approveBooking(
//            @PathVariable Long id,
//            @RequestBody ApproveRequestDTO request) {
//        return ResponseEntity.ok(bookingService.approveBooking(id, request));
//    }
//
//    @PutMapping("/{id}/reject")
//    public ResponseEntity<BookingResponseDTO> rejectBooking(@PathVariable Long id) {
//        return ResponseEntity.ok(bookingService.rejectBooking(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
//        return ResponseEntity.ok(bookingService.getAllBookings());
//    }
//
//    @GetMapping("/pending")
//    public ResponseEntity<List<BookingResponseDTO>> getPendingBookings() {
//        return ResponseEntity.ok(bookingService.getPendingBookings());
//    }
//}


//package com.pgapp.controller.daily;

//import com.pgapp.dto.daily.*;
//import com.pgapp.service.daily.DailyBookingServiceDaily;
import com.pgapp.entity.DailyBookingGuest;
import com.pgapp.repository.DailyBookingGuestRepository;
import com.pgapp.request.tenant.DailyApproveRequestDTO;
import com.pgapp.request.tenant.DailyBookingRequestDTO;
import com.pgapp.response.RoomResponse;
import com.pgapp.response.tenant.DailyBookingResponseDTO;
import com.pgapp.response.tenant.DailyGuestDTO;
import com.pgapp.service.DailyBookingService;
//import com.pgapp.service.DailyBookingServiceDaily;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-bookings")
@CrossOrigin(origins = "*")
public class DailyBookingController {

    private final DailyBookingService bookingService;
    private final DailyBookingGuestRepository guestRepo;

    public DailyBookingController(DailyBookingService bookingService , DailyBookingGuestRepository guestRepo) {
        this.bookingService = bookingService;
        this.guestRepo = guestRepo;
    }

    @PostMapping
    public ResponseEntity<DailyBookingResponseDTO> createBooking(@RequestBody DailyBookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DailyBookingResponseDTO> approveBooking(
            @PathVariable Long id,
            @RequestBody DailyApproveRequestDTO request) {
        return ResponseEntity.ok(bookingService.approveBooking(id, request));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<DailyBookingResponseDTO> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    @GetMapping
    public ResponseEntity<List<DailyBookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DailyBookingResponseDTO>> getPendingBookings() {
        return ResponseEntity.ok(bookingService.getPendingBookings());
    }

    @GetMapping("/room/{roomId}/beds")
    public ResponseEntity<RoomResponse> getRoomWithBeds(@PathVariable Long roomId) {
        return ResponseEntity.ok(bookingService.getRoomWithBeds(roomId));
    }

    @GetMapping("/room/{roomId}/guests")
    public ResponseEntity<List<DailyGuestDTO>> getGuestsByRoom(@PathVariable Long roomId) {
        List<DailyBookingGuest> guests = guestRepo.findByRoomId(roomId);

        List<DailyGuestDTO> guestDTOs = guests.stream().map(guest -> {
            DailyGuestDTO dto = new DailyGuestDTO();
            dto.setId(guest.getId());
            dto.setName(guest.getName());
            dto.setAge(guest.getAge());
            dto.setGender(guest.getGender());
            dto.setBedId(guest.getBed().getId());
            dto.setCheckInDate(guest.getBooking().getCheckInDate());
            dto.setCheckOutDate(guest.getBooking().getCheckOutDate());
            return dto;
        }).toList();

        return ResponseEntity.ok(guestDTOs);
    }


}
