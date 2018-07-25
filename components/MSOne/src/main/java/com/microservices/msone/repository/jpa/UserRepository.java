package com.microservices.msone.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.msone.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}