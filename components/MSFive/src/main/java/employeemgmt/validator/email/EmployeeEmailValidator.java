package com.yoma.adminportal.employeemgmt.validator.email;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeeEntity;
import com.yoma.adminportal.employeemgmt.persistence.repository.EmployeeRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.MessageSource;

public class EmployeeEmailValidator {

  private final MessageSource messages;
  private final EmployeeRepository employeeRepository;

  /**
   * @param messages use for getting located error messages
   */
  public EmployeeEmailValidator(MessageSource messages,
      EmployeeRepository employeeRepository) {
    super();
    this.messages = messages;
    this.employeeRepository = employeeRepository;
  }

  /**
   * Method will validate entered email, if match common email criteria.
   *
   * @param email to match
   * @throws EmployeeEmailValidatorException in case validation fail
   */
  public void validate(String email) throws EmployeeEmailValidatorException {
    if (EmailValidator.getInstance().isValid(email)) {
      EmployeeEntity employeeExists = employeeRepository.findByEmail(email).orElse(null);
      if (employeeExists != null) {
        throw new EmployeeEmailValidatorException(messages.getMessage(
            "email.is.already.use", null, "Email address is already in use", null));
      }
      return;
    }
    throw new EmployeeEmailValidatorException(messages.getMessage(
        "email.does.not.match.criteria", null, "Email does not match criteria", null));
  }
}