package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tenant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate joinDate;
    private LocalDate endDate;




    private String bedNumber;

    // ✅ Optional KYC details
    private String nameAsPerAadhaar;
    private String gender;
    private String permanentAddress;
    private String state;
    private String city;
    private String aadhaarNumber;
    private String aadhaarFilePath; // store uploaded Aadhaar file/image path

    // ✅ Optional profile photo (can show on dashboard)
    private String profilePhotoPath;

    // ✅ Bank details (for refund)
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id")
    @JsonIgnore
    @ToString.Exclude
    private PG pg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    @ToString.Exclude
    private Room room;

    @OneToOne(mappedBy = "tenant")
    @JsonBackReference
    @ToString.Exclude
    private Bed bed;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<TenantApplication> tenantApplications;

}