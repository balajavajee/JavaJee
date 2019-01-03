
package com.yoma.adminportal.employeemgmt.service;

import com.backbase.persistence.employeemanagement.rest.spec.v1.functionsDB.FunctionsDBGetResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.dto.FunctionPrivilege;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import java.util.List;

public interface FunctionPersistenceService {

  FunctionsDBGetResponseBody getFunctionsDB(boolean includeHiddenFunctions);

  List<FunctionPrivilege> findFunctionPrivilegeForRole(RoleEntity roleEntity);

}
