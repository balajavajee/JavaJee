package com.yoma.adminportal.employeemgmt.rest;

import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.listener.spec.v1.passwordDB.PasswordDBListener;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.passwordDB.PasswordDBPostResponseBody;
import com.yoma.adminportal.employeemgmt.service.PasswordPersistenceService;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequestListener
public class PasswordDBListenerImpl implements PasswordDBListener {

  private final BackbaseRequestUtility backbaseRequestUtility;
  private final PasswordPersistenceService passwordPersistenceService;

  @Autowired
  public PasswordDBListenerImpl(BackbaseRequestUtility backbaseRequestUtility,
      PasswordPersistenceService passwordPersistenceService) {
    this.backbaseRequestUtility = backbaseRequestUtility;
    this.passwordPersistenceService = passwordPersistenceService;
  }

  @Override
  public RequestProxyWrapper<PasswordDBGetResponseBody> getPasswordDB(
      RequestProxyWrapper<Void> request, Exchange exchange, Boolean generate) {
    log.debug(IN);
    boolean gen = BooleanUtils.isTrue(generate);
    String password = passwordPersistenceService.getPassword(gen);
    String defaultPassword = gen ? null : password;
    String generatedPassword = gen ? password : null;
    RequestProxyWrapper<PasswordDBGetResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(new PasswordDBGetResponseBody()
                .withDefaultPassword(defaultPassword).withGeneratedPassword(generatedPassword),
            HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<PasswordDBPostResponseBody> postPasswordDB(
      RequestProxyWrapper<PasswordDBPostRequestBody> passwordDBPostRequestBody, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PasswordDBPostResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(
            passwordPersistenceService
                .checkPassword(passwordDBPostRequestBody.getRequest().getData()),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

}
