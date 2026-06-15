package com.Satliate.NASA.Controller;





import com.Satliate.NASA.DTO.SatcatDto;
import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Expection.SpaceTrackException;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import com.Satliate.NASA.Service.PropagationService;
import com.Satliate.NASA.Service.SpaceTrackAuthService;
import com.Satliate.NASA.Service.SpaceTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/satellites")
@Tag(name = "Satellites", description = "Satellite metadata and position APIs")
public class SatelliteController {

    private final SpaceTrackService spaceTrackService;
    private final SatelliteRepository satelliteRepository;
    private final PropagationService propagationService;
    private final SpaceTrackAuthService authService;

    public SatelliteController(SpaceTrackService spaceTrackService,
                               SatelliteRepository satelliteRepository,
                               PropagationService propagationService,
                               SpaceTrackAuthService authService) {
        this.spaceTrackService = spaceTrackService;
        this.satelliteRepository = satelliteRepository;
        this.propagationService = propagationService;
        this.authService = authService;
    }

    // ✅ Login first
    @Operation(summary = "Login to Space-Track")
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            authService.login(username, password);
            return "Login successful!";
        } catch (SpaceTrackException ex) {
            throw ex; // handled by GlobalExceptionHandler
        }
    }

    @Operation(summary = "List all active satellites")
    @GetMapping("/active")
    public List<SatcatDto> getActiveSatellites() {
        return spaceTrackService.fetchActiveSatellites();
    }

    @Operation(summary = "List all inactive satellites")
    @GetMapping("/inactive")
    public List<SatcatDto> getInactiveSatellites() {
        return spaceTrackService.fetchInactiveSatellites();
    }

    @Operation(summary = "Get metadata for one satellite by NORAD ID")
    @GetMapping("/{noradId}")
    public SatcatDto getSatelliteByNoradId(@PathVariable Integer noradId) {
        return spaceTrackService.fetchSatelliteByNoradId(noradId);
    }

    @Operation(summary = "Get real-time propagated position of a satellite")
    @GetMapping("/{noradId}/position")
    public GeodeticPoint getSatellitePosition(@PathVariable Integer noradId) {
        Satellite sat = satelliteRepository.findByNoradId(noradId)
                .orElseThrow(() -> new RuntimeException("Satellite not found in DB"));
        return propagationService.propagate(sat);
    }


}
