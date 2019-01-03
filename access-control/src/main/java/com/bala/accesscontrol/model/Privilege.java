package com.bala.accesscontrol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "privilege")
@Data
public class Privilege extends BaseEntity {
	private static final long serialVersionUID = 2L;

	@Column(unique = true)
	private String code;

	@Column(unique = true)
	private String name;

}
