package com.golfacademy.clientapplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@Slf4j
public class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final String REGISTRATION_ID = "golf-client";

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private OAuth2AccessToken accessToken;

    public OAuthClientCredentialsRestTemplateInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull HttpRequest request,
                                                 @NonNull byte[] body,
                                                 @NonNull ClientHttpRequestExecution execution)
            throws IOException {
        final OAuth2AccessToken accessToken = getAccessToken();
        if (accessToken == null) {
            return execution.execute(request, body);
        }
        request.getHeaders().setBearerAuth(accessToken.getTokenValue());
        return execution.execute(request, body);
    }

    private @Nullable OAuth2AccessToken getAccessToken() {
        log.info("getAccessToken()");
        if (!isTokenValid(accessToken))
            generateNewAccessToken();
        return accessToken;
    }

    private void generateNewAccessToken() {
        log.info("generateNewAccessToken()");
        accessToken = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return;
        OAuth2AuthorizeRequest req = OAuth2AuthorizeRequest
                .withClientRegistrationId(REGISTRATION_ID).principal(authentication.getName()).build();
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(req);
        if (authorizedClient == null)
            return;
        accessToken = authorizedClient.getAccessToken();
    }

    private static boolean isTokenValid(@Nullable OAuth2AccessToken token) {
        return token != null && token.getExpiresAt() != null
                && token.getExpiresAt().isAfter(Instant.now());
    }
}
