package com.example.demo.repository;

import com.example.demo.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserUsernameAndDestinationId(String username, Long destinationId);
}
