package com.pgapp.repository;

import com.pgapp.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    List<Tenant> findByPgId(Long pgId);
}
