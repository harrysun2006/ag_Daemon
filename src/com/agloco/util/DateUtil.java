package com.agloco.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author terry_zhao
 *
 */
public class DateUtil extends com.liferay.util.DateUtil{
	public final static String DATE_PATTERN_ALL = "z(Z) yyyy-MM-dd HH:mm:ss";
	public final static String DATE_PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_PATTERN_DAY = "yyyy-MM-dd";
	public final static String DATE_PATTERN_MONTH = "yyyyMM";
	
	private final static DateFormat DF_ALL = new SimpleDateFormat(DATE_PATTERN_ALL);
	private final static DateFormat DF_STANDARD = new SimpleDateFormat(DATE_PATTERN_STANDARD);
	private final static DateFormat DF_DAY = new SimpleDateFormat(DATE_PATTERN_DAY);
	private final static DateFormat DF_MONTH = new SimpleDateFormat(DATE_PATTERN_MONTH);
	
	
	private static String getDate2String(Date date,DateFormat df){
		if(date == null){
			return StringUtil.EMPTY_STRING;	
		}
		return df.format(date);
	}
	
	public static String getDate2String(Date date,String datePattern){
		
		if(DATE_PATTERN_ALL.equals(datePattern)){
			return getDate2String(date, DF_ALL);
		}
		else if(DATE_PATTERN_STANDARD.equals(datePattern)){
			return getDate2String(date, DF_STANDARD);
		}
		else if(DATE_PATTERN_DAY.equals(datePattern)){
			return getDate2String(date, DF_DAY);
		}
		
		return getDate2String(date, new SimpleDateFormat(datePattern));
		
	}
	/**
	 * add at 2007-05-08
	 * @param beginDate
	 * @param endDate
	 * @return months between (beginDate,endDate) pattern: "yyyyMM"
	 * 
	 */
	private static List betweenMonths(Calendar beginDate,Calendar endDate){
		
		if(beginDate == null || endDate == null){
			return null;
		}
		
		if(beginDate.after(endDate)){
			return null;
		}
		
		String beginMonth = getDate2String(beginDate.getTime(),DF_MONTH);
		String endMonth   = getDate2String(endDate.getTime(),DF_MONTH);
		if(beginMonth.equals(endMonth) || (Integer.parseInt(beginMonth) + 1 == Integer.parseInt(endMonth))){
			return null;
		}
		
		Calendar compareDate = Calendar.getInstance();
		compareDate.setTime(beginDate.getTime());
		String compareMonth = "";
		List list = new ArrayList();
		while(compareDate.before(endDate)){
			compareDate.add(Calendar.MONTH, 1);
			compareMonth = getDate2String(compareDate.getTime(),DF_MONTH);
			if(!compareMonth.equals(endMonth) && (Integer.parseInt(compareMonth) < Integer.parseInt(endMonth))){
				list.add(compareMonth);
			}
		}
		
		return list;
	}
}
