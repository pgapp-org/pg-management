package com.pgapp.controller;

import com.pgapp.response.owner.RentStatusResponse;
import com.pgapp.response.owner.RevenueSummaryResponse;
import com.pgapp.service.OwnerFinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner/finance")
@CrossOrigin(origins = "http://localhost:4200")
public class OwnerFinanceController {

    private final OwnerFinanceService ownerFinanceService;

    public OwnerFinanceController(OwnerFinanceService ownerFinanceService) {
        this.ownerFinanceService = ownerFinanceService;
    }

//    @GetMapping("/pending-rents")
//    public ResponseEntity<Map<String, Object>> getPendingRents() {
//        return ResponseEntity.ok(ownerFinanceService.getPendingRents());
//    }
@GetMapping("/pending-rents/{pgId}")
public ResponseEntity<Map<String, Object>> getPendingRentsByPg(@PathVariable Long pgId) {
    return ResponseEntity.ok(ownerFinanceService.getPendingRentsByPg(pgId));
}


    @GetMapping("/paid-rents/{pgId}")
    public ResponseEntity<Map<String, Object>> getPaidRentsByPg(@PathVariable Long pgId) {
        return ResponseEntity.ok(ownerFinanceService.getPaidRentsByPg(pgId));
    }


//    @GetMapping("/advance-payments")
//    public ResponseEntity<Map<String, Object>> getAdvancePayments() {
//        return ResponseEntity.ok(ownerFinanceService.getAdvancePayments());
//    }

    @GetMapping("/advance-payments/{pgId}")
    public ResponseEntity<Map<String, Object>> getAdvancePaymentsByPg(@PathVariable Long pgId) {
        return ResponseEntity.ok(ownerFinanceService.getAdvancePaymentsByPg(pgId));
    }


//
//    @GetMapping("/refunds")
//    public ResponseEntity<Map<String, Object>> getRefunds() {
//        return ResponseEntity.ok(ownerFinanceService.getRefunds());
//    }

    @GetMapping("/refunds/{pgId}")
    public ResponseEntity<Map<String, Object>> getRefundsByPg(@PathVariable Long pgId) {
        return ResponseEntity.ok(ownerFinanceService.getRefundsByPg(pgId));
    }


//    @GetMapping("/revenue-summary")
//    public ResponseEntity<RevenueSummaryResponse> getRevenueSummary(@RequestParam(required = false) String month) {
//        LocalDate date = month != null ? LocalDate.parse(month + "-01") : LocalDate.now();
//        return ResponseEntity.ok(ownerFinanceService.getRevenueSummary(date));
//    }


    @GetMapping("/revenue-summary/{pgId}")
    public ResponseEntity<RevenueSummaryResponse> getRevenueSummaryByPg(
            @PathVariable Long pgId,
            @RequestParam(required = false) String month) {
        LocalDate date = month != null ? LocalDate.parse(month + "-01") : LocalDate.now();
        return ResponseEntity.ok(ownerFinanceService.getRevenueSummaryByPg(pgId, date));
    }


}
