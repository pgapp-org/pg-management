package com.pgapp.repository;

import com.pgapp.entity.TenantApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TenantApplicationRepository extends JpaRepository<TenantApplication, Long> {
    List<TenantApplication> findByPgId(Long pgId);
    List<TenantApplication> findByTenantId(Long tenantId);
}
