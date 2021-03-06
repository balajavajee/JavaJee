/*package com.bala.accesscontrol.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.model.RoleFunctionPrivilege;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface RoleFunctionPrivilegeRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> extends BaseRepository<RoleFunctionPrivilege, Long> {
	
  List<RoleFunctionPrivilege> findByRole(Role role);

  List<RoleFunctionPrivilege> findByRoleId(Integer id);
}
*/