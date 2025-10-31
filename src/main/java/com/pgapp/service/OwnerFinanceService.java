package com.pgapp.service;

//import com.pgapp.response.owner.RentCollectionResponse;
import com.pgapp.enums.PaymentStatus;
import com.pgapp.entity.OwnerExpense;
import com.pgapp.repository.OwnerExpenseRepository;
import com.pgapp.response.owner.*;
import com.pgapp.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OwnerFinanceService {

    private final PaymentRepository paymentRepository;
    private final OwnerExpenseRepository ownerExpenseRepository;

    public OwnerFinanceService(PaymentRepository paymentRepository,
                               OwnerExpenseRepository ownerExpenseRepository) {
        this.paymentRepository = paymentRepository;
        this.ownerExpenseRepository = ownerExpenseRepository;
    }


//    public Map<String, Object> getPendingRents() {
//        List<RentStatusResponse> pendingList = paymentRepository.findPendingRentsForCurrentMonth();
//        double totalPending = pendingList.stream()
//                .mapToDouble(RentStatusResponse::getRentAmount)
//                .sum();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("pendingList", pendingList);
//        response.put("totalPendingAmount", totalPending);
//        return response;
//    }
public Map<String, Object> getPendingRentsByPg(Long pgId) {
    List<RentStatusResponse> pendingList = paymentRepository.findPendingRentsForCurrentMonthByPg(pgId);
    double totalPendingAmount = pendingList.stream()
            .mapToDouble(RentStatusResponse::getRentAmount)
            .sum();

    Map<String, Object> result = new HashMap<>();
    result.put("pendingList", pendingList);
    result.put("totalPendingAmount", totalPendingAmount);
    return result;
}


//    public Map<String, Object> getPaidRents() {
//        List<RentCollectionResponse> paidList = paymentRepository.findPaidRentsForCurrentMonth();
//        double totalCollected = paidList.stream()
//                .mapToDouble(RentCollectionResponse::getRentAmount)
//                .sum();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("paidList", paidList);
//        response.put("totalCollectedAmount", totalCollected);
//        return response;
//    }
public Map<String, Object> getPaidRentsByPg(Long pgId) {
    List<RentCollectionResponse> paidList = paymentRepository.findPaidRentsForCurrentMonthByPg(pgId);

    double totalCollected = paidList.stream()
            .mapToDouble(RentCollectionResponse::getRentAmount)
            .sum();

    Map<String, Object> response = new HashMap<>();
    response.put("paidList", paidList);
    response.put("totalCollectedAmount", totalCollected);
    return response;
}



//    public Map<String, Object> getAdvancePayments() {
//        List<AdvancePaymentResponse> advanceList = paymentRepository.findAdvancePaymentsForCurrentMonth();
//
//
//
//        double totalAdvance = advanceList.stream()
//                .filter(a -> PaymentStatus.SUCCESS.equals(a.getStatus()))  // ✅ compare enum
//                .mapToDouble(AdvancePaymentResponse::getAmount)
//                .sum();
//
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("advanceList", advanceList);
//        response.put("totalAdvanceAmount", totalAdvance);
//        return response;
//    }
public Map<String, Object> getAdvancePaymentsByPg(Long pgId) {
    List<AdvancePaymentResponse> advanceList = paymentRepository.findAdvancePaymentsForCurrentMonthByPg(pgId);

    double totalAdvance = advanceList.stream()
            .filter(a -> PaymentStatus.SUCCESS.equals(a.getStatus()))
            .mapToDouble(AdvancePaymentResponse::getAmount)
            .sum();

    Map<String, Object> response = new HashMap<>();
    response.put("advanceList", advanceList);
    response.put("totalAdvanceAmount", totalAdvance);
    return response;
}




//    public Map<String, Object> getRefunds() {
//        List<RefundResponse> refundList = paymentRepository.findProcessedRefunds();
//        double totalRefund = refundList.stream()
//                .mapToDouble(RefundResponse::getAmount) // <-- use correct getter
//                .sum();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("refundList", refundList);
//        response.put("totalRefundAmount", totalRefund);
//        return response;
//    }

    public Map<String, Object> getRefundsByPg(Long pgId) {
        List<RefundResponse> refundList = paymentRepository.findProcessedRefundsByPg(pgId);
        double totalRefund = refundList.stream()
                .mapToDouble(RefundResponse::getAmount)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("refundList", refundList);
        response.put("totalRefundAmount", totalRefund);
        return response;
    }



//    public RevenueSummaryResponse getRevenueSummary(LocalDate month) {
//        // Paid rents
//
//        double totalPaidRents = paymentRepository.findPaidRentsForCurrentMonthByPg()
//                .stream().mapToDouble(RentCollectionResponse::getRentAmount).sum();
//
//        // Advance payments
//        double totalAdvance = paymentRepository.findAdvancePaymentsForCurrentMonth()
//                .stream()
//                .filter(a -> PaymentStatus.SUCCESS.equals(a.getStatus()))
//                .mapToDouble(AdvancePaymentResponse::getAmount)
//                .sum();
//
//        // Refunds
//        double totalRefunds = paymentRepository.findProcessedRefunds()
//                .stream().mapToDouble(RefundResponse::getAmount).sum();
//
//        // Other expenses
//        double totalOtherExpenses = ownerExpenseRepository.findByMonth(month)
//                .stream().mapToDouble(OwnerExpense::getAmount).sum();
//
//        double netRevenue = totalPaidRents + totalAdvance - totalRefunds - totalOtherExpenses;
//
//        return RevenueSummaryResponse.builder()
//                .totalPaidRents(totalPaidRents)
//                .totalAdvancePayments(totalAdvance)
//                .totalRefunds(totalRefunds)
//                .totalOtherExpenses(totalOtherExpenses)
//                .netRevenue(netRevenue)
//                .build();
//    }

    public RevenueSummaryResponse getRevenueSummaryByPg(Long pgId, LocalDate month) {
        double totalPaidRents = paymentRepository.findPaidRentsForCurrentMonthByPg(pgId)
                .stream().mapToDouble(RentCollectionResponse::getRentAmount).sum();

        double totalAdvancePayments = paymentRepository.findAdvancePaymentsForCurrentMonthByPg(pgId)
                .stream()
                .filter(a -> PaymentStatus.SUCCESS.equals(a.getStatus()))
                .mapToDouble(AdvancePaymentResponse::getAmount)
                .sum();

        double totalRefunds = paymentRepository.findProcessedRefundsByPg(pgId)
                .stream().mapToDouble(RefundResponse::getAmount).sum();

        double totalOtherExpenses = ownerExpenseRepository.findByPgAndMonth(pgId, month)
                .stream().mapToDouble(OwnerExpense::getAmount).sum();

        double netRevenue = totalPaidRents + totalAdvancePayments - totalRefunds - totalOtherExpenses;

        return RevenueSummaryResponse.builder()
                .totalPaidRents(totalPaidRents)
                .totalAdvancePayments(totalAdvancePayments)
                .totalRefunds(totalRefunds)
                .totalOtherExpenses(totalOtherExpenses)
                .netRevenue(netRevenue)
                .build();
    }


}
