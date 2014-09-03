package com.agloco.service.dao.hibernate;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.model.AGQuery;
import com.agloco.report.util.AGReportResultList;
import com.agloco.service.dao.ReportDao;

/**
 * 
 * @author Erick Kong
 * 
 */
public class ReportDaoHibernate extends HibernateDaoSupport implements
		ReportDao
{
	//The query string has two paramters: begin time and end time
	public AGReportResultList getReportList(Calendar date, AGQuery aq)
	{
		Calendar beginDate = (Calendar)date.clone();
		Calendar endDate = (Calendar)date.clone();
		if(aq == null)
			return null;
		if(aq.getQueryType() == 1)
			beginDate.set(Calendar.HOUR, -24);
		else if(aq.getQueryType() == 7)
			beginDate.set(Calendar.HOUR, -24*7);
		else if(aq.getQueryType() == 30)
			beginDate.set(Calendar.MONTH, -1);
		
		String sql = aq.getQuery();
		
		if(sql == null)
			return null;
		// TODO Auto-generated method stub
		Query q = getSession().createSQLQuery(sql);
		
		q.setParameter("param1",beginDate);
		q.setParameter("param2",endDate);
		
		try
		{
			List list = q.list();
			if(list!=null)
			{
				AGReportResultList rl = new AGReportResultList();
				rl.setList(list);
				rl.setTitle(aq.getQueryName());
				rl.setResultTitle(aq.getQueryTitle());
				rl.setDate(beginDate);
				return rl;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	// Query without paramter, the AGQuery must be not multi query
	public List getReportList(AGQuery aq)
	{
		String sql = aq.getQuery();
		
		if(sql == null)
			return null;
		// TODO Auto-generated method stub
		Query q = getSession().createSQLQuery(sql);
		
		return q.list();
	}

	// Query with paramters
	public List getReportList(String sql,Map params)
	{
		// TODO Auto-generated method stub
		return getReportList(sql,params,0,0);
	}

	// Query with paramters
	public List getReportList(String sql,Map params,int pageSize, int pageNumber)
	{
		// TODO Auto-generated method stub
		Query q = getSession().createSQLQuery(sql.toString());
		setParams(q,params);
		if(pageNumber != 0)
		{
			q.setFirstResult(pageNumber*pageSize);
			q.setMaxResults(pageSize);
		}
		return q.list();
	}
	
	public List getReportList(String sql)
	{		
		return getReportList(sql,null);
	}

	public String getQueryString(int queryId)
	{
		// TODO Auto-generated method stub
		String sql = "select agq.query from AGQuery agq where agq.queryId= :param";
		
		Query q = getSession().createQuery(sql.toString());
		q.setParameter("param", new Integer(queryId));
		List list = q.list();
		if(list!=null)
		{
			return (String)list.get(0);
		}
		return null;
	}
	
	public AGQuery getAGQuery(int queryId)
	{
		return (AGQuery) getHibernateTemplate().get(AGQuery.class, new Long(queryId));
	}

	public List getAGQueryByType(int queryType)
	{
		List list = null;
		if(queryType >=0 )
			list = getHibernateTemplate().find("from AGQuery agq where agq.queryType=?", new Long(queryType));
		else
			list = getHibernateTemplate().find("from AGQuery agq");
		return list;
	}

	private void setParams(Query q,Map params)
	{
		if(params == null)
			return;
		String[] parameterNames = q.getNamedParameters();
		for(int i=0;i<parameterNames.length;i++)
		{
			if(params.containsKey(parameterNames[i]))
				q.setParameter((String)parameterNames[i], params.get(parameterNames[i]));
		}
	}
}
