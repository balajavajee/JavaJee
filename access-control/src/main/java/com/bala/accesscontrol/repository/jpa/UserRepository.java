package com.bala.accesscontrol.repository.jpa;

import java.util.Date;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.User;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

	// Collection<User> findByFirstName(String userName);

	Optional<User> findOne(String userName);

	Optional<User> findOneByName(String userName);

	@Query("SELECT max(u.updated) FROM USER u")
	Date lastUpdate();

	Page<User> findAll(Pageable pageable);

}