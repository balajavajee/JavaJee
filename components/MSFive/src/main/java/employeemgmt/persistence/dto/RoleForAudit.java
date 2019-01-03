package com.yoma.adminportal.employeemgmt.persistence.dto;

import com.yoma.auditservice.changeset.AuditPropertyLabelProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class RoleForAudit implements AuditPropertyLabelProvider {

  @Override
  public Map<String, String> getAuditPropertyLabels() {
    return Arrays.stream(PropertyLabels.values())
        .collect(Collectors.toMap(Enum::name, PropertyLabels::getLabel));
  }

  List<String> employees = new ArrayList<>();
  List<String> functions = new ArrayList<>();
  private Integer id;
  private String name;
  private String description;
  private String functionGroupId;

  private enum PropertyLabels {
    id("Role ID"),
    name("Name"),
    description("Decription"),
    functionGroupId("Function group"),
    employees("Employees"),
    functions("Functions");

    private final String label;

    PropertyLabels(String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }

  }

}
