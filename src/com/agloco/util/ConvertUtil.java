package com.agloco.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;

/**
 * @author hysun
 * 
 */
public class ConvertUtil {

	/**
	 * some usually used date formats
	 */
	private static final String[] dateFormats = new String[] {
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd", "yyyyMM", };

	static {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
	}

	public static void noop() {
	}

	private ConvertUtil() {
	}

	/**
	 * return a java.text.DateFormat array using formats array<br>
	 * <b><i>Note: </i></b> java.text.DateFormat is not thread-safe
	 * 
	 * @param formats
	 *          Date string format array
	 * @return
	 */
	private static DateFormat[] getDateFormats(String[] formats) {
		DateFormat[] dfs = null;
		if (formats != null) {
			dfs = new DateFormat[formats.length];
			for (int i = 0; i < formats.length; i++)
				dfs[i] = new SimpleDateFormat(formats[i]);
		} else {
			dfs = new DateFormat[0];
		}
		return dfs;
	}

	/**
	 * convert an object value into java.sql.Date value
	 * 
	 * @param value
	 * @return
	 */
	public static java.sql.Date toSqlDate(Object value) {
		return toSqlDate(value, null, 0);
	}

	/**
	 * convert an object value into java.sql.Date value using given format
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static java.sql.Date toSqlDate(Object value, String format) {
		return toSqlDate(value, format, 0);
	}

	public static java.sql.Date toSqlDate(Object value, String format, int pos) {
		Date d = toDate(value, format, pos);
		return (d == null) ? null : new java.sql.Date(d.getTime());
	}

	/**
	 * convert an object value into java.util.Date value
	 * 
	 * @param value
	 * @return
	 */
	public static Date toDate(Object value) {
		return toDate(value, null, 0);
	}

	/**
	 * convert an object value into java.util.Date value using given format
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static Date toDate(Object value, String format) {
		return toDate(value, format, 0);
	}

	public static Date toDate(Object value, String format, int pos) {
		Calendar cal = toCalendar(value, format, pos);
		return cal.getTime();
	}

	/**
	 * convert an object value into java.util.Calendar value
	 * 
	 * @param value
	 * @return
	 */
	public static Calendar toCalendar(Object value) {
		return toCalendar(value, null, 0);
	}

	/**
	 * convert an object value into java.util.Calendar value using given format
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static Calendar toCalendar(Object value, String format) {
		return toCalendar(value, format, 0);
	}

	public static Calendar toCalendar(Object value, String format, int pos) {
		Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (value == null || value.equals(""))
			return null;
		if (Date.class.isInstance(value)) {
			c.setTime((Date) value);
		} else if (Calendar.class.isInstance(value)) {
			c = (Calendar) value;
		} else {
			DateFormat[] dfs;
			if (format == null || format.equals(""))
				dfs = getDateFormats(dateFormats);
			else
				dfs = new DateFormat[] { new SimpleDateFormat(format) };
			String s = String.valueOf(value);
			for (int i = 0; i < dfs.length; i++) {
				try {
					c.setTime(dfs[i].parse(s));
					break;
				} catch (Exception e) {
				}
			}
		}
		return c;
	}

	/**
	 * convert a string value into given type value
	 * 
	 * @param value
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static Object toString(String value, Class type)
			throws NumberFormatException, ParseException {
		Object result = value;
		if (int.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)) {
			result = Integer.valueOf(value);
		} else if (boolean.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)) {
			result = Boolean.valueOf(value);
		} else if (float.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)) {
			result = Float.valueOf(value);
		} else if (double.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)) {
			result = Double.valueOf(value);
		} else if (long.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)) {
			result = Long.valueOf(value);
		} else if (char.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)) {
			if (value.length() > 0)
				result = new Character(value.charAt(0));
		} else if (byte.class.isAssignableFrom(type)
				|| Byte.class.isAssignableFrom(type)) {
			result = Byte.valueOf(value);
		} else if (short.class.isAssignableFrom(type)
				|| Short.class.isAssignableFrom(type)) {
			result = Short.valueOf(value);
		} else if (BigDecimal.class.isAssignableFrom(type)) {
			result = new BigDecimal(Double.parseDouble(value));
		} else if (BigInteger.class.isAssignableFrom(type)) {
			result = new BigInteger(value);
		} else if (type.isArray()) {
			String[] values = value.split(",|;");
			Object[] objects = new Object[values.length];
			for (int i = 0; i < values.length; i++) {
				objects[i] = convert(values[i], type.getComponentType());
			}
			result = objects;
		} else if (Collection.class.isAssignableFrom(type)) {
			String[] values = value.split(",|;");
			Collection collection = new ArrayList();
			for (int i = 0; i < values.length; i++) {
				collection.add(values[i]);
			}
			Collection t = (Collection) result;
			t.addAll(collection);
		} else if (Map.class.isAssignableFrom(type)) {
			String[] values = value.split(",|;");
			Map map = new Hashtable();
			for (int i = 0; i < values.length; i++) {
			}
			Map t = (Map) result;
			t.putAll(map);
		} else if (Calendar.class.isAssignableFrom(type)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(df.parse(value));
			result = calendar;
		} else if (Date.class.isAssignableFrom(type)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result = df.parse(value);
		}
		return result;
	}

	/**
	 * convert a number value into given type value
	 * 
	 * @param value
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static Object toNumber(Number value, Class type)
			throws NumberFormatException, ParseException {
		Object result = value;
		if (int.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)) {
			result = new Integer(value.intValue());
		} else if (boolean.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)) {
			result = new Boolean(value.longValue() != 0);
		} else if (float.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)) {
			result = new Float(value.floatValue());
		} else if (double.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)) {
			result = new Double(value.doubleValue());
		} else if (long.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)) {
			result = new Long(value.longValue());
		} else if (char.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)) {
			result = new Character((char) value.intValue());
		} else if (byte.class.isAssignableFrom(type)
				|| Byte.class.isAssignableFrom(type)) {
			result = new Byte(value.byteValue());
		} else if (short.class.isAssignableFrom(type)
				|| Short.class.isAssignableFrom(type)) {
			result = new Short(value.shortValue());
		} else if (BigDecimal.class.isAssignableFrom(type)) {
			result = new BigDecimal(value.doubleValue());
		} else if (BigInteger.class.isAssignableFrom(type)) {
			result = new BigInteger(value.toString());
		}
		return result;
	}

	/**
	 * convert an object value into Integer value
	 * 
	 * @param value
	 * @return
	 */
	public static Integer toInteger(Object value) {
		Integer i;
		if (value == null)
			return null;
		if (Integer.class.isInstance(value)) {
			i = (Integer) value;
		} else {
			try {
				i = Integer.valueOf(String.valueOf(value));
			} catch (NumberFormatException nfe) {
				i = null;
			}
		}
		return i;
	}

	/**
	 * convert an object value into java.math.BigDecimal value
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object value) {
		double d;
		BigDecimal r = null;
		try {
			if (Double.class.isInstance(value)) {
				d = ((Double) value).doubleValue();
			} else {
				d = Double.parseDouble(String.valueOf(value));
			}
			r = new BigDecimal(d);
		} catch (NumberFormatException nfe) {
		}
		return r;
	}

	/**
	 * convert an object value into java.math.BigInteger value
	 * 
	 * @param value
	 * @return
	 */
	public static BigInteger toBigInteger(Object value) {
		BigInteger r = null;
		try {
			r = new BigInteger(value.toString());
		} catch (NumberFormatException nfe) {
		}
		return r;
	}

	/**
	 * convert an object value into given type
	 * 
	 * @param source
	 * @param type
	 * @return
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static Object convert(Object source, Class type)
			throws NumberFormatException, ParseException {
		Object result = source;
		if (String.class.isInstance(source)) {
			String value = (String) source;
			result = toString(value, type);
		} else if (Number.class.isInstance(source)) {
			Number value = (Number) source;
			result = toNumber(value, type);
		} else {
			if (type.equals(BigDecimal.class)) {
				result = toBigDecimal(source);
			} else if (type.equals(BigInteger.class)) {
				result = toBigInteger(source);
			}
		}
		return result;
	}

	private final static class DateConverter implements Converter {

		private Object defaultValue;

		private boolean useDefault;

		public DateConverter() {
			this.defaultValue = null;
			this.useDefault = false;
		}

		public DateConverter(Object defaultValue) {
			this.defaultValue = defaultValue;
			this.useDefault = true;
		}

		public Object convert(Class clazz, Object value) {
			if (value == null) {
				if (useDefault)
					return defaultValue;
				else
					throw new ConversionException("No value specified");
			}
			if (Date.class.isAssignableFrom(clazz)) {
				return toDate(value);
			} else if (Calendar.class.isAssignableFrom(clazz)) {
				return toCalendar(value);
			} else {
				return null;
			}
		}
	}
}
