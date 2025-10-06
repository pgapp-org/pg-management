//package com.pgapp.service;
//
//import com.pgapp.converter.PaymentConverter;
//import com.pgapp.entity.Payment;
////import com.pgapp.entity.PaymentType;
//import com.pgapp.entity.TenantApplication;
//import com.pgapp.enums.PaymentType;
//import com.pgapp.enums.TenantStatus;
//import com.pgapp.repository.PaymentRepository;
//import com.pgapp.repository.TenantApplicationRepository;
////import com.pgapp.request.payment.PaymentRequest;
////import com.pgapp.response.payment.PaymentResponse;
//import com.pgapp.request.PaymentRequest;
//import com.pgapp.response.PaymentResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final TenantApplicationRepository tenantApplicationRepository;
//
//    public PaymentResponse makePayment(PaymentRequest request) {
//        TenantApplication application = tenantApplicationRepository.findById(request.getTenantApplicationId())
//                .orElseThrow(() -> new RuntimeException("TenantApplication not found"));
//
//        Payment payment = new Payment();
//        payment.setTenantApplication(application);
//        payment.setAmount(request.getAmount());
//        payment.setType(PaymentType.valueOf(request.getType()));
//        payment.setPaymentDate(LocalDate.now());
//        payment.setTransactionId("TXN-" + UUID.randomUUID());
//        payment.setSuccess(true); // ✅ sandbox/test mode: always true for now
//
//        paymentRepository.save(payment);
//
//        // update application status based on payment type
//        switch (payment.getType()) {
//            case TOKEN -> application.setStatus(com.pgapp.enums.TenantStatus.TOKEN_PAID);
//            case ADVANCE -> application.setStatus(com.pgapp.enums.TenantStatus.ADVANCE_PAID);
//            case RENT -> application.setStatus(com.pgapp.enums.TenantStatus.ACTIVE);
//            case REFUND -> application.setStatus(com.pgapp.enums.TenantStatus.CHECKED_OUT);
//        }
//
//        tenantApplicationRepository.save(application);
//
//        return PaymentConverter.toResponse(payment);
//    }
//
//    public List<PaymentResponse> getPaymentsForApplication(Long applicationId) {
//        return paymentRepository.findByTenantApplicationId(applicationId).stream()
//                .map(PaymentConverter::toResponse)
//                .collect(Collectors.toList());
//    }
//}
