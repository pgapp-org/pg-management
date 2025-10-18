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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TenantApplicationRepository tenantApplicationRepository;

    @Transactional
    public PaymentResponse makePayment(PaymentRequest request) {

        TenantApplication app = tenantApplicationRepository.findById(request.getTenantApplicationId())
                .orElseThrow(() -> new RuntimeException("Tenant application not found"));

        PaymentType type = request.getType();


        Payment payment = Payment.builder()
                .tenantApplication(app)
                .tenant(app.getTenant())
                .type(type)
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)  // for dummy payment, mark success
                .timestamp(LocalDateTime.now())
                .transactionRef("TXN-" + UUID.randomUUID())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update TenantApplication based on payment type
        switch (type) {
            case TOKEN -> {
                app.setTokenPaid(true);
                System.out.println(app.getTokenAmount()+" "+app.isTokenPaid());
                app.setTokenAmount(request.getAmount());
            }
            case ADVANCE -> {
                app.setAdvancePaid(true);
                app.setHasCheckedIn(true);
            }
            case RENT -> {
                app.setFirstMonthRentPaid(true);
                // you can store monthly rent payment logic if needed
                // for example, add to a list of rentPayments or mark lastPaidMonth
            }
            case REFUND -> {
                app.setRefundProcessed(true);
                app.setRefundAmount(request.getAmount());
            }
        }

        tenantApplicationRepository.save(app);

        return PaymentConverter.toResponse(savedPayment);
    }

    public PaymentInitResponse initiatePayment(Long tenantApplicationId) {
        TenantApplication app = tenantApplicationRepository.findById(tenantApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant Application not found"));

        PaymentInitResponse response = new PaymentInitResponse();

        // Case 1️⃣: TOKEN
        if (!app.isTokenPaid()) {
            response.setTenantApplicationId(app.getId());
            response.setTokenAmount(1000.0);
            response.setTotalAmount(1000.0);
            response.setType("TOKEN");
            return response;
        }

        // Case 2️⃣: ADVANCE
        else if (!app.isAdvancePaid()) {
            double advance = app.getAdvanceAmount() != null ? app.getAdvanceAmount() : 0.0;
            double rent = app.getFinalMonthlyRent() != null ? app.getFinalMonthlyRent() : 0.0;

            double gstRate = 0.18;
            double gst = (advance + rent) * gstRate;
            double total = advance + rent + gst;

            response.setTenantApplicationId(app.getId());
            response.setAdvanceAmount(advance);
            response.setFirstMonthRent(rent);
            response.setGstAmount(gst);
            response.setFinalAmountWithGST(total);
            response.setTotalAmount(total);
            response.setType("ADVANCE");
            return response;
        }

        // Case 3️⃣: RENT
        else {
            double rent = app.getFinalMonthlyRent() != null ? app.getFinalMonthlyRent() : 0.0;

            double gstRate = 0.18;
            double gst = rent * gstRate;
            double total = rent + gst;

            response.setTenantApplicationId(app.getId());
            response.setRentAmount(rent);
            response.setGstAmount(gst);
            response.setFinalAmountWithGST(total);
            response.setTotalAmount(total);
            response.setType("RENT");
            return response;
        }
    }


    // Run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void generateUpcomingRentPayments() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1).withDayOfMonth(1);
        LocalDate generateDate = nextMonth.minusDays(5); // generate 5 days before new month

        if (today.equals(generateDate)) {
            System.out.println("📅 Generating rent payments for next month...");

            tenantApplicationRepository.findAll().forEach(app -> {
                if (app.isHasCheckedIn()) {
                    boolean exists = paymentRepository.existsByTenantApplicationIdAndTypeAndRentForMonth(
                            app.getId(), PaymentType.RENT, nextMonth);

                    if (!exists) {
                        double rent = app.getFinalMonthlyRent();
                        double gstRate = 0.18;
                        double gst = rent * gstRate;
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
                        System.out.println("✅ Rent entry created for: " + app.getTenant().getName());
                    }
                }
            });
        }
    }

    @Transactional
    public PaymentInitResponse initiateMonthlyRent(Long tenantApplicationId) {
        TenantApplication app = tenantApplicationRepository.findById(tenantApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant Application not found"));

        if (!app.isHasCheckedIn()) {
            throw new RuntimeException("Tenant has not checked in yet!");
        }

        // Determine current month (1st day of this month)
        LocalDate rentForMonth = LocalDate.now().withDayOfMonth(1);

        boolean exists = paymentRepository.existsByTenantApplicationIdAndTypeAndRentForMonth(
                tenantApplicationId, PaymentType.RENT, rentForMonth);

        Payment rentPayment;

        if (exists) {
            // Rent entry already exists for this month
            rentPayment = paymentRepository.findByTenantApplicationIdAndTypeAndRentForMonth(
                    tenantApplicationId, PaymentType.RENT, rentForMonth
            ).get();
        } else {
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

        // Prepare response
        PaymentInitResponse response = new PaymentInitResponse();
        response.setTenantApplicationId(app.getId());
        response.setRentAmount(app.getFinalMonthlyRent());
        response.setGstAmount(app.getFinalMonthlyRent() * 0.18);
        response.setFinalAmountWithGST(rentPayment.getAmount());
        response.setTotalAmount(rentPayment.getAmount());
        response.setType("RENT");

        return response;
    }

}
