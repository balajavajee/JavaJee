package com.yoma.adminportal.employeemgmt.persistence.model;

import java.io.Serializable;
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
@Table(name = "function_privilege_relation")
public class FunctionPrivilegeRelationEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "parent_id", nullable = false)
  private FunctionPrivilegeEntity parent;

  @ManyToOne
  @JoinColumn(name = "child_id", nullable = false)
  private FunctionPrivilegeEntity child;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public FunctionPrivilegeEntity getParent() {
    return parent;
  }

  public void setParent(FunctionPrivilegeEntity parent) {
    this.parent = parent;
  }

  public FunctionPrivilegeEntity getChild() {
    return child;
  }

  public void setChild(FunctionPrivilegeEntity child) {
    this.child = child;
  }

}
