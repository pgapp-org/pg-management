//package com.pgapp.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "owner_expenses")
//@Data // ✅ generates getters, setters, toString, equals, hashCode
//@NoArgsConstructor // ✅ generates no-args constructor
//@AllArgsConstructor // ✅ generates all-args constructor
//@Builder // ✅ allows using builder pattern
//public class OwnerExpense {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String description;
//
//    private Double amount;
//
//    private LocalDate expenseDate; // the date the expense is for
//
//    private LocalDateTime createdAt = LocalDateTime.now();
//}


package com.pgapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "owner_expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerExpense {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String description;


    private Double amount;


    private LocalDate expenseDate; // the date the expense is for


    private LocalDateTime createdAt = LocalDateTime.now();


    // Link to PG
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id")
    @JsonIgnore
    private PG pg;
}