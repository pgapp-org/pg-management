package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//@Entity
//@Table(name = "daily_booking_guests")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DailyBookingGuest {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String guestName;
//    private Integer age;
//    private String gender;
//    private String idProof;
//
//    // assigned after approval:
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "room_id", nullable = true)
//    @JsonIgnore
//    private Room room;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bed_id", nullable = true)
//    @JsonIgnore
//    private Bed bed;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "booking_id")
//    @JsonIgnore
//    private DailyBookings booking;
//}

@Entity
@Table(name = "daily_booking_guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyBookingGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String gender;
    private String idProof;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = true)
    private Bed bed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private DailyBookings booking;
}
