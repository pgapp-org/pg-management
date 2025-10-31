//package com.pgapp.repository;
//
////import com.pgapp.entity.OwnerExpense;
//import com.pgapp.entity.OwnerExpense;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import java.time.LocalDate;
//import java.util.List;
//
//public interface OwnerExpenseRepository extends JpaRepository<OwnerExpense, Long> {
//
//    // Get all expenses for a particular month
//    @Query("""
//        SELECT e FROM OwnerExpense e
//        WHERE MONTH(e.expenseDate) = MONTH(:month)
//          AND YEAR(e.expenseDate) = YEAR(:month)
//        ORDER BY e.expenseDate ASC
//    """)
//    List<OwnerExpense> findByMonth(LocalDate month);
//}


package com.pgapp.repository;


import com.pgapp.entity.OwnerExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;


public interface OwnerExpenseRepository extends JpaRepository<OwnerExpense, Long> {


    @Query("""
SELECT e FROM OwnerExpense e
WHERE e.pg.id = :pgId
AND MONTH(e.expenseDate) = MONTH(:month)
AND YEAR(e.expenseDate) = YEAR(:month)
ORDER BY e.expenseDate ASC
""")
    List<OwnerExpense> findByPgAndMonth(@Param("pgId") Long pgId, @Param("month") LocalDate month);
}