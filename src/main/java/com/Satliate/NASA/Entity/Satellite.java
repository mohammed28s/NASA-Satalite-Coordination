package com.Satliate.NASA.Entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "satellite")
public class Satellite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "NORAD ID is required")
    @Column(name = "norad_id", unique = true)
    private Integer noradId;

    @NotBlank(message = "Satellite name is required")
    private String name;

    private String manufacturer;
    private LocalDateTime launchDate;
    private LocalDateTime decayDate;

    @Positive(message = "Mass must be positive")
    private Double massKg;

    @Lob
    @Column(name = "size_json")
    private String sizeJson;

    private String purpose;

    private Instant createdAt;
    private Instant updatedAt;

    private String line1;
    private String line2;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }


}
