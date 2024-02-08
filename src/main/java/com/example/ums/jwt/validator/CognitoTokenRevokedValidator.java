package com.example.ums.jwt.validator;

import com.example.ums.config.CognitoConfigurationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Setter
@Slf4j
@RequiredArgsConstructor
public final class CognitoTokenRevokedValidator implements OAuth2TokenValidator<Jwt> {

    private final RestTemplate restTemplate;

    private final CognitoConfigurationProperties cognitoConfigurationProperties;

    private String uri = null;

    @PostConstruct
    void buildUrl() {
        this.uri = String.format("%s/oauth2/userInfo", cognitoConfigurationProperties.getDomain());
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token.getTokenValue());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(uri, entity, String.class);
        } catch (HttpClientErrorException ex) {
            return handleClientErrorException(ex);

        } catch (HttpServerErrorException ex) {
            return handleServerErrorException(ex);
        }

        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult handleHttpErrorException(HttpStatusCodeException ex, String errorType, String errorMessage) {
        log.error("(Cognito) - httpStatus: {}: {} exception while calling userInfo endpoint of cognito during Jwt token validation",
                ex.getMessage(), errorType, ex);

        OAuth2Error oAuth2Error = new OAuth2Error("invalid_request", errorMessage, uri);

        return OAuth2TokenValidatorResult.failure(oAuth2Error);
    }

    private OAuth2TokenValidatorResult handleClientErrorException(HttpClientErrorException ex) {
        return handleHttpErrorException(ex, "client", "Access token is expired or user has globally signed out, disabled, or been deleted");
    }

    private OAuth2TokenValidatorResult handleServerErrorException(HttpServerErrorException ex) {
        return handleHttpErrorException(ex, "server", "Could not validate access token, received server exception from Cognito userIndo endpoint");
    }

}
