package com.yoma.adminportal.employeemgmt.persistence.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class EmployeeEntity implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String title;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "family_name")
  private String familyName;

  @Column(unique = true)
  private String login;

  @Column
  private String password;

  @Column
  private String email;

  @Column(name = "password_change_required", nullable = false)
  private boolean passwordChangeRequired;

  @Column
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column
  private EmployeeStatus status;

  @Column(name = "credential_expires")
  private Date credentialExpires;

  @Column(name = "last_login")
  private Date lastLogin;

  @Column(name = "blocked_date")
  private Date blockedDate;

  @Column(name = "unblocked_date")
  private Date unblockedDate;

  @Column(name = "failed_login_attempts")
  private Integer failedLoginAttempts;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public EmployeeStatus getStatus() {
    return status;
  }

  public void setStatus(EmployeeStatus status) {
    this.status = status;
  }

  public Date getCredentialExpires() {
    return credentialExpires;
  }

  public void setCredentialExpires(Date credentialExpires) {
    this.credentialExpires = credentialExpires;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Date getBlockedDate() {
    return blockedDate;
  }

  public void setBlockedDate(Date blockedDate) {
    this.blockedDate = blockedDate;
  }

  public Date getUnblockedDate() {
    return unblockedDate;
  }

  public void setUnblockedDate(Date unblockedDate) {
    this.unblockedDate = unblockedDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public boolean getPasswordChangeRequired() {
    return passwordChangeRequired;
  }

  public void setPasswordChangeRequired(boolean passwordChangeRequired) {
    this.passwordChangeRequired = passwordChangeRequired;
  }

  public Integer getFailedLoginAttempts() {
    return failedLoginAttempts;
  }

  public void setFailedLoginAttempts(Integer failedLoginAttempts) {
    this.failedLoginAttempts = failedLoginAttempts;
  }

  @Override
  public String toString() {
    return "EmployeeEntity [id=" + id + ", title=" + title + ", firstName=" + firstName
        + ", familyName=" + familyName + ", login=" + login + ", password=" + password + ", email="
        + email + ", phone=" + phone + ", status=" + status + ", credentialExpires="
        + credentialExpires + ", lastLogin=" + lastLogin + ", blockedDate=" + blockedDate
        + ", unblockedDate=" + unblockedDate + ", failedLoginAttempts=" + failedLoginAttempts + "]";
  }

}
