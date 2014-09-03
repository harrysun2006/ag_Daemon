package com.agloco.service;

import java.util.List;

import com.agloco.form.LogMessageForm;
import com.agloco.model.MessageModel;

public interface LogMessageService {
	public void addLogMessage(MessageModel messageMo);
	public String getLatestMaxCreateDate(String tableName);
	public void createNewLogTable(String className,String newTableName)throws Exception;
	public Long getLatestMaxLogID(String tableName);
	public void alterLogTableIncrement(String tableName,Long incrementNo)throws Exception;
	public void alterTableName(String oldName,String newName)throws Exception;
	public boolean checkTableExsit(String tableName);
	public void backupAndCreateTable(String className, String tableName,String backupTableName) throws Exception;
	public List queryLogInfo(String sql) throws Exception;
	public List getLogMessageByForm(LogMessageForm logMessageForm);
	
}
