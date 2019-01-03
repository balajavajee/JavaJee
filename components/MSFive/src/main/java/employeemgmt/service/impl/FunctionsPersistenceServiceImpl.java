package com.yoma.adminportal.employeemgmt.service.impl;

import com.backbase.persistence.employeemanagement.rest.spec.v1.functionsDB.Function;
import com.backbase.persistence.employeemanagement.rest.spec.v1.functionsDB.FunctionsDBGetResponseBody;
import com.yoma.adminportal.employeemgmt.persistence.dto.FunctionPrivilege;
import com.yoma.adminportal.employeemgmt.persistence.model.FunctionPrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleFunctionPrivilegeEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.FunctionPrivilegeRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.FunctionRepository;
import com.yoma.adminportal.employeemgmt.persistence.repository.RoleFunctionPrivilegeRepository;
import com.yoma.adminportal.employeemgmt.service.FunctionPersistenceService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FunctionsPersistenceServiceImpl implements FunctionPersistenceService {

  private final FunctionRepository functionRepository;

  private final FunctionPrivilegeRepository functionPrivilegeRepository;

  private final RoleFunctionPrivilegeRepository roleFunctionPrivilegeRepository;

  @Autowired
  public FunctionsPersistenceServiceImpl(FunctionRepository functionRepository,
      FunctionPrivilegeRepository functionPrivilegeRepository,
      RoleFunctionPrivilegeRepository roleFunctionPrivilegeRepository) {
    this.functionRepository = functionRepository;
    this.functionPrivilegeRepository = functionPrivilegeRepository;
    this.roleFunctionPrivilegeRepository = roleFunctionPrivilegeRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public FunctionsDBGetResponseBody getFunctionsDB(boolean includeHiddenFunctions) {
    Map<Integer, Function> mapped = new HashMap<>();
    functionRepository.findAll().forEach(fe -> {
      Function f = new Function();
      f.setId(fe.getId());
      f.setCode(fe.getCode());
      f.setName(fe.getName());
      f.setPrivileges(new ArrayList<>());
      mapped.put(fe.getId(), f);
    });
    functionPrivilegeRepository.findAll().stream()
        .filter(
            f -> includeHiddenFunctions || Objects.isNull(f.getUserVisible()) || f.getUserVisible())
        .forEach(fpe -> {
          Function f = mapped.get(fpe.getFunction().getId());
          final String privilege = fpe.getPrivilege().getCode();
          if (!f.getPrivileges().contains(privilege)) {
            f.getPrivileges().add(privilege);
          }
        });
    return new FunctionsDBGetResponseBody().withFunctions(
        mapped.values().stream().filter(f -> !f.getPrivileges().isEmpty())
            .collect(Collectors.toList()));
  }

  @Transactional(readOnly = true)
  @Override
  public List<FunctionPrivilege> findFunctionPrivilegeForRole(RoleEntity roleEntity) {
    Objects.requireNonNull(roleEntity);
    Map<String, FunctionPrivilege> functionPrivileges = new HashMap<>();
    for (RoleFunctionPrivilegeEntity rfpe : roleFunctionPrivilegeRepository
        .findByRole(roleEntity)) {
      loadFunctionPrivileges(functionPrivileges, rfpe.getFunctionPrivilege());
    }
    return new ArrayList<>(functionPrivileges.values());
  }

  private void loadFunctionPrivileges(Map<String, FunctionPrivilege> functionPrivileges,
      FunctionPrivilegeEntity functionPrivilegeEntity) {
    final String key = String.format("%s|%s", functionPrivilegeEntity.getFunction().getCode(),
        functionPrivilegeEntity.getPrivilege().getName());
    if (!functionPrivileges.containsKey(key)) {
      functionPrivileges.put(key,
          new FunctionPrivilege(functionPrivilegeEntity.getFunction().getCode(),
              functionPrivilegeEntity.getFunction().getName(),
              functionPrivilegeEntity.getPrivilege().getName()));
    }
  }

}

