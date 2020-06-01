package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.enums.EFinStmtType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "OBJ_BAL_SHEET")
public class ObjFinStmt implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1773057542765188702L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotNull
	@ManyToOne(targetEntity = ObjParty.class)
	@JoinColumn(name = "PARTY_ID")
	@JsonBackReference
	private ObjParty party;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", updatable = false)
	private Date timestampCreate;

	@NotNull
	@Column(name = "FIN_YEAR", updatable = false)
	private int finYear;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EFinStmtType finStmtType;

	public ObjFinStmt() {
	}

	public ObjFinStmt(@NotNull ObjParty party, @NotNull int finYear, @NotNull EFinStmtType finStmtType) {
		super();
		this.finStmtType = finStmtType;
		this.name = finStmtType.getName() + " for " + party.getName() + " as of Financial Year: " + finYear;
		this.party = party;
		this.finYear = finYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjParty getParty() {
		return party;
	}

	public void setParty(ObjParty party) {
		this.party = party;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

	public void setFinStmtType(EFinStmtType finStmtType) {
		this.finStmtType = finStmtType;
	}

}
