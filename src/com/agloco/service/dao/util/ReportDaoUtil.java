package com.agloco.service.dao.util;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.service.dao.ReportDao;
import com.liferay.portal.spring.util.SpringUtil;
/**
 * 
 * @author Erick Kong
 *
 */
public class ReportDaoUtil {
	
	private ReportDao reportDao;
	
	public static AGQuery getAGQuery(int queryId)
	{
		return getReportDao().getAGQuery(queryId);
	}
	
	public static List getAGQueryByType(int queryType)
	{
		return getReportDao().getAGQueryByType(queryType);
	}
	
	public static AGReportResultList getReportList(Calendar date, AGQuery query)
	{
		return getReportDao().getReportList(date, query);
	}
	
	//Query without paramter 
	public static List getReportList(AGQuery aq)
	{
		return getReportDao().getReportList(aq);
	}
	
	// Query with paramters
	public static List getReportList(String sql,Map params)
	{
		return getReportDao().getReportList(sql,params);
	}
	
	public static List getReportList(String sql,Map params,int pageSize, int pageNumber)
	{
		return getReportDao().getReportList(sql,params,pageSize,pageNumber);
	}

	public static List getReportList(String sql)
	{
		return getReportDao().getReportList(sql);
	}
	
	public static ReportDao getReportDao(){
		ApplicationContext ctx = SpringUtil.getContext();
		ReportDaoUtil util = (ReportDaoUtil) ctx.getBean(ReportDaoUtil.class.getName());
		return util.reportDao;
	}
	
	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	
}
