package com.bala.accesscontrol.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.accesscontrol.model.Function;
import com.bala.accesscontrol.repository.jpa.FunctionRepository;
import com.bala.accesscontrol.service.FunctionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements FunctionService {

	@Autowired
	private FunctionRepository functionRepository;
	/*
	@Autowired
	public FunctionServiceImpl(FunctionRepository functionRepository) {
		this.functionRepository = functionRepository;
	}*/

}
