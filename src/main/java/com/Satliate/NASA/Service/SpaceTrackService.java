package com.Satliate.NASA.Service;






import com.Satliate.NASA.DTO.SatcatDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class SpaceTrackService {

    private final WebClient webClient;

    public SpaceTrackService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<SatcatDto> fetchActiveSatellites() {   // This is fetching the active satellites
        return webClient.get()
                .uri("/basicspacedata/query/class/satcat/DECAY_DATE/null-val/orderby/SATNAME/format/json")
                .retrieve()
                .bodyToFlux(SatcatDto.class)
                .collectList()
                .block();
    }

    public List<SatcatDto> fetchInactiveSatellites() {    // This is fetching the unactive satellites
        return webClient.get()
                .uri("/basicspacedata/query/class/satcat/DECAY_DATE/<>null-val/orderby/SATNAME/format/json")
                .retrieve()
                .bodyToFlux(SatcatDto.class)
                .collectList()
                .block();
    }

    public SatcatDto fetchSatelliteByNoradId(Integer noradId) {   // This is fetching the satellites by the nordId
        return webClient.get()
                .uri("/basicspacedata/query/class/satcat/NORAD_CAT_ID/" + noradId + "/orderby/SATNAME/format/json")
                .retrieve()
                .bodyToFlux(SatcatDto.class)
                .blockFirst();
    }
}
