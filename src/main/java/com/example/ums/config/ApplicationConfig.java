package com.example.ums.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
  private final CognitoConfigurationProperties cognitoConfig;

  @Bean
  CognitoIdentityProviderClient cognitoClient(final OkHttpClient okHttpClient) {

    return CognitoIdentityProviderClient.builder()
        .build();
  }

  @Bean
  OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyManagementException {

    // okhttp builder
    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

    // ssl trust store
    TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
      @Override
      public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
      }

      @Override
      public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
      }

      @Override
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
      }
    };
    // get current ssl context
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, new TrustManager[] { TRUST_ALL_CERTS }, new java.security.SecureRandom());

    okHttpBuilder.sslSocketFactory(sslContext.getSocketFactory(),
        (X509TrustManager) TRUST_ALL_CERTS);

    okHttpBuilder.hostnameVerifier((hostname, sslSession) -> true);

    return okHttpBuilder.build();
  }

  @Bean
  RestTemplate restTemplateLocal(final RestTemplateBuilder builder, final OkHttpClient okHttpClient) {
    RestTemplate restTemplate = builder.build();

    restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));

    return restTemplate;
  }

}
