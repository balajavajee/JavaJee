package com.bala.accesscontrol.repository;

import java.util.Collection;

public interface ReadOnlyRepository<TE, T> {
    TE get(T id);
    boolean contains(T id);
    Collection<TE> getAll();
}