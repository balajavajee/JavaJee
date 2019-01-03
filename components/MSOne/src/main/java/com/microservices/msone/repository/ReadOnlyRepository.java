package com.microservices.msone.repository;

import java.util.Collection;

public interface ReadOnlyRepository<TE, T> {
    TE get(T id);
    boolean contains(T id);
    Collection<TE> getAll();
}