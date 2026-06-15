package com.Satliate.NASA.Service;







import com.Satliate.NASA.DTO.SatcatDto;
import com.Satliate.NASA.DTO.SatcatMapper;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteSyncService {

    private final SpaceTrackService spaceTrackService;
    private final TleFetcherService tleFetcherService;
    private final SatelliteRepository satelliteRepository;

    public SatelliteSyncService(SpaceTrackService spaceTrackService,
                                TleFetcherService tleFetcherService,
                                SatelliteRepository satelliteRepository) {
        this.spaceTrackService = spaceTrackService;
        this.tleFetcherService = tleFetcherService;
        this.satelliteRepository = satelliteRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // nightly sync CRON Job
    public void syncSatellites() {
        // Step 1: Fetch metadata from Space-Track
        List<SatcatDto> active = spaceTrackService.fetchActiveSatellites();
        active.forEach(dto -> satelliteRepository.save(SatcatMapper.toEntity(dto)));

        // Step 2: Fetch TLEs from Celestrak and update line1/line2
        tleFetcherService.fetchTles();
    }
}
