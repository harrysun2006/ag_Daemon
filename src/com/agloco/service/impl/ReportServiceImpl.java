package com.agloco.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.report.util.ReportScriptUtil;
import com.agloco.service.ReportService;
import com.agloco.service.dao.util.ReportDaoUtil;

/**
 * 
 * @author Erick Kong
 * 
 */
public class ReportServiceImpl implements ReportService
{

	public AGReportResultList getReportResultList(Calendar date, AGQuery query)
	{
		// TODO Auto-generated method stub
		return ReportDaoUtil.getReportList(date, query);
	}
	
	public AGReportResultList getReportResultList(int queryId, Map params)
	{
		Calendar date = Calendar.getInstance();
		// TODO Auto-generated method stub
		AGReportResultList result = new AGReportResultList();
		AGQuery query = ReportDaoUtil.getAGQuery(queryId);
		
		ReportScriptUtil.executeScript(query.getPreScript(),"getParamter",params);
		//		ParamterSet.getParamter(params);
		
		params.put("pwdparam", Constants.AGLOCO_AESKEY.toString());
		
		List list = ReportDaoUtil.getReportList(query.getQuery(),params,0,0);
		result = ReportScriptUtil.suffixScript(query,list);
		result.setTitle(query.getQueryName());
		result.setResultTitle(query.getQueryTitle());
		result.setDate(date);
		
		return result;
	}
	
	public AGReportResultList getReportResultList(AGQuery query, int pageSize, int pageNumber)
	{
		AGReportResultList rl = new AGReportResultList();
		Calendar date = Calendar.getInstance();
		if(query.isMultiQuery())
		{
			// get paramter through groovy
			Map params = ReportScriptUtil.prefixScript(query);
			
			String[] sqls = query.getQuery().split(com.agloco.Constants.MULTI_SQL_SEPARATOR);
			List result = new ArrayList();
			for(int i=0;i<sqls.length;i++)
			{
				List list = ReportDaoUtil.getReportList(sqls[i],params,pageSize,pageNumber);
				
				result.add(list);
			}
			// process result through groovy
			rl = ReportScriptUtil.suffixScript(query,result);
			rl.setTitle(query.getQueryName());
			rl.setResultTitle(query.getQueryTitle());
			rl.setDate(date);
			
			return rl;
		}
		else
		{
			// get paramter through groovy
			Map params = ReportScriptUtil.prefixScript(query);
			
			// process result through groovy
			List list = ReportDaoUtil.getReportList(query.getQuery(),params,pageSize,pageNumber);
			rl = ReportScriptUtil.suffixScript(query,list);
			rl.setTitle(query.getQueryName());
			rl.setResultTitle(query.getQueryTitle());
			rl.setDate(date);
			
			return rl;
		}
	}

	public AGReportResultList getReportResultList(AGQuery query)
	{
		return getReportResultList(query, 0, 0);		
	}

	public AGQuery getAGQuery(int queryId)
	{
		return ReportDaoUtil.getAGQuery(queryId);
	}
	
	public List getAGQueryByType(int queryType)
	{
		return ReportDaoUtil.getAGQueryByType(queryType);
	}
	
	private static Log _log = LogFactory.getLog(ReportServiceImpl.class);

}
