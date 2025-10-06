package com.pgapp.enums;

public enum TenantStatus {
    APPLIED,        // tenant applied for PG
    TOKEN_PAID,     // token paid
    CHECKED_IN,     // advance + first month rent paid
    ACTIVE,         // paying monthly rent
    NOTICE_GIVEN,   // notice submitted
    REFUNDED,       // advance refunded
    REJECTED,
    PENDING,
    NOTICE_PERIOD,
    APPROVED,
    CHECKED_OUT,
    ADVANCE_PAID

}