package com.example.ums.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfigurationProperties {
    private final Validation validation = new Validation();
    private String clientId = "";

    @Data
    public static class Validation {
        private boolean checkRevoked = false;
        private boolean checkClientId = false;
    }
}
