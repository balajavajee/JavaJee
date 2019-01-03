package com.microservices.msone.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.microservices.msone.model.User;
import com.microservices.msone.model.UserDetails;

@Repository
public class InMemUserRepository implements UserRepository {

	private Map<String, UserDetails> entities;

	public InMemUserRepository() {
		entities = new HashMap<>();

		UserDetails user = new UserDetails("1", "User Name 1", "Address 1", "City 1", "9999911111");
		entities.put("1", user);

		UserDetails user2 = new UserDetails("1", "User Name 2", "Address 2", "City 2", "9999922222");
		entities.put("2", user2);
	}

	@Override
	public void add(UserDetails entity) {
		entities.put(entity.getId(), entity);
	}

	@Override
	public void update(UserDetails entity) {
		if (entities.containsKey(entity.getId())) {
			entities.put(entity.getId(), entity);
		}
	}

	@Override
	public void remove(String id) {
		if (entities.containsKey(id)) {
			entities.remove(id);
		}
	}

	@Override
	public UserDetails get(String id) {
		return entities.get(id);
	}

	@Override
	public boolean contains(String id) {
		throw new UnsupportedOperationException("Method not supported yet.");
	}

	@Override
	public Collection<UserDetails> getAll() {
		return entities.values();
	}

	@Override
	public boolean containsName(String name) {
		try {
			return findByName(name).size() > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public Collection<UserDetails> findByName(String name) throws Exception {
		Collection<UserDetails> users = new ArrayList<>();
		entities.forEach((k, v) -> {
			if (v.getName().toLowerCase().contains(name))
				users.add(v);
		});
		return users;
	}
}