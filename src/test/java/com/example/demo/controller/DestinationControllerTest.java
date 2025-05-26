package com.example.demo.controller;

import com.example.demo.model.Destination;
import com.example.demo.model.User;
import com.example.demo.model.Comment;
import com.example.demo.model.Like;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
@Import({ DestinationRepository.class, UserRepository.class, CommentRepository.class, LikeRepository.class })
class DestinationControllerTest {

    private static final String URL_DESTINATION_BASE = "/destination";
    private static final String URL_DESTINATION_NEW = URL_DESTINATION_BASE + "/new";
    private static final String URL_DESTINATION_1 = URL_DESTINATION_BASE + "/1";
    private static final String URL_HOME = "/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    private User testUser;
    private Destination testDestination;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testDestination = new Destination();
        testDestination.setId(1L);
        testDestination.setName("Test Destination");
        testDestination.setDescription("Test Description");
        testDestination.setImageUrl("http://test.com/image.jpg");
        testDestination.setCreatedBy(testUser);

        reset(destinationRepository, userRepository, commentRepository, likeRepository);
    }

    @Test
    void testHomePageShowsAllDestinations() throws Exception {
        when(destinationRepository.findAll()).thenReturn(Arrays.asList(testDestination));

        mockMvc.perform(get(URL_HOME))
               .andExpect(status().isOk())
               .andExpect(view().name("home"))
               .andExpect(model().attributeExists("destinations"));

        verify(destinationRepository).findAll();
    }

    @Test
    void testDestinationPageShowsDetails() throws Exception {
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
        when(commentRepository.findByDestinationId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get(URL_DESTINATION_1))
               .andExpect(status().isOk())
               .andExpect(view().name("destination"))
               .andExpect(model().attributeExists("destination", "comments"));

        verify(destinationRepository).findById(1L);
        verify(commentRepository).findByDestinationId(1L);
    }

    @Test
    @WithMockUser
    void testCreateDestinationSuccess() throws Exception {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(testUser));
        when(destinationRepository.save(any(Destination.class))).thenReturn(testDestination);

        mockMvc.perform(post(URL_DESTINATION_NEW)
               .with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("name", "New Destination")
               .param("description", "New Description")
               .param("imageUrl", "http://test.com/new-image.jpg"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern(URL_DESTINATION_BASE + "/*"));

        verify(destinationRepository).save(any(Destination.class));
    }

    @Test
    @WithMockUser
    void testCreateDestinationValidationFailure() throws Exception {
        mockMvc.perform(post(URL_DESTINATION_NEW)
               .with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("name", "") // Empty name should fail validation
               .param("description", "New Description")
               .param("imageUrl", "http://test.com/new-image.jpg"))
               .andExpect(status().isOk())
               .andExpect(view().name("destination-form"));

        verify(destinationRepository, never()).save(any(Destination.class));
    }

    @Test
    @WithMockUser
    void testLikeDestinationSuccess() throws Exception {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(testUser));
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
        when(likeRepository.existsByUserUsernameAndDestinationId("user", 1L)).thenReturn(false);

        mockMvc.perform(post(URL_DESTINATION_1 + "/like")
               .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_DESTINATION_1));

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    @WithMockUser
    void testLikeDestinationPreventDuplicate() throws Exception {
        when(likeRepository.existsByUserUsernameAndDestinationId("user", 1L)).thenReturn(true);

        mockMvc.perform(post(URL_DESTINATION_1 + "/like")
               .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_DESTINATION_1));

        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    @WithMockUser
    void testCommentOnDestinationSuccess() throws Exception {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(testUser));
        when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));

        mockMvc.perform(post(URL_DESTINATION_1 + "/comment")
               .with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("content", "Great place!"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_DESTINATION_1));

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @WithMockUser
    void testCommentOnDestinationEmptyContent() throws Exception {
        mockMvc.perform(post(URL_DESTINATION_1 + "/comment")
               .with(csrf())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("content", ""))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(URL_DESTINATION_1));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetNewDestinationFormUnauthenticated() throws Exception {
        mockMvc.perform(get(URL_DESTINATION_NEW))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void testGetNewDestinationFormAuthenticated() throws Exception {
        mockMvc.perform(get(URL_DESTINATION_NEW))
               .andExpect(status().isOk())
               .andExpect(view().name("destination-form"))
               .andExpect(model().attributeExists("destinationDto"));
    }
}
