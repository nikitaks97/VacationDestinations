package com.example.demo.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.CommentRepository;

/**
 * Custom health indicator to monitor application health
 * This demonstrates monitoring capabilities for the monolith
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DestinationRepository destinationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Health health() {
        try {
            // Check database connectivity by counting records
            long destinationCount = destinationRepository.count();
            long userCount = userRepository.count();
            long likeCount = likeRepository.count();
            long commentCount = commentRepository.count();
            
            // Calculate some basic metrics
            double avgLikesPerDestination = destinationCount > 0 ? (double) likeCount / destinationCount : 0;
            double avgCommentsPerDestination = destinationCount > 0 ? (double) commentCount / destinationCount : 0;
            
            return Health.up()
                    .withDetail("destinations", destinationCount)
                    .withDetail("users", userCount)
                    .withDetail("likes", likeCount)
                    .withDetail("comments", commentCount)
                    .withDetail("avgLikesPerDestination", String.format("%.2f", avgLikesPerDestination))
                    .withDetail("avgCommentsPerDestination", String.format("%.2f", avgCommentsPerDestination))
                    .withDetail("status", "Database is accessible")
                    .build();
                    
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("status", "Database is not accessible")
                    .build();
        }
    }
}
