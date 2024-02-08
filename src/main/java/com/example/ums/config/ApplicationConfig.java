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

import okhttp3.OkHttpClient;

@Configuration
public class ApplicationConfig {

  @Bean
  RestTemplate restTemplateLocal(final RestTemplateBuilder builder)
      throws NoSuchAlgorithmException, KeyManagementException {
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
        return new java.security.cert.X509Certificate[]{};
      }
    };
    // get current ssl context
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, new TrustManager[]{TRUST_ALL_CERTS}, new java.security.SecureRandom());

    okHttpBuilder.sslSocketFactory(sslContext.getSocketFactory(),
        (X509TrustManager) TRUST_ALL_CERTS);

    okHttpBuilder.hostnameVerifier((hostname, sslSession) -> true);

    RestTemplate restTemplate = builder.build();

    restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpBuilder.build()));

    return restTemplate;
  }

}
