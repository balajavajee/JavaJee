package com.yoma.adminportal.employeemgmt.service.impl;

import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.createPage;
import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.mapBrief;
import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.myLogin;
import static com.yoma.adminportal.utilities.datetime.DateTimeUtility.dateToString;
import static com.yoma.adminportal.utilities.datetime.DateTimeUtility.isDateWithin;
import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import com.backbase.buildingblocks.jwt.internal.token.InternalJwt;
import com.backbase.persistence.employeemanagement.HttpStatus;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.Function;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.FunctionGroupIdPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.Role;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBDeleteResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RoleIdDBPutResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.rolesDB.RolesDBPostResponseBody;
import com.yoma.adminportal.auditservice.AuditMessage.EventAction;
import com.yoma.adminportal.auditservice.AuditMessage.ObjectType;
import com.yoma.adminportal.employeemgmt.persistence.dto.RoleForAudit;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeRoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeRelationEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.PrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleFunctionPrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRoleRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.FunctionPrivilegeRelationRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.FunctionPrivilegeRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.RoleFunctionPrivilegeRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.RoleRepository;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBrief;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceFilterBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistencePageBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBrief;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBriefList;
import com.yoma.adminportal.employeemgmt.service.RolePersistenceService;
import com.yoma.adminportal.employeemgmt.service.impl.exception.EntityNotFoundException;
import com.yoma.adminportal.employeemgmt.service.impl.operation.EmployeeRoleOperation;
import com.yoma.adminportal.employeemgmt.service.impl.operation.EntityOperation;
import com.yoma.adminportal.employeemgmt.service.impl.operation.RoleFunctionPrivilegeOperation;
import com.yoma.adminportal.utilities.datetime.DateTimeUtility;
import com.yoma.adminportal.utilities.exception.UnauthorizedException;
import com.yoma.auditservice.publisher.AuditEventPublisher;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RolePersistenceServiceImpl extends AbstractAuditableService implements
    RolePersistenceService {

  private static final String SUCCESS = "SUCCESS";
  private static final String BAD_REQUEST = "BAD_REQUEST";

  private final RoleRepository roleRepository;
  private final EmployeeRoleRepository employeeRoleRepository;
  private final EmployeeRepository employeeRepository;
  private final RoleFunctionPrivilegeRepository roleFunctionPrivilegeRepository;
  private final FunctionPrivilegeRepository functionPrivilegeRepository;
  private final FunctionPrivilegeRelationRepository functionPrivilegeRelationRepository;

  @Autowired
  public RolePersistenceServiceImpl(RoleRepository roleRepository,
      EmployeeRoleRepository employeeRoleRepository, EmployeeRepository employeeRepository,
      RoleFunctionPrivilegeRepository roleFunctionPrivilegeRepository,
      FunctionPrivilegeRepository functionPrivilegeRepository,
      FunctionPrivilegeRelationRepository functionPrivilegeRelationRepository,
      AuditEventPublisher auditEventPublisher) {
    super(auditEventPublisher);
    this.roleRepository = roleRepository;
    this.employeeRoleRepository = employeeRoleRepository;
    this.employeeRepository = employeeRepository;
    this.roleFunctionPrivilegeRepository = roleFunctionPrivilegeRepository;
    this.functionPrivilegeRepository = functionPrivilegeRepository;
    this.functionPrivilegeRelationRepository = functionPrivilegeRelationRepository;
  }

  @Transactional
  @Override
  public RolesDBPostResponseBody createRole(RolesDBPostRequestBody requestBody,
      InternalJwt internalJwt) {
    log.debug(IN);
    RolesDBPostResponseBody responseBody = new RolesDBPostResponseBody();
    HttpStatus httpStatus = new HttpStatus();
    if (null != roleRepository.findByName(requestBody.getName()).orElse(null)) {
      httpStatus.setCode(SC_CONFLICT);
      httpStatus.setMessage("Role with name: '" + requestBody.getName() + "' already exists.");
      responseBody.setHttpStatus(httpStatus);
      return responseBody;
    }
    responseBody.setEmployeesAdded(new ArrayList<>());
    responseBody.setEmployeesRemoved(new ArrayList<>());
    if (requestBody.getFunctions() == null || requestBody.getFunctions().isEmpty()) {
      httpStatus.setCode(SC_BAD_REQUEST);
      httpStatus.setMessage("No function specified");
    } else {
      RoleForAudit oldState = fetchRoleForAudit(null);
      RoleForAudit newState = null;
      RoleEntity roleEntity = new RoleEntity();
      roleEntity.setDescription(requestBody.getDescription());
      roleEntity.setName(requestBody.getName());
      roleEntity.setFunctionGroupId(requestBody.getFunctionGroupId());
      try {
        RoleEntity save = roleRepository.save(roleEntity);
        List<RoleFunctionPrivilegeOperation> changedFunctionAssignments =
            identifyRoleFunctionPrivilegeChanges(save, requestBody.getFunctions());
        handledChangedFunctionAssignments(changedFunctionAssignments);
        List<EmployeeRoleOperation> changedEmployeeAssignments =
            identifyRoleEmployeeChanges(save, requestBody.getEmployees(), internalJwt);
        handleChangedEmployeeAssignments(changedEmployeeAssignments,
            responseBody.getEmployeesAdded(),
            responseBody.getEmployeesRemoved());
        httpStatus.setMessage(SUCCESS);
        httpStatus.setCode(SC_CREATED);
        responseBody.setId(save.getId());
        newState = fetchRoleForAudit(save.getId());
        log.info("Saved role: " + save.toString());
      } catch (DataIntegrityViolationException e) {
        httpStatus.setMessage(BAD_REQUEST);
        httpStatus.setCode(SC_BAD_REQUEST);
        log.error("Error while saving role: " + e.getMessage(), e);
      } catch (ParseException e) {
        httpStatus.setMessage("Invalid data format");
        httpStatus.setCode(SC_BAD_REQUEST);
      } catch (EntityNotFoundException e) {
        if (FunctionEntity.class.equals(e.getEntityClass())) {
          httpStatus.setMessage(String.format("Role %s not found", e.getSearchKey()));
          httpStatus.setCode(SC_BAD_REQUEST);
        }
        if (EmployeeEntity.class.equals(e.getEntityClass())) {
          httpStatus.setMessage(String.format("Employee %s not found", e.getSearchKey()));
          httpStatus.setCode(SC_BAD_REQUEST);
        } else {
          httpStatus.setMessage("Not found");
          httpStatus.setCode(SC_NOT_FOUND);
        }
        log.error("Exception in saving Data: " + e.getMessage(), e);
      } catch (Exception e) {
        httpStatus.setMessage("Exception in saving role: " + e);
        httpStatus.setCode(SC_INTERNAL_SERVER_ERROR);
        log.error("Error while saving role: " + e.getMessage(), e);
      } finally {
        log.debug(OUT);
        final String msg =
            Objects.isNull(newState) ? String
                .format("Create role %s Failed.", requestBody.getName())
                : String.format("Role %s Created Successfully", newState.getName());
        publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
            Objects.isNull(oldState) ? new RoleForAudit() : oldState,
            Objects.isNull(newState) ? new RoleForAudit() : newState,
            ObjectType.ROLE_MANAGEMENT, EventAction.CREATE_ROLE,
            msg);
      }
    }
    responseBody.setHttpStatus(httpStatus);
    return responseBody;
  }

  private RoleForAudit fetchRoleForAudit(Integer roleId) {
    RoleForAudit rfa = new RoleForAudit();
    if (Objects.isNull(roleId)) {
      return rfa;
    }
    RoleEntity roleEntity = roleRepository.findOne(roleId);
    if (Objects.isNull(roleEntity)) {
      return rfa;
    }
    rfa.setId(roleEntity.getId());
    rfa.setName(roleEntity.getName());
    rfa.setDescription(roleEntity.getDescription());
    rfa.setFunctionGroupId(roleEntity.getFunctionGroupId());
    rfa.setFunctions(findFunctionPrivilegeNamesForGroup(roleEntity));
    rfa.setEmployees(findEmployeeNamesForGroup(roleEntity));
    return rfa;
  }

  private List<String> findFunctionPrivilegeNamesForGroup(RoleEntity entity) {
    if (Objects.isNull(entity) || Objects.isNull(entity.getId())) {
      return null;
    }
    return roleFunctionPrivilegeRepository.findByRole(entity).stream()
        .map(RoleFunctionPrivilegeEntity::getFunctionPrivilege)
        .map(fpe -> String
            .format("%s : %s", fpe.getFunction().getName(), fpe.getPrivilege().getName()))
        .distinct().collect(
            Collectors.toList());
  }

  private List<String> findEmployeeNamesForGroup(RoleEntity entity) {
    if (Objects.isNull(entity) || Objects.isNull(entity.getId())) {
      return null;
    }
    return employeeRoleRepository.findByRole(entity).stream()
        .map(ere -> String.format("%s (%s - %s)", ere.getEmployee().getLogin(),
            Objects.isNull(ere.getDateFrom()) ? "" : dateToString(ere.getDateFrom()),
            Objects.isNull(ere.getDateTo()) ? "" : dateToString(ere.getDateTo()))).collect(
            Collectors.toList());
  }

  private void handleChangedEmployeeAssignments(
      List<EmployeeRoleOperation> changedEmployeeAssignments, List<String> employeesAdded,
      List<String> employeesRemoved) {
    Date now = new Date();
    for (EmployeeRoleOperation ero : changedEmployeeAssignments) {
      EmployeeRoleEntity ere = ero.getEntity();
      if (EntityOperation.add.equals(ero.getOperation())) {
        ere.setActive(false);
        if (isDateWithin(now, ere.getDateFrom(), ere.getDateTo())) {
          ere.setActive(true);
          employeesAdded.add(ere.getEmployee().getLogin());
        }
        employeeRoleRepository.save(ere);
      } else if (EntityOperation.remove.equals(ero.getOperation())) {
        employeeRoleRepository.delete(ere);
        employeesRemoved.add(ere.getEmployee().getLogin());
      } else if (EntityOperation.modify.equals(ero.getOperation())) {
        ere.setActive(false);
        if (isDateWithin(now, ere.getDateFrom(), ere.getDateTo())) {
          ere.setActive(true);
          employeesAdded.add(ere.getEmployee().getLogin());
        } else {
          employeesRemoved.add(ere.getEmployee().getLogin());
        }
        employeeRoleRepository.save(ere);
      }
    }
  }

  private void handledChangedFunctionAssignments(
      List<RoleFunctionPrivilegeOperation> changedFunctionAssignments) {
    for (RoleFunctionPrivilegeOperation rfpo : changedFunctionAssignments) {
      if (EntityOperation.add.equals(rfpo.getOperation())) {
        roleFunctionPrivilegeRepository.save(rfpo.getEntity());
      } else if (EntityOperation.remove.equals(rfpo.getOperation())) {
        roleFunctionPrivilegeRepository.delete(rfpo.getEntity());
      }
    }
  }

  @Transactional
  @Override
  public RoleIdDBPutResponseBody updateRole(String roleIdDb, RoleIdDBPutRequestBody requestBody,
      InternalJwt internalJwt) {
    log.debug(IN);
    RoleIdDBPutResponseBody responseBody = new RoleIdDBPutResponseBody();
    responseBody.setEmployeesAdded(new ArrayList<>());
    responseBody.setEmployeesRemoved(new ArrayList<>());

    HttpStatus httpStatus = new HttpStatus();
    if (requestBody.getFunctions() == null || requestBody.getFunctions().isEmpty()) {
      httpStatus.setCode(SC_BAD_REQUEST);
      httpStatus.setMessage("No function specified");
    } else {
      RoleForAudit oldState = null;
      RoleForAudit newState = null;
      try {
        RoleEntity role = roleRepository.findOne(Integer.parseInt(roleIdDb));
        if (role == null) {
          throw new EntityNotFoundException(RoleEntity.class, roleIdDb);
        }
        oldState = fetchRoleForAudit(Integer.valueOf(roleIdDb));
        role.setDescription(requestBody.getDescription());
        role.setFunctionGroupId(
            Objects.isNull(requestBody.getFunctionGroupId()) ? role.getFunctionGroupId()
                : requestBody.getFunctionGroupId());
        role = roleRepository.save(role);
        List<RoleFunctionPrivilegeOperation> changedFunctionAssignments =
            identifyRoleFunctionPrivilegeChanges(role, requestBody.getFunctions());
        handledChangedFunctionAssignments(changedFunctionAssignments);
        List<EmployeeRoleOperation> changedEmployeeAssignments =
            identifyRoleEmployeeChanges(role, requestBody.getEmployees(), internalJwt);
        handleChangedEmployeeAssignments(changedEmployeeAssignments,
            responseBody.getEmployeesAdded(), responseBody.getEmployeesRemoved());
        newState = fetchRoleForAudit(role.getId());
        httpStatus.setMessage(SUCCESS);
        httpStatus.setCode(SC_OK);
        log.info("Saved role: " + role.toString());
      } catch (DataIntegrityViolationException e) {
        httpStatus.setMessage(BAD_REQUEST);
        httpStatus.setCode(SC_BAD_REQUEST);
        log.error("Error while saving role: " + e.getMessage(), e);
      } catch (ParseException e) {
        httpStatus.setMessage("Invalid data format");
        httpStatus.setCode(SC_BAD_REQUEST);
        log.error("Error while saving role: " + e.getMessage(), e);
      } catch (EntityNotFoundException e) {
        httpStatus.setMessage("Role not found");
        httpStatus.setCode(SC_NOT_FOUND);
        log.error("Error while saving role: " + e.getMessage(), e);
      } catch (Exception e) {
        httpStatus.setMessage("Exception in saving role: " + e);
        httpStatus.setCode(SC_INTERNAL_SERVER_ERROR);
        log.error("Error while saving role: " + e.getMessage(), e);
      } finally {
        log.debug(OUT);
        final String msg =
            Objects.isNull(newState) ? String.format("Update Role %s Failed", requestBody.getName())
                : String.format("Role %s Updated Successfully", newState.getName());
        publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
            Objects.isNull(oldState) ? new RoleForAudit() : oldState,
            Objects.isNull(newState) ? new RoleForAudit() : newState,
            ObjectType.ROLE_MANAGEMENT, EventAction.MODIFY_ROLE,
            msg);
      }
    }
    responseBody.setHttpStatus(httpStatus);
    return responseBody;
  }

  @Override
  public FunctionGroupIdPutResponseBody updateFunctionGroupId(String roleIdDb,
      FunctionGroupIdPutRequestBody requestBody,
      InternalJwt internalJwt) {
    log.debug(IN);
    FunctionGroupIdPutResponseBody responseBody = new FunctionGroupIdPutResponseBody();
    HttpStatus httpStatus = new HttpStatus();
    try {
      RoleEntity role = roleRepository.findOne(Integer.parseInt(roleIdDb));
      if (role == null) {
        throw new EntityNotFoundException(RoleEntity.class, roleIdDb);
      }
      RoleForAudit oldState = fetchRoleForAudit(role.getId());
      role.setFunctionGroupId(requestBody.getFunctionGroupId());
      role = roleRepository.save(role);
      responseBody.setRoleId(role.getId());
      responseBody.setFunctionGroupId(role.getFunctionGroupId());
      httpStatus.setMessage(SUCCESS);
      httpStatus.setCode(SC_OK);
    } catch (DataIntegrityViolationException e) {
      httpStatus.setMessage(BAD_REQUEST);
      httpStatus.setCode(SC_BAD_REQUEST);
      log.error("Error while saving role: " + e.getMessage(), e);
    } catch (EntityNotFoundException e) {
      httpStatus.setMessage("Role not found");
      httpStatus.setCode(SC_NOT_FOUND);
      log.error("Error while saving role: " + e.getMessage(), e);
    } catch (Exception e) {
      httpStatus.setMessage("Exception in saving role: " + e);
      httpStatus.setCode(SC_INTERNAL_SERVER_ERROR);
      log.error("Error while saving role: " + e.getMessage(), e);
    }
    return responseBody;
  }

  @Override
  public PersistenceRoleBriefList getBriefDB(PersistenceOrderedPagedFiltered request) {
    log.debug(IN);
    Page<RoleEntity> page = null;
    List<RoleEntity> list = null;
    Sort sort = null;
    Pageable pageable = null;
    Example<RoleEntity> example = null;
    if (!Objects.isNull(request)) {
      if (!Objects.isNull(request.getOrderBy())) {
        PersistenceOrderBy ob = request.getOrderBy();
        sort = new Sort(Direction.fromString(ob.getDirection().name()), ob.getProperty());
      }
      if (!Objects.isNull(request.getFilterBy()) && !request.getFilterBy().isEmpty()) {
        example = createExample(request.getFilterBy());
      }
      if (!Objects.isNull(request.getPageBy())) {
        PersistencePageBy pb = request.getPageBy();
        pageable = new PageRequest(pb.getPage(), pb.getSize(), sort);
      }
    }
    if (!Objects.isNull(example)) {
      if (!Objects.isNull(pageable)) {
        page = roleRepository.findAll(example, pageable);
      } else if (!Objects.isNull(sort)) {
        list = roleRepository.findAll(example, sort);
      } else {
        list = roleRepository.findAll(example);
      }
    } else if (!Objects.isNull(pageable)) {
      page = roleRepository.findAll(pageable);
    }
    if (!Objects.isNull(page)) {
      list = page.getContent();
    }
    if (Objects.isNull(list)) {
      list = roleRepository.findAll();
    }
    List<PersistenceRoleBrief> roles = list.stream().filter(r -> !r.isCxsRole())
        .map(r -> mapBrief(r, null, null))
        .collect(Collectors.toList());
    PersistenceRoleBriefList responseBody = new PersistenceRoleBriefList();
    responseBody.setRoles(roles);
    responseBody.setPage(createPage(pageable, page, roles));
    log.debug(OUT);
    return responseBody;
  }

  private Example<RoleEntity> createExample(List<PersistenceFilterBy> filterBy) {
    Map<String, String> mapped = filterBy.stream()
        .collect(Collectors.toMap(PersistenceFilterBy::getProperty, PersistenceFilterBy::getValue));
    RoleEntity r = new RoleEntity();
    r.setId(mapped.containsKey("id") ? Integer.valueOf(mapped.get("id")) : null);
    r.setName(mapped.get("name"));
    r.setDescription(mapped.get("description"));
    return Example.of(r, ExampleMatcher.matchingAll().withStringMatcher(StringMatcher.EXACT));
  }

  @Override
  public RoleIdDBGetResponseBody getRoleByIdDB(String roleId, boolean includeHiddenFunctions) {
    log.debug(IN);
    RoleIdDBGetResponseBody responseBody = new RoleIdDBGetResponseBody();
    RoleEntity foundRole = roleRepository.findOne(Integer.parseInt(roleId));
    if (null == foundRole) {
      log.info("Role not found");
      return responseBody;
    }
    log.info("Found role: " + foundRole);
    responseBody.setRole(map(foundRole, true, includeHiddenFunctions, true));
    log.debug(OUT);
    return responseBody;
  }

  /**
   * Map the {@link RoleEntity} to {@link Role}
   *
   * @param loadFunctions should the functions be fetched as well
   * @param loadEmployees should the employees eb loaded as well
   */
  private Role map(RoleEntity entity, boolean loadFunctions, boolean includeHiddenFunctions,
      boolean loadEmployees) {
    Role role = new Role();
    role.setId(entity.getId());
    role.setName(entity.getName());
    role.setDescription(entity.getDescription());
    if (loadFunctions) {
      role.setFunctionGroupId(entity.getFunctionGroupId());
      Map<Integer, Function> mapped = new HashMap<>();
      List<FunctionPrivilegeEntity> fpeList = roleFunctionPrivilegeRepository.findByRole(entity)
          .stream()
          .map(RoleFunctionPrivilegeEntity::getFunctionPrivilege)
          .filter(f -> includeHiddenFunctions || Objects.isNull(f.getUserVisible()) || f
              .getUserVisible()).collect(
              Collectors.toList());
      for (FunctionPrivilegeEntity fpe : fpeList) {
        processFunctionPrivilege(fpe, mapped, includeHiddenFunctions);
      }
      role.setFunctions(new ArrayList<>(mapped.values()));
    }
    if (loadEmployees) {
      role.setEmployees(employeeRoleRepository.findByRole(entity).stream()
          .map(ere -> mapBrief(ere.getEmployee(), ere.getDateFrom(), ere.getDateTo()))
          .collect(Collectors.toList()));
    }
    return role;
  }

  private void processFunctionPrivilege(FunctionPrivilegeEntity fpe, Map<Integer, Function> mapped,
      boolean includeHiddenFunctions) {
    if (includeHiddenFunctions || Objects.isNull(fpe.getUserVisible()) || fpe.getUserVisible()) {
      FunctionEntity fe = fpe.getFunction();
      PrivilegeEntity pe = fpe.getPrivilege();
      Function f;
      if (mapped.containsKey(fe.getId())) {
        f = mapped.get(fe.getId());
      } else {
        f = new Function().withId(fe.getId()).withCode(fe.getCode()).withName(fe.getName())
            .withPrivileges(new ArrayList<>());
        mapped.put(fe.getId(), f);
      }
      if (!f.getPrivileges().contains(pe.getCode())) {
        f.getPrivileges().add(pe.getCode());
        //only process children if adding function privilege to prevent circular assignment
        List<FunctionPrivilegeRelationEntity> relations = functionPrivilegeRelationRepository
            .findByParent(fpe);
        relations.forEach(r ->
            processFunctionPrivilege(r.getChild(), mapped, includeHiddenFunctions)
        );
      }
    }
  }

  /**
   * Get the employee name
   *
   * @param employee {@link EmployeeEntity}
   * @return nameof employee
   */
  private String getEmployeeName(EmployeeEntity employee) {
    StringBuilder sb = new StringBuilder();
    if (employee.getFirstName() != null) {
      sb.append(employee.getFirstName());
    }
    if (employee.getFamilyName() != null) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      sb.append(employee.getFamilyName());
    }
    return sb.toString();
  }

  /**
   * Identity the changes in assigned {@link RoleFunctionPrivilegeEntity}
   */
  private List<RoleFunctionPrivilegeOperation> identifyRoleFunctionPrivilegeChanges(RoleEntity role,
      List<Function> functions) throws EntityNotFoundException {
    List<RoleFunctionPrivilegeOperation> changes = new ArrayList<>();
    List<RoleFunctionPrivilegeEntity> current = roleFunctionPrivilegeRepository.findByRole(role);
    Set<Integer> currentIds =
        current.stream().map(o -> o.getFunctionPrivilege().getId()).collect(Collectors.toSet());
    List<RoleFunctionPrivilegeEntity> fromRequest =
        convertToRoleFunctionPrivilegeEntityList(role, functions);
    Set<Integer> fromRequestIds =
        fromRequest.stream().map(o -> o.getFunctionPrivilege().getId()).collect(Collectors.toSet());
    current.forEach(rfpe -> {
      if (!fromRequestIds.contains(rfpe.getFunctionPrivilege().getId())) {
        changes.add(new RoleFunctionPrivilegeOperation(EntityOperation.remove, rfpe));
      }
    });
    fromRequest.forEach(rfpe -> {
      if (!currentIds.contains(rfpe.getFunctionPrivilege().getId())) {
        changes.add(new RoleFunctionPrivilegeOperation(EntityOperation.add, rfpe));
      }
    });
    return changes;
  }

  private List<EmployeeRoleOperation> identifyRoleEmployeeChanges(RoleEntity role,
      List<PersistenceEmployeeBrief> employees,
      InternalJwt internalJwt) throws EntityNotFoundException, ParseException {
    for (PersistenceEmployeeBrief e : employees) {
      boolean datesValid = DateTimeUtility.isDatesRangeValid(e.getDateFrom(), e.getDateTo());
      if (!datesValid) {
        throw new DataIntegrityViolationException("Invalid FROM or TO date provided");
      }
    }
    final String myLogin = myLogin(internalJwt);
    List<EmployeeRoleOperation> changes = new ArrayList<>();
    Map<Integer, EmployeeRoleEntity> currentEmployees =
        employeeRoleRepository.findByRole(role).stream()
            .collect(Collectors.toMap(ere -> ere.getEmployee().getId(), ere -> ere));
    Map<Integer, EmployeeRoleEntity> fromRequest = convertToEmployeeRoleEntityMap(role, employees);
    currentEmployees.values().forEach(ere -> {
      if (!fromRequest.containsKey(ere.getEmployee().getId())) {
        if (Objects.equals(myLogin, ere.getEmployee().getLogin())) {
          throw new UnauthorizedException("Removing yourself from a role is not permitted");
        }
        changes.add(new EmployeeRoleOperation(EntityOperation.remove, ere));
      }
    });
    fromRequest.values().forEach(ere -> {
      if (!currentEmployees.containsKey(ere.getEmployee().getId())) {
        if (Objects.equals(myLogin, ere.getEmployee().getLogin())) {
          throw new UnauthorizedException("Assigning yourself to a role is not permitted");
        }
        changes.add(new EmployeeRoleOperation(EntityOperation.add, ere));
      } else {
        EmployeeRoleEntity current = currentEmployees.get(ere.getEmployee().getId());
        EmployeeRoleEntity request = fromRequest.get(ere.getEmployee().getId());
        if (!Objects.equals(current.getDateFrom(), request.getDateFrom()) || !Objects
            .equals(current.getDateTo(), request.getDateTo())) {
          current.setDateFrom(request.getDateFrom());
          current.setDateTo(request.getDateTo());
          if (Objects.equals(myLogin, ere.getEmployee().getLogin())) {
            throw new UnauthorizedException("Changing self assignment to a role is not permitted");
          }
          changes.add(new EmployeeRoleOperation(EntityOperation.modify, current));
        }
      }
    });
    return changes;
  }

  /**
   * Convert to list for easier processing
   */
  private List<RoleFunctionPrivilegeEntity> convertToRoleFunctionPrivilegeEntityList(
      RoleEntity role, List<Function> list) throws EntityNotFoundException {
    if (list == null) {
      return Collections.emptyList();
    }
    List<FunctionPrivilegeEntity> functionPrivileges = functionPrivilegeRepository.findAll();
    List<RoleFunctionPrivilegeEntity> result = new ArrayList<>();
    for (Function f : list) {
      if (f.getPrivileges() != null) {
        for (String p : f.getPrivileges()) {
          FunctionPrivilegeEntity fpe =
              findByFunctionAndPrivilege(functionPrivileges, f.getId(), p);
          if (fpe == null) {
            throw new EntityNotFoundException(FunctionPrivilegeEntity.class,
                String.format("%s:%s", f.getId(), p));
          }
          RoleFunctionPrivilegeEntity rfpe = new RoleFunctionPrivilegeEntity();
          rfpe.setRole(role);
          rfpe.setFunctionPrivilege(fpe);
          result.add(rfpe);
        }
      }
    }
    return result;
  }

  /**
   * Convert to list for easier processing
   */
  private Map<Integer, EmployeeRoleEntity> convertToEmployeeRoleEntityMap(RoleEntity role,
      List<PersistenceEmployeeBrief> employees) throws EntityNotFoundException, ParseException {
    if (employees == null || employees.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<Integer, EmployeeEntity> employeesById = employeeRepository
        .findAll(employees.stream().map(PersistenceEmployeeBrief::getId).distinct()
            .collect(Collectors.toList()))
        .stream().collect(Collectors.toMap(EmployeeEntity::getId, e -> e));
    Map<Integer, EmployeeRoleEntity> result = new HashMap<>();
    for (PersistenceEmployeeBrief e : employees) {
      if (!employeesById.containsKey(e.getId())) {
        throw new EntityNotFoundException(EmployeeEntity.class, e.getId());
      }
      EmployeeRoleEntity ere = new EmployeeRoleEntity();
      ere.setRole(role);
      ere.setEmployee(employeesById.get(e.getId()));
      ere.setDateFrom(DateTimeUtility.stringToDate(e.getDateFrom()));
      ere.setDateTo(DateTimeUtility.stringToDate(e.getDateTo()));
      result.put(e.getId(), ere);
    }
    return result;
  }

  /**
   * Find the matching {@link FunctionPrivilegeEntity} object
   *
   * @param from list to search
   * @param functionId functionId
   * @param privilege privilege code
   * @return matching {@link FunctionPrivilegeEntity} or null
   */
  private FunctionPrivilegeEntity findByFunctionAndPrivilege(List<FunctionPrivilegeEntity> from,
      Integer functionId, String privilege) {
    for (FunctionPrivilegeEntity fpe : from) {
      if (functionId.equals(fpe.getFunction().getId())
          && privilege.equalsIgnoreCase(fpe.getPrivilege().getCode())) {
        return fpe;
      }
    }
    return null;
  }

  @Override
  public List<Role> getRolesForEmployee(EmployeeEntity employee) {
    log.debug(IN);
    if (employee == null) {
      return Collections.emptyList();
    }
    return employeeRoleRepository.findByEmployee(employee).stream()
        .map(e -> map(e.getRole(), true, false, false)).collect(Collectors.toList());
  }

  @Override
  public RoleIdDBDeleteResponseBody deleteRole(String id,
      InternalJwt internalJwt) {
    log.debug(IN);
    RoleIdDBDeleteResponseBody response = new RoleIdDBDeleteResponseBody();
    HttpStatus status = new HttpStatus();
    response.setHttpStatus(status);
    int id1;
    try {
      id1 = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      status.setMessage("Could not parse: " + id);
      status.setCode(HttpServletResponse.SC_BAD_REQUEST);

      return response;
    }

    RoleEntity roleEntity = roleRepository.findOne(id1);
    if (null == roleEntity) {
      log.error("Role with id: " + id + " not found. Cannot perform delete operation");
      status.setMessage("Role not found");
      status.setCode(HttpServletResponse.SC_NOT_FOUND);
      return response;
    }
    RoleForAudit oldState = fetchRoleForAudit(roleEntity.getId());
    if (!employeeRoleRepository.findByRole(roleEntity).isEmpty()) {
      log.error(
          "Role with id: " + id + " is assigned to employees. Cannot perform delete operation");
      status.setMessage("Role is assigned to employees");
      status.setCode(HttpServletResponse.SC_CONFLICT);
      return response;
    }

    List<RoleFunctionPrivilegeEntity> byRoleId = roleFunctionPrivilegeRepository.findByRoleId(id1);
    log.info("Role function privileges: found : " + byRoleId.size() + " entries in DB");
    if (!byRoleId.isEmpty()) {
      roleFunctionPrivilegeRepository.delete(byRoleId);
    }

    roleRepository.delete(roleEntity);
    RoleForAudit newState = fetchRoleForAudit(roleEntity.getId());
    publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
        oldState, newState, ObjectType.ROLE_MANAGEMENT, EventAction.DELETE_ROLE,
        String.format("Deleted role %s", oldState.getName()));

    response.setFunctionGroupId(roleEntity.getFunctionGroupId());

    status.setMessage("Deleted");
    status.setCode(HttpServletResponse.SC_OK);
    log.debug(OUT);

    return response;
  }

}

