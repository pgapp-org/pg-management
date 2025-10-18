////package com.pgapp.service;
////
////import com.pgapp.converter.tenant.TenantApplicationConverter;
////import com.pgapp.entity.*;
////import com.pgapp.enums.ApplicationStatus;
////import com.pgapp.repository.*;
////import com.pgapp.request.tenant.TenantApplicationRequest;
////import com.pgapp.response.tenant.TenantApplicationResponse;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.time.LocalDate;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////@RequiredArgsConstructor
////public class TenantApplicationService {
////
////    private final TenantApplicationRepository applicationRepository;
////    private final TenantRepository tenantRepository;
////    private final PGRepository pgRepository;
////    private final RoomRepository roomRepository;
////    private final FloorRepository floorRepository;
////    private final DailyBookingGuestRepository dailyBookingGuestRepository;
////
////
////    // ✅ Apply for a PG room
////    @Transactional
////    public TenantApplicationResponse applyForRoom(TenantApplicationRequest request) {
////        Tenant tenant = tenantRepository.findById(request.getTenantId())
////                .orElseThrow(() -> new RuntimeException("Tenant not found"));
////
////        System.out.println("Searching PG with name: '" + request.getPgName() + "'");
////
////        PG pg = pgRepository.findByName(request.getPgName())
////                .orElseThrow(() -> new RuntimeException("PG not found"));
////
////        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), request.getFloorNumber())
////                .orElseThrow(() -> new RuntimeException("Floor not found"));
////
////        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), request.getRoomNumber())
////                .orElseThrow(() -> new RuntimeException("Room not found"));
////
////        TenantApplication application = TenantApplicationConverter.fromRequest(request, tenant, pg, room);
////        TenantApplication saved = applicationRepository.save(application);
////
////        return TenantApplicationConverter.toResponse(saved);
////    }
////
////    // ✅ Get all applications for a PG
////    @Transactional(readOnly = true)
////    public List<TenantApplicationResponse> getApplicationsForPG(Long pgId) {
////        return applicationRepository.findByPgId(pgId).stream()
////                .map(TenantApplicationConverter::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    // ✅ Get all applications for a Tenant
////    @Transactional(readOnly = true)
////    public List<TenantApplicationResponse> getApplicationsForTenant(Long tenantId) {
////        return applicationRepository.findByTenantId(tenantId).stream()
////                .map(TenantApplicationConverter::toResponse)
////                .collect(Collectors.toList());
////    }
////
////    // ✅ Update application status (APPROVED / REJECTED / PENDING)
////    @Transactional
////    public TenantApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
////        TenantApplication application = applicationRepository.findById(applicationId)
////                .orElseThrow(() -> new RuntimeException("Application not found"));
////
////        Room room = application.getRoom();
////        Tenant tenant = application.getTenant();
////
////        // Assign bed if approved
////        if (status == ApplicationStatus.APPROVED) {
////            int bedNum = Integer.parseInt(application.getBedNumber());
////            Bed bed = room.getBeds().stream()
////                    .filter(b -> b.getBedNumber() == bedNum)
////                    .findFirst()
////                    .orElseThrow(() -> new RuntimeException("Bed not found in this room"));
////
////            // Check for short-term conflict
////            // Check for short-term conflicts
////            // Check for short-term conflicts
////            List<DailyBookingGuest> shortTermGuests = dailyBookingGuestRepository.findByBedId(bed.getId());
////            for (DailyBookingGuest guest : shortTermGuests) {
////                DailyBookings booking = guest.getBooking();  // now available
////                if (booking != null && booking.getCheckOutDate() != null &&
////                        !booking.getCheckOutDate().isBefore(LocalDate.now())) {
////                    throw new RuntimeException(
////                            "Bed is reserved for short-term until " + booking.getCheckOutDate()
////                    );
////                }
////            }
////
////
////
////
////            if (bed.isOccupied()) {
////                throw new RuntimeException("This bed is already occupied");
////            }
////
////            // Assign tenant
////            tenant.setPg(application.getPg());
////            tenant.setRoom(room);
////            tenant.setBedNumber(application.getBedNumber());
////            bed.setOccupied(true);
////            bed.setTenant(tenant);
////
////            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
////            roomRepository.save(room);
////            tenantRepository.save(tenant);
////        }
////
////        application.setStatus(status);
////        TenantApplication saved = applicationRepository.save(application);
////        return TenantApplicationConverter.toResponse(saved);
////    }
////}
//
//
//package com.pgapp.service;
//
//import com.pgapp.converter.tenant.TenantApplicationConverter;
//import com.pgapp.entity.*;
//import com.pgapp.enums.ApplicationStatus;
//import com.pgapp.repository.*;
//import com.pgapp.request.tenant.TenantApplicationRequest;
//import com.pgapp.response.tenant.TenantApplicationResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class TenantApplicationService {
//
//    private final TenantApplicationRepository applicationRepository;
//    private final TenantRepository tenantRepository;
//    private final PGRepository pgRepository;
//    private final RoomRepository roomRepository;
//    private final FloorRepository floorRepository;
//    private final DailyBookingGuestRepository dailyBookingGuestRepository;
//
//    // ✅ Apply for a PG room
//    @Transactional
//    public TenantApplicationResponse applyForRoom(TenantApplicationRequest request) {
//        Tenant tenant = tenantRepository.findById(request.getTenantId())
//                .orElseThrow(() -> new RuntimeException("Tenant not found"));
//
//        PG pg = pgRepository.findByName(request.getPgName())
//                .orElseThrow(() -> new RuntimeException("PG not found"));
//
//        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), request.getFloorNumber())
//                .orElseThrow(() -> new RuntimeException("Floor not found"));
//
//        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), request.getRoomNumber())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        TenantApplication application = TenantApplicationConverter.fromRequest(request, tenant, pg, room);
//        TenantApplication saved = applicationRepository.save(application);
//
//        return TenantApplicationConverter.toResponse(saved);
//    }
//
//    // ✅ Get all applications for a PG
//    @Transactional(readOnly = true)
//    public List<TenantApplicationResponse> getApplicationsForPG(Long pgId) {
//        return applicationRepository.findByPgId(pgId).stream()
//                .map(TenantApplicationConverter::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // ✅ Get all applications for a Tenant
//    @Transactional(readOnly = true)
//    public List<TenantApplicationResponse> getApplicationsForTenant(Long tenantId) {
//        return applicationRepository.findByTenantId(tenantId).stream()
//                .map(TenantApplicationConverter::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    // ✅ Update application status (APPROVED / REJECTED / PENDING)
//    @Transactional
//    public TenantApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
//        TenantApplication application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        Room room = application.getRoom();
//        Tenant tenant = application.getTenant();
//
//        if (status == ApplicationStatus.APPROVED) {
//            int bedNum = Integer.parseInt(application.getBedNumber());
//            Bed bed = room.getBeds().stream()
//                    .filter(b -> b.getBedNumber() == bedNum)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("Bed not found in this room"));
//
//            if (bed.isOccupied()) {
//                throw new RuntimeException("This bed is already occupied");
//            }
//
//            // Assign tenant
//            tenant.setPg(application.getPg());
//            tenant.setRoom(room);
//            tenant.setBedNumber(application.getBedNumber());
//            bed.setOccupied(true);
//            bed.setTenant(tenant);
//
//            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
//            roomRepository.save(room);
//            tenantRepository.save(tenant);
//        }
//
//        application.setStatus(status);
//        TenantApplication saved = applicationRepository.save(application);
//        return TenantApplicationConverter.toResponse(saved);
//    }
//}


