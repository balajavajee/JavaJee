package com.yoma.adminportal.employeemgmt.persistence.repository;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeePasswordHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePasswordHistoryRepository
    extends JpaRepository<EmployeePasswordHistoryEntity, Long> {

   List<EmployeePasswordHistoryEntity> findByEmployeeId(Integer employeeId);

}
