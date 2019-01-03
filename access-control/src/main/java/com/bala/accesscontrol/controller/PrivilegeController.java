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

import com.bala.accesscontrol.model.Privilege;
import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.service.PrivilegeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/privilegesapi")
@Slf4j
public class PrivilegeController {

	@Autowired
	PrivilegeService privilegeService;

	/*@GetMapping("/privileges")
	public Page<Privilege> getAll(Pageable pageable) {
		log.info("Find the privileges list");
		return privilegeService.getAll(pageable);
	}*/

	@GetMapping("/privileges")
	public Collection<Privilege> getAll() {
		return privilegeService.getAll();
	}

	@GetMapping("/privileges/{privilegeId}")
	public Optional<Privilege> getById(@PathVariable Long privilegeId) {
		return privilegeService.getById(privilegeId);
	}

	/*
	 * @GetMapping("/privileges/{privilegeName}") public Collection<Privilege> getByName(String
	 * privilegeName) { return privilegeService.getByName(privilegeName); }
	 * 
	 * @GetMapping("/privileges/{privilegeName}") public Optional<Privilege> getByNameOne(String
	 * privilegeName) { return privilegeService.getByNameOne(privilegeName); }
	 */

	@GetMapping("/privileges/{privilegeId}/roles/")
	public Collection<Role> getAllRoles(@PathVariable Long privilegeId) {
		return null;
		// ...
	}

	@GetMapping("/privileges/{privilegeId}/roles/{roleId}")
	public Optional<Role> getRoleById(@PathVariable Long privilegeId, @PathVariable Long roleId) {
		return null;
		// ...
	}

	@PostMapping("/privileges")
	public Privilege addPrivilege(@Valid @RequestBody Privilege privilege) {
		return privilegeService.add(privilege);
	}

	/*@PostMapping("/privileges")
	public List<Privilege> addPrivilege(@Valid @RequestBody List<Privilege> privilege) {
		return privilegeService.add(privilege);
	}*/

	@PutMapping("/privileges/{privilegeId}")
	public Privilege updatePrivilege(@PathVariable Long privilegeId, @Valid @RequestBody Privilege privilege) throws Throwable {
		return privilegeService.update(privilegeId, privilege);
	}

	/*@PutMapping("/privileges")
	public List<Privilege> updatePrivilege(@Valid @RequestBody List<Privilege> privileges) {
		return privilegeService.update(privileges);
	}*/

	@DeleteMapping("/privileges/{privilegeId}")
	public ResponseEntity<?> deletePrivilege(@PathVariable Long privilegeId) throws Throwable {
		return privilegeService.delete(privilegeId);

	}

}