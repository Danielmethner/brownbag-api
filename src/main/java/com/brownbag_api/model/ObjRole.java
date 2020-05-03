package com.brownbag_api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ROLE", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
public class ObjRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 20)
	private String name;

	public ObjRole(String name) {
		this.name = name;
	}

	public ObjRole() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@Override
//	public String toString() {
//		return "Role [id=" + id + ", name=" + name + "]";
//	}

}
