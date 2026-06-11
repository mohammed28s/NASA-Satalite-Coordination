package com.Satliate.NASA.Controller;



import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Entity.TleRecord;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import com.Satliate.NASA.Repostiory.TleRecordRepository;
import com.Satliate.NASA.Service.PropagationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200") // Allows Angular to connect
@RestController
@RequestMapping("/api/v1/satellites")
@RequiredArgsConstructor
public class SatelliteController {
    private final SatelliteRepository satelliteRepo;
    private final TleRecordRepository tleRepo;
    private final PropagationService propagationService;

    @GetMapping("/fetch")   // fetching all Satellite Data
    public List<Satellite> list() {
        return satelliteRepo.findAll();
    }

    @PostMapping("/create") // Create a new Satellite Data
    public Satellite create(@RequestBody Satellite s)
    {
        return satelliteRepo.save(s);
    }

    @GetMapping("/{id}/position") // Getting Specific Satellite Data by id
    public PropagationService.PositionDto position(@PathVariable Long id, @RequestParam(required = false) String at) {
        Satellite sat = satelliteRepo.findById(id).orElseThrow();
        TleRecord latest = tleRepo.findFirstBySatelliteOrderByEpochDesc(sat).orElseThrow();
        Instant when = (at == null) ? Instant.now() : Instant.parse(at);
        return propagationService.computePosition(latest.getLine1(), latest.getLine2(), when);
    }
}