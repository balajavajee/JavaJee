package com.bala.accesscontrol.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.Privilege;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface PrivilegeRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> /*extends BaseRepository<Privilege, Long> */{

}
