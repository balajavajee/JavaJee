package com.yoma.adminportal.employeemgmt.service.impl;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeManagementSettingsRepository;
import com.yoma.adminportal.employeemgmt.service.EmployeeManagementSettingsService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeManagementSettingsServiceImpl implements EmployeeManagementSettingsService {

  private final EmployeeManagementSettingsRepository settingsRepository;

  @Autowired
  public EmployeeManagementSettingsServiceImpl(
      EmployeeManagementSettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  @Override
  public EmployeeManagementSettingEntity getSetting(String key) {
    return settingsRepository.findByProperty(key).orElse(null);
  }

  @Override
  public Collection<EmployeeManagementSettingEntity> getSettings() {
    return settingsRepository.findAll();
  }

}
