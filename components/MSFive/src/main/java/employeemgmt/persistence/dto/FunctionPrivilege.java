package com.yoma.adminportal.employeemgmt.persistence.dto;

public class FunctionPrivilege {

  private final String functionCode;
  private final String functionName;
  private final String privilege;

  public FunctionPrivilege(String functionCode, String function, String privilege) {
    this.functionCode = functionCode;
    this.functionName = function;
    this.privilege = privilege;
  }

  public String getFunctionCode() {
    return functionCode;
  }

  public String getFunctionName() {
    return functionName;
  }

  public String getPrivilege() {
    return privilege;
  }

}
