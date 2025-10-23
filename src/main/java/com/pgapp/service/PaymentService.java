////package com.pgapp.service;
////
////import com.pgapp.converter.PaymentConverter;
////import com.pgapp.entity.Payment;
//////import com.pgapp.entity.PaymentType;
////import com.pgapp.entity.TenantApplication;
////import com.pgapp.enums.PaymentType;
////import com.pgapp.enums.TenantStatus;
////import com.pgapp.repository.PaymentRepository;
////import com.pgapp.repository.TenantApplicationRepository;
//////import com.pgapp.request.payment.PaymentRequest;
//////import com.pgapp.response.payment.PaymentResponse;
////import com.pgapp.request.PaymentRequest;
////import com.pgapp.response.PaymentResponse;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.util.List;
////import java.util.UUID;
////import java.util.stream.Collectors;
////
////@Service
////@RequiredArgsConstructor
////public class PaymentService {
////
////    private final PaymentRepository paymentRepository;
////    private final TenantApplicationRepository tenantApplicationRepository;
////
////    public PaymentResponse makePayment(PaymentRequest request) {
////        TenantApplication application = tenantApplicationRepository.findById(request.getTenantApplicationId())
////                .orElseThrow(() -> new RuntimeException("TenantApplication not found"));
////
////        Payment payment = new Payment();
////        payment.setTenantApplication(application);
////        payment.setAmount(request.getAmount());
////        payment.setType(PaymentType.valueOf(request.getType()));
////        payment.setPaymentDate(LocalDate.now());
////        payment.setTransactionId("TXN-" + UUID.randomUUID());
////        payment.setSuccess(true); // ✅ sandbox/test mode: always true for now
////
////        paymentRepository.save(payment);
////
////        // update application status based on payment type
////        switch (payment.getType()) {
////            case TOKEN -> application.setStatus(com.pgapp.enums.TenantStatus.TOKEN_PAID);
////            case ADVANCE -> application.setStatus(com.pgapp.enums.TenantStatus.ADVANCE_PAID);
////            case RENT -> application.setStatus(com.pgapp.enums.TenantStatus.ACTIVE);
////            case REFUND -> application.setStatus(com.pgapp.enums.TenantStatus.CHECKED_OUT);
////        }
////
////        tenantApplicationRepository.save(application);
////
////        return PaymentConverter.toResponse(payment);
////    }
////
////    public List<PaymentResponse> getPaymentsForApplication(Long applicationId) {
////        return paymentRepository.findByTenantApplicationId(applicationId).stream()
////                .map(PaymentConverter::toResponse)
////                .collect(Collectors.toList());
////    }
////}
//package com.pgapp.service;
//
//import com.pgapp.converter.PaymentConverter;
//import com.pgapp.entity.Payment;
//import com.pgapp.entity.TenantApplication;
//import com.pgapp.enums.PaymentStatus;
//import com.pgapp.enums.PaymentType;
//import com.pgapp.repository.PaymentRepository;
//import com.pgapp.repository.TenantApplicationRepository;
//import com.pgapp.request.PaymentRequest;
//import com.pgapp.response.PaymentResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final TenantApplicationRepository tenantApplicationRepository;
//
//    @Transactional
//    public PaymentResponse makePayment(PaymentRequest request) {
//        TenantApplication application = tenantApplicationRepository.findById(request.getTenantApplicationId())
//                .orElseThrow(() -> new RuntimeException("Tenant application not found"));
//
//        PaymentType type = PaymentType.valueOf(request.getType().toUpperCase());
//
//        Payment payment = Payment.builder()
//                .tenantApplication(application)
//                .tenant(application.getTenant())
//                .type(type)
//                .amount(request.getAmount())
//                .status(PaymentStatus.SUCCESS) // Always success for now
//                .timestamp(LocalDateTime.now())
//                .transactionRef("TXN-" + UUID.randomUUID())
//                .build();
//
//        Payment saved = paymentRepository.save(payment);
//        return PaymentConverter.toResponse(saved);
//    }
//}

package com.pgapp.service;

import com.pgapp.entity.Payment;
import com.pgapp.entity.TenantApplication;
import com.pgapp.enums.PaymentStatus;
import com.pgapp.enums.PaymentType;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.PaymentRepository;
import com.pgapp.repository.TenantApplicationRepository;
import com.pgapp.request.PaymentRequest;
import com.pgapp.response.PaymentInitResponse;
import com.pgapp.response.PaymentResponse;
import com.pgapp.converter.PaymentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TenantApplicationRepository tenantApplicationRepository;

    private final double TOKEN_AMOUNT = 1000.0;

    // ✅ Make payment
