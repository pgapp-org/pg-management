package com.pgapp.service;

import com.pgapp.entity.Tenant;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.TenantRepository;
import com.pgapp.request.tenant.TenantKycRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    // ✅ Register Tenant
    public Tenant registerTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    // ✅ Get tenant by ID
    public Optional<Tenant> getTenantById(Long id) {
        return tenantRepository.findById(id);
    }

    // ✅ Get all tenants
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    // ✅ Update tenant details
    public Optional<Tenant> updateTenant(Long id, Tenant updatedTenant) {
        return tenantRepository.findById(id).map(t -> {
            t.setName(updatedTenant.getName());
            t.setEmail(updatedTenant.getEmail());
            t.setPhone(updatedTenant.getPhone());
            t.setJoinDate(updatedTenant.getJoinDate());
            t.setEndDate(updatedTenant.getEndDate());

            // ✅ KYC fields
            t.setNameAsPerAadhaar(updatedTenant.getNameAsPerAadhaar());
            t.setGender(updatedTenant.getGender());
            t.setPermanentAddress(updatedTenant.getPermanentAddress());
            t.setState(updatedTenant.getState());
            t.setCity(updatedTenant.getCity());
            t.setAadhaarNumber(updatedTenant.getAadhaarNumber());
            t.setAadhaarFilePath(updatedTenant.getAadhaarFilePath());
            t.setProfilePhotoPath(updatedTenant.getProfilePhotoPath());

            // Optional: Bank details
            t.setAccountHolderName(updatedTenant.getAccountHolderName());
            t.setBankName(updatedTenant.getBankName());
            t.setAccountNumber(updatedTenant.getAccountNumber());
            t.setIfscCode(updatedTenant.getIfscCode());

            return tenantRepository.save(t);
        });
    }


    // ✅ Delete tenant
    public boolean deleteTenant(Long id) {
        if (tenantRepository.existsById(id)) {
            tenantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Tenant> login(String email, String password) {
        return tenantRepository.findByEmail(email)
                .filter(t -> t.getPassword().equals(password));
    }

    public void saveKycFiles(Long tenantId, MultipartFile aadhaarFile, MultipartFile profilePhoto) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id " + tenantId));

        try {
            Path folder = Paths.get("uploads/tenant-kyc");
            Files.createDirectories(folder);

            // ✅ Save Aadhaar File
            if (aadhaarFile != null && !aadhaarFile.isEmpty()) {
                String aadhaarFilename = UUID.randomUUID() + "_" + aadhaarFile.getOriginalFilename();
                Path aadhaarPath = folder.resolve(aadhaarFilename);
                Files.write(aadhaarPath, aadhaarFile.getBytes());
                tenant.setAadhaarFilePath("/uploads/tenant-kyc/" + aadhaarFilename);
            }

            // ✅ Save Profile Photo (optional)
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                String profileFilename = UUID.randomUUID() + "_" + profilePhoto.getOriginalFilename();
                Path profilePath = folder.resolve(profileFilename);
                Files.write(profilePath, profilePhoto.getBytes());
                tenant.setProfilePhotoPath("/uploads/tenant-kyc/" + profileFilename);
            }

            tenantRepository.save(tenant);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save KYC files: " + e.getMessage());
        }
    }
//
//    public Tenant updateTenantKyc(Long tenantId, TenantKycRequest kycRequest) {
//        Tenant tenant = tenantRepository.findById(tenantId)
//                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id " + tenantId));
//
//        tenant.setNameAsPerAadhaar(kycRequest.getNameAsPerAadhaar());
//        tenant.setGender(kycRequest.getGender());
//        tenant.setPermanentAddress(kycRequest.getPermanentAddress());
//        tenant.setState(kycRequest.getState());
//        tenant.setCity(kycRequest.getCity());
//        tenant.setAadhaarNumber(kycRequest.getAadhaarNumber());
//        tenant.setAadhaarFilePath(kycRequest.getAadhaarFilePath());
//        tenant.setProfilePhotoPath(kycRequest.getProfilePhotoPath());
//        tenant.setAccountHolderName(kycRequest.getAccountHolderName());
//        tenant.setBankName(kycRequest.getBankName());
//        tenant.setAccountNumber(kycRequest.getAccountNumber());
//        tenant.setIfscCode(kycRequest.getIfscCode());
//
//        return tenantRepository.save(tenant);
//    }

    public Tenant updateTenantKyc(Long tenantId, TenantKycRequest kycRequest) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id " + tenantId));

        // Use a proper Function for normalization
        Function<String, String> normalizePath = path -> {
            if (path == null || path.isBlank()) return null;  // use isBlank() instead of isEmpty()
            String cleaned = path.replace("http://localhost:8080", ""); // remove host if already present
            if (!cleaned.startsWith("/uploads")) {
                cleaned = "/uploads/tenant-kyc/" + cleaned;
            }
            return cleaned;
        };

        tenant.setNameAsPerAadhaar(kycRequest.getNameAsPerAadhaar());
        tenant.setGender(kycRequest.getGender());
        tenant.setPermanentAddress(kycRequest.getPermanentAddress());
        tenant.setState(kycRequest.getState());
        tenant.setCity(kycRequest.getCity());
        tenant.setAadhaarNumber(kycRequest.getAadhaarNumber());
        tenant.setAadhaarFilePath(normalizePath.apply(kycRequest.getAadhaarFilePath()));
        tenant.setProfilePhotoPath(normalizePath.apply(kycRequest.getProfilePhotoPath()));
        tenant.setAccountHolderName(kycRequest.getAccountHolderName());
        tenant.setBankName(kycRequest.getBankName());
        tenant.setAccountNumber(kycRequest.getAccountNumber());
        tenant.setIfscCode(kycRequest.getIfscCode());

        System.out.println("✅ Saving Tenant with cleaned paths:");
        System.out.println("   Aadhaar: " + tenant.getAadhaarFilePath());
        System.out.println("   Profile: " + tenant.getProfilePhotoPath());

        return tenantRepository.save(tenant);
    }


}
