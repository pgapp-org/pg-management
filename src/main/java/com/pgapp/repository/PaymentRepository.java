package com.pgapp.repository;

import com.pgapp.entity.Payment;
import com.pgapp.enums.PaymentStatus;
import com.pgapp.enums.PaymentType;
//import com.pgapp.response.owner.RentCollectionResponse;
import com.pgapp.response.owner.AdvancePaymentResponse;
import com.pgapp.response.owner.RefundResponse;
import com.pgapp.response.owner.RentCollectionResponse;
import com.pgapp.response.owner.RentStatusResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantApplicationId(Long applicationId);
    List<Payment> findByTenantId(Long tenantId);
    List<Payment> findByTenantIdAndType(Long tenantId, PaymentType type);
//    List<Payment> findByTenantIdAndTypeAndRentForMonth(Long tenantId, PaymentType type, LocalDate rentMonth);

    boolean existsByTenantApplicationIdAndType(Long tenantApplicationId, PaymentType type);
    Optional<Payment> findByTenantApplicationIdAndTypeAndRentForMonth(Long tenantApplicationId, PaymentType type, LocalDate rentForMonth);

    boolean existsByTenantApplicationIdAndTypeAndRentForMonth(Long id, PaymentType paymentType, LocalDate nextMonth);

    List<Payment> findByTenantIdAndTypeAndStatus(Long tenantId, PaymentType type, PaymentStatus status);

    Optional<Payment> findTopByTenantApplicationIdAndTypeAndStatusOrderByTimestampDesc(
            Long tenantApplicationId,
            PaymentType type,
            PaymentStatus status
    );

    @Query("""
SELECT p FROM Payment p
WHERE p.tenant.id = :tenantId
  AND p.type = :type
  AND MONTH(p.rentForMonth) = MONTH(:month)
  AND YEAR(p.rentForMonth)  = YEAR(:month)
""")
    Optional<Payment> findMonthlyRentPayment(Long tenantId, PaymentType type, LocalDate month);


    // this is related to owner


//    @Query("""
//    SELECT new com.pgapp.response.owner.RentStatusResponse(
//        t.name,
//        t.phone,
//        r.roomNumber,
//        p.amount,
//        p.status
//    )
//    FROM Payment p
//    JOIN p.tenant t
//    JOIN t.bed b
//    JOIN b.room r
//    WHERE p.type = com.pgapp.enums.PaymentType.RENT
//      AND p.status = com.pgapp.enums.PaymentStatus.PENDING
//      AND FUNCTION('MONTH', p.rentForMonth) = FUNCTION('MONTH', CURRENT_DATE)
//      AND FUNCTION('YEAR', p.rentForMonth) = FUNCTION('YEAR', CURRENT_DATE)
//""")
//    List<RentStatusResponse> findPendingRentsForCurrentMonth();

    @Query("""
    SELECT new com.pgapp.response.owner.RentStatusResponse(
        t.name,
        t.phone,
        t.room.roomNumber,
        p.amount,
        p.status
    )
    FROM Payment p
    JOIN p.tenant t
    WHERE p.type = com.pgapp.enums.PaymentType.RENT
      AND p.status = com.pgapp.enums.PaymentStatus.PENDING
      AND FUNCTION('MONTH', p.rentForMonth) = FUNCTION('MONTH', CURRENT_DATE)
      AND FUNCTION('YEAR', p.rentForMonth) = FUNCTION('YEAR', CURRENT_DATE)
      AND t.pg.id = :pgId
""")
    List<RentStatusResponse> findPendingRentsForCurrentMonthByPg(Long pgId);


