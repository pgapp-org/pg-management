package com.pgapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
//import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pg_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PgNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which PG this notification belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id")
    @JsonIgnore
    private PG pg;

    private String title; // optional title
    @Column(length = 2000)
    private String message; // main content

    private LocalDateTime createdAt;
}
