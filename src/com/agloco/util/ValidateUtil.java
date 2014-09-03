package com.agloco.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import com.agloco.Constants;
import com.liferay.portal.util.PropsUtil;

public class ValidateUtil {
	
	public final static String DATE_FORMAT = "MM/dd/yyyy";

	public static boolean isPassword(String password){
		if (StringUtils.isBlank(password)) {
			return false;
		}

		if (password.length() < 6) {
			return false;
		}

//		char[] c = password.toCharArray();
//
//		for (int i = 0; i < c.length; i++) {
//			if ((!isChar(c[i])) &&
//				(!isDigit(c[i]))) {
//				return false;
//			}
//		}

		return true;
	
	}
	
	public static boolean isChar(char c) {
		return Character.isLetter(c);
	}
	
	public static boolean isDigit(char c) {
		int x = (int)c;

		if ((x >= 48) && (x <= 57)) {
			return true;
		}

		return false;
	}
	
	public static boolean isClearCacheAdmin(String userid)
	{
		if(PropsUtil.get(Constants.CLEARCACHEADMIN).equalsIgnoreCase(userid))
			return true;
		else
			return false;
	}
	
	//add at 2007-03-13 by Terry
	public static boolean isValidateDate(String strDate){
		
		if(StringUtils.isBlank(strDate)){
			return false;
		}
		
		try {
			
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date date = df.parse(strDate);
			if(date == null){
				return false;
			}
			
			String strDate2 = df.format(date);
			if(!strDate.equals(strDate2)){
				return false;
			}
			
			Calendar c = new GregorianCalendar();
			c.setTime(date);
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			int year = c.get(Calendar.YEAR);
			if(c.getTimeInMillis() > Calendar.getInstance().getTimeInMillis() || year > thisYear){
				return false;
			}
			
			if(thisYear - year > Constants.MAX_MEMBER_AGE){
				return false;
			}
			
		} catch (ParseException e) {
			return false;	
		}
		return true;
	}
	
}
