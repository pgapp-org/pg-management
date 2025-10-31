//package com.pgapp.controller;
//
//
//import com.pgapp.entity.OwnerExpense;
//import com.pgapp.service.OwnerExpenseService;
//import com.pgapp.service.OwnerFinanceService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/owner/finance")
//@CrossOrigin(origins = "http://localhost:4200")
//public class OwnerExpenseController {
//
//    private final OwnerExpenseService ownerExpenseService;
//
//    public OwnerExpenseController(OwnerExpenseService ownerExpenseService) {
//        this.ownerExpenseService = ownerExpenseService;
//    }
//
//    @PostMapping("/expenses")
//    public ResponseEntity<OwnerExpense> addExpense(@RequestBody OwnerExpense expense) {
//        return ResponseEntity.ok(ownerExpenseService.addExpense(expense));
//    }
//
//    @PutMapping("/expenses/{id}")
//    public ResponseEntity<OwnerExpense> updateExpense(@PathVariable Long id,
//                                                      @RequestBody OwnerExpense expense) {
//        return ResponseEntity.ok(ownerExpenseService.updateExpense(id, expense));
//    }
//
//    @DeleteMapping("/expenses/{id}")
//    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
//        ownerExpenseService.deleteExpense(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/expenses")
//    public ResponseEntity<List<OwnerExpense>> getMonthlyExpenses(@RequestParam(required = false) String month) {
//        LocalDate date = month != null ? LocalDate.parse(month + "-01") : LocalDate.now();
//        return ResponseEntity.ok(ownerExpenseService.getMonthlyExpenses(date));
//    }
//}

package com.pgapp.controller;


import com.pgapp.entity.OwnerExpense;
import com.pgapp.service.OwnerExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/owner/finance")
@CrossOrigin(origins = "http://localhost:4200")
public class OwnerExpenseController {


    private final OwnerExpenseService ownerExpenseService;


    public OwnerExpenseController(OwnerExpenseService ownerExpenseService) {
        this.ownerExpenseService = ownerExpenseService;
    }


    @PostMapping("/expenses/{pgId}")
    public ResponseEntity<OwnerExpense> addExpense(@PathVariable Long pgId, @RequestBody OwnerExpense expense) {
        return ResponseEntity.ok(ownerExpenseService.addExpenseForPg(pgId, expense));
    }


    @PutMapping("/expenses/{id}")
    public ResponseEntity<OwnerExpense> updateExpense(@PathVariable Long id, @RequestBody OwnerExpense expense) {
        return ResponseEntity.ok(ownerExpenseService.updateExpense(id, expense));
    }


    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        ownerExpenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/expenses/{pgId}")
    public ResponseEntity<List<OwnerExpense>> getMonthlyExpenses(
            @PathVariable Long pgId,
            @RequestParam(required = false) String month) {
        LocalDate date = month != null ? LocalDate.parse(month + "-01") : LocalDate.now();
        return ResponseEntity.ok(ownerExpenseService.getMonthlyExpensesForPg(pgId, date));
    }
}