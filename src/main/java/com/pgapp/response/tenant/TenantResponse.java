package com.pgapp.response.tenant;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TenantResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Long pgId;


    // KYC fields
    private String nameAsPerAadhaar;
    private String gender;
    private String permanentAddress;
    private String state;
    private String city;
    private String aadhaarNumber;
    private String aadhaarFilePath;  // <--- ADD THIS
    private String profilePhotoPath; // <--- ADD THIS

    // Bank details
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;

}



