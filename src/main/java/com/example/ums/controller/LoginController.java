package com.example.ums.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
        private final CognitoIdentityProviderClient cognitoClient;

        @PostMapping("/initiate-login")
        ResponseEntity<Void> login(@RequestBody(required = false) Map<String, String> loginRequest) {

                ListUserPoolsRequest request = ListUserPoolsRequest.builder()
                                .maxResults(10)
                                .build();

                ListUserPoolsResponse response = cognitoClient.listUserPools(request);
                response.userPools().stream().forEach(userPool -> log.debug("Userpool: name: {} ", userPool.name()));
                return ResponseEntity.ok()
                                .build();
        }
}
