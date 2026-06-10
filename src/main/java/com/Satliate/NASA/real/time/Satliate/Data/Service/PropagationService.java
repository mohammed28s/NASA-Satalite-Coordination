package com.Satliate.NASA.real.time.Satliate.Data.Service;



import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.time.Instant;



@Service
public class PropagationService {

    @PostConstruct
    public void initOrekit() {
        // Place orekit-data in src/main/resources/orekit-data or configure a directory.
        // Example:
        // DataProvidersManager.getInstance().addProvider(new DirectoryCrawler(new File("src/main/resources/orekit-data")));
    }

    public PositionDto computePosition(String line1, String line2, Instant when) {
        // Minimal stub: return zeros until Orekit is configured.
        // Replace with Orekit TLE propagation when orekit-data is available.
        return new PositionDto(0.0, 0.0, 0.0);
    }

    public record PositionDto(double latitude, double longitude, double altitude) {}
}