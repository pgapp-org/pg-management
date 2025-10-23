package com.pgapp.entity;

import com.pgapp.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tenant_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id")
    private PG pg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private String bedNumber;

    // TenantApplication.java
    private Boolean foodOpted;
    private Double finalMonthlyRent;    // rent applicable for this tenant

    private Double advanceAmount;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, APPROVED, REJECTED

    // booking dates (set during apply)
    private LocalDate checkInDate;
    private boolean checkoutRequested = false;  // tenant applied for checkout
    private boolean checkoutConfirmed = false;  // checkout finalized by owner


    private LocalDate checkOutDate;

    // quick flags (optional - mostly tracked by Payment entity)
    private boolean tokenPaid = false;
    private boolean advancePaid = false;
    private boolean refundProcessed = false;

    private Double tokenAmount;

    private Double refundAmount;
    private LocalDate tokenPaymentDate;
    private LocalDate advancePaymentDate;
    private LocalDate refundDate;

    // TenantApplication.java
    private boolean hasCheckedIn = false;
    private boolean firstMonthRentPaid = false;

    @Column(nullable = false)
    private boolean noticeApplied = false;

    @OneToMany(mappedBy = "tenantApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}