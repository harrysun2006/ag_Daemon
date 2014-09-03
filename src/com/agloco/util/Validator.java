package com.agloco.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author terry_zhao
 *
 */
public class Validator{

	private final static Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	
	public static boolean isValidEmailAddress(String emailAddress){
	
		Matcher m = p.matcher(emailAddress);
		if(m.matches()){
			return com.liferay.util.Validator.isEmailAddress(emailAddress);
		}
		return false;
	}
	
}
