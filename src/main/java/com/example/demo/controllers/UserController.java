package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user == null) {
			log.error("User not found with id \"{}\"", id);
		}
		return ResponseEntity.of(user);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("User not found with \"{}\"", username);
		}
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		log.info("Username assigned as \"{}\"", createUserRequest.getUsername());

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if (createUserRequest.getPassword().length() < 7 || createUserRequest.getPassword() == null || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.error("Password must not be empty, must match and must be a minimum of 7 characters. Cannot create {}", createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("\"{}\" created successfully", createUserRequest.getUsername());

		return ResponseEntity.ok(user);
	}
	
}
