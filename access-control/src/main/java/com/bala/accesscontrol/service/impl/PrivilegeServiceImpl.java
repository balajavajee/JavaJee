package com.bala.accesscontrol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.accesscontrol.model.Privilege;
import com.bala.accesscontrol.repository.jpa.PrivilegeRepository;
import com.bala.accesscontrol.service.PrivilegeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege> implements PrivilegeService {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}
	
}