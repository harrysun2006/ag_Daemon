package com.agloco.service.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.service.dao.hibernate.ReportDaoHibernate;

/**
 * 
 * @author Erick Kong
 * @see ReportDaoHibernate	
 */
public interface ReportDao{

	public AGReportResultList getReportList(Calendar date, AGQuery queryId);
	
	public String getQueryString(int queryId);
	public AGQuery getAGQuery(int queryId);
	public List getAGQueryByType(int queryType);
	
	// Query without paramter 
	public List getReportList(AGQuery aq);
	
	// Query with paramters
	public List getReportList(String sql,Map params);

	// Query with paramters
	public List getReportList(String sql,Map params,int pageSize, int pageNumber);
	
	public List getReportList(String sql);

}
