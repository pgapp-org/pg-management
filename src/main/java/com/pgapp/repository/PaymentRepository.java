package com.pgapp.repository;


import com.pgapp.entity.Payment;
//import com.pgapp.entity.PaymentType;
import com.pgapp.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantApplicationId(Long tenantApplicationId);
    List<Payment> findByType(PaymentType type);
}