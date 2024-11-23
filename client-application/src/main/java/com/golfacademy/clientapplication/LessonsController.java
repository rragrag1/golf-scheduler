package com.golfacademy.clientapplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@RestController
@Slf4j
public class LessonsController {

    private final RestClient restClient;

    public LessonsController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/lessons")
    public String fetchLessons() {
        log.info("Calling Get /lessons ");
        return restClient.get()
                .uri("http://localhost:8081/lessons")
                .attributes(clientRegistrationId("golf-client"))
                .retrieve()
                .body(String.class);
    }
}