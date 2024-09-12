package com.example.crud.controller;


import com.example.crud.config.ExternalApiConfig;
import com.example.crud.exception.ApiCallException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/externalApi")
public class ExternalApiController {

    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;
    private static final String ERRORMESSAGE = "Errore durante la chiamata all'API esterna";

    public ExternalApiController(RestTemplate restTemplate, ExternalApiConfig externalApiConfig) {
        this.restTemplate = restTemplate;
        this.externalApiConfig = externalApiConfig;
    }


    @GetMapping("/breeds")
    public String getExternalData() throws ApiCallException {

        String apiUrl = externalApiConfig.getApiUrl();
        try {
            return restTemplate.getForObject(apiUrl, String.class);
        } catch (Exception e) {
            throw new ApiCallException(ERRORMESSAGE);
        }

    }


}
