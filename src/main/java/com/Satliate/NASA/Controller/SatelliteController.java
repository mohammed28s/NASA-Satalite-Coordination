package com.Satliate.NASA.Controller;






import com.Satliate.NASA.DTO.SatcatDto;
import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import com.Satliate.NASA.Service.PropagationService;
import com.Satliate.NASA.Service.SpaceTrackService;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/satellites")
public class SatelliteController {

    private final SpaceTrackService spaceTrackService;
    private final SatelliteRepository satelliteRepository;
    private final PropagationService propagationService;

    public SatelliteController(SpaceTrackService spaceTrackService,
                               SatelliteRepository satelliteRepository,
                               PropagationService propagationService) {
        this.spaceTrackService = spaceTrackService;
        this.satelliteRepository = satelliteRepository;
        this.propagationService = propagationService;
    }

    // ✅ API 1: List all active satellites (from Space-Track)
    @GetMapping("/active")
    public List<SatcatDto> getActiveSatellites() {
        return spaceTrackService.fetchActiveSatellites();
    }

    // ✅ API 2: List all inactive satellites (from Space-Track)
    @GetMapping("/inactive")
    public List<SatcatDto> getInactiveSatellites() {
        return spaceTrackService.fetchInactiveSatellites();
    }

    // ✅ API 3: Metadata for one satellite by NORAD ID
    @GetMapping("/{noradId}")
    public SatcatDto getSatelliteByNoradId(@PathVariable Integer noradId) {
        return spaceTrackService.fetchSatelliteByNoradId(noradId);
    }

    // ✅ API 4: Real-time propagated position of a satellite
    @GetMapping("/{noradId}/position")
    public GeodeticPoint getSatellitePosition(@PathVariable Integer noradId) {
        Satellite sat = satelliteRepository.findByNoradId(noradId)
                .orElseThrow(() -> new RuntimeException("Satellite not found in DB"));
        return propagationService.propagate(sat);
    }
}
