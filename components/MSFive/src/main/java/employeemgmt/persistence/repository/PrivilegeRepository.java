package com.yoma.adminportal.employeemgmt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yoma.adminportal.employeemgmt.persistence.model.PrivilegeEntity;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Integer> {

}
