package com.bala.accesscontrol.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_setting")
@Data
public class UserSetting  extends BaseEntity {

	private static final long serialVersionUID = 2L;

	@Column(unique = true)
	private String property;

	@Column
	private String value;

}
