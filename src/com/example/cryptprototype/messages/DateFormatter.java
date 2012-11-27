package com.example.cryptprototype.messages;

import java.util.Date;

public class DateFormatter {

	public static String getDay(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(8, 10);
	}

	public static String getDayOfWeek(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(0, 3);
	}

	public static String getMonth(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(4, 7);
	}

	public static String getTime(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(11, 19);
	}	

	public static String getYear(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(date.length()-4);
	}

	//---------------------------------------------------------------------------------

	public static String getHour(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(11, 13);
	}

	public static String getMinute(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(14, 16);
	}

	public static String getSecond(String date) {
		if (null == date)
			date = new Date().toString();
		return date.substring(17, 19);
	}

	//---------------------------------------------------------------------------------

	public static String getMonthNum(String date) {
		if (null == date)
			date = new Date().toString();
		String mon = getMonth(date);
		if (mon.equals("Jan"))
			return "01";
		if (mon.equals("Feb"))
			return "02";
		if (mon.equals("Mar"))
			return "03";
		if (mon.equals("Apr"))
			return "04";
		if (mon.equals("May"))
			return "05";
		if (mon.equals("Jun"))
			return "06";
		if (mon.equals("Jul"))
			return "07";
		if (mon.equals("Aug"))
			return "08";
		if (mon.equals("Sep"))
			return "09";
		if (mon.equals("Oct"))
			return "10";
		if (mon.equals("Nov"))
			return "11";
		if (mon.equals("Dec"))
			return "12";
		return "??";
	}

	public static String getStandardDate(String date) {
		if (null == date)
			date = new Date().toString();
		return (getMonthNum(date) + "/" + getDay(date) + "/" + getYear(date));
	}

	public static String getStandardTime(String date) {
		if (null == date)
			date = new Date().toString();
		int hour = toInt(getHour(date));
		if (hour < 12)
			return (getStandardHour(hour) + ":" + getMinute(date) + " AM");
		else
			return (getStandardHour(hour) + ":" + getMinute(date) + " PM");
	}

	public static String getStandardHour(int hour) {
		switch (hour) {
			case 0:
				return "12";
			case 1:
				return "01";
			case 2:
				return "02";
			case 3:
				return "03";
			case 4:
				return "04";
			case 5:
				return "05";
			case 6:
				return "06";
			case 7:
				return "07";
			case 8:
				return "08";
			case 9:
				return "09";
			case 10:
				return "10";
			case 11:
				return "11";
			case 12:
				return "12";
			case 13:
				return "01";
			case 14:
				return "02";
			case 15:
				return "03";
			case 16:
				return "04";
			case 17:
				return "05";
			case 18:
				return "06";
			case 19:
				return "07";
			case 20:
				return "08";
			case 21:
				return "09";
			case 22:
				return "10";
			case 23:
				return "11";
			default:
				return "??";
		}
	}

	public static String getAMPM(String date) {
		if (null == date)
			date = new Date().toString();
		if (toInt(getHour(date)) < 12)
			return "AM";
		else
			return "PM";
	}

	//---------------------------------------------------------------------------------

	private static int toInt(String num) {
		int result = 0;
		int place = 1;
		for (int i = num.length()-1; i > -1; i--) {
			int c = num.charAt(i);
			if (isDigit(c)) {
				result += (c-48) * place;
				place = place * 10;
			}
		}
		return result;
	}

	private static boolean isDigit(int c) {
		return (c > 47 && c < 58);
	}

}