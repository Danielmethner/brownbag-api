package com.brownbag_api.model.jpa;

import java.io.Serializable;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "OBJ_BAL_SHEET_SECTION")
public class ObjFinStmtSection implements Serializable {

	private static final long serialVersionUID = 7386741814449599918L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = ObjFinStmt.class)
	@JoinColumn(name = "FIN_STMT_ID")
	@JsonBackReference
	private ObjFinStmt finStmt;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EFinStmtSectionType section;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotNull
	@Column(name = "FIN_YEAR", updatable = false)
	private int finYear;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double qty;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EFinStmtType finStmtType;

	public ObjFinStmtSection() {

	}

	public ObjFinStmtSection(@NotNull ObjFinStmt balSheet, EFinStmtSectionType eBalSheetSection, double qty,
			@NotNull EFinStmtType finStmtType) {
		super();
		this.finStmt = balSheet;
		this.section = eBalSheetSection;
		this.qty = qty;
		this.finYear = balSheet.getFinYear();
		this.name = balSheet.getParty().getName() + ": " + eBalSheetSection.getName() + ": " + "Year: "
				+ balSheet.getFinYear();
		this.finStmtType = finStmtType;
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

	public ObjFinStmt getFinStmt() {
		return finStmt;
	}

	public void setFinStmt(ObjFinStmt finStmt) {
		this.finStmt = finStmt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EFinStmtSectionType getSection() {
		return section;
	}

	public void setSection(EFinStmtSectionType section) {
		this.section = section;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public void increaseQty(double qty) {
		this.qty = this.qty + qty;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

	public void setFinStmtType(EFinStmtType finStmtType) {
		this.finStmtType = finStmtType;
	}

}
