package com.Satliate.NASA.Service;




import com.Satliate.NASA.Expection.SpaceTrackException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SpaceTrackAuthService {

    private final WebClient webClient;

    public SpaceTrackAuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void login(String username, String password) {
        String body = "identity=" + username + "&password=" + password;

        try {
            webClient.post()
                    .uri("/ajaxauth/login")
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(
                                            new SpaceTrackException(errorBody, response.statusCode().value())
                                    ))
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new SpaceTrackException("Unable to connect to Space-Track server", 500);
        }
    }
}
