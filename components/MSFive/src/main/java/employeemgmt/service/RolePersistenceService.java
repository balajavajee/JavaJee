
package com.yoma.adminportal.employeemgmt.service;

import com.backbase.buildingblocks.jwt.internal.token.InternalJwt;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.Role;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBDeleteResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBriefList;
import java.util.List;

public interface RolePersistenceService {

  RolesDBPostResponseBody createRole(RolesDBPostRequestBody requestBody,
      InternalJwt internalJwt);

  RoleIdDBGetResponseBody getRoleByIdDB(String roleId, boolean includeHiddenFunctions);

  RoleIdDBPutResponseBody updateRole(String roleIdDb, RoleIdDBPutRequestBody data,
      InternalJwt internalJwt);

  FunctionGroupIdPutResponseBody updateFunctionGroupId(String roleIdDb,
      FunctionGroupIdPutRequestBody functionGroupIdPutRequestBody,
      InternalJwt internalJwt);

  List<Role> getRolesForEmployee(EmployeeEntity employee);

  RoleIdDBDeleteResponseBody deleteRole(String id,
      InternalJwt internalJwt);

  PersistenceRoleBriefList getBriefDB(PersistenceOrderedPagedFiltered request);

}
