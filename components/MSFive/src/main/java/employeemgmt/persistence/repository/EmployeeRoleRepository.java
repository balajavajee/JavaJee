package com.yoma.adminportal.employeemgmt.persistence.repository;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeRoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRoleEntity, Integer> {

   List<EmployeeRoleEntity> findByEmployee(EmployeeEntity employee);

   List<EmployeeRoleEntity> findByRole(RoleEntity role);

}
