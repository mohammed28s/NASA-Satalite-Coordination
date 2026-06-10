package com.Satliate.NASA.real.time.Satliate.Data.Repostiory;


import com.Satliate.NASA.real.time.Satliate.Data.Entity.Satellite;
import com.Satliate.NASA.real.time.Satliate.Data.Entity.TleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TleRecordRepository extends JpaRepository<TleRecord, Long> {
    Optional<TleRecord> findFirstBySatelliteOrderByEpochDesc(Satellite satellite);
}