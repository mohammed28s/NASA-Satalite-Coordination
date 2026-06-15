package com.Satliate.NASA.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {   // Make sure you have a Space‑Track account: Space‑Track.org
        return builder
                .baseUrl("https://www.space-track.org")
                .defaultHeaders(headers -> headers.setBasicAuth("your_username", "your_password"))
                .build();
    }
}
