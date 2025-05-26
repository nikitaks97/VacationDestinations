package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebsiteIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void homePageLoads() {
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/", String.class);
        assertThat(response).isNotNull();
        
        if (response.getStatusCode() == HttpStatus.FOUND) {
            var headers = response.getHeaders();
            var location = headers != null ? headers.getLocation() : null;
            
            if (location != null) {
                String loginUrl = location.toString();
                ResponseEntity<String> loginPage = restTemplate.getForEntity(loginUrl, String.class);
                
                // Check login page content only if the response is not null
                if (loginPage != null && loginPage.getBody() != null) {
                    assertThat(loginPage.getBody()).contains("Login");
                }
            }
        } else {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("<!DOCTYPE html>");
        }
    }

    @Test
    void registerAndLoginFlow() {
        // Create form data for registration
        MultiValueMap<String, String> registerForm = new LinkedMultiValueMap<>();
        registerForm.add("username", "testuser");
        registerForm.add("password", "testpass");

        // Register a new user
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
            getBaseUrl() + "/register",
            registerForm,
            String.class
        );
        assertThat(registerResponse.getStatusCode().is3xxRedirection()).isTrue();

        // Create form data for login
        MultiValueMap<String, String> loginForm = new LinkedMultiValueMap<>();
        loginForm.add("username", "testuser");
        loginForm.add("password", "testpass");

        // Try to login
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            getBaseUrl() + "/login",
            loginForm,
            String.class
        );
        assertThat(loginResponse.getStatusCode().is3xxRedirection()).isTrue();
    }
}
