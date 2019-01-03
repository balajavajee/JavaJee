package com.yoma.adminportal.employeemgmt.persistence.repository;

import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeRelationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionPrivilegeRelationRepository extends
    JpaRepository<FunctionPrivilegeRelationEntity, Integer> {

  List<FunctionPrivilegeRelationEntity> findByParent(FunctionPrivilegeEntity parent);

}
