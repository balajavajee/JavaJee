package com.bala.accesscontrol.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "user")
@Data
@AttributeOverride(name = "id", column = @Column(name = "user_id",  
nullable = false, columnDefinition = "BIGINT UNSIGNED")) 
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private String address;

	@Column
	private String city;

	@Column
	private String phoneNo;

	@Column
	private String email;

	@Column(name = "password_change_required", nullable = false)
	private boolean passwordChangeRequired;

	@Column
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column
	private UserStatus status;

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

}
