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
import com.pgapp.request.PaymentRequest;
import com.pgapp.request.tenant.TenantApplicationRequest;
import com.pgapp.response.tenant.TenantApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
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
    private final BedRepository bedRepository;

    private final PaymentService paymentService; // injected to create first month rent automatically

    private final double TOKEN_AMOUNT = 1000.0; // global token amount

    // ✅ Apply for a PG room
    @Transactional
    public TenantApplicationResponse applyForRoom(TenantApplicationRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        PG pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found"));
        Floor floor = floorRepository.findByPgIdAndFloorNumber(pg.getId(), request.getFloorNumber())
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        Room room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        TenantApplication application = TenantApplicationConverter.fromRequest(request, tenant, pg, room);
        TenantApplication saved = applicationRepository.save(application);

        return TenantApplicationConverter.toResponse(saved);
    }

    // ✅ Update application status
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
            tenant.setPg(application.getPg());
            tenant.setRoom(room);
            tenant.setBedNumber(application.getBedNumber());
            bed.setTenant(tenant);
            bed.setOccupied(true);
            bedRepository.save(bed);
            room.setOccupiedBeds(room.getOccupiedBeds() + 1);
            roomRepository.save(room);
            tenantRepository.save(tenant);
        }

        application.setStatus(status);
        TenantApplication saved = applicationRepository.save(application);
        return TenantApplicationConverter.toResponse(saved);
    }

    // ✅ Check-in logic
    @Transactional
    public TenantApplicationResponse checkIn(Long applicationId) {
        TenantApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.isAdvancePaid()) {
            throw new RuntimeException("Advance payment is pending.");
        }

        if (!app.isFirstMonthRentPaid()) {
            // Create FIRST_MONTH_RENT payment based on pro-rated rent
            paymentService.createFirstMonthRentPayment(app);
        }

        if (!app.isFirstMonthRentPaid()) {
            throw new RuntimeException("First month rent payment is pending.");
        }

        app.setHasCheckedIn(true);
        app.setStatus(ApplicationStatus.ACTIVE);
        app.setCheckInDate(LocalDate.now());
        applicationRepository.save(app);

        return TenantApplicationConverter.toResponse(app);
    }

    // ✅ Get all applications for PG
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForPG(Long pgId) {
        return applicationRepository.findByPgId(pgId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get all applications for Tenant
    @Transactional(readOnly = true)
    public List<TenantApplicationResponse> getApplicationsForTenant(Long tenantId) {
        return applicationRepository.findByTenantId(tenantId).stream()
                .map(TenantApplicationConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TenantApplicationResponse applyCheckout(Long applicationId, LocalDate checkoutDate) {
        TenantApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setCheckOutDate(checkoutDate);
        app.setCheckoutRequested(true);

        TenantApplication saved = applicationRepository.save(app);
        return TenantApplicationConverter.toResponse(saved);
    }

    @Transactional
    public TenantApplicationResponse confirmCheckout(Long applicationId) {
        TenantApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.isCheckoutRequested()) {
            throw new RuntimeException("Tenant has not applied for checkout");
        }

        LocalDate checkoutDate = app.getCheckOutDate() != null ? app.getCheckOutDate() : LocalDate.now();

        // Calculate refundable amount WITHOUT extra charges
        double refund;
        if (app.getPg().isVariableAdvance()) {
            double perDayRent = app.getFinalMonthlyRent() / checkoutDate.lengthOfMonth();
            long daysStayed = java.time.temporal.ChronoUnit.DAYS.between(app.getCheckInDate(), checkoutDate) + 1;
            refund = app.getAdvanceAmount() - (perDayRent * daysStayed);
        } else {
            refund = app.getAdvanceAmount();
        }

        app.setRefundAmount(refund);
        app.setCheckoutConfirmed(true); // tenant confirmed
        TenantApplication saved = applicationRepository.save(app);

        return TenantApplicationConverter.toResponse(saved);
    }

//    @Transactional
//    public TenantApplicationResponse finalizeRefund(Long applicationId, double extraCharges) {
//        TenantApplication app = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        if (!app.isCheckoutConfirmed()) {
//            throw new RuntimeException("Tenant has not confirmed checkout yet");
//        }
//
//        // Subtract extra charges from already calculated refundable amount
//        double finalRefund = app.getRefundAmount() - extraCharges;
//        app.setRefundAmount(finalRefund);
//
//        // Free bed, room, tenant
//        Tenant tenant = app.getTenant();
//        Room room = tenant.getRoom();
//        if (room != null) {
//            room.setOccupiedBeds(room.getOccupiedBeds() - 1);
//            roomRepository.save(room);
//        }
//        Bed bed = tenant.getBed();
//        if (bed != null) {
//            bed.setOccupied(false);
//            bed.setTenant(null);
//            bedRepository.save(bed);
//        }
//        tenant.setPg(null);
//        tenant.setRoom(null);
//        tenant.setBed(null);
//        tenantRepository.save(tenant);
//
//        // Create REFUND payment
//        paymentService.makePayment(PaymentRequest.builder()
//                .tenantApplicationId(app.getId())
//                .amount(finalRefund)
//                .type(PaymentType.REFUND)
//                .build());
//
//        TenantApplication saved = applicationRepository.save(app);
//        return TenantApplicationConverter.toResponse(saved);
//    }

//    @Transactional
//    public String finalizeRefund(Long applicationId, double extraCharges) {
//        TenantApplication app = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        if (!app.isCheckoutConfirmed()) {
//            throw new RuntimeException("Tenant has not confirmed checkout yet");
//        }
//
//        double finalRefund = (app.getRefundAmount() != null ? app.getRefundAmount() - extraCharges : 0);
//        if (finalRefund < 0) finalRefund = 0;
//        app.setRefundAmount(finalRefund);
//
//        // ✅ Build Payment safely
//        Payment payment = new Payment();
//        payment.setTenantApplication(app);   // use managed entity directly
//        payment.setTenant(app.getTenant());   // optional, if needed
//        payment.setAmount(finalRefund);
//        payment.setType(PaymentType.REFUND);
//
//        paymentRepository.save(payment);  // Hibernate will now save without transient issues
//
//        // Free bed and room
//        Tenant tenant = app.getTenant();
//        if (tenant != null) {
//            Bed bed = tenant.getBed();
//            if (bed != null) {
//                bed.setOccupied(false);
//                bed.setTenant(null);
//                bedRepository.save(bed);
//            }
//
//            Room room = tenant.getRoom();
//            if (room != null) {
//                room.setOccupiedBeds(Math.max(room.getOccupiedBeds() - 1, 0));
//                roomRepository.save(room);
//            }
//
//            tenant.setPg(null);
//            tenant.setBed(null);
//            tenant.setRoom(null);
//            tenantRepository.save(tenant);
//        }
//
//        applicationRepository.delete(app);
//
//        return "Refund finalized successfully";
//    }



    @Transactional
    public String processRefund(Long applicationId, double extraCharges) {
        TenantApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.isCheckoutConfirmed()) {
            throw new RuntimeException("Checkout not confirmed yet");
        }

        double finalRefund = (app.getRefundAmount() != null ? app.getRefundAmount() - extraCharges : 0);
        if (finalRefund < 0) finalRefund = 0;
        app.setRefundAmount(finalRefund);
        app.setRefundProcessed(true);

        Payment payment = new Payment();
        payment.setTenantApplication(app);
        payment.setTenant(app.getTenant());
        payment.setAmount(finalRefund);
        payment.setType(PaymentType.REFUND);
        payment.setRentForMonth(LocalDate.now());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        applicationRepository.save(app);

        return "Refund payment recorded successfully";
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void runRefundCleanup() {
        finalizeRefundCleanup();
    }



    @Transactional
    public void finalizeRefundCleanup() {
        List<TenantApplication> apps = applicationRepository
                .findByCheckoutConfirmedTrueAndRefundProcessedTrue();

        for (TenantApplication app : apps) {
            Tenant tenant = app.getTenant();
            if (tenant != null) {
                Bed bed = tenant.getBed();
                if (bed != null) {
                    bed.setOccupied(false);
                    bed.setTenant(null);
                    bedRepository.save(bed);
                }

                Room room = tenant.getRoom();
                if (room != null) {
                    room.setOccupiedBeds(Math.max(room.getOccupiedBeds() - 1, 0));
                    roomRepository.save(room);
                }

                tenant.setPg(null);
                tenant.setBed(null);
                tenant.setRoom(null);
                tenantRepository.save(tenant);
            }

            applicationRepository.delete(app);
        }
    }

}
