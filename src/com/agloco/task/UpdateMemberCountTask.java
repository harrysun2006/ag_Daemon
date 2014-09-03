package com.agloco.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.log4j.LogUtil;
import com.agloco.service.util.MemberServiceUtil;

public class UpdateMemberCountTask {
	
	Log _log = LogFactory.getLog(UpdateMemberCountTask.class);

	public UpdateMemberCountTask() {
		System.out.println("UpdateMemberCountTask init!");
		_log.info("UpdateMemberCountTask init!");
	}

	public void update(){
		
		try{
			
			if(_log.isInfoEnabled()){
				_log.info("update member count task begin to execute...");
			}

			MemberServiceUtil.updateAGMemberCountTask();
			MemberServiceUtil.clearMemberServiceUtilPool();
			
			if(_log.isInfoEnabled()){
				_log.info("update member count task execute successfully!");		
			}

		}
		catch(Exception e){
			LogUtil.error("UpdateMemberCountTask is error", e);
		}

	}
}
