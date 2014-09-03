package com.agloco.task;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.mail.MailProducer;
import com.agloco.model.AGMailMessage;
import com.agloco.service.util.CommonServiceUtil;

public class ScanMailQueueTask {

	private final static Log _log = LogFactory.getLog(ScanMailQueueTask.class);
	private final static int PAGE_SIZE = 50;

	public ScanMailQueueTask() {
		System.out.println("ScanMailQueueTask init!");
		_log.info("ScanMailQueueTask init!");
	}

	public void scan(){
		
		int count = CommonServiceUtil.getAGMailMessageNumber();
		if(count < 1){
			return;
		}
		int i = 0;
		
		while(true){
			
			List list = CommonServiceUtil.listAGMailMessage(PAGE_SIZE);
			if(list == null || list.size() < 1){
				return;
			}
			
			if(_log.isInfoEnabled()){
				_log.info("there are " + list.size() + " messages need to be sent");
			}
			
			for(Iterator it = list.iterator(); it.hasNext();){
				
				AGMailMessage msg = (AGMailMessage)it.next();
				if(msg == null){
					continue;
				}
	
				try {
					MailProducer.produce(msg.getSerialiableMsg());
					CommonServiceUtil.deleteAGMailMessage(msg);
				} 
				catch (SQLException e) {
					if(_log.isErrorEnabled()){
						_log.error("from mysql get serializable message error", e);
					}
				}
				
				i++;
				if(i >= count){
					return;
				}
				
			}
			
			try {
				Thread.sleep(1000*10);
			} catch (InterruptedException e) {
				if(_log.isErrorEnabled()){
					_log.error("thread sleep error", e);
				}
			}
		}
		
	}
	
	
}
