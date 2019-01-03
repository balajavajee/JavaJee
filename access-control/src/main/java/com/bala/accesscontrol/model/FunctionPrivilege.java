/*package com.bala.accesscontrol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "function_privilege")
@Data
public class FunctionPrivilege extends BaseEntity {
	private static final long serialVersionUID = 2L;
	
	@ManyToOne
	@JoinColumn(name = "function_id", nullable = false)
	private Function function;

	@ManyToOne
	@JoinColumn(name = "privilege_id", nullable = false)
	private Privilege privilege;

}*/