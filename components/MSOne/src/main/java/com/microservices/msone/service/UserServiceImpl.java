package com.microservices.msone.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.msone.model.UserDetails;
import com.microservices.msone.repository.UserRepository;

@Service
public class UserServiceImpl extends BaseService<UserDetails, String> implements UserService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;
	}

	@Override
	public void add(UserDetails user) throws Exception {
		if (userRepository.containsName(user.getName())) {
			throw new Exception("Username already taken");
		}
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new Exception("Username cannot be null or empty");
		}
		userRepository.add(user);
	}

	@Override
	public void update(UserDetails user) throws Exception {
		userRepository.update(user);
	}

	@Override
	public void delete(String id) throws Exception {
		userRepository.remove(id);
	}

	@Override
	public UserDetails findById(String userId) throws Exception {
		return userRepository.get(userId);
	}

	@Override
	public Collection<UserDetails> findByName(String name) throws Exception {
		return userRepository.findByName(name);
	}

	@Override
	public boolean findByCriteria(Map<String, ArrayList<String>> name) throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}