package com.microservices.msone.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.microservices.msone.model.UserDetails;

@Service
public interface UserService {

	void add(UserDetails user) throws Exception;

	void update(UserDetails user) throws Exception;

	void delete(String id) throws Exception;

	UserDetails findById(String userId) throws Exception;

	Collection<UserDetails> findByName(String name) throws Exception;

	boolean findByCriteria(Map<String, ArrayList<String>> name) throws Exception;

}