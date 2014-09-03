package com.agloco.service.util;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.agloco.form.LogMessageForm;
import com.agloco.model.MessageModel;
import com.agloco.service.LogMessageService;
import com.liferay.portal.spring.util.SpringUtil;

public class LogMessageServiceUtil {
	private LogMessageService logMessageService;

	public static void addLogMessage(MessageModel messageMo) {
		getLogMessageService().addLogMessage(messageMo);

	}

	public static String getLatestMaxCreateDate(String tableName) {
		return getLogMessageService().getLatestMaxCreateDate(tableName);
	}

	public static void backupAndCreateTable(String className, String tableName,String backupTableName) throws Exception{
		getLogMessageService().backupAndCreateTable(className, tableName,backupTableName);
	}

	public static Long getLatestMaxLogID(String tableName) {
		return getLogMessageService().getLatestMaxLogID(tableName);
	}
	
	public static void createNewLogTable(String className,String tableName) throws Exception {
		 getLogMessageService().createNewLogTable(className, tableName);
	}

	public static void alterLogTableIncrement(String tableName, Long incrementNo)throws Exception {
		getLogMessageService().alterLogTableIncrement(tableName, incrementNo);
	}

	public static void alterTableName(String oldName, String newName)throws Exception {
		getLogMessageService().alterTableName(oldName, newName);
	}
	public static boolean checkTableExsit(String tableName){
		return getLogMessageService().checkTableExsit(tableName);
	}
	public static LogMessageService getLogMessageService() {
		ApplicationContext ctx = SpringUtil.getContext();
		LogMessageServiceUtil util = (LogMessageServiceUtil) ctx
				.getBean(LogMessageServiceUtil.class.getName());
		return util.logMessageService;
	}
	
	public static List queryLogInfo(String sql) throws Exception
	{
		return getLogMessageService().queryLogInfo(sql);
	}
	
	public static List getLogMessageByForm(LogMessageForm logMessageForm)
	{
		return getLogMessageService().getLogMessageByForm(logMessageForm);
	}

	public void setLogMessageService(LogMessageService logMessageService) {
		this.logMessageService = logMessageService;
	}

}
