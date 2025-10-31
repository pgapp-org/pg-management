//package com.pgapp.service;
//
//import com.pgapp.entity.OwnerExpense;
//import com.pgapp.repository.OwnerExpenseRepository;
//import org.springframework.stereotype.Service;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class OwnerExpenseService {
//
//    private final OwnerExpenseRepository expenseRepository;
//
//    public OwnerExpenseService(OwnerExpenseRepository expenseRepository) {
//        this.expenseRepository = expenseRepository;
//    }
//
//    // Add a new expense
//    public OwnerExpense addExpense(OwnerExpense expense) {
//        return expenseRepository.save(expense);
//    }
//
//    // Edit existing expense
//    public OwnerExpense updateExpense(Long id, OwnerExpense updatedExpense) {
//        OwnerExpense existing = expenseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//        existing.setDescription(updatedExpense.getDescription());
//        existing.setAmount(updatedExpense.getAmount());
//        existing.setExpenseDate(updatedExpense.getExpenseDate());
//        return expenseRepository.save(existing);
//    }
//
//    // Delete an expense
//    public void deleteExpense(Long id) {
//        expenseRepository.deleteById(id);
//    }
//
//    // Get all expenses for a month
//    public List<OwnerExpense> getMonthlyExpenses(LocalDate month) {
//        return expenseRepository.findByMonth(month);
//    }
//}

package com.pgapp.service;


import com.pgapp.entity.OwnerExpense;
import com.pgapp.entity.PG;
import com.pgapp.repository.OwnerExpenseRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class OwnerExpenseService {


    private final OwnerExpenseRepository expenseRepository;
    private final PGRepository pgRepository;


    public OwnerExpenseService(OwnerExpenseRepository expenseRepository, PGRepository pgRepository) {
        this.expenseRepository = expenseRepository;
        this.pgRepository = pgRepository;
    }


    // Add a new expense for a PG
    public OwnerExpense addExpenseForPg(Long pgId, OwnerExpense expense) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG not found"));
        expense.setPg(pg);
        return expenseRepository.save(expense);
    }


    // Edit existing expense
    public OwnerExpense updateExpense(Long id, OwnerExpense updatedExpense) {
        OwnerExpense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        existing.setDescription(updatedExpense.getDescription());
        existing.setAmount(updatedExpense.getAmount());
        existing.setExpenseDate(updatedExpense.getExpenseDate());
// Do not change pg here (unless you want to allow transferring expense to another PG)
        return expenseRepository.save(existing);
    }


    // Delete an expense
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }


    // Get all expenses for a month for a PG
    public List<OwnerExpense> getMonthlyExpensesForPg(Long pgId, LocalDate month) {
        return expenseRepository.findByPgAndMonth(pgId, month);
    }


}