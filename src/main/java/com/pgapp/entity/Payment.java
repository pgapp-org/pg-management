package com.pgapp.entity;

import com.pgapp.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentType type;   // TOKEN, ADVANCE, RENT, REFUND

    private LocalDate paymentDate;

    private String transactionId;  // for sandbox / test reference
    private boolean success;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_application_id")
    private TenantApplication tenantApplication;
}
