package com.yoma.adminportal.employeemgmt.persistence.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Entity
@Table(name = "function_privilege")
public class FunctionPrivilegeEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "function_id", nullable = false)
  private FunctionEntity function;

  @ManyToOne
  @JoinColumn(name = "privilege_id", nullable = false)
  private PrivilegeEntity privilege;

  @Column(name = "user_visible")
  private Boolean userVisible;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public FunctionEntity getFunction() {
    return function;
  }

  public void setFunction(FunctionEntity function) {
    this.function = function;
  }

  public PrivilegeEntity getPrivilege() {
    return privilege;
  }

  public void setPrivilege(PrivilegeEntity privilege) {
    this.privilege = privilege;
  }

  public Boolean getUserVisible() {
    return userVisible;
  }

  public void setUserVisible(Boolean userVisible) {
    this.userVisible = userVisible;
  }

}
