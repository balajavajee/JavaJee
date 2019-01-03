package com.yoma.adminportal.employeemgmt.service.impl;

import com.backbase.persistence.employeemanagement.IdentityDetails;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostRequestBody.Operation;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostResponseBody.Result;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import com.yoma.adminportal.employeemgmt.service.EmployeeManagementSettingsService;
import com.yoma.adminportal.employeemgmt.service.EmployeePersistenceService;
import com.yoma.adminportal.employeemgmt.service.PasswordPersistenceService;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordCriteriaException;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordHistoryException;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordUtility;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordPersistenceServiceImpl implements PasswordPersistenceService {

  private static final String DEFAULT_PASSWORD = "cJ2+jC2=";

  private final EmployeeManagementSettingsService settingsService;
  private final EmployeePasswordHistoryServiceImpl passwordHistoryService;
  private final EmployeePersistenceService employeePersistenceService;
  private final MessageSource messages;
  private final EmployeePasswordUtility employeePasswordUtility;

  @Autowired
  public PasswordPersistenceServiceImpl(
      EmployeeManagementSettingsService settingsService,
      EmployeePasswordHistoryServiceImpl passwordHistoryService,
      EmployeePersistenceService employeePersistenceService,
      MessageSource messages) {
    this.settingsService = settingsService;
    this.passwordHistoryService = passwordHistoryService;
    this.employeePersistenceService = employeePersistenceService;
    this.messages = messages;
    this.employeePasswordUtility = new EmployeePasswordUtility(passwordHistoryService, messages);
  }

  @Override
  public String getPassword(boolean generate) {
    String password = DEFAULT_PASSWORD;
    if (generate) {
      password = employeePasswordUtility.generate();
    } else {
      EmployeeManagementSettingEntity setting =
          settingsService.getSetting(EmployeeManagementSettingsService.EMPLOYEES_DEFAULT_PASSWORD);
      if (setting != null) {
        password = setting.getValue();
      }
    }
    return password;
  }

  @Override
  public PasswordDBPostResponseBody checkPassword(
      PasswordDBPostRequestBody passwordDBPostRequestBody) {
    if (Operation.VALIDATE.equals(passwordDBPostRequestBody.getOperation())) {
      try {
        this.employeePasswordUtility.validate(
            passwordDBPostRequestBody.getLogin(), passwordDBPostRequestBody.getPassword());
        return createPostResponseBody(Result.VALIDATED, null);
      } catch (EmployeePasswordCriteriaException | EmployeePasswordHistoryException e) {
        return createPostResponseBody(Result.ERROR, e.getMessage());
      }
    } else if (Operation.AUTHENTICATE.equals(passwordDBPostRequestBody.getOperation())) {
      final String login = passwordDBPostRequestBody.getLogin();
      if (Objects.isNull(login)) {
        return createPostResponseBody(Result.ERROR, "Login not provided in the request");
      } else {
        IdentityDetails details =
            employeePersistenceService.getIdentity(login);
        if (null == details) {
          return createPostResponseBody(Result.ERROR, "User not found");
        }
        PasswordEncoder encoder = new EmployeePasswordEncoder();
        if (encoder.matches(passwordDBPostRequestBody.getPassword(),
            details.getPassword())) {
          return createPostResponseBody(Result.AUTHENTICATED, null);
        } else {
          return createPostResponseBody(Result.ERROR, "Passwords don't match");
        }
      }
    }
    return createPostResponseBody(Result.ERROR, "Unknown error");
  }

  private PasswordDBPostResponseBody createPostResponseBody(Result result, String message) {
    PasswordDBPostResponseBody body = new PasswordDBPostResponseBody();
    body.setResult(result);
    body.setMessage(message);
    return body;
  }


}
