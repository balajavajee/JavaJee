package com.yoma.adminportal.employeemgmt.persistence.dto;

import com.yoma.auditservice.changeset.AuditPropertyLabelProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class EmployeeForAudit implements AuditPropertyLabelProvider {

  private String title;
  private String firstname;
  private String familyname;
  private String status;
  private String login;
  private String email;
  private String passwordChangeRequired;
  private String phone;
  private String credentialExpires;
  private String lastLogin;
  private String blockedDate;
  private String unblockedDate;
  private Integer failedLoginAttempts;

  List<String> roles = new ArrayList<>();

  @Override
  public Map<String, String> getAuditPropertyLabels() {
    return Arrays.stream(PropertyLabels.values())
        .collect(Collectors.toMap(Enum::name, PropertyLabels::getLabel));
  }

  private enum PropertyLabels {
    title("Title"),
    firstname("First Name"),
    familyname("Family Name"),
    status("Status"),
    login("Login"),
    roles("Roles"),
    email("Email"),
    passwordChangeRequired("Password Change Required"),
    phone("Phone"),
    credentialExpires("Credential Expires"),
    lastLogin("Last Logged In"),
    blockedDate("Blocked Date"),
    unblockedDate("Unblocked Date"),
    failedLoginAttempts("Failed Login Attempts");

    private final String label;

    PropertyLabels(String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }

  }
}