package com.pgapp.service;

import com.pgapp.converter.tenant.TenantApplicationConverter;
import com.pgapp.entity.*;
import com.pgapp.enums.ApplicationStatus;
import com.pgapp.enums.PaymentStatus;
import com.pgapp.enums.PaymentType;
import com.pgapp.repository.*;
import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantApplicationService {

    private final TenantApplicationRepository applicationRepository;
    private final TenantRepository tenantRepository;
    private final PGRepository pgRepository;
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final PaymentRepository paymentRepository;

    private final double TOKEN_AMOUNT = 1000.0; // global token amount

    // ✅ Apply for a PG room + automatic token payment
    @Transactional
    public TenantApplicationResponse applyForRoom(TenantApplicationRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        System.out.println("pg Id: "+request.getPgId());
        PG pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found"));

        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), request.getFloorNumber())
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        TenantApplication application = TenantApplicationConverter.fromRequest(request, tenant, pg, room);
        TenantApplication saved = applicationRepository.save(application);

        // Create dummy token payment
//        Payment tokenPayment = Payment.builder()
//                .tenantApplication(saved)
//                .tenant(tenant)
//                .type(PaymentType.TOKEN)
//                .amount(TOKEN_AMOUNT)
//                .status(PaymentStatus.SUCCESS)
//                .timestamp(LocalDateTime.now())
//                .transactionRef("TXN-" + UUID.randomUUID())
//                .build();
//
//        paymentRepository.save(tokenPayment);

        return TenantApplicationConverter.toResponse(saved);
    }

    // ✅ Get all applications for a PG
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForPG(Long pgId) {
        return applicationRepository.findByPgId(pgId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get all applications for a Tenant
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForTenant(Long tenantId) {
        return applicationRepository.findByTenantId(tenantId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Update application status + assign bed + prepare for advance payment
    @Transactional
    public TenantApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        TenantApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Room room = application.getRoom();
        Tenant tenant = application.getTenant();

        if (status == ApplicationStatus.APPROVED) {
            int bedNum = Integer.parseInt(application.getBedNumber());
            Bed bed = room.getBeds().stream()
                    .filter(b -> b.getBedNumber() == bedNum)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Bed not found in this room"));

            if (bed.isOccupied()) {
                throw new RuntimeException("This bed is already occupied");
            }

            // Assign tenant
            tenant.setPg(application.getPg());
            tenant.setRoom(room);
            tenant.setBedNumber(application.getBedNumber());
            bed.setOccupied(true);
            bed.setTenant(tenant);

            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
            roomRepository.save(room);
            tenantRepository.save(tenant);

            // **Advance payment is now due** (can be paid via PaymentController)
        }

        application.setStatus(status);
        TenantApplication saved = applicationRepository.save(application);
        return TenantApplicationConverter.toResponse(saved);
    }

    @Transactional
    public TenantApplicationResponse checkIn(Long applicationId) {
        TenantApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // 🔐 Ensure tenant paid advance and first month rent
        if (!app.isAdvancePaid()) {
            throw new RuntimeException("Advance payment is pending.");
        }

        // ✅ Check if first-month rent was paid through the Payment entity
        boolean rentPaid = paymentRepository.existsByTenantApplicationIdAndType(app.getId(), PaymentType.RENT);
        if (!rentPaid) {
            throw new RuntimeException("Please pay first-month rent before check-in.");
        }

        // ✅ Mark check-in
        app.setHasCheckedIn(true);
        System.out.println(app.isHasCheckedIn());
        app.setStatus(ApplicationStatus.ACTIVE);
        app.setCheckInDate(java.time.LocalDate.now());
        applicationRepository.save(app);

        return TenantApplicationConverter.toResponse(app);
    }

}
