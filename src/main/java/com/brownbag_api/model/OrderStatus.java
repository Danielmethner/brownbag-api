package com.brownbag_api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ORDER_STATUS")
public class OrderStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 50)
	@Column(name = "INTL_ID")
	private String intlId;

	@NotBlank
	@Size(max = 50)
	private String name;
	
//	@OneToMany(mappedBy = "orderStatus")
//	@MapKey(name = "orderStatus")
//	public List<Order> orders;

	public OrderStatus() {
	}

	public OrderStatus(@NotBlank @Size(max = 50) String name, String intlId) {
		super();
		this.name = name;
		this.intlId = intlId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIntlId() {
		return intlId;
	}

	public void setIntlId(String intlId) {
		this.intlId = intlId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
