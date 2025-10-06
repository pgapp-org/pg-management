package com.pgapp.enums;

public enum ApplicationStatus {
    PENDING,
    APPROVED,   // owner approved but not checked-in (tenant still needs to pay advance at check-in)
    ACTIVE,     // tenant has checked-in (advance + first month paid)
    REJECTED,
    COMPLETED   // optional: after check-out & settlement
}
