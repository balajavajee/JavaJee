package com.yoma.adminportal.employeemgmt.rest;

import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.HttpStatus;
import com.backbase.persistence.employeemanagement.IdentityDetails;
import com.backbase.persistence.employeemanagement.IdentityUpdate;
import com.backbase.persistence.employeemanagement.listener.spec.v1.employeesDB.EmployeesDBListener;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBriefList;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;
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
public class EmployeesDBListenerImpl implements EmployeesDBListener {

  private final BackbaseRequestUtility backbaseRequestUtility;
  private final EmployeePersistenceService employeePersistenceService;

  @Autowired
  public EmployeesDBListenerImpl(BackbaseRequestUtility backbaseRequestUtility,
      EmployeePersistenceService employeePersistenceService) {
    this.backbaseRequestUtility = backbaseRequestUtility;
    this.employeePersistenceService = employeePersistenceService;
  }

  @Override
  public RequestProxyWrapper<EmployeesDBPostResponseBody> postEmployeesDB(
      RequestProxyWrapper<EmployeesDBPostRequestBody> employeesDBPostRequestBody,
      Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<EmployeesDBPostResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService
                .createEmployee(employeesDBPostRequestBody.getRequest().getData(),
                    backbaseRequestUtility.extractInternalJwt(
                        employeesDBPostRequestBody.getRequest().getInternalRequestContext())),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<IdentityDetails> getIdentityDB(
      RequestProxyWrapper<Void> request, Exchange exchange, String login) {
    log.debug(IN);
    RequestProxyWrapper<IdentityDetails> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.getIdentity(login), HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<HttpStatus> putIdentityDB(
      RequestProxyWrapper<IdentityUpdate> identityUpdate, Exchange exchange,
      String login) {
    log.debug(IN);
    RequestProxyWrapper<HttpStatus> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService
            .updateIdentity(identityUpdate.getRequest().getData(), login, backbaseRequestUtility
                .extractInternalJwt(
                    identityUpdate.getRequest().getInternalRequestContext())), HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<EmployeesDBGetResponseBody> getEmployeesDB(
      RequestProxyWrapper<Void> request, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<EmployeesDBGetResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.getEmployeesDB(), HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<PersistenceEmployeeBriefList> postPaged(
      RequestProxyWrapper<PersistenceOrderedPagedFiltered> request, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PersistenceEmployeeBriefList> response = backbaseRequestUtility
        .buildRequestProxyWrapper(
            employeePersistenceService.getBriefDB(request.getRequest().getData()),
            HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<EmployeePersistenceValidationPostResponseBody> postEmployeePersistenceValidation(
      RequestProxyWrapper<EmployeePersistenceValidationPostRequestBody> employeePersistenceValidationPostRequestBody,
      Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<EmployeePersistenceValidationPostResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService
                .validate(employeePersistenceValidationPostRequestBody.getRequest().getData()),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<PersistenceEmployeeBriefList> getBriefDB(
      RequestProxyWrapper<Void> request,
      Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PersistenceEmployeeBriefList> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.getBriefDB(null), HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<LoginDBPutResponseBody> putLoginDB(
      RequestProxyWrapper<LoginDBPutRequestBody> loginPutRequestBody, Exchange exchange,
      String login) {
    log.debug(IN);
    RequestProxyWrapper<LoginDBPutResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.updateEmployee(login,
            loginPutRequestBody.getRequest().getData(), backbaseRequestUtility
                .extractInternalJwt(
                    loginPutRequestBody.getRequest().getInternalRequestContext())), HttpMethod.put);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<LoginDBGetResponseBody> getLoginDB(RequestProxyWrapper<Void> request,
      Exchange exchange, String login) {
    log.debug(IN);
    RequestProxyWrapper<LoginDBGetResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(employeePersistenceService.getUserDetailsByIdDB(login),
            HttpMethod.get);
    log.debug(OUT);
    return response;
  }

}
