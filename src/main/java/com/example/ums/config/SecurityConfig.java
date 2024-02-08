package com.example.ums.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Setter
@RequiredArgsConstructor
public class SecurityConfig {

    @Qualifier(AppConstants.ACCESS_TOKEN_JWT_DECODER)
    private final JwtDecoder accessTokenJwtDecoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest()
                        .permitAll()
                )
                .build();
        // return http
        //         .authorizeHttpRequests((authorize) -> authorize
        //                 .anyRequest()
        //                 .authenticated()
        //         )
        //         .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer.jwt(
        //                 jwtConfigurer -> jwtConfigurer.decoder(accessTokenJwtDecoder)
        //         ))
        //         .build();
    }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {
    //     JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    //     jwtAuthenticationConverter.setPrincipalClaimName("username");
    //     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt ->
    //             Stream.of(
    //                             Optional.ofNullable((List<String>) jwt.getClaims().get("cognito:groups"))
    //                                     .orElse(Collections.<String>emptyList()
    //                                     )
    //                     )
    //                     .flatMap(java.util.Collection::stream)
    //                     .map(group -> new SimpleGrantedAuthority("ROLE_" + group.toLowerCase(Locale.ROOT)))
    //                     .collect(Collectors.toSet())
    //     );
    //
    //     return jwtAuthenticationConverter;
    // }

}
