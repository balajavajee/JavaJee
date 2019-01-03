package com.yoma.adminportal.employeemgmt.persistence.repository;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeManagementSettingsRepository
    extends JpaRepository<EmployeeManagementSettingEntity, Integer> {

   Optional<EmployeeManagementSettingEntity> findByProperty(String property);

}
