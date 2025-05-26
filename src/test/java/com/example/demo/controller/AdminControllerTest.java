package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Optional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    private static final String URL_ADMIN_BASE = "/admin";
    private static final String URL_ADMIN_DASHBOARD = URL_ADMIN_BASE + "/dashboard";
    private static final String URL_ADMIN_USERS = URL_ADMIN_BASE + "/users";
    private static final String URL_ADMIN_USER_EDIT = URL_ADMIN_USERS + "/edit/{id}";
    private static final String URL_ADMIN_USER_DELETE = URL_ADMIN_USERS + "/delete/{id}";
    
    private static final String VIEW_ADMIN_DASHBOARD = "admin-dashboard";
    private static final String VIEW_EDIT_USER = "edit-user";
    
    private static final String ATTR_USERS = "users";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_SUCCESS = "success";
    
    private static final String MSG_CANT_DELETE_SELF = "You cannot delete your own account.";
    private static final String MSG_USER_DELETED = "User deleted successfully.";
    private static final String MSG_CANT_REMOVE_ADMIN = "Cannot remove ADMIN role from the primary 'admin' user.";
    private static final String MSG_ROLE_UPDATED = "User role updated successfully.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setRole("ADMIN");

        regularUser = new User();
        regularUser.setId(2L);
        regularUser.setUsername("user");
        regularUser.setRole("USER");

        // No need to reset mock as @MockBean is recreated for each test
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));
    }

    @Test
    void testAdminDashboardUnauthorizedRedirectsToLogin() throws Exception {
        mockMvc.perform(get(URL_ADMIN_DASHBOARD))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdminDashboardForbiddenForUsers() throws Exception {
        mockMvc.perform(get(URL_ADMIN_DASHBOARD))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminDashboardDisplaysUserList() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, regularUser));

        mockMvc.perform(get(URL_ADMIN_DASHBOARD))
               .andExpect(status().isOk())
               .andExpect(view().name(VIEW_ADMIN_DASHBOARD))
               .andExpect(model().attributeExists(ATTR_USERS));

        verify(userRepository).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditUserFormDisplays() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(regularUser));

        mockMvc.perform(get(URL_ADMIN_USER_EDIT, 1))
               .andExpect(status().isOk())
               .andExpect(view().name(VIEW_EDIT_USER))
               .andExpect(model().attributeExists(ATTR_USER));

        verify(userRepository).findById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUserRoleSuccess() throws Exception {
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(any(User.class))).thenReturn(regularUser);

        mockMvc.perform(post(URL_ADMIN_USER_EDIT, 2)
               .with(csrf())
               .param("role", "ADMIN"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_ADMIN_DASHBOARD))
               .andExpect(flash().attribute(ATTR_SUCCESS, MSG_ROLE_UPDATED));

        verify(userRepository).save(any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCannotRemoveAdminRoleFromSelf() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(post(URL_ADMIN_USER_EDIT, 1)
               .with(csrf())
               .param("role", "USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_ADMIN_DASHBOARD))
               .andExpect(flash().attribute(ATTR_ERROR, MSG_CANT_REMOVE_ADMIN));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserSuccess() throws Exception {
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));

        mockMvc.perform(post(URL_ADMIN_USER_DELETE, 2)
               .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_ADMIN_DASHBOARD))
               .andExpect(flash().attribute(ATTR_SUCCESS, MSG_USER_DELETED));

        verify(userRepository).deleteById(2L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCannotDeleteSelf() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        mockMvc.perform(post(URL_ADMIN_USER_DELETE, 1)
               .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_ADMIN_DASHBOARD))
               .andExpect(flash().attribute(ATTR_ERROR, MSG_CANT_DELETE_SELF));

        verify(userRepository, never()).deleteById(anyLong());
    }
}
