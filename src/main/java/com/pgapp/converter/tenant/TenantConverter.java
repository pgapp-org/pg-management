package com.pgapp.converter.tenant;



import com.pgapp.entity.Tenant;
import com.pgapp.request.tenant.TenantRequest;
import com.pgapp.response.tenant.TenantResponse;

public class TenantConverter {

    public static Tenant toEntity(TenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setEmail(request.getEmail());
        tenant.setPassword(request.getPassword());
        tenant.setPhone(request.getPhone());
        return tenant;
    }

    public static TenantResponse toResponse(Tenant tenant) {
        TenantResponse response = new TenantResponse();
        response.setId(tenant.getId());
        response.setName(tenant.getName());
        response.setEmail(tenant.getEmail());
        response.setPhone(tenant.getPhone());
        if (tenant.getPg() != null) {
            response.setPgId(tenant.getPg().getId());
        }
        if (tenant.getRoom() != null) {
            response.setRoomNumber(tenant.getRoom().getRoomNumber());
        }


        if (tenant.getBed() != null) {
            // ✅ Assuming Bed has an Integer field bedNumber
            response.setBedNumber(tenant.getBed().getBedNumber());
        }
        // ✅ Map KYC fields
        response.setNameAsPerAadhaar(tenant.getNameAsPerAadhaar());
        response.setGender(tenant.getGender());
        response.setPermanentAddress(tenant.getPermanentAddress());
        response.setState(tenant.getState());
        response.setCity(tenant.getCity());
        response.setAadhaarNumber(tenant.getAadhaarNumber());
        response.setAadhaarFilePath(tenant.getAadhaarFilePath());
        response.setProfilePhotoPath(tenant.getProfilePhotoPath());

        // ✅ Map Bank details
        response.setAccountHolderName(tenant.getAccountHolderName());
        response.setBankName(tenant.getBankName());
        response.setAccountNumber(tenant.getAccountNumber());
        response.setIfscCode(tenant.getIfscCode());

        return response;
    }

}