package com.pgapp.controller;

import com.pgapp.converter.tenant.TenantConverter;
import com.pgapp.entity.Tenant;
import com.pgapp.request.tenant.TenantKycRequest;
import com.pgapp.request.tenant.TenantRequest;
import com.pgapp.response.tenant.TenantResponse;
import com.pgapp.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // ✅ Register a tenant
    @PostMapping("/register")
    public ResponseEntity<TenantResponse> registerTenant(@RequestBody TenantRequest request) {
        Tenant tenant = TenantConverter.toEntity(request);   // convert request → entity
        Tenant savedTenant = tenantService.registerTenant(tenant);  // save entity
        return ResponseEntity.ok(TenantConverter.toResponse(savedTenant)); // convert entity → response
    }

    @PostMapping("/login")
    public ResponseEntity<TenantResponse> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        System.out.println(email+" "+password);


        return tenantService.login(email, password)
                .map(TenantConverter::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }



    // ✅ Get tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(tenant -> ResponseEntity.ok(TenantConverter.toResponse(tenant)))
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ Get all tenants
    // Get tenants by PG
    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<TenantResponse>> getTenantsByPg(@PathVariable Long pgId) {
        List<TenantResponse> tenants = tenantService.getTenantsByPg(pgId);
        return ResponseEntity.ok(tenants);
    }


    // ✅ Update tenant
//    @PutMapping("/{id}")
//    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @RequestBody Tenant tenant) {
//        return tenantService.updateTenant(id, tenant)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable Long id, @RequestBody TenantRequest request) {
        Tenant updatedTenant = TenantConverter.toEntity(request);  // convert request → entity
        return tenantService.updateTenant(id, updatedTenant)
                .map(TenantConverter::toResponse) // convert entity → response
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ Delete tenant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        return tenantService.deleteTenant(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
//
//    @PutMapping("/{tenantId}/kyc")
//    public ResponseEntity<Tenant> updateKyc(
//            @PathVariable Long tenantId,
//            @RequestBody TenantKycRequest kycRequest) {
//
//        Tenant updatedTenant = tenantService.updateTenantKyc(tenantId, kycRequest);
//        return ResponseEntity.ok(updatedTenant);
//    }
    @PutMapping("/{tenantId}/kyc")
    public ResponseEntity<Tenant> updateKyc(
            @PathVariable Long tenantId,
            @RequestBody TenantKycRequest kycRequest) {

        System.out.println("🟩 Received KYC Request for Tenant ID: " + tenantId);
        System.out.println("➡ Name As Per Aadhaar: " + kycRequest.getNameAsPerAadhaar());
        System.out.println("➡ Gender: " + kycRequest.getGender());
        System.out.println("➡ Address: " + kycRequest.getPermanentAddress());
        System.out.println("➡ State: " + kycRequest.getState());
        System.out.println("➡ City: " + kycRequest.getCity());
        System.out.println("➡ Aadhaar Number: " + kycRequest.getAadhaarNumber());
        System.out.println("➡ Aadhaar File Path: " + kycRequest.getAadhaarFilePath());
        System.out.println("➡ Profile Photo Path: " + kycRequest.getProfilePhotoPath());
        System.out.println("➡ Account Holder Name: " + kycRequest.getAccountHolderName());
        System.out.println("➡ Bank Name: " + kycRequest.getBankName());
        System.out.println("➡ Account Number: " + kycRequest.getAccountNumber());
        System.out.println("➡ IFSC Code: " + kycRequest.getIfscCode());

        Tenant updatedTenant = tenantService.updateTenantKyc(tenantId, kycRequest);
        return ResponseEntity.ok(updatedTenant);
    }



//    @PostMapping(value = "/{tenantId}/upload-kyc", consumes = "multipart/form-data")
//    public ResponseEntity<Map<String, String>> uploadKyc(
//            @PathVariable Long tenantId,
//            @RequestParam("aadhaarFile") MultipartFile aadhaarFile,
//            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
//
//        tenantService.saveKycFiles(tenantId, aadhaarFile, profilePhoto);
//        return ResponseEntity.ok(Map.of("message", "KYC files uploaded successfully"));
//    }

    @PostMapping(value = "/{tenantId}/upload-kyc", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadKyc(
            @PathVariable Long tenantId,
            @RequestParam("aadhaarFile") MultipartFile aadhaarFile,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto) {

        tenantService.saveKycFiles(tenantId, aadhaarFile, profilePhoto);
        Tenant tenant = tenantService.getTenantById(tenantId).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "aadhaarFilePath", tenant.getAadhaarFilePath(),
                "profilePhotoPath", tenant.getProfilePhotoPath()
        ));
    }


}
