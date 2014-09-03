package com.agloco.service.dao.hibernate;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.form.LogMessageForm;
import com.agloco.model.MessageModel;
import com.agloco.model.MessageObject;
import com.agloco.service.dao.LogMessageDao;
import com.agloco.util.HibernateUtil;
import com.agloco.util.StringUtil;

public class LogMessageDaoHibernate extends HibernateDaoSupport implements
		LogMessageDao
{

	public void addLogMessage(MessageModel messageMo)
	{
		// TODO Auto-generated method stub
		getHibernateTemplate().save(messageMo);
	}

	public String getLatestMaxCreateDate(String tableName)
	{

		String getLatestMaxCreateDateSQL = "select max(createDate) from "
				+ tableName;
		Query q = getSession().createSQLQuery(getLatestMaxCreateDateSQL);

		List temList = null;

		temList = q.list();
		if (temList == null)
		{
			return "";
		}
		else
		{
			return temList.get(0).toString();
		}
	}

	public Long getLatestMaxLogID(String tableName)
	{
		String getLatestMaxLogIDSQL = "select max(logId) from " + tableName;
		Query q = getSession().createSQLQuery(getLatestMaxLogIDSQL);
		List temList = null;
		temList = q.list();
		if (temList == null)
		{
			return Long.valueOf("0");
		}
		else
		{
			return Long.valueOf(temList.get(0).toString());
		}
	}

	public void alterLogTableIncrement(String tableName, Long incrementNo)
			throws Exception
	{

		try
		{
			Connection conn = getSession().connection();
			Statement sql = conn.createStatement();
			String alterTableIncrementSQL = " ALTER TABLE " + tableName
					+ " AUTO_INCREMENT = " + incrementNo;
			sql.execute(alterTableIncrementSQL);
		}
		catch (Exception ex)
		{
			throw ex;
		}

	}

	public void alterTableName(String oldName, String newName) throws Exception
	{

		try
		{
			Connection conn = getSession().connection();
			Statement sql = conn.createStatement();
			String alterTableNameSQL = " ALTER TABLE " + oldName
					+ " RENAME TO " + newName;
			sql.execute(alterTableNameSQL);
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public boolean checkTableExsit(String tableName)
	{
		String checkSQL = "select * from " + tableName + " where 1=0 ";
		Query q = getSession().createSQLQuery(checkSQL);
		try
		{
			q.list();
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public List getLogList(final String sql, final Object[] params){
		List list = (List)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query q = getSession().createQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.list();
			}
			
		});
		return list;
	}
	
	public List getLogMessageByForm(LogMessageForm logMessageForm)
	{

		List paramsList = new ArrayList();
		String sql = getSelectSQL(logMessageForm, paramsList);
		String countSql = "select count(*) "+sql;
		int count = getTotalRows(countSql,paramsList.toArray());
		
		Query q = getSession().createSQLQuery("select * "+sql).addEntity(MessageObject.class);
		
		for(int i=0;i<paramsList.size();i++)
		{
			q.setParameter(i, paramsList.get(i));
		}
		if(logMessageForm.getPageSize().intValue()!=0)
		{
			q.setFirstResult((logMessageForm.getPageNum().intValue()*logMessageForm.getPageSize().intValue()));
			q.setMaxResults(logMessageForm.getPageSize().intValue());
		}
		logMessageForm.setTotalResult(count);
		
		return q.list();

	}

	private String getSelectSQL(LogMessageForm logMessageForm, List paramsList)
	{
		StringBuffer sql = new StringBuffer(" from ");
		String logDateString = logMessageForm.getLogDate();
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String today = df.format(now);
		
		if (!StringUtil.isNotEmpty(logDateString)
				|| today.equals(logDateString))
			sql.append(" AG_Log ");
		else
		{
			sql.append(" AG_Log_" + logDateString);
		}

		sql.append(" where 1 = 1 ");

		if (logMessageForm.getOperate() != null
				&& !(logMessageForm.getOperate().equals("All") || logMessageForm
						.getOperate().equals("")))
		{
			sql.append(" and operate = ? ");
			paramsList.add(logMessageForm.getOperate());
		}

		if (logMessageForm.getPriority() != null
				&& !(logMessageForm.getPriority().equals("All") || logMessageForm
						.getPriority().equals("")))
		{
			sql.append(" and priority = ? ");
			paramsList.add(logMessageForm.getPriority());
		}

		if (!logMessageForm.getEmailAddress().equals(""))
		{
			if (logMessageForm.getEmailAddressType().equals("like"))
			{
				sql.append(" and emailAddress like '%"
						+ logMessageForm.getEmailAddress() + "%' ");
			}
			else
			{
				sql.append(" and emailAddress = ? ");
				paramsList.add(logMessageForm.getEmailAddress());
			}
		}

		if (!logMessageForm.getMemberCode().equals(""))
		{
			if (logMessageForm.getMemberCodeType().equals("like"))
			{
				sql.append(" and memberCode like '%"
						+ logMessageForm.getMemberCode() + "%' ");
			}
			else
			{
				sql.append(" and memberCode = ? ");
				paramsList.add(logMessageForm.getMemberCode());
			}
		}

		if (!logMessageForm.getReferralCode().equals(""))
		{
			if (logMessageForm.getReferralCodeType().equals("like"))
			{
				sql.append(" and referralCode like '%"
						+ logMessageForm.getReferralCode() + "%' ");
			}
			else
			{
				sql.append(" and referralCode = ? ");
				paramsList.add(logMessageForm.getReferralCode());
			}
		}

		if (!logMessageForm.getIp().equals(""))
		{
			if (logMessageForm.getIpType().equals("like"))
			{
				sql.append(" and IP like '%" + logMessageForm.getIp() + "%' ");
			}
			else
			{
				sql.append(" and IP = ? ");
				paramsList.add(logMessageForm.getIp());
			}
		}

		return sql.toString();
	}

	private Date stringToDate(String formatType, String formatString)
	{
		DateFormat df = new SimpleDateFormat(formatType);
		try
		{
			return df.parse(formatString);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private String dateToString(String formatType, Date formatDate)
	{
		DateFormat df = new SimpleDateFormat(formatType);
		try
		{
			return df.format(formatDate);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public List queryLogInfo(String sql) throws Exception
	{
		Query q = getSession().createSQLQuery(sql);
		return q.list();
	}

	public void createNewLogTable(String className, String newTableName)
			throws Exception
	{
		try
		{
			Connection conn = getSession().connection();
			Statement sql = conn.createStatement();

			// String createSQL = "CREATE TABLE `"
			// + newTableName
			// + "`("
			// + "`logId` int(11) NOT NULL auto_increment,"
			// + "`createDate` datetime NOT NULL default '0000-00-00 00:00:00',"
			// + "`thread` varchar(75) default '',"
			// + "`priority` varchar(75) default '',"
			// + "`category` varchar(75) default '',"
			// + "`message` varchar(255) default '',"
			// + "`operate` varchar(15) default '',"
			// + "`description` varchar(255) default '',"
			// + "`userId` varchar(75) default '',"
			// + "`emailAddress` varchar(100) default '',"
			// + "`exception` varchar(255) default '',"
			// + "`memberCode` varchar(15) default '',"
			// + "`referralCode` varchar(15) default '',"
			// + "`IP` varchar(24) default '',"
			// + "PRIMARY KEY (`logId`))";
			String createSQL = HibernateUtil.generateSchemaCreationScript(
					className, newTableName);

			sql.execute(createSQL);
		}
		catch (Exception ex)
		{
			throw ex;
		}
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
	
	private int getTotalRows(final String sql, final Object[] params){
		BigInteger number = (BigInteger)getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				Query q = getSession().createSQLQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}
				return q.uniqueResult();
			}
			
		});
		return number == null ? 0 : (new Integer(number.toString())).intValue();
	}
	

}
