package com.pgapp.entity;

import com.pgapp.enums.PaymentStatus;
import com.pgapp.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private TenantApplication tenantApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime timestamp;

    // For monthly rent, store the month this payment is for as the first day of the month:
    // e.g., 2025-10-01 means October 2025 rent.
    private LocalDate rentForMonth;

    // optional transaction/reference
    private String transactionRef;
}
