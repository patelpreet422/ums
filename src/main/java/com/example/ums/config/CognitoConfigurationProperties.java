package com.example.ums.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cognito")
@Data
public class CognitoConfigurationProperties {
    private String domain = "";
    private String jwksUri = "";
    private String issuerUri = "";
}
