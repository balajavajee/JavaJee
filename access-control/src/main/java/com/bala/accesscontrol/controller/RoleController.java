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

import com.bala.accesscontrol.model.Function;
import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rolesapi")
@Slf4j
public class RoleController {

	@Autowired
	RoleService roleService;

/*	@GetMapping("/roles")
	public Page<Role> getAll(Pageable pageable) {
		log.info("Find the roles list");
		return roleService.getAll(pageable);
	}
*/
	@GetMapping("/roles")
	public Collection<Role> getAll() {
		return roleService.getAll();
	}

	@GetMapping("/roles/{roleId}")
	public Optional<Role> getById(@PathVariable Long roleId) {
		return roleService.getById(roleId);
	}

	/*
	 * @GetMapping("/roles/{roleName}") public Collection<Role> getByName(String
	 * roleName) { return roleService.getByName(roleName); }
	 * 
	 * @GetMapping("/roles/{roleName}") public Optional<Role> getByNameOne(String
	 * roleName) { return roleService.getByNameOne(roleName); }
	 */

	@GetMapping("/roles/{roleId}/previs/")
	public Collection<Function> getAllRoles(@PathVariable Long roleId) {
		return null;
		// ...
	}

	@GetMapping("/roles/{roleId}/previs/{prevId}")
	public Optional<Function> getRoleById(@PathVariable Long roleId, @PathVariable Long prevId) {
		return null;
		// ...
	}

	@PostMapping("/roles")
	public Role addRole(@Valid @RequestBody Role role) {
		return roleService.add(role);
	}

/*	@PostMapping("/roles")
	public List<Role> addRole(@Valid @RequestBody List<Role> role) {
		return roleService.add(role);
	}*/

	@PutMapping("/roles/{roleId}")
	public Role updateRole(@PathVariable Long roleId, @Valid @RequestBody Role role) throws Throwable {
		return roleService.update(roleId, role);
	}

	@PutMapping("/roles")
	public List<Role> updateRole(@Valid @RequestBody List<Role> roles) {
		return roleService.update(roles);
	}

	@DeleteMapping("/roles/{roleId}")
	public ResponseEntity<?> deleteRole(@PathVariable Long roleId) throws Throwable {
		return roleService.delete(roleId);

	}

}
