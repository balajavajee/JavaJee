package com.microservices.msone.service;

import java.util.Collection;

import com.microservices.msone.repository.ReadOnlyRepository;

public abstract class ReadOnlyService<TE, T> {
	private final ReadOnlyRepository<TE, T> repository;

	public ReadOnlyService(ReadOnlyRepository repository) {
		this.repository = repository;
	}

	public ReadOnlyRepository<TE, T> getRepository() {
		return repository;
	}

	Collection<TE> getAll() {
		return repository.getAll();
	}
}