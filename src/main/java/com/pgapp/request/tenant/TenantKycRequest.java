package com.pgapp.request.tenant;

import lombok.Data;

@Data
public class TenantKycRequest {
    private String nameAsPerAadhaar;
    private String gender;
    private String permanentAddress;
    private String state;
    private String city;
    private String aadhaarNumber;
    private String aadhaarFilePath;
    private String profilePhotoPath;
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
}
