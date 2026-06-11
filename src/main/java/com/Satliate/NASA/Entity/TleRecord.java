package com.Satliate.NASA.Entity;



import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;


@Data
@Entity
@Table(name = "tle_record", indexes = {
        @Index(name = "idx_sat_epoch", columnList = "satellite_id, epoch")
})
public class TleRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "satellite_id")
    private Satellite satellite;

    private OffsetDateTime epoch;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String line1;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String line2;

    private String source;
    private OffsetDateTime fetchedAt;

    // getters and setters omitted for brevity
}