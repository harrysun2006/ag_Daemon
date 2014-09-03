package com.agloco.task;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.resources.FileDirContext;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.report.util.ReportUtil;
import com.agloco.service.dao.util.ReportDaoUtil;
import com.agloco.service.util.ReportServiceUtil;
import com.liferay.portal.util.PropsUtil;

/**
 * 
 * @author Erick Kong
 * 
 */
public class ReportTask
{
	protected int type = 1;

	protected TimeZone timeZone = TimeZone.getTimeZone("PST");
	
	public static final int LES_MAIL_COUNT = 1;

	protected String typeString = "DEFAULT";

	protected static String DAILY = "DAILY";

	protected static String WEEKLY = "WEEKLY";

	protected static String MONTHLY = "MONTHLY";

	public void setType(int type)
	{
		this.type = type;
		switch (type)
		{
		case 1:
			typeString = DAILY;
			break;
		case 7:
			typeString = WEEKLY;
			break;
		case 30:
			typeString = MONTHLY;
			break;
		}
		System.out.println(typeString + " ReportTask init!");
		_log.info(typeString + " ReportTask init! ");
	}
	
	public void setTimeZone(TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}

	public ReportTask()
	{

	}

	public void report()
	{
		report(timeZone);
	}

	public void report(TimeZone timeZone)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(timeZone);

		String filePath = PropsUtil.get("report.dir");
		File fold = new File(filePath);
		if(!fold.exists())
		{
			fold.mkdirs();
		}

		String outFile = filePath + df.format(Calendar.getInstance().getTime())
				+ "_" + typeString + ".zip";
		String fileName = typeString + "_"
			+ df.format(Calendar.getInstance().getTime()) + ".csv";
		StringBuffer sb = new StringBuffer(); 
		
		try
		{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outFile));

			List agQuerys = ReportServiceUtil.getAGQueryByType(this.type);
			for (int i = 0; i < agQuerys.size(); i++)
			{
				AGReportResultList resultList = null;
				AGQuery aq = (AGQuery) agQuerys.get(i);
				try
				{
					resultList = ReportServiceUtil.getReportResultList(aq);
				}
				catch (Exception e)
				{
					_log.error(aq.getQueryName() + " reportTask failed! "
							+ typeString);
				}
				if (resultList == null)
					continue;

				resultList.exportToExcel(sb);

			}
			out.putNextEntry(new ZipEntry(fileName));
			int len = sb.length();
			byte[] b = new byte[len];
			sb.toString().getBytes(0, len, b, 0);
			out.write(b, 0, len);

			// Complete the entry
			out.closeEntry();

			// Complete the ZIP file
			out.close();

			ReportUtil.sendReportMail(outFile , typeString);
			_log.info("ReportTask secceed! " + typeString);
		}
		catch (Exception e)
		{
			_log.error("ReportTask failed! " + typeString);
			_log.error(e.getMessage());
		}
	}

	private static Log _log = LogFactory.getLog(ReportTask.class);
}
