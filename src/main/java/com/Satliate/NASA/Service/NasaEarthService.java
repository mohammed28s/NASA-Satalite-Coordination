package com.Satliate.NASA.Service;

import com.Satliate.NASA.Expection.NasaApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NasaEarthService {

    private final WebClient webClient;

    @Value("${app.nasa.api-key:DEMO_KEY}")
    private String apiKey;

    public NasaEarthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.nasa.gov").build();
    }

    public String getEarthImage(double lon, double lat, String date, double dim) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/planetary/earth/assets")
                        .queryParam("lon", lon)
                        .queryParam("lat", lat)
                        .queryParam("date", date)
                        .queryParam("dim", dim)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    HttpStatus status = (HttpStatus) response.statusCode();
                    if (status == HttpStatus.UNAUTHORIZED || status == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.error(new NasaApiException("NASA API error: " + status.getReasonPhrase(), status.value()));
                    }
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
    }
}
