package com.yoma.adminportal.employeemgmt.service.impl.operation;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeRoleEntity;

/**
 * Class to hold the Role to FunctionPrivilege changes
 *
 * @author TSNR
 */
public class EmployeeRoleOperation extends AbstractEntityOperation<EmployeeRoleEntity> {

  public EmployeeRoleOperation(EntityOperation operation, EmployeeRoleEntity entity) {
    super(operation, entity);
  }

}
