package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsiteIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void homePageLoads() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        // If redirected to login, follow the redirect and check login page
        if (response.getStatusCode().is3xxRedirection() && response.getHeaders().getLocation() != null) {
            String loginUrl = response.getHeaders().getLocation().toString();
            ResponseEntity<String> loginPage = restTemplate.getForEntity(loginUrl, String.class);
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
        String registerForm = "username=testuser&password=testpass";
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/register", registerForm, String.class);
        assertThat(registerResponse.getStatusCode().is3xxRedirection()).isTrue();
        // Try to login (should redirect to /)
        String loginForm = "username=testuser&password=testpass";
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginForm, String.class);
        assertThat(loginResponse.getStatusCode().is3xxRedirection()).isTrue();
    }
}
