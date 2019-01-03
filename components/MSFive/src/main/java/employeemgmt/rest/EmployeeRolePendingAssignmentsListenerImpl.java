package com.yoma.adminportal.employeemgmt.rest;

import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.listener.spec.v1.pendingassignments.EmployeeRolePendingAssignmentsListener;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeRolePendingAssignments;
import com.yoma.adminportal.employeemgmt.service.EmployeePersistenceService;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequestListener
public class EmployeeRolePendingAssignmentsListenerImpl implements
    EmployeeRolePendingAssignmentsListener {

  private final BackbaseRequestUtility backbaseRequestUtility;
  private final EmployeePersistenceService employeePersistenceService;

  @Autowired
  public EmployeeRolePendingAssignmentsListenerImpl(
      BackbaseRequestUtility backbaseRequestUtility,
      EmployeePersistenceService employeePersistenceService) {
    this.backbaseRequestUtility = backbaseRequestUtility;
    this.employeePersistenceService = employeePersistenceService;
  }

  @Override
  public RequestProxyWrapper<PersistenceEmployeeRolePendingAssignments> getEmployeeRolePendingAssignments(
      RequestProxyWrapper<Void> request, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PersistenceEmployeeRolePendingAssignments> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.getEmployeeRolePendingAssignments(),
            HttpMethod.get);
    log.debug(OUT);
    return response;
  }

}
