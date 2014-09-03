package com.agloco.service.util;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.service.ReportService;
import com.liferay.portal.spring.util.SpringUtil;

/**
 * 
 * @author Erick Kong
 * 
 */
public class ReportServiceUtil
{

	private ReportService reportService;

	public static AGReportResultList getReportResultList(Calendar date, AGQuery query)
	{
		return getReportService().getReportResultList(date, query);
	}

	public static AGReportResultList getReportResultList(int queryId, Map params)
	{
		return getReportService().getReportResultList(queryId, params);
	}

	public static AGReportResultList getReportResultList(AGQuery query)
	{
		return getReportService().getReportResultList(query);
	}


	public static AGQuery getAGQuery(int queryId)
	{
		return getReportService().getAGQuery(queryId);
	}
	
	public static List getAGQueryByType(int queryType)
	{
		return getReportService().getAGQueryByType(queryType);
	}
	
	public static ReportService getReportService()
	{
		ApplicationContext ctx = SpringUtil.getContext();
		ReportServiceUtil util = (ReportServiceUtil) ctx
				.getBean(ReportServiceUtil.class.getName());
		return util.reportService;
	}

	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

}