//    @Transactional
//    public PaymentResponse makePayment(PaymentRequest request) {
//        TenantApplication app = tenantApplicationRepository.findById(request.getTenantApplicationId())
//                .orElseThrow(() -> new RuntimeException("Tenant application not found"));
//
//        PaymentType type = request.getType();
//
//
//        Payment payment = Payment.builder()
//                .tenantApplication(app)
//                .tenant(app.getTenant())
//                .type(type)
//                .amount(request.getAmount())
//                .status(PaymentStatus.SUCCESS)
//                .timestamp(LocalDateTime.now())
//                .transactionRef("TXN-" + UUID.randomUUID())
//                .build();
//
//        Payment savedPayment = paymentRepository.save(payment);
//
//        switch (type) {
//            case TOKEN -> {
//                app.setTokenPaid(true);
//                app.setTokenAmount(request.getAmount());
//            }
//            case ADVANCE -> app.setAdvancePaid(true);
//            case FIRST_MONTH_RENT -> app.setFirstMonthRentPaid(true);
//            case RENT -> {
//                    Payment pending = paymentRepository
//                            .findTopByTenantApplicationIdAndTypeAndStatusOrderByTimestampDesc(
//                                    app.getId(), PaymentType.RENT, PaymentStatus.PENDING)
//                            .orElseThrow(() -> new RuntimeException("No pending rent payment found"));
//
//                    pending.setStatus(PaymentStatus.SUCCESS);
//                    pending.setTimestamp(LocalDateTime.now());
//                    pending.setTransactionRef("TXN-" + UUID.randomUUID());
//                    pending.setAmount(request.getAmount());
//
//                    paymentRepository.save(pending);
//                    return PaymentConverter.toResponse(pending);
//
//
//            }
//
//            case REFUND -> {
//                app.setRefundProcessed(true);
//                app.setRefundAmount(request.getAmount());
//            }
//        }
//
//        if (app.isAdvancePaid() && app.isFirstMonthRentPaid()){
//            app.setHasCheckedIn(true);
//        }
//        tenantApplicationRepository.save(app);
//        return PaymentConverter.toResponse(savedPayment);
//    }
    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {
        TenantApplication app = tenantApplicationRepository.findById(request.getTenantApplicationId())
                .orElseThrow(() -> new RuntimeException("Tenant application not found"));

        PaymentType type = request.getType();
        Payment savedPayment = null;

        if (type == PaymentType.RENT) {
            // fetch pending RENT payment and update it
            System.out.println(app.getId()+" "+PaymentType.RENT+" "+PaymentStatus.PENDING);
            Payment pending = paymentRepository
                    .findTopByTenantApplicationIdAndTypeAndStatusOrderByTimestampDesc(
                            app.getId(), PaymentType.RENT, PaymentStatus.PENDING)
                    .orElseThrow(() -> new RuntimeException("No pending rent payment found"));

            pending.setStatus(PaymentStatus.SUCCESS);
            pending.setTimestamp(LocalDateTime.now());
            pending.setTransactionRef("TXN-" + UUID.randomUUID());
            pending.setAmount(request.getAmount());

            savedPayment = paymentRepository.save(pending);
        } else {
            // create new payment for TOKEN, ADVANCE, FIRST_MONTH_RENT, REFUND
            Payment payment = Payment.builder()
                    .tenantApplication(app)
                    .tenant(app.getTenant())
                    .type(type)
                    .amount(request.getAmount())
                    .status(PaymentStatus.SUCCESS)
                    .timestamp(LocalDateTime.now())
                    .transactionRef("TXN-" + UUID.randomUUID())
                    .build();

            savedPayment = paymentRepository.save(payment);
        }

        switch (type) {
            case TOKEN -> {
                app.setTokenPaid(true);
                app.setTokenAmount(request.getAmount());
            }
            case ADVANCE -> app.setAdvancePaid(true);
            case FIRST_MONTH_RENT -> app.setFirstMonthRentPaid(true);
            case RENT -> { System.out.println("done");}

            case REFUND -> {
                app.setRefundProcessed(true);
                app.setRefundAmount(request.getAmount());
            }
        }

        if (app.isAdvancePaid() && app.isFirstMonthRentPaid()){
            app.setHasCheckedIn(true);
        }
        tenantApplicationRepository.save(app);
        return PaymentConverter.toResponse(savedPayment);
    }


    // ✅ Initiate payment for check-in
    public PaymentInitResponse initiatePayment(Long tenantApplicationId) {
        TenantApplication app = tenantApplicationRepository.findById(tenantApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant Application not found"));

        PaymentInitResponse response = new PaymentInitResponse();

        // TOKEN
        if (!app.isTokenPaid()) {
            response.setTenantApplicationId(app.getId());
            response.setTokenAmount(TOKEN_AMOUNT);
            response.setTotalAmount(TOKEN_AMOUNT);
            response.setType("TOKEN");
            return response;
        }

        // ADVANCE
        if (!app.isAdvancePaid()) {
            double advance = app.getAdvanceAmount() != null ? app.getAdvanceAmount() : 0.0;
            response.setTenantApplicationId(app.getId());
            response.setAdvanceAmount(advance);
            response.setTotalAmount(advance);
            response.setType("ADVANCE");
            return response;
        }

        // FIRST_MONTH_RENT
        if (!app.isFirstMonthRentPaid()) {
            double rent = calculateFirstMonthRent(app);
            response.setTenantApplicationId(app.getId());
            response.setFirstMonthRent(rent);
            response.setTotalAmount(rent);
            response.setType("FIRST_MONTH_RENT");
            return response;
        }

        // NORMAL RENT
        double rent = app.getFinalMonthlyRent();
        double gst = rent * 0.18;
        double total = rent + gst;
        response.setTenantApplicationId(app.getId());
        response.setRentAmount(rent);
        response.setGstAmount(gst);
        response.setFinalAmountWithGST(total);
        response.setTotalAmount(total);
        response.setType("RENT");
        return response;
    }

    // ✅ Create first month rent payment (pro-rated)
    @Transactional
    public void createFirstMonthRentPayment(TenantApplication app) {
        if (app.isFirstMonthRentPaid()) return;

        double firstMonthRent = calculateFirstMonthRent(app);

        Payment firstMonthPayment = Payment.builder()
                .tenantApplication(app)
                .tenant(app.getTenant())
                .type(PaymentType.FIRST_MONTH_RENT)
                .amount(firstMonthRent)
                .status(PaymentStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .transactionRef("FIRSTMONTH-" + UUID.randomUUID())
                .build();

        paymentRepository.save(firstMonthPayment);
        app.setFirstMonthRentPaid(true);
        tenantApplicationRepository.save(app);
    }

    // ✅ Calculate pro-rated rent for first month
    private double calculateFirstMonthRent(TenantApplication app) {
        LocalDate checkIn = app.getCheckInDate() != null ? app.getCheckInDate() : LocalDate.now();
        YearMonth month = YearMonth.from(checkIn);
        int totalDays = month.lengthOfMonth();
        int remainingDays = totalDays - checkIn.getDayOfMonth() + 1;
        double dailyRent = app.getFinalMonthlyRent() / totalDays;
        return dailyRent * remainingDays;
    }

    // ✅ Generate monthly rent 5 days before month start
    @Scheduled(cron = "0 0 9 * * *") // runs every day at 9 AM
    @Transactional
    public void generateUpcomingRentPayments() {
//        LocalDate today = LocalDate.now();
        LocalDate today = LocalDate.of(2025, 10, 27);
        LocalDate nextMonth = today.plusMonths(1).withDayOfMonth(1);
        LocalDate generateDate = nextMonth.minusDays(5); // 5 days before next month

        if (!today.equals(generateDate)) return; // only run on the generateDate

        tenantApplicationRepository.findAll().forEach(app -> {
            if (app.isHasCheckedIn()) {
                boolean exists = paymentRepository.existsByTenantApplicationIdAndTypeAndRentForMonth(
                        app.getId(), PaymentType.RENT, nextMonth);
                if (!exists) {
                    double rent = app.getFinalMonthlyRent();
                    double gst = rent * 0.18;
                    double total = rent + gst;

                    Payment rentPayment = Payment.builder()
                            .tenantApplication(app)
                            .tenant(app.getTenant())
                            .type(PaymentType.RENT)
                            .amount(total)
                            .status(PaymentStatus.PENDING)
                            .rentForMonth(nextMonth)
                            .timestamp(LocalDateTime.now())
                            .transactionRef("AUTO-RENT-" + UUID.randomUUID())
                            .build();
                    paymentRepository.save(rentPayment);
                }
            }
        });
    }


    //    @Transactional
//    public PaymentInitResponse initiateMonthlyRent(Long tenantApplicationId) {
//        TenantApplication app = tenantApplicationRepository.findById(tenantApplicationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Tenant Application not found"));
//
//        if (!app.isHasCheckedIn()) {
//            throw new RuntimeException("Tenant has not checked in yet!");
//        }
//
//        // Determine current month (1st day of this month)
//        LocalDate rentForMonth = LocalDate.now().withDayOfMonth(1);
//
//        boolean exists = paymentRepository.existsByTenantApplicationIdAndTypeAndRentForMonth(
//                tenantApplicationId, PaymentType.RENT, rentForMonth);
//
//        Payment rentPayment;
//        if (exists) {
//            rentPayment = paymentRepository.findByTenantApplicationIdAndTypeAndRentForMonth(
//                    tenantApplicationId, PaymentType.RENT, rentForMonth).get();
//        } else {
//            double rent = app.getFinalMonthlyRent();
//            double gstRate = 0.18;
//            double gst = rent * gstRate;
//            double total = rent + gst;
//
//            rentPayment = Payment.builder()
//                    .tenantApplication(app)
//                    .tenant(app.getTenant())
//                    .type(PaymentType.RENT)
//                    .amount(total)
//                    .status(PaymentStatus.PENDING)
//                    .rentForMonth(rentForMonth)
//                    .timestamp(LocalDateTime.now())
//                    .transactionRef("RENT-" + UUID.randomUUID())
//                    .build();
//
//            paymentRepository.save(rentPayment);
//        }
//
//        PaymentInitResponse response = new PaymentInitResponse();
//        response.setTenantApplicationId(app.getId());
//        response.setRentAmount(app.getFinalMonthlyRent());
//        response.setGstAmount(app.getFinalMonthlyRent() * 0.18);
//        response.setFinalAmountWithGST(rentPayment.getAmount());
//        response.setTotalAmount(rentPayment.getAmount());
//        response.setType("RENT");
//
//        return response;
//    }
@Transactional
public PaymentInitResponse initiateMonthlyRent(Long tenantApplicationId) {
    // 1️⃣ Fetch tenant application
    TenantApplication app = tenantApplicationRepository.findById(tenantApplicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant Application not found"));

    if (!app.isHasCheckedIn()) {
        throw new RuntimeException("Tenant has not checked in yet!");
    }

    // 2️⃣ Determine current month (first day)
    LocalDate rentForMonth = LocalDate.now().withDayOfMonth(1);

    // 3️⃣ Skip creating rent for first month if already paid
    LocalDate checkInMonth = app.getCheckInDate().withDayOfMonth(1);
    if (checkInMonth.equals(rentForMonth) && app.isFirstMonthRentPaid()) {
        throw new RuntimeException("No pending rent for current month. First month already paid.");
    }

    // 4️⃣ Check if pending rent already exists
    Optional<Payment> rentPaymentOpt = paymentRepository.findByTenantApplicationIdAndTypeAndRentForMonth(
            tenantApplicationId, PaymentType.RENT, rentForMonth);

    Payment rentPayment;
    if (rentPaymentOpt.isPresent()) {
        rentPayment = rentPaymentOpt.get(); // use existing pending rent
    } else {
        // 5️⃣ Create new RENT row (only for months after first month)
        double rent = app.getFinalMonthlyRent();
        double gstRate = 0.18;
        double gst = rent * gstRate;
        double total = rent + gst;

        rentPayment = Payment.builder()
                .tenantApplication(app)
                .tenant(app.getTenant())
                .type(PaymentType.RENT)
                .amount(total)
                .status(PaymentStatus.PENDING)
                .rentForMonth(rentForMonth)
                .timestamp(LocalDateTime.now())
                .transactionRef("RENT-" + UUID.randomUUID())
                .build();

        paymentRepository.save(rentPayment);
    }

    // 6️⃣ Build response for frontend
    PaymentInitResponse response = new PaymentInitResponse();
    response.setTenantApplicationId(app.getId());
    response.setRentAmount(app.getFinalMonthlyRent());
    response.setGstAmount(app.getFinalMonthlyRent() * 0.18);
    response.setFinalAmountWithGST(rentPayment.getAmount());
    response.setTotalAmount(rentPayment.getAmount());
    response.setType("RENT");

    return response;
}


    @Transactional(readOnly = true)
    public List<PaymentResponse> getUpcomingRentPayments(Long tenantId) {
        return paymentRepository.findByTenantIdAndType(tenantId, PaymentType.RENT).stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                .map(PaymentConverter::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentHistory(Long tenantId) {
        return paymentRepository.findByTenantId(tenantId).stream()
                .map(PaymentConverter::toResponse)
                .toList();
    }

}
