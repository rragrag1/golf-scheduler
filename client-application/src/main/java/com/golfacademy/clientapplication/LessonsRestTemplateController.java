package com.golfacademy.clientapplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@RestController
@Slf4j
public class LessonsRestTemplateController {

    private final RestTemplate restTemplate;

    public LessonsRestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/lessons2")
    public String fetchLessons() {
        log.info("Calling Get /lessons2 ");
        String url="http://localhost:8081/lessons";
        return restTemplate.getForObject(url,String.class );
                // .attributes(clientRegistrationId("golf-client"))
    }
}