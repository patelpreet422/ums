package com.example.ums.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.ums.config.CognitoConfigurationProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2CallbackController {

    private final RestTemplate restTemplate;

    private final CognitoConfigurationProperties cognitoConfig;

    private final ObjectMapper objectMapper;

    @GetMapping("/callback")
    ResponseEntity<Void> handleCallback(@RequestParam("code") String code) throws JsonProcessingException {
        String url = cognitoConfig.getDomain() + "/oauth2/token";

        MultiValueMap<String, Object> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("grant_type", "authorization_code");
        queryParams.add("redirect_uri", cognitoConfig.getClientRedirectUri());
        queryParams.add("client_id", cognitoConfig.getClientId());
        queryParams.add("client_secret", cognitoConfig.getClientSecret());
        queryParams.add("code", code);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(queryParams, httpHeaders);

        Map<String, Object> response = restTemplate
                .exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {
                }).getBody();

        log.debug("Response: {}", objectMapper.writeValueAsString(response));

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(String.format("https://jwt.io/?access_token=%s&id_token=%s",
                        response.get("access_token"), response.get("id_token"))))
                .build();
    }
}
