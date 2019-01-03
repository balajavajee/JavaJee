package com.yoma.adminportal.employeemgmt.validator.password;

import com.yoma.adminportal.employeemgmt.persistence.model.EmployeePasswordHistoryEntity;
import com.yoma.adminportal.employeemgmt.service.EmployeePasswordHistoryService;
import com.yoma.adminportal.employeemgmt.service.impl.EmployeePasswordEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EmployeePasswordUtility {

  private final EmployeePasswordHistoryService passwordHistoryService;
  private final MessageSource messages;

  private final LengthRule lengthRule;
  private final List<CharacterRule> generatorRules;
  private final List<Rule> validatorRules;

  /**
   * @param passwordHistoryService service for password history
   * @param messages use for getting located error messages
   */
  public EmployeePasswordUtility(EmployeePasswordHistoryService passwordHistoryService,
      MessageSource messages) {
    super();
    this.passwordHistoryService = passwordHistoryService;
    this.messages = messages;
    this.generatorRules = Arrays.asList(
        new CharacterRule(EnglishCharacterData.LowerCase, 2),
        new CharacterRule(EnglishCharacterData.UpperCase, 2),
        new CharacterRule(EnglishCharacterData.Digit, 2),
        new CharacterRule(new CustomSpecialCharacterData(), 2));
    this.lengthRule = new LengthRule(8, 12);
    this.validatorRules = new ArrayList<>(this.generatorRules);
    this.validatorRules.add(lengthRule);

  }

  /**
   * Generate the password based on given rules
   */
  public String generate() {
    return new PasswordGenerator().generatePassword(lengthRule.getMinimumLength(), generatorRules);
  }

  /**
   * Password Complexity Criteria is minimum of 8 characters and maximum of 12 characters - private
   * boolean matchLength(String password) with must have at-least 2 numbers, 2 small alphabets, 2
   * capital alphabets, 2 special characters - private boolean matchSpecial(String password) The
   * system should not accept the last 3 used passwords.
   *
   * @param login If null, check last 3 password is ignored
   * @param password user password
   * @throws EmployeePasswordCriteriaException in case validation fail
   */
  public void validate(String login, String password)
      throws EmployeePasswordCriteriaException, EmployeePasswordHistoryException {
    if (!matchRequiredFormat(password)) {
      throw new EmployeePasswordCriteriaException(messages.getMessage(
          "password.does.not.match.criteria", null, "Password does not match criteria", null));
    }
    if (!validateLast3(login, password)) {
      throw new EmployeePasswordHistoryException(messages.getMessage(
          "password.was.already.used", null, "Password was already used", null));
    }
  }

  private boolean matchRequiredFormat(String password) {
    return new PasswordValidator(validatorRules).validate(new PasswordData(password)).isValid();
  }

  private boolean validateLast3(String login, String password) {
    if (Objects.isNull(login)) {
      return true;
    }
    PasswordEncoder passwordEncoder = new EmployeePasswordEncoder();
    Collection<EmployeePasswordHistoryEntity> allPasswords =
        passwordHistoryService.getByLogin(login);
    List<String> last3 = allPasswords.stream().sorted((ph1, ph2) -> {
      return ph2.getPasswordDate().compareTo(ph1.getPasswordDate()); // reverse order
    }).limit(3).map(EmployeePasswordHistoryEntity::getPassword).collect(Collectors.toList());
    for (String p : last3) {
      if (passwordEncoder.matches(password, p)) {
        return false;
      }
    }
    return true;
  }

}
