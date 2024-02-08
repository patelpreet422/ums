package com.example.ums.config;

import com.example.ums.jwt.validator.CognitoTokenRevokedValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class JwtDecoderConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwks-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    private final JwtConfigurationProperties jwtConfigurationProperties;

    private final CognitoTokenRevokedValidator cognitoTokenRevokedValidator;

    @Bean(name = AppConstants.ACCESS_TOKEN_JWT_DECODER)
    JwtDecoder accessTokenJwtDecoder() {

        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .build();

        nimbusJwtDecoder.setJwtValidator(jwtValidator());

        return nimbusJwtDecoder;
    }

    public OAuth2TokenValidator<Jwt> jwtValidator() {

        List<OAuth2TokenValidator<Jwt>> jwtValidators = new ArrayList<>(List.of(
                new JwtTimestampValidator(),
                new JwtIssuerValidator(issuer)
        ));

        if(Objects.nonNull(jwtConfigurationProperties) && jwtConfigurationProperties.getValidation().isCheckRevoked()) {
            jwtValidators.add(new JwtClaimValidator<String>(
                            "client_id",
                            (claimValue) -> claimValue.equals(jwtConfigurationProperties.getClientId())
                    )
            );
        }

        if(Objects.nonNull(jwtConfigurationProperties) && jwtConfigurationProperties.getValidation().isCheckClientId()) {
            jwtValidators.add(cognitoTokenRevokedValidator);
        }

        return new DelegatingOAuth2TokenValidator<>(jwtValidators);
    }

    @Bean(name = AppConstants.ID_TOKEN_JWT_DECODER)
    JwtDecoder idTokenJwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .build();
    }
}
