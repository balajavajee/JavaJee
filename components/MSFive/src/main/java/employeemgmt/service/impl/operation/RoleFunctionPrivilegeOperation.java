package com.yoma.adminportal.employeemgmt.service.impl.operation;

import com.yoma.adminportal.employeemgmt.persistence.model.RoleFunctionPrivilegeEntity;

/**
 * Class to hold the Role to FunctionPrivilege changes
 *
 * @author TSNR
 */
public class RoleFunctionPrivilegeOperation extends AbstractEntityOperation<RoleFunctionPrivilegeEntity> {

  public RoleFunctionPrivilegeOperation(EntityOperation operation,
      RoleFunctionPrivilegeEntity entity) {
    super(operation, entity);
  }

}
