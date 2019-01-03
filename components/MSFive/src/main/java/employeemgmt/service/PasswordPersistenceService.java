package com.yoma.adminportal.employeemgmt.service;

import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostResponseBody;

public interface PasswordPersistenceService {

  String getPassword(boolean generate);

  PasswordDBPostResponseBody checkPassword(PasswordDBPostRequestBody data);

}
