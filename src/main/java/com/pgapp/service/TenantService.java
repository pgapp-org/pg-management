package com.pgapp.service;

import com.pgapp.entity.Tenant;
import com.pgapp.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
