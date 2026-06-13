package com.Satliate.NASA.Entity;



import jakarta.persistence.*;
import java.time.OffsetDateTime;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public void setSatellite(Satellite satellite) {
        this.satellite = satellite;
    }

    public OffsetDateTime getEpoch() {
        return epoch;
    }

    public void setEpoch(OffsetDateTime epoch) {
        this.epoch = epoch;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public OffsetDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(OffsetDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}
