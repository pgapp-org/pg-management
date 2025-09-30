package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pgs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PG {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String houseNo;   // House/Flat/Block No.
    private String area;      // Area name
    private String city;
    private String state;
    private String pincode;

    @Column(name = "short_term_allowed")
    private boolean shortTermAllowed = false;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;


    @OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();


    // ✅ Store only file paths
    @ElementCollection
    @CollectionTable(name = "pg_images", joinColumns = @JoinColumn(name = "pg_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();


    // ✅ Amenities
    @ElementCollection(targetClass = AmenityType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "pg_amenities", joinColumns = @JoinColumn(name = "pg_id"))
    @Column(name = "amenity")
    private List<AmenityType> amenities = new ArrayList<>();

}
