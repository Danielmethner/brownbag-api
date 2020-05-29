package com.brownbag_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public CtrlVar create(ECtrlVar eCtrlVar, LocalDateTime dateVal) {
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

	public CtrlVar create(ECtrlVar eCtrlVar, double valDbl) {
		if (ctrlVarRepo.findByKey(eCtrlVar.toString()) != null) {
			return null;
		}
		CtrlVar ctrlVar = new CtrlVar(eCtrlVar.getDataType(), eCtrlVar.getName(), eCtrlVar.toString(), null, null,
				valDbl, false);
		return ctrlVarRepo.save(ctrlVar);
	}

	public CtrlVar getByEnum(ECtrlVar eCtrlVar) {
		return ctrlVarRepo.findByKey(eCtrlVar.toString());
	}

	public LocalDateTime getFinDateDB() {
		LocalDateTime finDate = (LocalDateTime) getByEnum(ECtrlVar.FIN_DATE).getValDate();
		return finDate;
	}

	public void setFinDate() {
		LocalDateTime finDateLocal = getFinDateDB();
		UtilDate.setFinDate(finDateLocal);
	}

	public int setFinYear(int finYear) {
		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);
		LocalDateTime finDateLocal = ctrlVarFinDate.getValDate();
		finDateLocal = finDateLocal.plusYears(finYear - finDateLocal.getYear());
		ctrlVarFinDate.setValDate(finDateLocal);
		ctrlVarFinDate = ctrlVarRepo.save(ctrlVarFinDate);
		UtilDate.setFinDate(finDateLocal);
		return ctrlVarFinDate.getValDate().getYear();
	}

	// --------------------------------------------------------------
	// YEAR END PROCESSING
	// --------------------------------------------------------------
	public int switchFinYear() {
		int newFinYear = setFinYear(getFinYear() + 1);
		// TODO: Loan redemptions, Interest Payments, Revenue generation, cost
		// calculation
		return newFinYear;
	}

	public LocalDateTime getFinDate() {

		// ENSURE DATE IN CACHE IS IN SYNC WITH DB
		if (UtilDate.isFinDateSynced()) {
			return UtilDate.getFinDate();
		} else {
			LocalDateTime finDateDB = getFinDateDB();
			if (finDateDB != null) {
				UtilDate.setFinDateSynced(true);
				UtilDate.setFinDate(finDateDB);
				return finDateDB;
			} else {
				return null;
			}

		}
	}

	public int getFinYear() {
		return getFinDate().getYear();
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
