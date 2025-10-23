package com.pgapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private PG pg;

    private String title; // optional title
    @Column(length = 2000)
    private String message; // main content

    private LocalDateTime createdAt;
}
