package com.yoma.adminportal.employeemgmt.service.impl;

import static com.yoma.adminportal.utilities.datetime.DateTimeUtility.dateToString;

import com.backbase.buildingblocks.jwt.internal.token.InternalJwt;
import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.model.RoleEntity;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceEmployeeBrief;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistencePage;
import com.yoma.adminportal.employeemgmt.persistence.rest.PersistenceRoleBrief;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PersistenceUtilities {

  public static PersistenceEmployeeBrief mapBrief(EmployeeEntity employee, Date dateFrom,
      Date dateTo) {
    if (Objects.isNull(employee)) {
      return null;
    }
    return new PersistenceEmployeeBrief()
        .withId(employee.getId())
        .withLogin(employee.getLogin())
        .withName(getEmployeeName(employee))
        .withDateFrom(dateToString(dateFrom))
        .withDateTo(dateToString(dateTo));
  }

  public static PersistenceRoleBrief mapBrief(RoleEntity role, Date dateFrom, Date dateTo) {
    if (Objects.isNull(role)) {
      return null;
    }
    return new PersistenceRoleBrief()
        .withId(role.getId())
        .withName(role.getName())
        .withDateFrom(dateToString(dateFrom))
        .withDateTo(dateToString(dateTo))
        .withDescription(role.getDescription())
        .withFunctionGroupId(role.getFunctionGroupId());
  }

  /**
   * Get the employee name
   *
   * @param employee {@link EmployeeEntity}
   * @return nameof employee
   */
  public static String getEmployeeName(EmployeeEntity employee) {
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

  public static String myLogin(InternalJwt internalJwt) {
    return Objects.isNull(internalJwt) ? null
        : internalJwt.getClaimsSet().getSubject().orElse(null);
  }

  public static <T> PersistencePage createPage(Pageable pageable, Page<?> page,
      List<T> list) {
    Page<T> p =
        (Objects.isNull(pageable) || Objects.isNull(page)) ? new PageImpl<>(list)
            : new PageImpl<>(list, pageable, page.getTotalElements());
    return new PersistencePage().withNumber(p.getNumber())
        .withNumberOfElements(p.getNumberOfElements()).withSize(p.getSize())
        .withTotalElements(p.getTotalPages()).withTotalPages(p.getTotalPages());
  }

}
