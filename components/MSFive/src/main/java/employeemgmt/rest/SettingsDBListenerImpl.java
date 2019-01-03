package com.yoma.adminportal.employeemgmt.rest;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.listener.spec.v1.settingsDB.SettingsDBListener;
import com.backbase.persistence.employeemanagement.rest.spec.v1.settingsDB.PropertyDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.settingsDB.SettingsDBGetResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import com.yoma.adminportal.employeemgmt.service.EmployeeManagementSettingsService;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility.HttpMethod;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequestListener
public class SettingsDBListenerImpl implements SettingsDBListener {

  private final BackbaseRequestUtility backbaseRequestUtility;
  private final EmployeeManagementSettingsService settingsService;

  @Autowired
  public SettingsDBListenerImpl(BackbaseRequestUtility backbaseRequestUtility,
      EmployeeManagementSettingsService settingsService) {
    this.backbaseRequestUtility = backbaseRequestUtility;
    this.settingsService = settingsService;
  }

  @Override
  public RequestProxyWrapper<List<SettingsDBGetResponseBody>> getSettingsDB(
      RequestProxyWrapper<Void> request, Exchange exchange) {
    List<SettingsDBGetResponseBody> list = settingsService.getSettings().stream().map(
        s -> new SettingsDBGetResponseBody().withProperty(s.getProperty()).withValue(s.getValue()))
        .collect(Collectors.toList());
    return backbaseRequestUtility.buildRequestProxyWrapper(list,
        HttpMethod.get);
  }

  @Override
  public RequestProxyWrapper<PropertyDBGetResponseBody> getPropertyDB(
      RequestProxyWrapper<Void> request,
      Exchange exchange, String property) {
    EmployeeManagementSettingEntity s = settingsService.getSetting(property);
    PropertyDBGetResponseBody responseBody = null;
    if (s != null) {
      responseBody = new PropertyDBGetResponseBody().withProperty(s.getProperty())
          .withValue(s.getValue());
    }
    return backbaseRequestUtility.buildRequestProxyWrapper(responseBody,
        HttpMethod.get);
  }

}
