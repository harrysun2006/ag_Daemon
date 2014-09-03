/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agloco.log4j;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.agloco.model.MessageModel;
import com.agloco.service.util.LogMessageServiceUtil;
/**
 * <p>
 * <b><font color="#FF2222">WARNING: This version of JDBCAppender is very
 * likely to be completely replaced in the future. Moreoever, it does not log
 * exceptions</font></b>.
 * 
 * The JDBCAppender provides for sending log events to a database.
 * 
 * 
 * <p>
 * Each append call adds to an <code>ArrayList</code> buffer. When the buffer
 * is filled each log event is placed in a sql statement (configurable) and
 * executed.
 * 
 * <b>BufferSize</b>, <b>db URL</b>, <b>User</b>, & <b>Password</b> are
 * configurable options in the standard log4j ways.
 * 
 * <p>
 * The <code>setSql(String sql)</code> sets the SQL statement to be used for
 * logging -- this statement is sent to a <code>PatternLayout</code> (either
 * created automaticly by the appender or added by the user). Therefore by
 * default all the conversion patterns in <code>PatternLayout</code> can be
 * used inside of the statement. (see the test cases for examples)
 * 
 * <p>
 * Overriding the {@link #getLogStatement} method allows more explicit control
 * of the statement used for logging.
 * 
 * <p>
 * For use as a base class:
 * 
 * <ul>
 * 
 * <li>Override <code>getConnection()</code> to pass any connection you want.
 * Typically this is used to enable application wide connection pooling.
 * 
 * <li>Override <code>closeConnection(Connection con)</code> -- if you
 * override getConnection make sure to implement <code>closeConnection</code>
 * to handle the connection you generated. Typically this would return the
 * connection to the pool it came from.
 * 
 * <li>Override <code>getLogStatement(LoggingEvent event)</code> to produce
 * specialized or dynamic statements. The default uses the sql option value.
 * 
 * </ul>
 * 
 * @author Kevin Steppe (<A
 *         HREF="mailto:ksteppe@pacbell.net">ksteppe@pacbell.net</A>)
 * 
 */
