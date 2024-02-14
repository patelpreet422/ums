package com.example.ums.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@ConfigurationProperties(prefix = "cognito")
@Data
public class CognitoConfigurationProperties {
    private String domain = "";
    private String issuerUri = "";
    private String jwksUri = "";
    private String userpoolId = "";
    private String clientId = "";
    private String clientSecret = "";
    private String clientRedirectUri = "";
}
