package com.bala.accesscontrol.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BaseService<T> {

	public Page<T> getAll(Pageable pageable);

	public Collection<T> getAll();

	public Optional<T> getById(Long id);

	public T add(T entity);

	public List<T> add(List<T> entities);

	public T update(Long entityId, T entityRequest) throws Throwable;

	public List<T> update(List<T> entities);

	public ResponseEntity<?> delete(Long entityId) throws Throwable;

	public ResponseEntity<?> delete(List<T> entities);


/*	public Optional<T> getById(Long id, Long ids);
 * public Collection<T> getAll(Long entityId);
 * public Collection<T> getByName(String entityName);

	public Optional<T> getByNameOne(String entityName);*/

}
