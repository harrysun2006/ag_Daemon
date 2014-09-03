package com.agloco.service.impl;

import java.util.List;

import org.hibernate.HibernateException;

import com.agloco.form.LogMessageForm;
import com.agloco.model.MessageModel;
import com.agloco.service.LogMessageService;
import com.agloco.service.dao.util.LogMessageDaoUtil;

public class LogMessageServiceImpl implements LogMessageService
{

	public void addLogMessage(MessageModel messageMo)
	{
		LogMessageDaoUtil.addLogMessage(messageMo);
	}

	public String getLatestMaxCreateDate(String tableName)
	{
		if (!checkTableExsit(tableName))
			return null;
		return LogMessageDaoUtil.getLatestMaxCreateDate(tableName);
	}

	public void alterLogTableIncrement(String tableName, Long incrementNo)
			throws Exception
	{
		LogMessageDaoUtil.alterLogTableIncrement(tableName, incrementNo);
	}

	public void alterTableName(String oldName, String newName) throws Exception
	{
		LogMessageDaoUtil.alterTableName(oldName, newName);
	}

	public void createNewLogTable(String className, String newTableName)
			throws Exception
	{
		LogMessageDaoUtil.createNewLogTable(className, newTableName);
	}

	public Long getLatestMaxLogID(String tableName)
	{
		return LogMessageDaoUtil.getLatestMaxLogID(tableName);
	}

	public boolean checkTableExsit(String tableName)
	{
		return LogMessageDaoUtil.checkTableExsit(tableName);
	}

	public void backupAndCreateTable(String className, String tableName, String backupTableName)
			throws Exception
	{
		Long maxLogId = LogMessageDaoUtil.getLatestMaxLogID(tableName);
		if (!LogMessageDaoUtil.checkTableExsit(backupTableName))
		{
			LogMessageDaoUtil.alterTableName(tableName, backupTableName);
			LogMessageDaoUtil.createNewLogTable(className, tableName);
			LogMessageDaoUtil.alterLogTableIncrement(tableName, new Long(
					maxLogId.longValue() + new Long(1).longValue()));
		}
		else
		{
			throw new HibernateException("Table is exists:" + backupTableName);
		}
	}
	
	public List queryLogInfo(String sql) throws Exception
	{
		return LogMessageDaoUtil.queryLogInfo(sql);
	}

	public List getLogMessageByForm(LogMessageForm logMessageForm)
	{
		return LogMessageDaoUtil.getLogMessageByForm(logMessageForm);
	}

}
