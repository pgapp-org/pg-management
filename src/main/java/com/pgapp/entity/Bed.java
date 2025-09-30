package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "beds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer bedNumber;   // 1,2,3,...

    private boolean occupied;    // true = filled, false = vacant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

//    @Column(name = "short_term_occupied")
//    private boolean shortTermOccupied = false;

    private boolean shortTermOccupied = false;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    @JsonBackReference
    private Tenant tenant; // nullable, only if bed is occupied

}
