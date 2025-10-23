//package com.pgapp.controller;
//
//
//import com.pgapp.request.PaymentRequest;
//import com.pgapp.response.PaymentResponse;
//import com.pgapp.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final PaymentService paymentService;
//
//    // 🔹 Make a payment (token, advance, rent, refund)
//    @PostMapping("/make")
//    public PaymentResponse makePayment(@RequestBody PaymentRequest request) {
//        return paymentService.makePayment(request);
//    }
//
//    // 🔹 Get all payments for a tenant application
//    @GetMapping("/application/{applicationId}")
//    public List<PaymentResponse> getPaymentsForApplication(@PathVariable Long applicationId) {
//        return paymentService.getPaymentsForApplication(applicationId);
//    }
//}

package com.pgapp.controller;

import com.pgapp.request.PaymentRequest;
import com.pgapp.response.PaymentInitResponse;
import com.pgapp.response.PaymentResponse;
import com.pgapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Make any payment (TOKEN / ADVANCE / RENT / REFUND)
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.makePayment(request);
        return ResponseEntity.ok(response);
    }

    // Initiate payment (TOKEN or ADVANCE)
    @GetMapping("/initiate/{applicationId}")
    public ResponseEntity<PaymentInitResponse> initiatePayment(@PathVariable Long applicationId) {
        PaymentInitResponse response = paymentService.initiatePayment(applicationId);
        return ResponseEntity.ok(response);
    }

    // Initiate monthly rent for a tenant
    @PostMapping("/initiate-rent/{applicationId}")
    public ResponseEntity<PaymentInitResponse> initiateMonthlyRent(@PathVariable Long applicationId) {
        PaymentInitResponse response = paymentService.initiateMonthlyRent(applicationId);
        return ResponseEntity.ok(response);
    }

    // Get upcoming PENDING rent payments for tenant
    @GetMapping("/upcoming/{tenantId}")
    public ResponseEntity<List<PaymentResponse>> getUpcomingRent(@PathVariable Long tenantId) {
        List<PaymentResponse> response = paymentService.getUpcomingRentPayments(tenantId);
        return ResponseEntity.ok(response);
    }

    // Get all payments (history) for tenant
    @GetMapping("/history/{tenantId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(@PathVariable Long tenantId) {
        List<PaymentResponse> response = paymentService.getPaymentHistory(tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-generate-rent")
    public ResponseEntity<String> testGenerateRent() {
        paymentService.generateUpcomingRentPayments();
        return ResponseEntity.ok("Rent generation executed!");
    }

}
