package com.Satliate.NASA.Controller;

import com.Satliate.NASA.DTO.SatcatDto;
import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Expection.SpaceTrackException;
import com.Satliate.NASA.Expection.ResourceNotFoundException;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import com.Satliate.NASA.Service.PropagationService;
import com.Satliate.NASA.Service.SpaceTrackAuthService;
import com.Satliate.NASA.Service.SpaceTrackService;
import com.Satliate.NASA.Service.SatelliteSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/satellites")
@Tag(name = "Satellites", description = "Satellite metadata and position APIs")
public class SatelliteController {

    private final SpaceTrackService spaceTrackService;
    private final SatelliteRepository satelliteRepository;
    private final PropagationService propagationService;
    private final SpaceTrackAuthService authService;
    private final SatelliteSyncService satelliteSyncService;

    public SatelliteController(SpaceTrackService spaceTrackService,
                               SatelliteRepository satelliteRepository,
                               PropagationService propagationService,
                               SpaceTrackAuthService authService,
                               SatelliteSyncService satelliteSyncService) {
        this.spaceTrackService = spaceTrackService;
        this.satelliteRepository = satelliteRepository;
        this.propagationService = propagationService;
        this.authService = authService;
        this.satelliteSyncService = satelliteSyncService;
    }

    @Operation(summary = "Trigger a manual sync of satellite data")
    @GetMapping("/fetch")
    public ResponseEntity<List<Satellite>> fetchAndSync() {
        log.info("Manual synchronization triggered");
        satelliteSyncService.syncSatellites();
        List<Satellite> satellites = satelliteRepository.findAll();
        log.info("Synchronization completed. Found {} satellites", satellites.size());
        return ResponseEntity.ok(satellites);
    }

    @Operation(summary = "Create a new satellite record")
    @ApiResponse(responseCode = "201", description = "Satellite created successfully")
    @PostMapping("/create")
    public ResponseEntity<Satellite> createSatellite(@Valid @RequestBody Satellite satellite) {
        log.info("Creating new satellite: {}", satellite.getName());
        Satellite savedSat = satelliteRepository.save(satellite);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSat);
    }

    @Operation(summary = "List all tracked satellites")
    @GetMapping
    public ResponseEntity<List<Satellite>> listAll() {
        log.debug("Listing all satellites");
        return ResponseEntity.ok(satelliteRepository.findAll());
    }

    @Operation(summary = "Login to Space-Track")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        log.info("Attempting login to Space-Track for user: {}", username);
        try {
            authService.login(username, password);
            log.info("Login successful for user: {}", username);
            return ResponseEntity.ok("Login successful!");
        } catch (SpaceTrackException ex) {
            log.error("Login failed for user: {}. Reason: {}", username, ex.getMessage());
            throw ex;
        }
    }

    @Operation(summary = "List all active satellites from Space-Track")
    @GetMapping("/active")
    public ResponseEntity<List<SatcatDto>> getActiveSatellites() {
        log.info("Fetching active satellites from Space-Track");
        return ResponseEntity.ok(spaceTrackService.fetchActiveSatellites());
    }

    @Operation(summary = "List all inactive satellites from Space-Track")
    @GetMapping("/inactive")
    public ResponseEntity<List<SatcatDto>> getInactiveSatellites() {
        log.info("Fetching inactive satellites from Space-Track");
        return ResponseEntity.ok(spaceTrackService.fetchInactiveSatellites());
    }

    @Operation(summary = "Get metadata for one satellite by NORAD ID")
    @GetMapping("/{noradId}")
    public ResponseEntity<SatcatDto> getSatelliteByNoradId(@PathVariable Integer noradId) {
        log.info("Fetching metadata for NORAD ID: {}", noradId);
        return ResponseEntity.ok(spaceTrackService.fetchSatelliteByNoradId(noradId));
    }

    @Operation(summary = "Get real-time propagated position of a satellite")
    @ApiResponse(responseCode = "200", description = "Successfully calculated position")
    @ApiResponse(responseCode = "404", description = "Satellite or TLE data not found")
    @GetMapping("/{noradId}/position")
    public ResponseEntity<GeodeticPoint> getSatellitePosition(@PathVariable Integer noradId) {
        log.info("Requesting position for NORAD ID: {}", noradId);
        Satellite sat = satelliteRepository.findByNoradId(noradId)
                .orElseThrow(() -> {
                    log.warn("Satellite not found in DB with NORAD ID: {}", noradId);
                    return new ResourceNotFoundException("Satellite not found in DB with NORAD ID: " + noradId);
                });

        if (sat.getLine1() == null || sat.getLine2() == null) {
            log.warn("TLE data missing for satellite: {}", noradId);
            throw new ResourceNotFoundException("TLE data missing for satellite: " + noradId);
        }

        GeodeticPoint position = propagationService.propagate(sat);
        log.debug("Position for {}: Lat={}, Lon={}, Alt={}", noradId, position.getLatitude(), position.getLongitude(), position.getAltitude());
        return ResponseEntity.ok(position);
    }
}
