package com.microservices.msone.service;

import com.microservices.msone.repository.BaseRepository;

public class BaseService<TE, T> extends ReadOnlyService<TE, T> {

    private final BaseRepository repository;

    public BaseService(BaseRepository<TE, T> repository) {
        super(repository);
        this.repository = repository;
    }

    public void add(TE enitity) throws Exception {
        repository.add(enitity);
    }
}