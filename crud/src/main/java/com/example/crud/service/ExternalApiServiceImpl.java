package com.example.crud.service;


import com.example.crud.config.ExternalApiConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiServiceImpl implements ExternalApiService {


    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;

    public ExternalApiServiceImpl(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }


    @Override
    public String callExternalApi(String url) {
        String apiUrl = externalApiConfig.getApiUrl();
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
