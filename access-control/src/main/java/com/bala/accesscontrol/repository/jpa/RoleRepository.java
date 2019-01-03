package com.bala.accesscontrol.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface RoleRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> /*extends BaseRepository<Role, Long>*/ {

   Optional<Role> findByName(String name);
   Optional<Role> findById(Long roleId);

}
