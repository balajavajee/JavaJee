package com.yoma.adminportal.employeemgmt.persistence.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_function_privilege")
public class RoleFunctionPrivilegeEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private RoleEntity role;

  @ManyToOne
  @JoinColumn(name = "function_privilege_id", nullable = false)
  private FunctionPrivilegeEntity functionPrivilege;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public RoleEntity getRole() {
    return role;
  }

  public void setRole(RoleEntity role) {
    this.role = role;
  }

  public FunctionPrivilegeEntity getFunctionPrivilege() {
    return functionPrivilege;
  }

  public void setFunctionPrivilege(FunctionPrivilegeEntity functionPrivilege) {
    this.functionPrivilege = functionPrivilege;
  }

}
