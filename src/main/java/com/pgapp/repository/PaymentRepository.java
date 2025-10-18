package com.pgapp.repository;

import com.pgapp.entity.Payment;
import com.pgapp.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantApplicationId(Long applicationId);
    List<Payment> findByTenantId(Long tenantId);
    List<Payment> findByTenantIdAndType(Long tenantId, PaymentType type);
    List<Payment> findByTenantIdAndTypeAndRentForMonth(Long tenantId, PaymentType type, LocalDate rentMonth);

    boolean existsByTenantApplicationIdAndType(Long tenantApplicationId, PaymentType type);
    Optional<Payment> findByTenantApplicationIdAndTypeAndRentForMonth(Long tenantApplicationId, PaymentType type, LocalDate rentForMonth);

    boolean existsByTenantApplicationIdAndTypeAndRentForMonth(Long id, PaymentType paymentType, LocalDate nextMonth);
}
