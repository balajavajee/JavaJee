package com.bala.accesscontrol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role extends BaseEntity {

	private static final long serialVersionUID = 2L;

	@Column(unique = true)
	private String name;

	@Column
	private String description;

	@Column(name = "cxs_role")
	private boolean cxsRole = false;

	@Column(name = "function_group_id", unique = true)
	private String functionGroupId;

/*	@Column(name = "automatic", nullable = false)
	private boolean automatic = false;*/

}
