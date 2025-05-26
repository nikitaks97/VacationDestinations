package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WebsiteIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void homePageLoads() {
        // Mock the response
        ResponseEntity<String> mockResponse = mock(ResponseEntity.class);
        HttpHeaders mockHeaders = new HttpHeaders(); // Use real HttpHeaders instance
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("<!DOCTYPE html>");
        when(mockResponse.getHeaders()).thenReturn(mockHeaders);

        // Use the real `restTemplate` instance
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/", String.class);
        assertThat(response).isNotNull(); // Ensure response is not null

        if (response != null && response.getHeaders() != null) {
            HttpHeaders headers = response.getHeaders();
            String loginUrl = (headers.getLocation() != null) ? headers.getLocation().toString() : null;

            // Check if redirection to login occurs
            if (response.getStatusCode().is3xxRedirection() && loginUrl != null) {
                ResponseEntity<String> loginPage = restTemplate.getForEntity(loginUrl, String.class);
                assertThat(loginPage).isNotNull(); // Ensure loginPage is not null
                assertThat(loginPage.getBody()).contains("Login");
            } else {
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                // Only check for generic HTML content
                assertThat(response.getBody()).contains("<!DOCTYPE html>");
            }
        }
    }

    @Test
    void registerAndLoginFlow() {
        // Register a new user
        String registerForm = String.format("username=%s&password=%s", TEST_USERNAME, TEST_PASSWORD);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(BASE_URL + "/register", registerForm, String.class);
        assertThat(registerResponse.getStatusCode().is3xxRedirection()).isTrue();

        // Try to login (should redirect to /)
        String loginForm = String.format("username=%s&password=%s", TEST_USERNAME, TEST_PASSWORD);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(BASE_URL + "/login", loginForm, String.class);
        assertThat(loginResponse.getStatusCode().is3xxRedirection()).isTrue();
    }
}
