package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;    // e.g. "101", "105A"
    private Integer capacity;     // beds in this room, e.g. 3
    private Integer occupiedBeds = 0; // current count, default 0

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    @JsonIgnore
    private Floor floor;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tenant> tenants = new ArrayList<>();

    public int getVacantBeds() {
        return capacity - occupiedBeds;
    }

    @Transient
    public String getRoomType() {
        return switch (capacity) {
            case 1 -> "Single Sharing";
            case 2 -> "Double Sharing";
            case 3 -> "Triple Sharing";
            case 4 -> "Four Sharing";
            default -> capacity + "-Sharing";
        };
    }

}
