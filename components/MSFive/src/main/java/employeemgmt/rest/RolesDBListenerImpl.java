package com.yoma.adminportal.employeemgmt.rest;


import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;

import com.backbase.buildingblocks.backend.communication.event.annotations.RequestListener;
import com.backbase.buildingblocks.backend.communication.event.proxy.RequestProxyWrapper;
import com.backbase.persistence.employeemanagement.listener.spec.v1.rolesDB.RolesDBListener;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBDeleteResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBriefList;
import com.yoma.adminportal.employeemgmt.service.RolePersistenceService;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility;
import com.yoma.adminportal.utilities.backbase.BackbaseRequestUtility.HttpMethod;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequestListener
public class RolesDBListenerImpl implements RolesDBListener {

  private final RolePersistenceService rolePersistenceService;
  private final BackbaseRequestUtility backbaseRequestUtility;

  @Autowired
  public RolesDBListenerImpl(RolePersistenceService rolePersistenceService,
      BackbaseRequestUtility backbaseRequestUtility) {
    this.rolePersistenceService = rolePersistenceService;
    this.backbaseRequestUtility = backbaseRequestUtility;
  }

  @Override
  public RequestProxyWrapper<PersistenceRoleBriefList> getRolesDB(RequestProxyWrapper<Void> request,
      Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PersistenceRoleBriefList> response = backbaseRequestUtility
        .buildRequestProxyWrapper(rolePersistenceService.getBriefDB(null), HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<RolesDBPostResponseBody> postRolesDB(
      RequestProxyWrapper<RolesDBPostRequestBody> rolesDBPostRequestBody, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<RolesDBPostResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(
            rolePersistenceService.createRole(rolesDBPostRequestBody.getRequest().getData(),
                backbaseRequestUtility.extractInternalJwt(rolesDBPostRequestBody)),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<PersistenceRoleBriefList> postPaged(
      RequestProxyWrapper<PersistenceOrderedPagedFiltered> request, Exchange exchange) {
    log.debug(IN);
    RequestProxyWrapper<PersistenceRoleBriefList> response = backbaseRequestUtility
        .buildRequestProxyWrapper(
            rolePersistenceService.getBriefDB(request.getRequest().getData()),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<RoleIdDBPutResponseBody> putRoleIdDB(
      RequestProxyWrapper<RoleIdDBPutRequestBody> roleIdDBPutRequestBody, Exchange exchange,
      String roleIdDb) {
    log.debug(IN);
    RequestProxyWrapper<RoleIdDBPutResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(rolePersistenceService
                .updateRole(roleIdDb, roleIdDBPutRequestBody.getRequest().getData(),
                    backbaseRequestUtility.extractInternalJwt(roleIdDBPutRequestBody)),
            HttpMethod.post);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<RoleIdDBDeleteResponseBody> deleteRoleIdDB(
      RequestProxyWrapper<Void> request, Exchange exchange, String roleIdDb) {
    log.debug(IN);
    RequestProxyWrapper<RoleIdDBDeleteResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(rolePersistenceService
                .deleteRole(roleIdDb, backbaseRequestUtility.extractInternalJwt(request)),
            HttpMethod.delete);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<RoleIdDBGetResponseBody> getRoleIdDB(RequestProxyWrapper<Void> request,
      Exchange exchange, String roleIdDb, Boolean includeHiddenFunctions) {
    log.debug(IN);
    RequestProxyWrapper<RoleIdDBGetResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(rolePersistenceService.getRoleByIdDB(roleIdDb,
            !Objects.isNull(includeHiddenFunctions) && includeHiddenFunctions),
            HttpMethod.get);
    log.debug(OUT);
    return response;
  }

  @Override
  public RequestProxyWrapper<FunctionGroupIdPutResponseBody> putFunctionGroupId(
      RequestProxyWrapper<FunctionGroupIdPutRequestBody> functionGroupIdPutRequestBody,
      Exchange exchange, String roleIdDb) {
    log.debug(IN);
    RequestProxyWrapper<FunctionGroupIdPutResponseBody> response = backbaseRequestUtility
        .buildRequestProxyWrapper(rolePersistenceService.updateFunctionGroupId(roleIdDb,
            functionGroupIdPutRequestBody.getRequest().getData(),
            backbaseRequestUtility.extractInternalJwt(functionGroupIdPutRequestBody)),
            HttpMethod.put);
    log.debug(OUT);
    return response;
  }

}
