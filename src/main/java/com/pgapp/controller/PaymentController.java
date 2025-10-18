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

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.makePayment(request);
        return ResponseEntity.ok(response);
    }

    // ✅ NEW ENDPOINT
    @GetMapping("/initiate/{applicationId}")
    public ResponseEntity<PaymentInitResponse> initiatePayment(@PathVariable Long applicationId) {
        PaymentInitResponse response = paymentService.initiatePayment(applicationId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/initiate-rent/{applicationId}")
    public ResponseEntity<PaymentInitResponse> initiateMonthlyRent(@PathVariable Long applicationId) {
        return ResponseEntity.ok(paymentService.initiateMonthlyRent(applicationId));
    }

}
