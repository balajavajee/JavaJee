package com.yoma.adminportal.employeemgmt.persistence.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  private String name;

  @Column
  private String description;

  @Column(name = "cxs_role")
  private boolean cxsRole = false;

  @Column(name = "function_group_id", unique = true)
  private String functionGroupId;

  @Column(name = "automatic", nullable = false)
  private boolean automatic = false;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCxsRole() {
    return cxsRole;
  }

  public void setCxsRole(boolean cxsRole) {
    this.cxsRole = cxsRole;
  }

  public String getFunctionGroupId() {
    return functionGroupId;
  }

  public void setFunctionGroupId(String functionGroupId) {
    this.functionGroupId = functionGroupId;
  }

  public boolean isAutomatic() {
    return automatic;
  }

  public void setAutomatic(boolean automatic) {
    this.automatic = automatic;
  }
}
