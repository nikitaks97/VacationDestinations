package com.example.demo.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.CommentRepository;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * Performance monitoring endpoint for the monolith
 * Provides insights into current system performance and bottlenecks
 */
@RestController
@RequestMapping("/api/monitoring")
public class PerformanceController {

    @Autowired
    private DestinationRepository destinationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    // NEW: Modern Java 21+ service
    @Autowired
    private ModernMetricsService modernMetricsService;

    @GetMapping("/performance")
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Database metrics
            long destinationCount = destinationRepository.count();
            long userCount = userRepository.count();
            long likeCount = likeRepository.count();
            long commentCount = commentRepository.count();
            
            long dbQueryTime = System.currentTimeMillis() - startTime;
            
            // Application metrics
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            // Calculate performance indicators
            double memoryUsagePercent = (double) usedMemory / totalMemory * 100;
            double avgLikesPerDestination = destinationCount > 0 ? (double) likeCount / destinationCount : 0;
            double avgCommentsPerDestination = destinationCount > 0 ? (double) commentCount / destinationCount : 0;
            double userEngagement = userCount > 0 ? (double) (likeCount + commentCount) / userCount : 0;
            
            // Build response
            metrics.put("timestamp", LocalDateTime.now().toString());
            metrics.put("applicationName", "VacationDestinations-Monolith");
            metrics.put("architecture", "MONOLITHIC");
            
            // Database metrics
            Map<String, Object> dbMetrics = new HashMap<>();
            dbMetrics.put("destinations", destinationCount);
            dbMetrics.put("users", userCount);
            dbMetrics.put("likes", likeCount);
            dbMetrics.put("comments", commentCount);
            dbMetrics.put("queryTimeMs", dbQueryTime);
            metrics.put("database", dbMetrics);
            
            // Performance metrics
            Map<String, Object> perfMetrics = new HashMap<>();
            perfMetrics.put("totalMemoryMB", totalMemory / (1024 * 1024));
            perfMetrics.put("usedMemoryMB", usedMemory / (1024 * 1024));
            perfMetrics.put("freeMemoryMB", freeMemory / (1024 * 1024));
            perfMetrics.put("memoryUsagePercent", String.format("%.2f", memoryUsagePercent));
            perfMetrics.put("availableProcessors", runtime.availableProcessors());
            metrics.put("performance", perfMetrics);
            
            // Business metrics
            Map<String, Object> businessMetrics = new HashMap<>();
            businessMetrics.put("avgLikesPerDestination", String.format("%.2f", avgLikesPerDestination));
            businessMetrics.put("avgCommentsPerDestination", String.format("%.2f", avgCommentsPerDestination));
            businessMetrics.put("userEngagementScore", String.format("%.2f", userEngagement));
            metrics.put("business", businessMetrics);
            
            // Architecture analysis
            Map<String, Object> archMetrics = new HashMap<>();
            archMetrics.put("deploymentUnits", 1);
            archMetrics.put("databases", 1);
            archMetrics.put("services", "All-in-One");
            archMetrics.put("scalingStrategy", "Vertical");
            archMetrics.put("couplingLevel", "HIGH");
            archMetrics.put("microservicesReady", false);
            metrics.put("architecture", archMetrics);
            
            // Recommendations
            Map<String, Object> recommendations = new HashMap<>();
            if (memoryUsagePercent > 80) {
                recommendations.put("memory", "Consider increasing heap size or implementing caching");
            }
            if (dbQueryTime > 100) {
                recommendations.put("database", "Database queries are slow, consider indexing or connection pooling");
            }
            if (destinationCount > 100) {
                recommendations.put("scaling", "Consider microservices architecture for better scalability");
            }
            recommendations.put("nextSteps", "Review ARCHITECTURE_MODERNIZATION.md for microservices migration plan");
            metrics.put("recommendations", recommendations);
            
            metrics.put("status", "SUCCESS");
            
        } catch (Exception e) {
            metrics.put("status", "ERROR");
            metrics.put("error", e.getMessage());
            metrics.put("queryTimeMs", System.currentTimeMillis() - startTime);
        }
        
        return metrics;
    }

    @GetMapping("/architecture-analysis")
    public Map<String, Object> getArchitectureAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        
        analysis.put("currentArchitecture", "MONOLITHIC");
        analysis.put("assessmentDate", LocalDateTime.now().toString());
        
        // Current state
        Map<String, Object> currentState = new HashMap<>();
        currentState.put("deploymentComplexity", "LOW");
        currentState.put("scalabilityLimitations", "HIGH");
        currentState.put("teamCollaboration", "CHALLENGING");
        currentState.put("technologyFlexibility", "LIMITED");
        currentState.put("faultIsolation", "POOR");
        analysis.put("currentState", currentState);
        
        // Microservices readiness
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("domainBoundaries", "CLEAR"); // User, Destination, Social
        readiness.put("dataOwnership", "SEPARABLE");
        readiness.put("businessCapabilities", "IDENTIFIABLE");
        readiness.put("teamStructure", "REQUIRES_PLANNING");
        readiness.put("devOpsMaturity", "NEEDS_IMPROVEMENT");
        analysis.put("microservicesReadiness", readiness);
        
        // Proposed services
        Map<String, Object> proposedServices = new HashMap<>();
        proposedServices.put("userService", "Authentication, user management");
        proposedServices.put("destinationService", "Destination CRUD, image management");
        proposedServices.put("socialService", "Likes, comments, ratings");
        proposedServices.put("searchService", "Search, recommendations");
        proposedServices.put("notificationService", "Email, push notifications");
        analysis.put("proposedServices", proposedServices);
        
        // Migration strategy
        Map<String, Object> migration = new HashMap<>();
        migration.put("approach", "STRANGLER_FIG_PATTERN");
        migration.put("phases", 5);
        migration.put("estimatedDuration", "10 weeks");
        migration.put("riskLevel", "MEDIUM");
        migration.put("documentationPath", "ARCHITECTURE_MODERNIZATION.md");
        analysis.put("migration", migration);
        
        return analysis;
    }

    /**
     * Modern Java 21+ endpoint using Records, Virtual Threads, and Pattern Matching
     */
    @GetMapping("/modern-metrics")
    public Object getModernMetrics() {
        try {
            var metricsFeature = modernMetricsService.getModernMetricsAsync();
            var systemStatus = metricsFeature.join(); // Non-blocking with Virtual Threads
            
            var recommendation = modernMetricsService.getArchitectureRecommendation(systemStatus);
            
            // Return both metrics and AI-powered recommendation
            return Map.of(
                "systemStatus", systemStatus,
                "recommendation", recommendation,
                "javaVersion", Runtime.version().toString(),
                "modernFeatures", Map.of(
                    "virtualThreads", "ENABLED",
                    "recordPatterns", "ENABLED", 
                    "patternMatching", "ENABLED",
                    "enhancedSwitch", "ENABLED"
                )
            );
        } catch (Exception e) {
            return Map.of(
                "error", "Failed to collect modern metrics: " + e.getMessage(),
                "fallback", "Use /api/monitoring/performance for legacy metrics"
            );
        }
    }
}
