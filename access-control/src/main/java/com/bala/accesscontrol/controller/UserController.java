package com.bala.accesscontrol.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.model.User;
import com.bala.accesscontrol.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usersapi")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

/*	@GetMapping("/users")
	public Page<User> getAll(Pageable pageable) {
		log.info("Find the users list");
		return userService.getAll(pageable);
	}
*/
	@GetMapping("/users")
	public Collection<User> getAll() {
		log.info("In Get Users");
		return userService.getAll();
	}

	@GetMapping("/users/{userId}")
	public Optional<User> getById(@PathVariable Long userId) {
		return userService.getById(userId);
	}

	/*
	 * @GetMapping("/users/{userName}") public Collection<User> getByName(String
	 * userName) { return userService.getByName(userName); }
	 * 
	 * @GetMapping("/users/{userName}") public Optional<User> getByNameOne(String
	 * userName) { return userService.getByNameOne(userName); }
	 */

	@GetMapping("/users/{userId}/roles/")
	public Collection<Role> getAllRoles(@PathVariable Long userId) {
		return null;
		// ...
	}

	@GetMapping("/users/{userId}/roles/{roleId}")
	public Optional<Role> getRoleById(@PathVariable Long userId, @PathVariable Long roleId) {
		return null;
		// ...
	}

	@PostMapping("/users")
	public User addUser(@Valid @RequestBody User user) {
		return userService.add(user);
	}

/*	@PostMapping("/users")
	public List<User> addUser(@Valid @RequestBody List<User> user) {
		return userService.add(user);
	}
*/
	@PutMapping("/users/{userId}")
	public User updateUser(@PathVariable Long userId, @Valid @RequestBody User user) throws Throwable {
		return userService.update(userId, user);
	}

	@PutMapping("/users")
	public List<User> updateUser(@Valid @RequestBody List<User> users) {
		return userService.update(users);
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) throws Throwable {
		return userService.delete(userId);

	}

}