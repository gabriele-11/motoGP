package com.example.crud.ServiceTest;

import com.example.crud.config.ExternalApiConfig;
import com.example.crud.controller.ExternalApiController;
import com.example.crud.exception.ApiCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExternalApiControllerTest {

    @Mock
    private ExternalApiConfig externalApiConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalApiController apiController;

    private final String mockUrl = "https://dogapi.dog/api/v2/breeds";
    private final String errorMessage = "Errore durante la chiamata all'API esterna";


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetExternalDataSuccess() throws ApiCallException {

        String mockResponse = "{\"data\": []}";
        when(externalApiConfig.getApiUrl()).thenReturn(mockUrl);
        when(restTemplate.getForObject(mockUrl, String.class)).thenReturn(mockResponse);

        String response = apiController.getExternalData();

        assertNotNull(response);
        assertEquals(mockResponse, response);
        verify(externalApiConfig).getApiUrl();
        verify(restTemplate).getForObject(mockUrl, String.class);
    }

    @Test
    void testGetExternalDataThrowsApiCallException() {

        when(externalApiConfig.getApiUrl()).thenReturn(mockUrl);
        when(restTemplate.getForObject(mockUrl, String.class)).thenThrow(new ApiCallException(errorMessage));


        ApiCallException exception = assertThrows(ApiCallException.class, () -> {
            apiController.getExternalData();
        });

        assertEquals(errorMessage, exception.getMessage());
    }

}
