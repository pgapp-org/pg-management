package com.pgapp.response.owner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevenueSummaryResponse {
    private double totalPaidRents;
    private double totalAdvancePayments;
    private double totalRefunds;
    private double totalOtherExpenses;
    private double netRevenue; // totalPaidRents + totalAdvancePayments - totalRefunds - totalOtherExpenses
}
