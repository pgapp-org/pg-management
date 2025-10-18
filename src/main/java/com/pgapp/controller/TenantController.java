package com.pgapp.controller;

import com.pgapp.converter.tenant.TenantConverter;
import com.pgapp.entity.Tenant;
import com.pgapp.request.tenant.TenantRequest;
import com.pgapp.response.tenant.TenantResponse;
import com.pgapp.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get all tenants
    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
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
}
