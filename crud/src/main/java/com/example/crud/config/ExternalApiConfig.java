package com.example.crud.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {

    @Value("${external.api.url}")
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

}
