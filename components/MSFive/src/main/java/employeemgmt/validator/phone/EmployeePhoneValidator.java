package com.yoma.adminportal.employeemgmt.validator.phone;

import org.springframework.context.MessageSource;

public class EmployeePhoneValidator {

  private static final int PHONE_NUMBER_LENGTH = 15;
  private final MessageSource messages;

  /**
   * @param messages use for getting located error messages
   */
  public EmployeePhoneValidator(MessageSource messages) {
    super();
    this.messages = messages;
  }

  /**
   * Method will validate entered phone number, if match criteria - length of phone number is
   * exactly 15 character
   *
   * @param phone to match
   * @throws EmployeePhoneValidatorException in case validation fail
   */
  public void validate(String phone) throws EmployeePhoneValidatorException {
    if (matchPhone(phone)) {
      return;
    }
    throw new EmployeePhoneValidatorException(messages.getMessage(
        "phone.does.not.match.criteria", null, "Phone does not match criteria", null));
  }

  private boolean matchPhone(String phone) {
    return phone.length() == PHONE_NUMBER_LENGTH;
  }
}