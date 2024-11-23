package com.golfacademy.clientapplication;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, OAuth2AuthorizedClientManager authorizedClientManager ,
                                     ClientRegistrationRepository clientRegistrationRepository) {

        return restTemplateBuilder
                .additionalInterceptors(new OAuthClientCredentialsRestTemplateInterceptor(authorizedClientManager))
                .build();
    }


}