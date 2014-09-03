package com.agloco.util;

import com.agloco.Constants;
import com.liferay.portal.security.pwd.BasicToolkit;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.PwdGenerator;

public class RegExpToolkit extends BasicToolkit {

	public static String KEY = "ACDFGHJKLMNPQRSTUVWXY";

	public RegExpToolkit() {
		_pattern = PropsUtil.get(PropsUtil.PASSWORDS_REGEXPTOOLKIT_PATTERN);
		String length = PropsUtil.get(Constants.PASSWORD_LENGTH);
		try {
			_length = Integer.parseInt(length);
		} catch(NumberFormatException nfe) {
			_length = PASSWORD_DEFAULT_LENGTH;
		}
	}

	public String generate() {
		return PwdGenerator.getPassword(KEY, _length);
	}

	public boolean validate(String password) {
		return password.matches(_pattern);
	}

	private String _pattern;
	private int _length;
	private final int PASSWORD_DEFAULT_LENGTH = 6;

}