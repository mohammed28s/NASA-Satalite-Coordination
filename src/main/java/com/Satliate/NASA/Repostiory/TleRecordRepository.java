package com.Satliate.NASA.Repostiory;


import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Entity.TleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TleRecordRepository extends JpaRepository<TleRecord, Long> {
    Optional<TleRecord> findFirstBySatelliteOrderByEpochDesc(Satellite satellite);
}