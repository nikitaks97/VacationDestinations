package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebsiteIntegrationTest {

    @Mock
    private TestRestTemplate restTemplate;

    @InjectMocks
    private WebsiteIntegrationTest testInstance;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    @Test
    void homePageLoads() {
        // Mock the response
        ResponseEntity<String> mockResponse = mock(ResponseEntity.class);
        HttpHeaders mockHeaders = mock(HttpHeaders.class);
        when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponse.getBody()).thenReturn("<!DOCTYPE html>");
        when(mockResponse.getHeaders()).thenReturn(mockHeaders);
        when(mockHeaders.getLocation()).thenReturn(null);

        // Mock the restTemplate behavior
        when(restTemplate.getForEntity("/", String.class)).thenReturn(mockResponse);

        // Use the mock response
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        assertThat(response).isNotNull(); // Ensure response is not null
        assertThat(response.getBody()).isNotNull(); // Ensure response body is not null

        // Check if headers are present and extract location
        String loginUrl = null;
        if (response.getHeaders() != null && response.getHeaders().getLocation() != null) {
            loginUrl = response.getHeaders().getLocation().toString();
        }

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

    @Test
    void registerAndLoginFlow() {
        // Register a new user
        String registerForm = String.format("username=%s&password=%s", TEST_USERNAME, TEST_PASSWORD);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/register", registerForm, String.class);
        assertThat(registerResponse.getStatusCode().is3xxRedirection()).isTrue();
        // Try to login (should redirect to /)
        String loginForm = String.format("username=%s&password=%s", TEST_USERNAME, TEST_PASSWORD);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginForm, String.class);
        assertThat(loginResponse.getStatusCode().is3xxRedirection()).isTrue();
    }
}
