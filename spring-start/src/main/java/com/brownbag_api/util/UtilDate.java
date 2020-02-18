package com.brownbag_api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilDate {
	
	// Date Formats
	public static SimpleDateFormat dateFormatAPI = new SimpleDateFormat("yyyyMMMddHH24mmss");
	public static SimpleDateFormat dateFormatSQLTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormatSQLDate = new SimpleDateFormat("yyyy-mm-dd");
	
	public static Calendar cal() {
		Calendar cal = new GregorianCalendar();
		return cal;
	}

	public static String dateAsSQLTimeStamp(Date date) {
		return dateFormatSQLTimeStamp.format(date);
	}
	
	public static Date now() {
		return Calendar.getInstance().getTime();
	}
	
	public static String nowAsSQLTimeStamp() {
		return dateFormatSQLTimeStamp.format(now());
	}
	
	public static String nowAsAPITimeStamp() {
		return dateFormatSQLTimeStamp.format(now());
	}

	public static Date minDate = new GregorianCalendar(1000, 0, 1).getTime();
	public static Date maxDate = new GregorianCalendar(3000, 11, 1).getTime();
	
	
}
