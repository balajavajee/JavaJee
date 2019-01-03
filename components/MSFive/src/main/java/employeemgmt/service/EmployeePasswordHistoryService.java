package com.yoma.adminportal.employeemgmt.service;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeePasswordHistoryEntity;
import java.util.Collection;

public interface EmployeePasswordHistoryService {

  Collection<EmployeePasswordHistoryEntity> getByLogin(String login);

  void savePasswordToHistory(EmployeePasswordHistoryEntity employeePasswordHistory);
}
