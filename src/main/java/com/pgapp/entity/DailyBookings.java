package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "daily_bookings")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DailyBookings {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String customerName;
//    private String customerPhone;
//    private String customerEmail;
//
//    private LocalDate checkInDate;
//    private LocalDate checkOutDate;
//
//    private Double tokenAmount;
//
//    @Enumerated(EnumType.STRING)
//    private ApplicationStatus status; // PENDING, APPROVED, REJECTED
//
//    // Owner assigns these after approval
//    @ManyToOne
//    @JoinColumn(name = "room_id", nullable = true)
//    private Room room;
//
//    @ManyToOne
//    @JoinColumn(name = "bed_id", nullable = true)
//    private Bed bed;
//
//    // inside DailyBookings.java
//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<DailyBookingGuest> guests = new ArrayList<>();
//
//}

@Entity
@Table(name = "daily_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DailyBookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;


    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;



//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<DailyBookingGuest> guests = new ArrayList<>();
           @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
           @JsonManagedReference
           private List<DailyBookingGuest> guests = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "pg_id")   // ✅ so you can call setPg(pg)
    private PG pg;

}
