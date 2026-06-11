package com.Satliate.NASA.Entity;



import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "satellite")
public class Satellite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "norad_id", unique = true)
    private Integer noradId;

    private String name;
    private String manufacturer;
    private LocalDate launchDate;
    private Double massKg;

    @Lob
    @Column(name = "size_json")
    private String sizeJson;

    private String purpose;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    // getters and setters omitted for brevity
}