public class DailyDBAppender extends org.apache.log4j.AppenderSkeleton
		implements org.apache.log4j.Appender
{

	// The code assumes that the following constants are in a increasing
	// sequence.
	static final int TOP_OF_TROUBLE = -1;

	static final int TOP_OF_MINUTE = 0;

	static final int TOP_OF_HOUR = 1;

	static final int HALF_DAY = 2;

	static final int TOP_OF_DAY = 3;

	static final int TOP_OF_WEEK = 4;

	static final int TOP_OF_MONTH = 5;

	/**
	 * The log file will be renamed to the value of the scheduledFilename
	 * variable when the next interval is entered. For example, if the rollover
	 * period is one hour, the log file will be renamed to the value of
	 * "scheduledFilename" at the beginning of the next hour.
	 * 
	 * The precise time when a rollover occurs depends on logging activity.
	 */
	private String scheduledTableName;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	Date now = new Date();

	SimpleDateFormat sdf;

	AGRollingCalendar rc = new AGRollingCalendar();

	int checkPeriod = TOP_OF_TROUBLE;

	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	private String tableName = "AG_Log";

	/**
	 * The date pattern. By default, the pattern is set to "'_'yyyy-MM-dd"
	 * meaning daily rollover.
	 */
	private String datePattern = "'_'yyyy-MM-dd";

	/**
	 * 
	 */
	private String messageModelClass = "com.agloco.model.MessageModel";

	/**
	 * The date pattern of the date when insert into db, you can config it in
	 * log4f configure
	 */
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * If mustMsgModel is true, logEvent message that instansof MessageModel
	 * will be insert into database, others will be ignore
	 */
	private boolean mustMsgModel = false;

	/**
	 * size of LoggingEvent buffer before writting to the database. Default is
	 * 1.
	 */
	protected int bufferSize = 1;

	/**
	 * ArrayList holding the buffer of Logging Events.
	 */
	protected ArrayList buffer;

	/**
	 * Helper object for clearing out the buffer
	 */
	protected ArrayList removes;

	private int delayTime = 0;

	public DailyDBAppender()
	{
		super();

		// if table not exists, create it
		buffer = new ArrayList(bufferSize);
		removes = new ArrayList(bufferSize);

	}

	/**
	 * Adds the event to the buffer. When full the buffer is flushed.
	 */
	public void append(LoggingEvent event)
	{
		buffer.add(event);

		if (buffer.size() >= bufferSize)
			flushBuffer();
	}

	/**
	 * Closes the appender, flushing the buffer first then closing the default
	 * connection if it is open.
	 */
	public void close()
	{
		flushBuffer();

		this.closed = true;
	}

	/**
	 * 
	 * Create new table here, when add the first log into database, must check
	 * is the table is exists
	 * 
	 */
	private void createNewTable()
	{
		try
		{
			// If table is not exists, dateStr is null, then create a new
			// table
			String dateStr = LogMessageServiceUtil
					.getLatestMaxCreateDate(tableName);
			if (dateStr == null)
				LogMessageServiceUtil.createNewLogTable(messageModelClass, tableName);
			// HibernateUtil.createTable(MessageObject.class.getName(),
			// tableName);
			else
			{
				// If table is exists, and the context is old, backup the
				// table first
				Date date = dateFormat.parse(dateStr);
				String backupTableName = tableName + sdf.format(date);
				String tmpTableName = tableName + sdf.format(now);

				if (!tmpTableName.equals(backupTableName))
				{
					LogMessageServiceUtil.backupAndCreateTable(messageModelClass, tableName,
							backupTableName);
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			LogUtil.error(e.getMessage());
		}
	}

	/**
	 * Rollover the current file to a new file.
	 */
	void rollOver() throws IOException
	{

		/* Compute tableName, but only if datePattern is specified */
		if (datePattern == null)
		{
			errorHandler.error("Missing DatePattern option in rollOver().");
			return;
		}

		String datedTableName = tableName + sdf.format(now);
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledTableName.equals(datedTableName))
		{
			createNewTable();
			return;
		}

		// rename current table to scheduledTableName, and renew current table
		try
		{
			LogMessageServiceUtil.backupAndCreateTable(messageModelClass,tableName,
					scheduledTableName);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			LogUtil.error(e.getMessage());
		}

		scheduledTableName = datedTableName;
	}

	/**
	 * loops through the buffer of LoggingEvents, gets a sql string from
	 * getLogStatement() and sends it to execute(). Errors are sent to the
	 * errorHandler.
	 * 
	 * If a statement fails the LoggingEvent stays in the buffer!
	 */
	public void flushBuffer()
	{
		long n = System.currentTimeMillis();
		if (n >= nextCheck + delayTime * 1000 * 60)
		{
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
			try
			{
				rollOver();
			}
			catch (IOException ioe)
			{
				LogUtil.error("rollOver() failed.", ioe);
			}
		}

		// Do the actual logging
		removes.ensureCapacity(buffer.size());
		for (Iterator i = buffer.iterator(); i.hasNext();)
		{
			try
			{
				LoggingEvent logEvent = (LoggingEvent) i.next();
				MessageModel msg = new MessageModel();

				if (logEvent.getMessage() instanceof MessageModel)
				{
					msg = (MessageModel) logEvent.getMessage();
					if (msg.getMessage() == null)
					{
						if (logEvent.getThrowableInformation() != null)
						{
							if (logEvent.getThrowableInformation()
									.getThrowable().getMessage() != null)
								msg.setMessage(logEvent
										.getThrowableInformation()
										.getThrowable().getMessage());
							else
								msg.setMessage(logEvent
										.getThrowableInformation()
										.getThrowable().toString());

							if (logEvent.getThrowableInformation() != null
									&& msg.getException() == null)
								msg.setException(logEvent
										.getThrowableInformation()
										.getThrowable().toString());
						}
					}
				}
				else
				{
					if (isMustMsgModel())
					{
						removes.add(logEvent);
						continue;
					}
					msg = (MessageModel) Class.forName(messageModelClass)
							.newInstance();
					if (msg.getMessage() == null)
					{
						if (logEvent.getMessage() != null)
							msg.setMessage(logEvent.getMessage().toString());
						else if (logEvent.getThrowableInformation() != null)
						{
							if (logEvent.getThrowableInformation()
									.getThrowable().getMessage() != null)
								msg.setMessage(logEvent
										.getThrowableInformation()
										.getThrowable().getMessage());
							else
								msg.setMessage(logEvent
										.getThrowableInformation()
										.getThrowable().toString());
						}
					}
					if (logEvent.getThrowableInformation() != null
							&& msg.getException() == null)
					{
						msg.setException(logEvent.getThrowableInformation()
								.getThrowable().toString());
					}
				}

				Calendar logDate = Calendar.getInstance();
				logDate.setTime(new Date(logEvent.timeStamp));
				msg.setCategory(logEvent.categoryName);
				msg.setPriority(logEvent.getLevel().toString());
				msg.setThread(logEvent.getThreadName());
				msg.setCreateDate(logDate);

				if (delayTime == 0 || (delayTime !=0 && logEvent.timeStamp < nextCheck))
				{
					LogMessageServiceUtil.addLogMessage(msg);
					removes.add(logEvent);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				LogUtil.error("Log error!", e);
				// TODO: handle exception
			}

		}

		// remove from the buffer any events that were reported
		buffer.removeAll(removes);

		// clear the buffer of reported events
		removes.clear();
	}

	/** closes the appender before disposal */
	public void finalize()
	{
		close();
	}

	/**
	 * JDBCAppender requires a layout.
	 */
	public boolean requiresLayout()
	{
		return true;
	}

	public void setBufferSize(int newBufferSize)
	{
		bufferSize = newBufferSize;
		buffer.ensureCapacity(bufferSize);
		removes.ensureCapacity(bufferSize);
	}

	/**
	 * Ensures that the given driver class has been loaded for sql connection
	 * creation.
	 */
	public void setDriver(String driverClass)
	{
		try
		{
			Class.forName(driverClass);
		}
		catch (Exception e)
		{
			errorHandler.error("Failed to load driver", e,
					ErrorCode.GENERIC_FAILURE);
		}
	}

	public String getDatePattern()
	{
		return datePattern;
	}

	public void setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public void activateOptions()
	{
		super.activateOptions();
		if (datePattern != null && tableName != null)
		{
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(datePattern);
			int type = computeCheckPeriod();

			rc.setType(type);

			this.scheduledTableName = tableName + sdf.format(now);
		}
		else
		{
			LogUtil
					.error("Either File or DatePattern options are not set for appender ["
							+ name + "].");
		}
	}

	int computeCheckPeriod()
	{
		AGRollingCalendar rollingCalendar = new AGRollingCalendar(gmtTimeZone,
				Locale.ENGLISH);
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null)
		{
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++)
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						datePattern);
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
				// formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);
				// System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
				if (r0 != null && r1 != null && !r0.equals(r1))
				{
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE; // Deliberately head for trouble...
	}

	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public void setMessageModelClass(String messageModelClass)
	{
		this.messageModelClass = messageModelClass;
	}

	public boolean isMustMsgModel()
	{
		return mustMsgModel;
	}

	public void setMustMsgModel(boolean mustMsgModel)
	{
		this.mustMsgModel = mustMsgModel;
	}

	public int getDelayTime()
	{
		return delayTime;
	}

	public void setDelayTime(int delayTime)
	{
		this.delayTime = delayTime;
	}

}

/**
 * AGRollingCalendar is a helper class to DailyJDBCAppender. Given a periodicity
 * type and the current time, it computes the start of the next interval.
 */
class AGRollingCalendar extends GregorianCalendar
{

	int type = DailyDBAppender.TOP_OF_TROUBLE;

	AGRollingCalendar()
	{
		super();
	}

	AGRollingCalendar(TimeZone tz, Locale locale)
	{
		super(tz, locale);
	}

	void setType(int type)
	{
		this.type = type;
	}

	public long getNextCheckMillis(Date now)
	{
		return getNextCheckDate(now).getTime();
	}

	public Date getNextCheckDate(Date now)
	{
		this.setTime(now);

		switch (type)
		{
		case DailyDBAppender.TOP_OF_MINUTE:
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MINUTE, 1);
			break;
		case DailyDBAppender.TOP_OF_HOUR:
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.HOUR_OF_DAY, 1);
			break;
		case DailyDBAppender.HALF_DAY:
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			int hour = get(Calendar.HOUR_OF_DAY);
			if (hour < 12)
			{
				this.set(Calendar.HOUR_OF_DAY, 12);
			}
			else
			{
				this.set(Calendar.HOUR_OF_DAY, 0);
				this.add(Calendar.DAY_OF_MONTH, 1);
			}
			break;
		case DailyDBAppender.TOP_OF_DAY:
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.DATE, 1);
			break;
		case DailyDBAppender.TOP_OF_WEEK:
			this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case DailyDBAppender.TOP_OF_MONTH:
			this.set(Calendar.DATE, 1);
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MONTH, 1);
			break;
		default:
			throw new IllegalStateException("Unknown periodicity type.");
		}
		return getTime();
	}

}
