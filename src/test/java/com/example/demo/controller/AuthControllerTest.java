package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String REGISTER_URL = "/register";
    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_VIEW = "register";
    private static final String LOGIN_VIEW = "login";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String ERROR_ATTR = "error";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String USERNAME_EXISTS_ERROR = "Username already exists";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void showRegistrationFormDisplaysForm() throws Exception {
        mockMvc.perform(get(REGISTER_URL))
            .andExpect(status().isOk())
            .andExpect(view().name(REGISTER_VIEW))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUserSuccessfully() throws Exception {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act & Assert
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(USERNAME_PARAM, TEST_USERNAME)
                .param(PASSWORD_PARAM, TEST_PASSWORD))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(LOGIN_URL));

        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserWithExistingUsername() throws Exception {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME))
            .thenReturn(Optional.of(new User()));

        // Act & Assert
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(USERNAME_PARAM, TEST_USERNAME)
                .param(PASSWORD_PARAM, TEST_PASSWORD))
            .andExpect(status().isOk())
            .andExpect(view().name(REGISTER_VIEW))
            .andExpect(model().attributeExists(ERROR_ATTR))
            .andExpect(model().attribute(ERROR_ATTR, USERNAME_EXISTS_ERROR));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void showLoginFormDisplaysForm() throws Exception {
        mockMvc.perform(get(LOGIN_URL))
            .andExpect(status().isOk())
            .andExpect(view().name(LOGIN_VIEW));
    }

    @Test
    void registerUserWithEmptyUsername() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(USERNAME_PARAM, "")
                .param(PASSWORD_PARAM, TEST_PASSWORD))
            .andExpect(status().isOk())
            .andExpect(view().name(REGISTER_VIEW))
            .andExpect(model().attributeExists(ERROR_ATTR));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserWithEmptyPassword() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(USERNAME_PARAM, TEST_USERNAME)
                .param(PASSWORD_PARAM, ""))
            .andExpect(status().isOk())
            .andExpect(view().name(REGISTER_VIEW))
            .andExpect(model().attributeExists(ERROR_ATTR));

        verify(userRepository, never()).save(any(User.class));
    }
}
