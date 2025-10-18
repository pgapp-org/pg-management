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
        return response;
    }
}