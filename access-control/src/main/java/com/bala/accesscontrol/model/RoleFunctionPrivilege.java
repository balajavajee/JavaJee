/*package com.bala.accesscontrol.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "role_function_privilege")
@Data
public class RoleFunctionPrivilege extends Auditable {

	private static final long serialVersionUID = 2L;

	@ManyToOne
	@JoinColumn(name = "id", nullable = false)
	private Role role;

	@ManyToOne
	@JoinColumn(name = "function_privileges_id", nullable = true)
	private FunctionPrivilege functionPrivilege;

}
*/