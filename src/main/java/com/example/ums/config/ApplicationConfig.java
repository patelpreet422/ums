package com.example.ums.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  @Bean
  CognitoIdentityProviderClient cognitoClient() {

    return CognitoIdentityProviderClient.builder()
        .build();
  }

  @Bean
  RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }

}