//    @Query("""
//    SELECT new com.pgapp.response.owner.RentCollectionResponse(
//        t.name,
//        t.phone,
//        r.roomNumber,
//        p.amount,
//        p.status || '',
//        p.timestamp
//    )
//    FROM Payment p
//    JOIN p.tenant t
//    JOIN t.bed b
//    JOIN b.room r
//    WHERE p.type = com.pgapp.enums.PaymentType.RENT
//      AND p.status = com.pgapp.enums.PaymentStatus.SUCCESS
//      AND FUNCTION('MONTH', p.rentForMonth) = FUNCTION('MONTH', CURRENT_DATE)
//      AND FUNCTION('YEAR', p.rentForMonth) = FUNCTION('YEAR', CURRENT_DATE)
//""")
//    List<RentCollectionResponse> findPaidRentsForCurrentMonth();
@Query("""
    SELECT new com.pgapp.response.owner.RentCollectionResponse(
        t.name,
        t.phone,
        r.roomNumber,
        p.amount,
        p.status || '',
        p.timestamp
    )
    FROM Payment p
    JOIN p.tenant t
    JOIN t.bed b
    JOIN b.room r
    WHERE p.type = com.pgapp.enums.PaymentType.RENT
      AND p.status = com.pgapp.enums.PaymentStatus.SUCCESS
      AND FUNCTION('MONTH', p.rentForMonth) = FUNCTION('MONTH', CURRENT_DATE)
      AND FUNCTION('YEAR', p.rentForMonth) = FUNCTION('YEAR', CURRENT_DATE)
      AND t.pg.id = :pgId
""")
List<RentCollectionResponse> findPaidRentsForCurrentMonthByPg(Long pgId);





//    @Query("""
//    SELECT new com.pgapp.response.owner.AdvancePaymentResponse(
//        t.name,
//        t.phone,
//        r.roomNumber,
//        ta.checkInDate,
//        p.amount,
//        p.status,
//        p.timestamp
//    )
//    FROM Payment p
//    JOIN p.tenant t
//    JOIN t.bed b
//    JOIN b.room r
//    JOIN t.tenantApplications ta
//    WHERE p.type = com.pgapp.enums.PaymentType.ADVANCE
//      AND FUNCTION('MONTH', p.timestamp) = FUNCTION('MONTH', CURRENT_DATE)
//      AND FUNCTION('YEAR', p.timestamp) = FUNCTION('YEAR', CURRENT_DATE)
//""")
//    List<AdvancePaymentResponse> findAdvancePaymentsForCurrentMonth();

    @Query("""
    SELECT new com.pgapp.response.owner.AdvancePaymentResponse(
        t.name,
        t.phone,
        r.roomNumber,
        ta.checkInDate,
        p.amount,
        p.status,
        p.timestamp
    )
    FROM Payment p
    JOIN p.tenant t
    JOIN t.bed b
    JOIN b.room r
    JOIN t.tenantApplications ta
    WHERE p.type = com.pgapp.enums.PaymentType.ADVANCE
      AND FUNCTION('MONTH', p.timestamp) = FUNCTION('MONTH', CURRENT_DATE)
      AND FUNCTION('YEAR', p.timestamp) = FUNCTION('YEAR', CURRENT_DATE)
      AND t.pg.id = :pgId
""")
    List<AdvancePaymentResponse> findAdvancePaymentsForCurrentMonthByPg(Long pgId);



//    @Query("""
//    SELECT new com.pgapp.response.owner.RefundResponse(
//        t.name,
//        t.phone,
//        r.roomNumber,
//        ta.checkOutDate,
//        ta.advanceAmount,
//        p.amount,
//        p.status,
//        p.timestamp
//    )
//    FROM Payment p
//    JOIN p.tenant t
//    JOIN p.tenantApplication ta
//    JOIN ta.room r
//    WHERE p.type = com.pgapp.enums.PaymentType.REFUND
//      AND p.status = com.pgapp.enums.PaymentStatus.SUCCESS
//""")
//    List<RefundResponse> findProcessedRefunds();

    @Query("""
SELECT new com.pgapp.response.owner.RefundResponse(
    t.name,
    t.phone,
    r.roomNumber,
    ta.checkOutDate,
    ta.advanceAmount,
    p.amount,
    p.status,
    p.timestamp
)
FROM Payment p
JOIN p.tenant t
JOIN p.tenantApplication ta
JOIN ta.room r
WHERE p.type = com.pgapp.enums.PaymentType.REFUND
  AND p.status = com.pgapp.enums.PaymentStatus.SUCCESS
  AND t.pg.id = :pgId
""")
    List<RefundResponse> findProcessedRefundsByPg(Long pgId);


}




