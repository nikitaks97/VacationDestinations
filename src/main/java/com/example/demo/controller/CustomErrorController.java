package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            model.addAttribute("status", statusCode);
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Page Not Found");
                model.addAttribute("message", "The page you are looking for doesn't exist.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Internal Server Error");
                model.addAttribute("message", "Something went wrong on our end.");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Access Forbidden");
                model.addAttribute("message", "You don't have permission to access this resource.");
            } else {
                model.addAttribute("error", "Error " + statusCode);
                model.addAttribute("message", "An unexpected error occurred.");
            }
        } else {
            model.addAttribute("status", "Unknown");
            model.addAttribute("error", "Unknown Error");
            model.addAttribute("message", "An unexpected error occurred.");
        }
        
        return "error";
    }
}
