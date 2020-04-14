package com.brownbag_api.util;

import java.text.SimpleDateFormat;

public class UtilFormat {

	// Date Formats
	public static SimpleDateFormat dateFormatAPI = new SimpleDateFormat("yyyyMMMddHH24mmss");

	public static String genIntlId(String string) {
		return string.toLowerCase().replaceAll("\\p{Zs}", "_").replaceAll("\\.", "");

	}

	public static String extendString(String text, String symbol, Integer len) {
		while (text.length() < len) {
			text += symbol;
		}
		return text;
	}

	public static String capitalize(String str) {
		if (str == null) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
