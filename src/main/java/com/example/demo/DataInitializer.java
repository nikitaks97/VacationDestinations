package com.example.demo;

import com.example.demo.model.Destination;
import com.example.demo.model.User;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(DestinationRepository destinationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create test user if not exists
            User admin;
            if (userRepository.count() == 0) {
                admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN"); // Changed from "ROLE_ADMIN" to "ADMIN"
                admin = userRepository.save(admin);
            } else {
                admin = userRepository.findByUsername("admin").orElseThrow();
            }

            // Create sample destinations
            if (destinationRepository.count() == 0) {
                destinationRepository.save(new Destination(null, "Paris", "The city of lights and romance. Home to iconic landmarks like the Eiffel Tower, Louvre Museum, and Notre-Dame Cathedral. Known for its art, fashion, gastronomy, and culture.", 
                    "https://images.unsplash.com/photo-1499856871958-5b9627545d1a", admin, null, null));
                
                destinationRepository.save(new Destination(null, "Tokyo", "A bustling metropolis that perfectly blends ultra-modern technology with traditional Japanese culture. Famous for its anime culture, sushi restaurants, and efficient transportation.", 
                    "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf", admin, null, null));
                
                destinationRepository.save(new Destination(null, "New York", "The city that never sleeps. A global center of art, culture, fashion, and finance. Home to Central Park, Times Square, Broadway theaters, and world-class museums.", 
                    "https://images.unsplash.com/photo-1522083165195-3424ed129620", admin, null, null));
            }
        };
    }
}
