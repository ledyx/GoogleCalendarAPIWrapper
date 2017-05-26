package io.github.xeyez.calendarexample;

import com.ibm.icu.util.ChineseCalendar;

import org.joda.time.DateTime;

public class LunarDateTime {
	
	private static final int YEAR_CORRECTION_VALUE = 2637;

	private LunarDateTime() {
	}
	
	/**
	 * 
	 * @return 오늘 음력날짜
	 */
	public static DateTime now() {
		ChineseCalendar cc = new ChineseCalendar();
		
		int year = cc.get(ChineseCalendar.EXTENDED_YEAR) - YEAR_CORRECTION_VALUE;
		int monthOfYear = cc.get(ChineseCalendar.MONTH) + 1;
		int dayOfMonth = cc.get(ChineseCalendar.DAY_OF_MONTH);
		
		int[] hms = getNowHMS();
		return new DateTime(year, monthOfYear, dayOfMonth, hms[0], hms[1], hms[2]);
	}
	
	public static DateTime solarToLunar(int solarYear, int solarMonthOfYear, int solarDayOfMonth) {
		
		int[] hms = getNowHMS();
		DateTime dt = new DateTime(solarYear, solarMonthOfYear, solarDayOfMonth, hms[0], hms[1], hms[2]);

		ChineseCalendar cc = new ChineseCalendar();
		cc.setTimeInMillis(dt.getMillis());
		
		int year = cc.get(ChineseCalendar.EXTENDED_YEAR) - YEAR_CORRECTION_VALUE;
		int monthOfYear = cc.get(ChineseCalendar.MONTH) + 1;
		int dayOfMonth = cc.get(ChineseCalendar.DAY_OF_MONTH);
		
		return new DateTime(year, monthOfYear, dayOfMonth, hms[0], hms[1], hms[2]);
	}
	
	public static DateTime lunar2Solar(int lunarYear, int lunarMonthOfYear, int lunarDayOfMonth) {
		ChineseCalendar cc = new ChineseCalendar();
		cc.set(ChineseCalendar.EXTENDED_YEAR, lunarYear + YEAR_CORRECTION_VALUE);
        cc.set(ChineseCalendar.MONTH, lunarMonthOfYear - 1);
        cc.set(ChineseCalendar.DAY_OF_MONTH, lunarDayOfMonth);

        int[] hms = getNowHMS();
        cc.set(ChineseCalendar.HOUR_OF_DAY, hms[0]);
        cc.set(ChineseCalendar.MINUTE, hms[0]);
        cc.set(ChineseCalendar.SECOND, hms[0]);
        
        return new DateTime(cc.getTimeInMillis());
	}
	
	private static int[] getNowHMS() {
		DateTime now = DateTime.now();
		return new int[] {now.getHourOfDay(), now.getMinuteOfHour(), now.getSecondOfMinute()};
	}
}