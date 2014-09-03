package com.agloco.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.service.impl.MemberServiceImpl;

/**
 * 
 * @author Erick Kong
 * @see ReportServiceImpl
 */
public interface ReportService extends BaseService {

	public AGReportResultList getReportResultList(Calendar date, AGQuery query); 
	
	public AGReportResultList getReportResultList(int queryId, Map params); 
	
	public AGReportResultList getReportResultList(AGQuery query); 

	public AGReportResultList getReportResultList(AGQuery query, int pageSize, int pageNumber); 
	
	public AGQuery getAGQuery(int queryId);
	
	public List getAGQueryByType(int queryType);
	
}
