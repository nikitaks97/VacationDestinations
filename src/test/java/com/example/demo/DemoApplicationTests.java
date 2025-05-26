package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserRepository;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private DestinationRepository destinationRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertThat(destinationRepository).isNotNull();
		assertThat(userRepository).isNotNull();
	}

	@Test
	void testHomePageLoads() {
		// This is a placeholder for a real web test
		assertThat(true).isTrue();
	}
}
