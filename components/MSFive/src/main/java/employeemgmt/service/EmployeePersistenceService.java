package com.yoma.adminportal.employeemgmt.service;

import com.backbase.buildingblocks.jwt.internal.token.InternalJwt;
import com.backbase.persistence.employeemanagement.HttpStatus;
import com.backbase.persistence.employeemanagement.IdentityDetails;
import com.backbase.persistence.employeemanagement.IdentityUpdate;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBriefList;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeRolePendingAssignments;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;

public interface EmployeePersistenceService {

  EmployeesDBPostResponseBody createEmployee(
      EmployeesDBPostRequestBody employeesDBPostRequestBody, InternalJwt internalJwt);

  LoginDBPutResponseBody updateEmployee(String login,
      LoginDBPutRequestBody employeesDBPutRequestBody, InternalJwt internalJwt);

  IdentityDetails getIdentity(String login);

  HttpStatus updateIdentity(
      IdentityUpdate IdentityUpdate, String login, InternalJwt internalJwt);

  EmployeesDBGetResponseBody getEmployeesDB();

  LoginDBGetResponseBody getUserDetailsByIdDB(String login);

  PersistenceEmployeeBriefList getBriefDB(PersistenceOrderedPagedFiltered request);

  EmployeePersistenceValidationPostResponseBody validate(
      EmployeePersistenceValidationPostRequestBody requestBody);

  PersistenceEmployeeRolePendingAssignments getEmployeeRolePendingAssignments();

}
