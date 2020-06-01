package com.brownbag_api.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.jpa.CtrlVar;
import com.brownbag_api.repo.CtrlVarRepo;

@Service
public class ControlSvc {

	@Autowired
	private CtrlVarRepo ctrlVarRepo;

	@Autowired
	private LogSvc logSvc;

	// Date Formats
	public static SimpleDateFormat dateFormatAPI = new SimpleDateFormat("yyyyMMMddHH24mmss");
	public static SimpleDateFormat dateFormatSQLTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormatSQLDate = new SimpleDateFormat("yyyy-mm-dd");

	// Central Dates
	public static Date minDate = new GregorianCalendar(1000, 0, 1).getTime();
	public static Date maxDate = new GregorianCalendar(3000, 11, 1).getTime();

	private Calendar cal() {
		Calendar cal = new GregorianCalendar();
		return cal;
	}

	private String dateAsSQLTimeStamp(Date date) {
		return dateFormatSQLTimeStamp.format(date);
	}

	private Date now() {
		return Calendar.getInstance().getTime();
	}

	private String nowAsSQLTimeStamp() {
		return dateFormatSQLTimeStamp.format(now());
	}

	private String nowAsAPITimeStamp() {
		return dateFormatSQLTimeStamp.format(now());
	}

	public LocalDateTime getLastDayOfYear(LocalDateTime localDate) {
		return localDate.with(TemporalAdjusters.lastDayOfYear());
	}

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

	public int setFinYear(int finYear) {
		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);
		LocalDateTime finDateLocal = ctrlVarFinDate.getValDate();
		finDateLocal = finDateLocal.plusYears(finYear - finDateLocal.getYear());
		ctrlVarFinDate.setValDate(finDateLocal);
		ctrlVarFinDate = ctrlVarRepo.save(ctrlVarFinDate);
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
	
	public LocalDateTime getCurrentDate() {

		LocalDateTime finDate = new Date().toInstant().atZone(ZoneId.of("Asia/Manila")).toLocalDateTime();
		finDate = getLastDayOfYear(finDate);
		return finDate;
	}

	public LocalDateTime getFinDate() {

		CtrlVar ctrlVarFinDate = getByEnum(ECtrlVar.FIN_DATE);
		LocalDateTime finDateDB = null;
		if(ctrlVarFinDate != null) {
			finDateDB = ctrlVarFinDate.getValDate();	
		}
		
		
		if (finDateDB == null) {
			ctrlVarFinDate.setValDate(getCurrentDate());
			ctrlVarRepo.save(ctrlVarFinDate);
			return finDateDB;
		} else {

			return finDateDB;
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
