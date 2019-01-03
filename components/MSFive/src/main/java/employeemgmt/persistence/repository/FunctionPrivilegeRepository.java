package com.yoma.adminportal.employeemgmt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeEntity;

public interface FunctionPrivilegeRepository extends JpaRepository<FunctionPrivilegeEntity, Integer> {

}
