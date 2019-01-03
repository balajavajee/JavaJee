package com.yoma.adminportal.employeemgmt.rest;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.listener.spec.v1.functionsDB.FunctionsDBListener;
import com.backbase.persistence.employeemanagement.rest.spec.v1.functionsDB.FunctionsDBGetResponseBody;
import com.yoma.adminportal.employeemgmt.service.FunctionPersistenceService;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility.HttpMethod;
import java.util.Objects;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequestListener
public class FunctionsDBListenerImpl implements FunctionsDBListener {

  private final FunctionPersistenceService rolePersistenceService;
  private final BackbaseRequestUtility backbaseRequestUtility;

  @Autowired
  public FunctionsDBListenerImpl(FunctionPersistenceService rolePersistenceService,
      BackbaseRequestUtility backbaseRequestUtility) {
    this.rolePersistenceService = rolePersistenceService;
    this.backbaseRequestUtility = backbaseRequestUtility;
  }

  @Override
  public RequestProxyWrapper<FunctionsDBGetResponseBody> getFunctionsDB(
      RequestProxyWrapper<Void> request, Exchange exchange, Boolean includeHiddenFunctions) {
    FunctionsDBGetResponseBody rolesDB = rolePersistenceService.getFunctionsDB(
        !Objects.isNull(includeHiddenFunctions) && includeHiddenFunctions);
    return backbaseRequestUtility.buildRequestProxyWrapper(rolesDB, HttpMethod.get);
  }

}
