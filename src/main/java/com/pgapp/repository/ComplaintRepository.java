package com.pgapp.repository;

import com.pgapp.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByTenantId(Long tenantId);
    List<Complaint> findByPgId(Long pgId);
}