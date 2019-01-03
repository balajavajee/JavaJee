package com.yoma.adminportal.employeemgmt.validator.password;

import org.passay.CharacterData;

public class CustomSpecialCharacterData implements CharacterData {

  @Override
  public String getErrorCode() {
    return "INSUFFICIENT_SPECIAL";
  }

  @Override
  public String getCharacters() {
    return "!@#$%^&*()_+-~[]{}.?':,";
  }

}
