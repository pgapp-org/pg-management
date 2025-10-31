package com.pgapp.repository;

import com.pgapp.entity.TenantApplication;
import com.pgapp.enums.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TenantApplicationRepository extends JpaRepository<TenantApplication, Long> {
    List<TenantApplication> findByPgId(Long pgId);
    List<TenantApplication> findByTenantId(Long tenantId);
    List<TenantApplication> findByCheckoutConfirmedTrueAndRefundProcessedTrue();
    Optional<TenantApplication> findByTenantIdAndCheckoutConfirmedFalse(Long tenantId);

//    List<TenantApplication> findByPgId(Long pgId);
//    List<TenantApplication> findByTenantId(Long tenantId);
//    List<TenantApplication> findByStatus(TenantStatus status);

}
