package com.yoma.adminportal.employeemgmt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionEntity;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Integer> {

}
