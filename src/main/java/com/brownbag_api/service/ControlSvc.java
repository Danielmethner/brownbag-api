package com.brownbag_api.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.jpa.CtrlVar;
import com.brownbag_api.repo.CtrlVarRepo;
import com.brownbag_api.util.UtilDate;

@Service
public class ControlSvc {

	@Autowired
	private CtrlVarRepo ctrlVarRepo;

	@Autowired
	private LogSvc logSvc;

	public CtrlVar create(ECtrlVar eCtrlVar, LocalDate dateVal) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, dateVal, 0,
				false);
		return ctrlVarRepo.save(ctrlVar);
	}

	public CtrlVar create(ECtrlVar eCtrlVar, boolean valBool) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, null, 0,
				valBool);
		return ctrlVarRepo.save(ctrlVar);
	}

	public CtrlVar getByEnum(ECtrlVar eCtrlVar) {
		return ctrlVarRepo.findByKey(eCtrlVar.toString());
	}

	public LocalDate getFinDateDB() {
		LocalDate finDate = getByEnum(ECtrlVar.FIN_DATE).getValDate();
		return finDate;
	}
	public void setFinDate() { 
		LocalDate finDateLocal = getFinDateDB();
		UtilDate.setFinDate(finDateLocal);
	}

	public int setFinYear(int finYear) {
		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);
		LocalDate finDateLocal = ctrlVarFinDate.getValDate();
		finDateLocal = finDateLocal.plusYears(finYear - finDateLocal.getYear());
		ctrlVarFinDate.setValDate(finDateLocal);
		ctrlVarFinDate = ctrlVarRepo.save(ctrlVarFinDate);
		UtilDate.setFinDate(finDateLocal);
		return ctrlVarFinDate.getValDate().getYear();
	}
	
	public int incrFinYear() {
		return setFinYear(getFinYear() + 1);
	}
	
	public int getFinYear() {
		return UtilDate.getFinYear();
	}

	public double getIntrRate() {
		return 1.25;
	}

	public CtrlVar setVal(ECtrlVar eCtrlVar, boolean valBool) {
		CtrlVar ctrlVar = ctrlVarRepo.findByKey(eCtrlVar.toString());
		if (ctrlVar == null) {
			logSvc.write("Control Variable with key: '" + eCtrlVar.toString() + "' could not be found.");
		}
		ctrlVar.setValBool(valBool);
		return ctrlVarRepo.save(ctrlVar);
	}

	public List<CtrlVar> getAll() {
		return ctrlVarRepo.findAll();
	}

}
