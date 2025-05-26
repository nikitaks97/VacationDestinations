package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUserRole(@PathVariable("id") Long id, @ModelAttribute("user") User userDetails, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Prevent changing the role of the currently logged-in admin if it's the 'admin' user to avoid lockout
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (user.getUsername().equals("admin") && user.getUsername().equals(currentPrincipalName) && !userDetails.getRole().contains("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Cannot remove ADMIN role from the primary 'admin' user.");
            return "redirect:/admin/dashboard";
        }

        user.setRole(userDetails.getRole());
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User role updated successfully.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        if (userToDelete.getUsername().equals(currentPrincipalName)) {
            redirectAttributes.addFlashAttribute("error", "You cannot delete your own account.");
            return "redirect:/admin/dashboard";
        }

        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        return "redirect:/admin/dashboard";
    }
}
