package com.example.demo.controller;

import com.example.demo.model.Destination;
import com.example.demo.model.Like;
import com.example.demo.model.Comment;
import com.example.demo.dto.DestinationDto;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.time.LocalDateTime;

@Controller
public class DestinationController {
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("destinations", destinationRepository.findAll());
        return "home";
    }

    @GetMapping("/destination/{id}")
    public String destinationPage(@PathVariable Long id, Model model) {
        Destination destination = destinationRepository.findById(id).orElseThrow();
        List<Comment> comments = commentRepository.findByDestinationId(id);
        model.addAttribute("destination", destination);
        model.addAttribute("comments", comments);
        return "destination";
    }

    @PostMapping("/destination/{id}/like")
    public String likeDestination(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            // Prevent duplicate likes
            if (!likeRepository.existsByUserUsernameAndDestinationId(username, id)) {
                Like like = new Like();
                like.setUser(userRepository.findByUsername(username).orElse(null));
                like.setDestination(destinationRepository.findById(id).orElse(null));
                likeRepository.save(like);
            }
        }
        return "redirect:/destination/" + id;
    }

    @PostMapping("/destination/{id}/comment")
    public String commentDestination(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String content) {
        if (userDetails != null && content != null && !content.trim().isEmpty()) {
            String username = userDetails.getUsername();
            Comment comment = new Comment();
            comment.setUser(userRepository.findByUsername(username).orElse(null));
            comment.setDestination(destinationRepository.findById(id).orElse(null));
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());
            commentRepository.save(comment);
        }
        return "redirect:/destination/" + id;
    }

    @GetMapping("/destination/new")
    public String showNewDestinationForm(Model model) {
        model.addAttribute("destinationDto", new DestinationDto());
        return "destination-form";
    }

    @PostMapping("/destination/new")
    public String createDestination(
            @Valid @ModelAttribute DestinationDto destinationDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (bindingResult.hasErrors()) {
            return "destination-form";
        }

        Destination destination = new Destination();
        destination.setName(destinationDto.getName());
        destination.setDescription(destinationDto.getDescription());
        destination.setImageUrl(destinationDto.getImageUrl());
        destination.setCreatedBy(userRepository.findByUsername(userDetails.getUsername()).orElseThrow());
        
        destinationRepository.save(destination);
        return "redirect:/destination/" + destination.getId();
    }
}
