package com.bala.accesscontrol.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bala.accesscontrol.exceptions.ResourceNotFoundException;
import com.bala.accesscontrol.repository.BaseRepository;
import com.bala.accesscontrol.service.BaseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	BaseRepository baseRepository;

	@Override
	public Page<T> getAll(Pageable pageable) {
		log.info("Find the users list");
		return baseRepository.findAll(pageable);
	}

	@Override
	public Collection<T> getAll() {
		return baseRepository.findAll();
	}

	@Override
	public Optional<T> getById(Long id) {
		return baseRepository.findById(id);
	}

	@Override
	public T add(T entity) {
		// TODO Auto-generated method stub
		return (T) baseRepository.save(entity);
	}

	@Override
	public List<T> add(List<T> entities) {
		// TODO Auto-generated method stub
		return baseRepository.saveAll(entities);
	}

	@Override
	public List<T> update(List<T> entities) {
		// TODO Auto-generated method stub
		return baseRepository.saveAll(entities);
	}

	@Override
	public T update(Long entityId, T entity) throws Throwable {
		// TODO Auto-generated method stub
		baseRepository.save(baseRepository.findById(entityId)
				.orElseThrow(() -> new ResourceNotFoundException("Entity", entityId, "Not found")));
		return (T) ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<?> delete(Long entityId) throws Throwable {
		baseRepository.delete(baseRepository.findById(entityId)
				.orElseThrow(() -> new ResourceNotFoundException("Entity ", entityId, "Not found")));
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<?> delete(List<T> entities) {
		// TODO baseR-generated method stub
		return ResponseEntity.ok().build();
	}

}
