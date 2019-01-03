package com.yoma.adminportal.employeemgmt.service.impl;

import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.createPage;
import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.mapBrief;
import static com.yoma.adminportal.employeemgmt.service.impl.PersistenceUtilities.myLogin;
import static com.yoma.adminportal.utilities.datetime.DateTimeUtility.isDateWithin;
import static com.yoma.adminportal.utilities.logging.Constants.IN;
import static com.yoma.adminportal.utilities.logging.Constants.OUT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.backbase.buildingblocks.jwt.internal.token.InternalJwt;
import com.backbase.persistence.employeemanagement.HttpStatus;
import com.backbase.persistence.employeemanagement.IdentityDetails;
import com.backbase.persistence.employeemanagement.IdentityDetails.PasswordStatus;
import com.backbase.persistence.employeemanagement.IdentityUpdate;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.Data;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.Employee;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostRequestBody.Property;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeePersistenceValidationPostResponseBody.Result;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.EmployeesDBPostResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBGetResponseBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutRequestBody;
import com.backbase.persistence.employeemanagement.rest.spec.v1.employeesDB.LoginDBPutResponseBody;
import com.yoma.adminportal.auditservice.AuditMessage.EventAction;
import com.yoma.adminportal.auditservice.AuditMessage.ObjectType;
import com.yoma.adminportal.employeemgmt.persistence.dto.EmployeeForAudit;
import com.yoma.adminportal.employeemgmt.persistence.dto.FunctionPrivilege;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeManagementSettingEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeePasswordHistoryEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeRoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeStatus;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRoleRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.RoleRepository;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBrief;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBriefList;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeRolePendingAssignment;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeRolePendingAssignment.Change;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeRolePendingAssignments;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceFilterBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceOrderedPagedFiltered;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistencePageBy;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBrief;
import com.yoma.adminportal.employeemgmt.service.EmployeeManagementSettingsService;
import com.yoma.adminportal.employeemgmt.service.EmployeePersistenceService;
import com.yoma.adminportal.employeemgmt.service.FunctionPersistenceService;
import com.yoma.adminportal.employeemgmt.service.RolePersistenceService;
import com.yoma.adminportal.employeemgmt.service.impl.exception.EntityNotFoundException;
import com.yoma.adminportal.employeemgmt.service.impl.operation.EmployeeRoleOperation;
import com.yoma.adminportal.employeemgmt.service.impl.operation.EntityOperation;
import com.yoma.adminportal.employeemgmt.validator.email.EmployeeEmailValidator;
import com.yoma.adminportal.employeemgmt.validator.email.EmployeeEmailValidatorException;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordCriteriaException;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordHistoryException;
import com.yoma.adminportal.employeemgmt.validator.password.EmployeePasswordUtility;
import com.yoma.adminportal.employeemgmt.validator.phone.EmployeePhoneValidator;
import com.yoma.adminportal.employeemgmt.validator.phone.EmployeePhoneValidatorException;
import com.yoma.adminportal.utilities.datetime.DateTimeUtility;
import com.yoma.adminportal.utilities.exception.ResponseStatusException;
import com.yoma.adminportal.utilities.exception.UnauthorizedException;
import com.yoma.auditservice.publisher.AuditEventPublisher;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class EmployeePersistenceServiceImpl extends AbstractAuditableService implements
    EmployeePersistenceService {

  private static final EmployeePasswordEncoder employeePasswordEncoder =
      new EmployeePasswordEncoder();

  private final EmployeeRepository employeeRepository;
  private final EmployeeRoleRepository employeeRoleRepository;
  private final RoleRepository roleRepository;
  private final EmployeePasswordHistoryServiceImpl employeePasswordHistoryService;
  private final MessageSource messages;
  private final EmployeeManagementSettingsService settingsService;
  private final RolePersistenceService rolePersistenceService;
  private final FunctionPersistenceService functionPersistenceService;
  private final EmployeePasswordUtility employeePasswordUtility;

  @Autowired
  public EmployeePersistenceServiceImpl(EmployeeRepository employeeRepository,
      EmployeeRoleRepository employeeRoleRepository, RoleRepository roleRepository,
      EmployeePasswordHistoryServiceImpl employeePasswordHistoryService,
      MessageSource messages,
      EmployeeManagementSettingsService settingsService,
      RolePersistenceService rolePersistenceService,
      FunctionPersistenceService functionPersistenceService,
      AuditEventPublisher auditEventPublisher) {
    super(auditEventPublisher);
    this.employeeRepository = employeeRepository;
    this.employeeRoleRepository = employeeRoleRepository;
    this.roleRepository = roleRepository;
    this.employeePasswordHistoryService = employeePasswordHistoryService;
    this.messages = messages;
    this.settingsService = settingsService;
    this.rolePersistenceService = rolePersistenceService;
    this.functionPersistenceService = functionPersistenceService;
    this.employeePasswordUtility = new EmployeePasswordUtility(employeePasswordHistoryService,
        messages);
  }

  @Transactional
  @Override
  public EmployeesDBPostResponseBody createEmployee(EmployeesDBPostRequestBody requestBody,
      InternalJwt internalJwt) {
    log.debug(IN);
    EmployeesDBPostResponseBody responseBody = new EmployeesDBPostResponseBody();
    responseBody.setRolesAdded(new ArrayList<>());
    responseBody.setRolesRemoved(new ArrayList<>());
    HttpStatus httpStatus = new HttpStatus();
    responseBody.setHttpStatus(httpStatus);

    if (isBlank(requestBody.getFamilyName()) || isBlank(requestBody.getFirstName())
        || isBlank(requestBody.getLogin()) || isBlank(requestBody.getPassword())
        || isBlank(requestBody.getStatus())) {
      httpStatus.setMessage("Some of required data are missing");
      httpStatus.setCode(SC_BAD_REQUEST);
      return responseBody;
    }
    EmployeeEntity employeeExists =
        employeeRepository.findByLogin(requestBody.getLogin()).orElse(null);
    EmployeeForAudit oldState = fetchEmployeeForAudit(null);
    EmployeeForAudit newState = null;
    try {
      if (null != employeeExists) {
        log.error("Employee with id '" + employeeExists.getLogin() + "' already exists.");
        httpStatus.setMessage("Employee already exists");
        httpStatus.setCode(SC_CONFLICT);
      } else {
        EmployeeEntity newEmployee = new EmployeeEntity();
        newEmployee.setFailedLoginAttempts(0);
        newEmployee.setTitle(requestBody.getTitle());
        newEmployee.setFirstName(requestBody.getFirstName());
        newEmployee.setFamilyName(requestBody.getFamilyName());
        newEmployee.setLogin(requestBody.getLogin());
        String encodePassword = employeePasswordEncoder.encode(requestBody.getPassword());
        if (isPasswordIncorrect(null, httpStatus, requestBody.getPassword())) {
          return responseBody;
        }
        String encodedPassword = employeePasswordEncoder.encode(requestBody.getPassword());
        newEmployee.setPassword(encodedPassword);
        newEmployee.setFailedLoginAttempts(0);
        setExpireDate(newEmployee);
        newEmployee.setStatus(EmployeeStatus.valueOf(requestBody.getStatus()));
        newEmployee.setPasswordChangeRequired(true);
        if (!requestBody.getEmail().isEmpty()) {
          validateEmail(requestBody.getEmail());
          newEmployee.setEmail(requestBody.getEmail());
        }
        if (!requestBody.getPhone().isEmpty()) {
          validatePhone(requestBody.getPhone());
          newEmployee.setPhone(requestBody.getPhone());
        }

        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        updatePasswordHistory(savedEmployee, encodePassword);
        responseBody.setNewId(savedEmployee.getId());

        List<EmployeeRoleOperation> changedRoleAssignments =
            identifyEmployeeRoleChanges(savedEmployee, requestBody.getRoles());
        handleChangedRoleAssignments(changedRoleAssignments, responseBody.getRolesAdded(),
            responseBody.getRolesRemoved());

        httpStatus.setMessage("SUCCESS");
        httpStatus.setCode(SC_CREATED);
        newState = fetchEmployeeForAudit(savedEmployee.getId());
        log.info("Employee was created successfully.");
      }
    } catch (EmployeePhoneValidatorException | EmployeeEmailValidatorException e) {
      httpStatus.setCode(SC_BAD_REQUEST);
      httpStatus.setMessage(e.getMessage());
    } catch (ParseException e) {
      httpStatus.setMessage("Invalid data format");
      httpStatus.setCode(SC_BAD_REQUEST);
    } catch (EntityNotFoundException e) {
      if (RoleEntity.class.equals(e.getEntityClass())) {
        httpStatus.setMessage(String.format("Role %s not found", e.getSearchKey()));
        httpStatus.setCode(SC_BAD_REQUEST);
      } else {
        httpStatus.setMessage("Not found");
        httpStatus.setCode(SC_NOT_FOUND);
      }
      log.error("Entity not found: " + e.getMessage(), e);
    } catch (Exception e) {
      httpStatus.setMessage("Exception in saving employee: " + e);
      httpStatus.setCode(SC_INTERNAL_SERVER_ERROR);
      log.error("Exception in saving data: " + e.getMessage(), e);
    } finally {
      final String msg =
          Objects.isNull(newState) ? String
              .format("Create employee with ID %s Failed.", requestBody.getLogin())
              : String.format("Employee created with ID %s Successfully", newState.getLogin());
      publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
          Objects.isNull(oldState) ? new EmployeeForAudit() : oldState,
          Objects.isNull(newState) ? new EmployeeForAudit() : newState,
          ObjectType.EMPLOYEE_MANAGEMENT, EventAction.CREATE_EMPLOYEE,
          msg);
    }

    log.debug(OUT);
    return responseBody;
  }

  private EmployeeForAudit fetchEmployeeForAudit(Integer id) {
    EmployeeForAudit efa = new EmployeeForAudit();
    if (Objects.isNull(id)) {
      return efa;
    }
    EmployeeEntity employeeEntity = employeeRepository.findOne(id);
    if (Objects.isNull(employeeEntity)) {
      return efa;
    }
    efa.setLogin(employeeEntity.getLogin());
    efa.setFirstname(employeeEntity.getFirstName());
    efa.setFamilyname(employeeEntity.getFamilyName());
    efa.setStatus(employeeEntity.getStatus().name());
    efa.setTitle(employeeEntity.getTitle());
    efa.setEmail(employeeEntity.getEmail());
    efa.setCredentialExpires(employeeEntity.getCredentialExpires().toString());
    efa.setPhone(employeeEntity.getPhone());
    efa.setRoles(fetchEmployeeRoles(employeeEntity));
    return efa;
  }

  private List<String> fetchEmployeeRoles(EmployeeEntity employeeEntity) {
    if (Objects.isNull(employeeEntity) || Objects.isNull(employeeEntity.getId())) {
      return null;
    }
    return employeeRoleRepository.findByEmployee(employeeEntity).stream()
        .map(ere -> String.format("%s : %s", ere.getRole().getId(), ere.getRole().getName()))
        .collect(
            Collectors.toList());
  }

  private List<EmployeeRoleOperation> identifyEmployeeRoleChanges(EmployeeEntity employee,
      List<PersistenceRoleBrief> roles) throws EntityNotFoundException, ParseException {
    for (PersistenceRoleBrief r : roles) {
      boolean datesValid;
      datesValid = DateTimeUtility.isDatesRangeValid(r.getDateFrom(), r.getDateTo());
      if (!datesValid) {
        throw new DataIntegrityViolationException("Invalid FROM or TO date provided");
      }
    }
    List<EmployeeRoleOperation> changes = new ArrayList<>();
    List<EmployeeRoleEntity> employeeRoles = employeeRoleRepository.findByEmployee(employee);
    Map<Integer, EmployeeRoleEntity> currentRoless = new HashMap<>();
    for (EmployeeRoleEntity ere : employeeRoles) {
      currentRoless.put(ere.getRole().getId(), ere);
    }
    Map<Integer, EmployeeRoleEntity> fromRequest = convertToEmployeeRoleEntityMap(employee, roles);
    currentRoless.values().forEach(ere -> {
      if (!fromRequest.containsKey(ere.getRole().getId())) {
        changes.add(new EmployeeRoleOperation(EntityOperation.remove, ere));
      }
    });
    fromRequest.values().forEach(ere -> {
      if (!currentRoless.containsKey(ere.getRole().getId())) {
        changes.add(new EmployeeRoleOperation(EntityOperation.add, ere));
      } else {
        EmployeeRoleEntity current = currentRoless.get(ere.getRole().getId());
        EmployeeRoleEntity request = fromRequest.get(ere.getRole().getId());
        if (!Objects.equals(current.getDateFrom(), request.getDateFrom()) || !Objects
            .equals(current.getDateTo(), request.getDateTo())) {
          current.setDateFrom(request.getDateFrom());
          current.setDateTo(request.getDateTo());
          changes.add(new EmployeeRoleOperation(EntityOperation.modify, current));
        }
      }
    });
    return changes;
  }

  /**
   * Convert to list for easier processing
   */
  private Map<Integer, EmployeeRoleEntity> convertToEmployeeRoleEntityMap(EmployeeEntity employee,
      List<PersistenceRoleBrief> roles) throws EntityNotFoundException, ParseException {
    if (roles == null || roles.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<Integer, RoleEntity> rolesById = roleRepository
        .findAll(
            roles.stream().map(PersistenceRoleBrief::getId).distinct().collect(Collectors.toList()))
        .stream().collect(Collectors.toMap(RoleEntity::getId, e -> e));
    Map<Integer, EmployeeRoleEntity> result = new HashMap<>();
    for (PersistenceRoleBrief r : roles) {
      if (!rolesById.containsKey(r.getId())) {
        throw new EntityNotFoundException(RoleEntity.class, r.getId());
      }
      EmployeeRoleEntity ere = new EmployeeRoleEntity();
      ere.setRole(rolesById.get(r.getId()));
      ere.setEmployee(employee);
      ere.setDateFrom(DateTimeUtility.stringToDate(r.getDateFrom()));
      ere.setDateTo(DateTimeUtility.stringToDate(r.getDateTo()));
      result.put(r.getId(), ere);
    }
    return result;
  }

  private void setExpireDate(EmployeeEntity newEmployee) {
    String defaultExpireDays = "30";
    EmployeeManagementSettingEntity setting = settingsService
        .getSetting(EmployeeManagementSettingsService.EMPLOYEES_DEFAULT_EXPIRE_PASSWORD_DAYS);
    if (setting != null) {
      defaultExpireDays = setting.getValue();
    }
    Calendar c = new GregorianCalendar();
    c.add(Calendar.DATE, Integer.parseInt(defaultExpireDays));
    newEmployee.setCredentialExpires(c.getTime());
  }

  @Transactional
  @Override
  public LoginDBPutResponseBody updateEmployee(String login, LoginDBPutRequestBody requestBody,
      InternalJwt internalJwt) {
    log.debug(IN);

    LoginDBPutResponseBody responseBody = new LoginDBPutResponseBody();
    responseBody.setRolesAdded(new ArrayList<>());
    responseBody.setRolesRemoved(new ArrayList<>());

    EmployeeEntity employeeExists = employeeRepository.findByLogin(login).orElse(null);
    HttpStatus httpStatus = new HttpStatus();
    responseBody.setHttpStatus(httpStatus);
    EmployeeForAudit oldState = null;
    EmployeeForAudit newState = null;
    try {
      if (null == employeeExists) {
        log.error("Employee with id '" + login + "' do not exists.");
        httpStatus.setMessage("Employee not found");
        httpStatus.setCode(SC_NOT_FOUND);
      } else {
        final boolean ownIdentity = Objects
            .equals(myLogin(internalJwt), employeeExists.getLogin());
        if (ownIdentity) {
          httpStatus.setMessage("Update of own user details is not permitted");
          httpStatus.setCode(SC_UNAUTHORIZED);
          return responseBody;
        }
        oldState = fetchEmployeeForAudit(employeeExists.getId());
        String encodedPassword = null;
        if (!StringUtils.isEmpty(requestBody.getPassword())) {
          final boolean admin = amIPermitted(internalJwt, "employee.passwords",
              "edit");
          if (!admin) {
            httpStatus.setMessage("Admin role is required for user password change");
            httpStatus.setCode(SC_UNAUTHORIZED);
            return responseBody;
          }
          if (isPasswordIncorrect(login, httpStatus, requestBody.getPassword())) {
            return responseBody;
          }
          encodedPassword = employeePasswordEncoder.encode(requestBody.getPassword());
          employeeExists.setPassword(encodedPassword);
          employeeExists.setFailedLoginAttempts(0);
          employeeExists.setPasswordChangeRequired(!ownIdentity);
          setExpireDate(employeeExists);
          employeeExists.setStatus(EmployeeStatus.active);
        }
        if (!requestBody.getTitle().isEmpty()) {
          employeeExists.setTitle(requestBody.getTitle());
        }
        if (!requestBody.getFirstName().isEmpty()) {
          employeeExists.setFirstName(requestBody.getFirstName());
        }
        if (!requestBody.getFamilyName().isEmpty()) {
          employeeExists.setFamilyName(requestBody.getFamilyName());
        }

        if (!requestBody.getEmail().isEmpty()) {
          if (null == employeeExists.getEmail()) { // If user do not have email, save it
            validateEmail(requestBody.getEmail());
            employeeExists.setEmail(requestBody.getEmail());
          } else {
            if (!employeeExists.getEmail().equals(requestBody.getEmail())) { // if user already use
              // this email, do not
              // update it
              try {
                validateEmail(requestBody.getEmail());
              } catch (EmployeeEmailValidatorException e) {
                httpStatus.setCode(SC_BAD_REQUEST);
                httpStatus.setMessage(e.getMessage());
                return responseBody;
              }
              employeeExists.setEmail(requestBody.getEmail());
            }
          }
        }
        if (!requestBody.getPhone().isEmpty()) {
          validatePhone(requestBody.getPhone());
          employeeExists.setPhone(requestBody.getPhone());
        }
        if (!requestBody.getStatus().isEmpty()) {
          boolean sameStatus = Objects
              .equals(requestBody.getStatus(), employeeExists.getStatus().toString());
          EmployeeStatus newStatus = EmployeeStatus.valueOf(requestBody.getStatus());
          employeeExists.setStatus(newStatus);
          if (!sameStatus && EmployeeStatus.active.equals(newStatus)) {
            employeeExists.setFailedLoginAttempts(0);
          }
        }
        EmployeeEntity savedEmployee = employeeRepository.save(employeeExists);
        if (null != encodedPassword) {
          updatePasswordHistory(savedEmployee, encodedPassword);
        }

        List<EmployeeRoleOperation> changedRoleAssignments =
            identifyEmployeeRoleChanges(savedEmployee, requestBody.getRoles());
        handleChangedRoleAssignments(changedRoleAssignments, responseBody.getRolesAdded(),
            responseBody.getRolesRemoved());

        httpStatus.setMessage("SUCCESS");
        httpStatus.setCode(SC_OK);
        newState = fetchEmployeeForAudit(savedEmployee.getId());
        log.info("Employee was updated successfully.");
      }
    } catch (EmployeePhoneValidatorException | EmployeeEmailValidatorException e) {
      httpStatus.setCode(SC_BAD_REQUEST);
      httpStatus.setMessage(e.getMessage());
    } catch (ParseException e) {
      httpStatus.setMessage("Invalid data format");
      httpStatus.setCode(SC_BAD_REQUEST);
    } catch (EntityNotFoundException e) {
      if (PersistenceRoleBrief.class.equals(e.getEntityClass())) {
        httpStatus.setMessage(String.format("Role %s not found", e.getSearchKey()));
        httpStatus.setCode(SC_BAD_REQUEST);
      } else {
        httpStatus.setMessage("Not found");
        httpStatus.setCode(SC_NOT_FOUND);
      }
      log.error("Not found: " + e.getMessage(), e);

    } catch (Exception e) {
      httpStatus.setCode(SC_INTERNAL_SERVER_ERROR);
      log.error("Exception in saving data: " + e.getMessage(), e);
    } finally {
      log.debug(OUT);
      final String msg =
          Objects.isNull(newState) ? String
              .format("Update employee with ID %s Failed.", login)
              : String.format("Employee updated with ID %s Successfully", newState.getLogin());
      publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
          Objects.isNull(oldState) ? new EmployeeForAudit() : oldState,
          Objects.isNull(newState) ? new EmployeeForAudit() : newState,
          ObjectType.EMPLOYEE_MANAGEMENT, EventAction.MODIFY_EMPLOYEE,
          msg);
    }
    return responseBody;
  }

  private void validatePhone(String phone) throws EmployeePhoneValidatorException {
    new EmployeePhoneValidator(messages).validate(phone);
  }

  private boolean isPasswordIncorrect(String login, HttpStatus httpStatus, String password) {
    try {
      this.employeePasswordUtility.validate(login,
          password);
    } catch (EmployeePasswordCriteriaException e) {
      httpStatus.setCode(SC_BAD_REQUEST);
      httpStatus.setMessage(e.getMessage());
      return true;
    } catch (EmployeePasswordHistoryException e) {
      httpStatus.setCode(SC_CONFLICT);
      httpStatus.setMessage(e.getMessage());
      return true;
    }
    return false;
  }

  private void handleChangedRoleAssignments(List<EmployeeRoleOperation> changedRoleAssignments,
      List<String> rolesAdded, List<String> rolesRemoved) {
    Date now = new Date();
    for (EmployeeRoleOperation ero : changedRoleAssignments) {
      EmployeeRoleEntity ere = ero.getEntity();
      if (EntityOperation.add.equals(ero.getOperation())) {
        ere.setActive(false);
        if (ere.getRole().getFunctionGroupId() != null) {
          if (isDateWithin(now, ere.getDateFrom(), ere.getDateTo())) {
            ere.setActive(true);
            rolesAdded.add(ere.getRole().getFunctionGroupId());
          }
        }
        employeeRoleRepository.save(ere);
      } else if (EntityOperation.remove.equals(ero.getOperation())) {
        employeeRoleRepository.delete(ere);
        if (ere.getRole().getFunctionGroupId() != null) {
          rolesRemoved.add(ere.getRole().getFunctionGroupId());
        }
      } else if (EntityOperation.modify.equals(ero.getOperation())) {
        ere.setActive(false);
        if (isDateWithin(now, ere.getDateFrom(), ere.getDateTo())) {
          ere.setActive(true);
        }
        employeeRoleRepository.save(ere);
      }
    }
  }

  private void validateEmail(String email) throws EmployeeEmailValidatorException {
    new EmployeeEmailValidator(messages, employeeRepository).validate(email);
  }

  private void updatePasswordHistory(EmployeeEntity savedEmployee, String encodedPassword) {
    EmployeePasswordHistoryEntity employeePasswordHistory = new EmployeePasswordHistoryEntity();
    employeePasswordHistory.setEmployeeId(savedEmployee.getId());
    employeePasswordHistory.setPasswordDate(new Date());
    employeePasswordHistory.setPassword(encodedPassword);

    employeePasswordHistoryService.savePasswordToHistory(employeePasswordHistory);
  }

  @Override
  public IdentityDetails getIdentity(String login) {
    log.debug(IN);
    EmployeeEntity employee = employeeRepository.findByLogin(login).orElse(null);
    if (employee == null) {
      log.info("Employee with login '{}' was not found", login);
      return null;
    }
    IdentityDetails body = new IdentityDetails();
    body.setLogin(employee.getLogin());
    body.setPassword(employee.getPassword());
    body.setStatus(IdentityDetails.Status.fromValue(employee.getStatus().toString()));
    body.setBlockedDate(employee.getBlockedDate());
    body.setUnblockedDate(employee.getUnblockedDate());
    body.setGrantedAuthorities(getAuthorities(employee));
    body.setRoles(getRoles(employee));
    body.setPasswordStatus(detectPasswordStatus(employee));
    log.debug(OUT);
    return body;
  }

  private PasswordStatus detectPasswordStatus(EmployeeEntity employee) {
    if (employee.getCredentialExpires() != null && !employee.getCredentialExpires()
        .after(new Date())) {
      return PasswordStatus.PASSWORD_EXPIRED;
    }
    if (employee.getPasswordChangeRequired()) {
      return PasswordStatus.PASSWORD_CHANGE_REQUIRED;
    }
    return PasswordStatus.PASSWORD_VALID;
  }

  private List<String> getAuthorities(EmployeeEntity employee) {
    List<Integer> roleIds = employeeRoleRepository.findByEmployee(employee).stream()
        .map(e -> e.getRole().getId()).distinct().collect(Collectors.toList());
    return roleRepository.findAll(roleIds).stream().map(RoleEntity::getName)
        .collect(Collectors.toList());
  }

  private List<com.backbase.persistence.employeemanagement.Role> getRoles(
      EmployeeEntity employee) {
    return rolePersistenceService.getRolesForEmployee(employee).stream().map(e -> {
      com.backbase.persistence.employeemanagement.Role r =
          new com.backbase.persistence.employeemanagement.Role();
      r.setId(e.getId());
      r.setName(e.getName());
      r.setDescription(e.getDescription());
      if (e.getFunctions() != null) {
        r.setFunctions(e.getFunctions().stream().map(f -> {
          com.backbase.persistence.employeemanagement.Function mf =
              new com.backbase.persistence.employeemanagement.Function();
          mf.setId(f.getId());
          mf.setCode(f.getCode());
          mf.setName(f.getName());
          mf.setPrivileges(f.getPrivileges());
          return mf;
        }).collect(Collectors.toList()));
      }
      return r;
    }).collect(Collectors.toList());
  }

  @Override
  public HttpStatus updateIdentity(IdentityUpdate request,
      String login, InternalJwt internalJwt) {
    log.debug(IN);
    HttpStatus httpStatus = new HttpStatus().withCode(SC_NOT_FOUND).withMessage(login);
    EmployeeEntity employee = employeeRepository.findByLogin(login).orElse(null);
    if (employee != null) {
      EmployeeForAudit oldState = null;
      EmployeeForAudit newState = null;
      try {
        if (!StringUtils.isEmpty(request.getPassword())) {
          boolean ownIdentity = Objects.equals(myLogin(internalJwt), employee.getLogin());
          final boolean admin = amIPermitted(internalJwt, "employee.passwords",
              "edit");
          if (ownIdentity) {
            if (!employeePasswordEncoder.matches(
                StringUtils.defaultString(request.getCurrentPassword()),
                employee.getPassword())) {
              throw new UnauthorizedException("Old password not correct");
            }
          } else if (!admin) {
            throw new UnauthorizedException("Admin role is required for user password change");
          }
          if (!isPasswordIncorrect(login, httpStatus, request.getPassword())) {
            oldState = fetchEmployeeForAudit(employee.getId());
            String encodedPassword = employeePasswordEncoder.encode(request.getPassword());
            employee.setPassword(encodedPassword);
            employee.setPasswordChangeRequired(!ownIdentity);
            setExpireDate(employee);
            if (!ownIdentity) {
              employee.setFailedLoginAttempts(0);
              //reset password does not automatically change state to active employee.setStatus(EmployeeStatus.active);
            }
            employeeRepository.save(employee);
            updatePasswordHistory(employee, encodedPassword);
            newState = fetchEmployeeForAudit(employee.getId());
            httpStatus.withCode(SC_OK)
                .withMessage(org.springframework.http.HttpStatus.OK.getReasonPhrase());
          }
        }
      } catch (ResponseStatusException e) {
        httpStatus.withCode(e.getCode()).withMessage(e.getMessage());
      } finally {
        final String msg =
            Objects.isNull(newState) ? String
                .format("Update identity for employee with ID %s Failed.", login)
                : String.format("Identity for employee with ID %s updated Successfully",
                    newState.getLogin());
        publishAuditEvent(internalJwt.getClaimsSet().getSubject().orElse(null),
            Objects.isNull(oldState) ? new EmployeeForAudit() : oldState,
            Objects.isNull(newState) ? new EmployeeForAudit() : newState,
            ObjectType.EMPLOYEE_MANAGEMENT, EventAction.MODIFY_EMPLOYEE,
            msg);
      }
    }
    log.debug(OUT);
    return httpStatus;
  }

  @Override
  public EmployeesDBGetResponseBody getEmployeesDB() {
    log.debug(IN);
    List<Employee> employeesList =
        new ArrayList<>();
    List<EmployeeEntity> employeeList = employeeRepository.findAll();
    if (employeeList.isEmpty()) {
      log.info("No Employees found");
    }
    for (EmployeeEntity emp : employeeList) {
      Employee employeeDetails = new Employee();
      employeeDetails.setId(emp.getId());
      employeeDetails.setLogin(emp.getLogin());
      employeeDetails.setEmail(emp.getEmail());
      employeeDetails.setPhone(emp.getPhone());
      employeeDetails.setFailedLoginAttempts(emp.getFailedLoginAttempts());
      employeeDetails.setFirstName(emp.getFirstName());
      employeeDetails.setFamilyName(emp.getFamilyName());
      employeeDetails.setTitle(emp.getTitle());
      employeeDetails.setStatus(emp.getStatus().toString());
      employeesList.add(employeeDetails);
    }
    EmployeesDBGetResponseBody employeesDBGetResponseBody = new EmployeesDBGetResponseBody();
    Data data = new Data();
    data.setEmployees(employeesList);
    employeesDBGetResponseBody.setData(data);

    HttpStatus setHttpStatus = new HttpStatus();
    setHttpStatus.setMessage("SUCCESS");
    setHttpStatus.setCode(SC_OK);

    employeesDBGetResponseBody.setHttpStatus(setHttpStatus);
    log.debug(OUT);
    return employeesDBGetResponseBody;
  }

  @Override
  public PersistenceEmployeeBriefList getBriefDB(
      PersistenceOrderedPagedFiltered request) {
    log.debug(IN);
    Page<EmployeeEntity> page = null;
    List<EmployeeEntity> list = null;
    Sort sort = null;
    Pageable pageable = null;
    Example<EmployeeEntity> example = null;
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
        page = employeeRepository.findAll(example, pageable);
      } else if (!Objects.isNull(sort)) {
        list = employeeRepository.findAll(example, sort);
      } else {
        list = employeeRepository.findAll(example);
      }
    } else if (!Objects.isNull(pageable)) {
      page = employeeRepository.findAll(pageable);
    }
    if (!Objects.isNull(page)) {
      list = page.getContent();
    }
    if (Objects.isNull(list)) {
      list = employeeRepository.findAll();
    }
    List<PersistenceEmployeeBrief> employees = list.stream()
        .map(e -> mapBrief(e, null, null))
        .collect(Collectors.toList());
    PersistenceEmployeeBriefList response = new PersistenceEmployeeBriefList();
    response.setEmployees(employees);
    response.setPage(createPage(pageable, page, employees));
    log.debug(OUT);
    return response;
  }

  private Example<EmployeeEntity> createExample(List<PersistenceFilterBy> filterBy) {
    Map<String, String> mapped = filterBy.stream()
        .collect(Collectors.toMap(PersistenceFilterBy::getProperty, PersistenceFilterBy::getValue));
    EmployeeEntity e = new EmployeeEntity();
    e.setId(mapped.containsKey("id") ? Integer.valueOf(mapped.get("id")) : null);
    e.setLogin(mapped.get("login"));
    e.setFirstName(mapped.get("firstName"));
    e.setFamilyName(mapped.get("familyName"));
    e.setStatus(mapped.containsKey("status") ? EmployeeStatus.valueOf(mapped.get("status")) : null);
    return Example.of(e, ExampleMatcher.matchingAll().withStringMatcher(StringMatcher.EXACT));
  }

  @Override
  public EmployeePersistenceValidationPostResponseBody validate(
      EmployeePersistenceValidationPostRequestBody requestBody) {
    log.debug(IN);
    if (Property.LOGIN.equals(requestBody.getProperty())) {
      if (StringUtils.isBlank(requestBody.getValue())) {
        return new EmployeePersistenceValidationPostResponseBody().withResult(Result.BAD_REQUEST);
      }
      return new EmployeePersistenceValidationPostResponseBody().withResult(
          employeeRepository.findByLogin(requestBody.getValue()).isPresent() ? Result.INVALID
              : Result.VALID);
    }
    EmployeePersistenceValidationPostResponseBody employeePersistenceValidationPostResponseBody = new EmployeePersistenceValidationPostResponseBody()
        .withResult(Result.BAD_REQUEST);
    log.debug(OUT);
    return employeePersistenceValidationPostResponseBody;
  }

  @Override
  public PersistenceEmployeeRolePendingAssignments getEmployeeRolePendingAssignments() {
    PersistenceEmployeeRolePendingAssignments assignments = new PersistenceEmployeeRolePendingAssignments();
    employeeRoleRepository.findAll().stream().filter(ere -> !Objects.isNull(change(ere)))
        .forEach(ere -> assignments.getAssignments().add(
            new PersistenceEmployeeRolePendingAssignment()
                .withEmployee(mapBrief(ere.getEmployee(), ere.getDateFrom(), ere.getDateTo()))
                .withRole(mapBrief(ere.getRole(), null, null))
                .withChange(change(ere))));
    return assignments;
  }

  private Change change(EmployeeRoleEntity employeeRole) {
    final boolean active = Objects.isNull(employeeRole.getActive()) || employeeRole.getActive();
    final boolean inRange = isDateWithin(new Date(), employeeRole.getDateFrom(),
        employeeRole.getDateTo());
    return (inRange && !active) ? Change.ADD : ((!inRange && active) ? Change.REMOVE : null);
  }

  @Override
  public LoginDBGetResponseBody getUserDetailsByIdDB(String login) {
    log.debug(IN);
    EmployeeEntity employee = employeeRepository.findByLogin(login).orElse(null);
    if (employee == null) {
      return null;
    }
    LoginDBGetResponseBody body = new LoginDBGetResponseBody();
    body.setId(employee.getId());
    body.setLogin(employee.getLogin());
    body.setTitle(employee.getTitle());
    body.setEmail(employee.getEmail());
    body.setPhone(employee.getPhone());
    body.setFailedLoginAttempts(employee.getFailedLoginAttempts());
    body.setFirstName(employee.getFirstName());
    body.setFamilyName(employee.getFamilyName());
    body.setStatus(employee.getStatus().toString());
    body.setFailedLoginAttempts(employee.getFailedLoginAttempts());
    body.setRoles(employeeRoleRepository.findByEmployee(employee).stream()
        .map(e -> mapBrief(e.getRole(), e.getDateFrom(), e.getDateTo()))
        .collect(Collectors.toList()));
    log.debug(OUT);
    return body;
  }

  private boolean amIPermitted(InternalJwt internalJwt, String functionCode,
      String privilege) {
    if (Objects.isNull(internalJwt)) {
      return false;
    }
    List<FunctionPrivilege> functionPrivileges = getFunctionPrivilegesForUser(
        internalJwt.getClaimsSet().getSubject().orElse(null));
    for (FunctionPrivilege fp : functionPrivileges) {
      if (Objects.equals(functionCode, fp.getFunctionCode()) && Objects
          .equals(privilege, fp.getPrivilege())) {
        return true;
      }
    }
    return false;
  }

  private List<FunctionPrivilege> getFunctionPrivilegesForUser(String login) {
    EmployeeEntity employee = employeeRepository.findByLogin(login).orElse(null);
    if (employee != null) {
      Date now = Date.from(Instant.now());
      return employeeRoleRepository.findByEmployee(employee)
          .stream().filter(o -> DateTimeUtility.isDateWithin(now, o.getDateFrom(), o.getDateTo()))
          .map(EmployeeRoleEntity::getRole)
          .map(functionPersistenceService::findFunctionPrivilegeForRole)
          .reduce(new ArrayList<>(), (l1, l2) -> {
            l1.addAll(l2);
            return l1;
          });
    }
    return Collections.emptyList();
  }

}
