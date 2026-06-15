package com.Satliate.NASA.Service;


import com.Satliate.NASA.Entity.Satellite;
import com.Satliate.NASA.Repostiory.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TleFetcherService {

    private final SatelliteRepository satelliteRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public TleFetcherService(SatelliteRepository satelliteRepository) {
        this.satelliteRepository = satelliteRepository;
    }

    public void fetchTles() {
        String url = "https://celestrak.com/NORAD/elements/gp.php?GROUP=active&FORMAT=tle";
        String response = restTemplate.getForObject(url, String.class);

        if (response != null) {
            String[] lines = response.split("\n");
            for (int i = 0; i < lines.length; i += 3) {
                String name = lines[i].trim();
                String line1 = lines[i + 1].trim();
                String line2 = lines[i + 2].trim();

                // Extract NORAD ID from line1
                Integer noradId = Integer.parseInt(line1.substring(2, 7));

                Satellite sat = satelliteRepository.findByNoradId(noradId)
                        .orElseGet(Satellite::new);

                sat.setNoradId(noradId);
                sat.setName(name);
                sat.setLine1(line1);
                sat.setLine2(line2);

                satelliteRepository.save(sat);
            }
        }
    }
}