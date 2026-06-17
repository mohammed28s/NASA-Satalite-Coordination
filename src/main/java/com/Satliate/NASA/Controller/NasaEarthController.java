package com.Satliate.NASA.Controller;

import com.Satliate.NASA.Service.NasaEarthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/earth")
@Tag(name = "NASA Earth", description = "NASA Earth Imagery APIs")
public class NasaEarthController {

    private final NasaEarthService nasaEarthService;

    public NasaEarthController(NasaEarthService nasaEarthService) {
        this.nasaEarthService = nasaEarthService;
    }

    @Operation(summary = "Get Earth image metadata", 
               description = "Returns the JSON response from NASA Earth assets API.")
    @GetMapping("/image")
    public ResponseEntity<String> getEarthImage(
            @RequestParam double lon,
            @RequestParam double lat,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0.025") double dim) {
        
        log.info("Fetching Earth image assets for lon: {}, lat: {}", lon, lat);
        String response = nasaEarthService.getEarthImage(lon, lat, date, dim);
        return ResponseEntity.ok(response);
    }
}
