package com.bala.accesscontrol.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.accesscontrol.model.User;
import com.bala.accesscontrol.repository.jpa.UserRepository;
import com.bala.accesscontrol.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	/*
	@Override
	public Collection<User> getAll() {
		return baseRepository.findAll();
	}*/
}