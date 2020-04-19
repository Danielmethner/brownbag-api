package com.brownbag_api.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilBA {

	// Date Formats
	private static double intrRate = 1.25;

	public static double getIntrRate() {
		return intrRate;
	}

	public static void setIntrRate(double intrRate) {
		UtilBA.intrRate = intrRate;
	}

}
