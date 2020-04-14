package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "BAL_SHEET")
public class BalSheet implements Serializable {

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
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "PARTY_ID")
	@JsonBackReference
	private Party party;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", updatable = false)
	private Date timestampCreate;

	@NotNull
	@Column(name = "FIN_YEAR", updatable = false)
	private int finYear;

	public BalSheet() {
	}

	public BalSheet(@NotNull Party party, @NotNull int finYear) {
		super();
		this.name = "Balance Sheet for " + party.getName() + " as of Financial Year: " + finYear;
		this.party = party;
		this.finYear = finYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
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

}
