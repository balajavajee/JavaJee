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
import com.bala.accesscontrol.service.FunctionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/functionsapi")
@Slf4j
public class FunctionController {

	@Autowired
	FunctionService functionService;

/*	@GetMapping("/functions")
	public Page<Function> getAll(Pageable pageable) {
		log.info("Find the functions list");
		return functionService.getAll(pageable);
	}*/

	@GetMapping("/functions")
	public Collection<Function> getAll() {
		return functionService.getAll();
	}

	@GetMapping("/functions/{functionId}")
	public Optional<Function> getById(@PathVariable Long functionId) {
		return functionService.getById(functionId);
	}

	/*
	 * @GetMapping("/functions/{functionName}") public Collection<Function>
	 * getByName(String functionName) { return
	 * functionService.getByName(functionName); }
	 * 
	 * @GetMapping("/functions/{functionName}") public Optional<Function>
	 * getByNameOne(String functionName) { return
	 * functionService.getByNameOne(functionName); }
	 */

	@GetMapping("/functions/{functionId}/previges/")
	public Collection<Role> getAllRoles(@PathVariable Long functionId) {
		return null;
		// ...
	}

	@GetMapping("/functions/{functionId}/previges/{previgeId}")
	public Optional<Role> getRoleById(@PathVariable Long functionId, @PathVariable Long previgeId) {
		return null;
		// ...
	}

	@PostMapping("/functions")
	public Function addFunction(@Valid @RequestBody Function function) {
		return functionService.add(function);
	}

	/*@PostMapping("/functions")
	public List<Function> addFunction(@Valid @RequestBody List<Function> function) {
		return functionService.add(function);
	}*/

	@PutMapping("/functions/{functionId}")
	public Function updateFunction(@PathVariable Long functionId, @Valid @RequestBody Function function)
			throws Throwable {
		return functionService.update(functionId, function);
	}
/*
	@PutMapping("/functions")
	public List<Function> updateFunction(@Valid @RequestBody List<Function> functions) {
		return functionService.update(functions);
	}*/

	@DeleteMapping("/functions/{functionId}")
	public ResponseEntity<?> deleteFunction(@PathVariable Long functionId) throws Throwable {
		return functionService.delete(functionId);

	}

}