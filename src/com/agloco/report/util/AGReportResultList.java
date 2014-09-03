package com.agloco.report.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 
 * @author Erick Kong
 * 
 */
public class AGReportResultList implements Serializable
{
	private static final long serialVersionUID = 4597389471925546001L;

	public final static int DAY = 1; // 1 day

	public final static int WEEK = 7;// 7 days

	public final static int MONTH = 30;// 30 days, not exact, it only means a
										// month

	private String title;

	private Calendar date;

	private String resultTitle;

	private List list;

	public List getList()
	{
		return list;
	}

	public void setList(List list)
	{
		this.list = list;
	}

	public int getResultCount()
	{
		return list.size();
	}

	public String getResultTitle()
	{
		return resultTitle;
	}

	public void setResultTitle(String resultTitle)
	{
		this.resultTitle = resultTitle;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Calendar getDate()
	{
		return date;
	}

	public void setDate(Calendar date)
	{
		this.date = date;
	}
	
	public StringBuffer exportToExcel(TimeZone tz)
	{
		StringBuffer result = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (tz != null)
			df.setTimeZone(tz);

		List list = this.getList();
		String[] titles = this.getResultTitle().split(",");
		result.append(this.getTitle() + "\n");

		for (int i = 0; i < titles.length; i++)
		{
			result.append(ReportUtil.processComma(titles[i]) + ",");
		}
		result.append("\n");
		for (int i = 0; i < list.size(); i++)
		{
			Object[] tmp;
			if (list.get(i) instanceof Object[])
				tmp = (Object[]) list.get(i);
			else
				tmp = new Object[] { list.get(i) };
			for (int j = 0; j < tmp.length; j++)
			{
				if(tmp[j] instanceof String)
					result.append(ReportUtil.processComma((String)tmp[j]) + ",");
				else
					result.append(tmp[j] + ",");
			}
			result.append("\n");
		}

		result.append("\n");
		if (titles.length > 0)
			for (int i = 0; i < titles.length-1; i++)
			{
				result.append(",");
			}
		result.append("Create Date:"+df.format(this.getDate().getTime()) + "\n");

		return result;
	}

	public void exportToExcel(StringBuffer result)
	{
		List list = this.getList();
		String[] titles = this.getResultTitle().split(",");
		result.append(this.getTitle() + "\n");

		for (int i = 0; i < titles.length; i++)
		{
			result.append(ReportUtil.processComma(titles[i]) + ",");
		}
		result.append("\n");
		for (int i = 0; i < list.size(); i++)
		{
			Object[] tmp;
			if (list.get(i) instanceof Object[])
				tmp = (Object[]) list.get(i);
			else
				tmp = new Object[] { list.get(i) };
			for (int j = 0; j < tmp.length; j++)
			{
				if(tmp[j] instanceof String)
					result.append(ReportUtil.processComma((String)tmp[j]) + ",");
				else
					result.append(tmp[j] + ",");
			}
			result.append("\n");
		}

		result.append("\n");
		if (titles.length > 0)
			for (int i = 0; i < titles.length-1; i++)
			{
				result.append(",");
			}
		result.append("\n\n");
		
	}

	public StringBuffer exportToExcel(String file, TimeZone tz)
	{
		StringBuffer result = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (tz != null)
			df.setTimeZone(tz);

		List list = this.getList();
		String[] titles = this.getResultTitle().split(",");
		result.append(this.getTitle() + "\n");

		for (int i = 0; i < titles.length; i++)
		{
			result.append(ReportUtil.processComma(titles[i]) + ",");
		}
		result.append("\n");
		for (int i = 0; i < list.size(); i++)
		{
			Object[] tmp;
			if (list.get(i) instanceof Object[])
				tmp = (Object[]) list.get(i);
			else
				tmp = new Object[] { list.get(i) };
			for (int j = 0; j < tmp.length; j++)
			{
				if(tmp[j] instanceof String)
					result.append(ReportUtil.processComma((String)tmp[j]) + ",");
				else
					result.append(tmp[j] + ",");
			}
			result.append("\n");
		}

		result.append("\n");
		if (titles.length > 0)
			for (int i = 0; i < titles.length-1; i++)
			{
				result.append(",");
			}
		result.append("Create Date:"+df.format(this.getDate().getTime()) + "\n");
		try
		{
			RandomAccessFile rafout = new RandomAccessFile(file, "rw");
			rafout.writeBytes(result.toString());
			rafout.close();
//			zipFile(file,df.format(this.getDate().getTime())+".zip",result);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public StringBuffer displayAsHTML(String style)
	{
		StringBuffer result = new StringBuffer();

		List list = this.getList();
		if(list.size()<1)
		{
			result.append("<font color='red'>The result is empty!</font>");
			return result;
		}
		result.append("<table class='"+style+"'>");
		String[] titles = this.getResultTitle().split(",");
		result.append("<tr>");

		for (int i = 0; i < titles.length; i++)
		{
			result.append("<th width='100'>" + titles[i] + "</th>");
		}
		result.append("</tr>");
		for (int i = 0; i < list.size(); i++)
		{
			Object[] tmp;
			if (list.get(i) instanceof Object[])
				tmp = (Object[]) list.get(i);
			else
				tmp = new Object[] { list.get(i) };
			result.append("<tr>");
			for (int j = 0; j < tmp.length; j++)
			{
				if(tmp[j] instanceof String)
					result.append("<td>" + (String)tmp[j] + "</td>");
				else
					result.append("<td>" + tmp[j] + "</td>");
			}
			result.append("</tr>");
		}

		result.append("</table>");
		return result;
	}
}
