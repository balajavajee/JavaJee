package com.microservices.msone.repository;
public interface BaseRepository<TE, T> extends ReadOnlyRepository<TE, T>{
    void add(TE entity);
    void update(TE entity);
    void remove(T id);
}