package com.microservices.msone.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.microservices.msone.model.UserDetails;

@Repository
public interface UserRepository extends BaseRepository<UserDetails, String> {
    boolean containsName(String name);
    Collection<UserDetails> findByName(String name) throws Exception;
}