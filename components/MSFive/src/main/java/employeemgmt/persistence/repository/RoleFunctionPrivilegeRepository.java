package com.yoma.adminportal.employeemgmt.persistence.repository;

import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleFunctionPrivilegeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleFunctionPrivilegeRepository
    extends JpaRepository<RoleFunctionPrivilegeEntity, Integer> {

  List<RoleFunctionPrivilegeEntity> findByRole(RoleEntity role);

  List<RoleFunctionPrivilegeEntity> findByRoleId(Integer id);
}
