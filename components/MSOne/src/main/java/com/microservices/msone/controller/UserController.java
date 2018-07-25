package com.microservices.msone.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.msone.exceptions.ResourceNotFoundException;
import com.microservices.msone.model.User;
import com.microservices.msone.repository.jpa.UserRepository;

@RestController
@RequestMapping("/userapi")
public class UserController {

	@Autowired
	UserRepository userRepository;

	// Get All Users
	@GetMapping("/user")
	public List<User> getAllUsers() {
		/*User user = new User();
		user.setName("Balachandra");
		user.setPassKey("chandra");
		System.out.println("Adding User details ");
		userRepository.save(user);
		System.out.println("Get all users");*/
		return userRepository.findAll();
	}

	// Get a Single User
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable(value = "id") Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", userId, "Not found"));
	}

	// Create a new User
	@PostMapping("/adduser")
	public User createUser(@Valid @RequestBody User user) {
		return userRepository.save(user);
	}

	// Update a User
	@PutMapping("/user/{id}")
	public User updateNote(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", userId, "Not found"));

		user.setName(userDetails.getName());
		user.setPassKey(userDetails.getPassKey());

		User updatedNote = userRepository.save(user);
		return updatedNote;
	}

	// Delete a User
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", userId, "Not found"));

		userRepository.delete(user);

		return ResponseEntity.ok().build();
	}

}
