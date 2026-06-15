package com.Satliate.NASA.Controller;




import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import com.Satliate.NASA.Service.PropagationService;
import org.orekit.bodies.GeodeticPoint;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200") // Allows Angular to connect
@RestController
@RequestMapping("/satellites")
public class SatelliteController {

    private final SatelliteRepository satelliteRepository;
    private final PropagationService propagationService;

    public SatelliteController(SatelliteRepository satelliteRepository,
                               PropagationService propagationService) {
        this.satelliteRepository = satelliteRepository;
        this.propagationService = propagationService;
    }

    @GetMapping("/active")
    public List<Satellite> getActiveSatellites() {
        return satelliteRepository.findAll();
    }

    @GetMapping("/{noradId}/position")
    public GeodeticPoint getSatellitePosition(@PathVariable Integer noradId) {
        Satellite sat = satelliteRepository.findByNoradId(noradId)
                .orElseThrow(() -> new RuntimeException("Satellite not found"));
        return propagationService.propagate(sat);
    }
}
