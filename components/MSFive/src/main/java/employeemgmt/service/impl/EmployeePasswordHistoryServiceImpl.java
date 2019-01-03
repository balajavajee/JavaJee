package com.yoma.adminportal.employeemgmt.service.impl;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeePasswordHistoryEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeePasswordHistoryRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRepository;
import com.yoma.adminportal.employeemgmt.service.EmployeePasswordHistoryService;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeePasswordHistoryServiceImpl implements
    EmployeePasswordHistoryService {

  private final EmployeeRepository employeeRepository;
  private final EmployeePasswordHistoryRepository employeePasswordHistoryRepository;

  @Autowired
  public EmployeePasswordHistoryServiceImpl(EmployeeRepository employeeRepository,
      EmployeePasswordHistoryRepository employeePasswordHistoryRepository) {
    this.employeeRepository = employeeRepository;
    this.employeePasswordHistoryRepository = employeePasswordHistoryRepository;
  }

  @Override
  public Collection<EmployeePasswordHistoryEntity> getByLogin(String login) {
    EmployeeEntity employee = employeeRepository.findByLogin(login).orElse(null);
    if (employee != null) {
      return employeePasswordHistoryRepository.findByEmployeeId(employee.getId()).stream()
          .sorted((ph1, ph2) -> {
            return ph2.getPasswordDate().compareTo(ph1.getPasswordDate()); // reverse order
          }).limit(3).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public void savePasswordToHistory(EmployeePasswordHistoryEntity employeePasswordHistory) {
    employeePasswordHistoryRepository.save(employeePasswordHistory);
  }

}
