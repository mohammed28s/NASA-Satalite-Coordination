package com.Satliate.NASA.real.time.Satliate.Data.Repostiory;



import com.Satliate.NASA.real.time.Satliate.Data.Entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
    Optional<Satellite> findByNoradId(Integer noradId);
}