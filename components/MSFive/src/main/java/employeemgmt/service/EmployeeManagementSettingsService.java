package com.yoma.adminportal.employeemgmt.service;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import java.util.Collection;

public interface EmployeeManagementSettingsService {

  String EMPLOYEES_DEFAULT_PASSWORD = "employees.defaultPassword";
  String EMPLOYEES_DEFAULT_EXPIRE_PASSWORD_DAYS = "employees.defaultExpirePasswordDays";

  EmployeeManagementSettingEntity getSetting(String key);

  Collection<EmployeeManagementSettingEntity> getSettings();

}
