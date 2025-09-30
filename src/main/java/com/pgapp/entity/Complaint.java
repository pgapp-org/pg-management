package com.pgapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tenant who raised complaint
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id")
    private PG pg;

    private String roomNumber;

    // Complaint category
    @Enumerated(EnumType.STRING)
    private ComplaintCategory category;

    @Column(length = 2000)
    private String description;

    private String status; // OPEN, IN_PROGRESS, RESOLVED

    @Column(length = 2000)
    private String ownerComments; // remarks when owner resolves

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    // Optional picture path/URL
    private String imageUrl;

    // After resolution - tenant feedback
    private Integer tenantRating; // 1-5 stars
    private String tenantFeedback;
}
