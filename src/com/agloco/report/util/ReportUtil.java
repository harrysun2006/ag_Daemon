package com.agloco.report.util;

import java.io.File;

import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.mail.MailMessage;
import com.agloco.mail.MailPart;
import com.agloco.mail.Part;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.util.ConfigUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;

public class ReportUtil
{
	public static String processComma(String context)
	{
		if(context.indexOf(',')>0)
		{
			return "\"" + context + "\"";
		}
		else
			return context;
	}
	
	public static void sendReportMail(String fileName, String queryType) {
		String msg;
		try {
			String subject = queryType + ConfigUtil.getConfig(REPORT_EMAIL_SUBJECT, COMPANY_ID);
				
			String body = " ";
			String fromName = PrefsPropsUtil.getString(COMPANY_ID,
					PropsUtil.ADMIN_EMAIL_FROM_NAME);
			String fromAddress = PrefsPropsUtil.getString(COMPANY_ID,
					PropsUtil.ADMIN_EMAIL_FROM_ADDRESS);
			String toAddress = ConfigUtil.getConfig(REPORT_EMAIL_TO_ADDRESS, COMPANY_ID);
			String ccAddress = ConfigUtil.getConfig(REPORT_EMAIL_CC_ADDRESS, COMPANY_ID);
			String bccAddress = ConfigUtil.getConfig(REPORT_EMAIL_BCC_ADDRESS, COMPANY_ID);
			InternetAddress from = new InternetAddress(fromAddress, fromName);
			MailMessage message = new MailMessage(from, null, subject, body, false);
			message.setRecipient(toAddress);
			message.setCarbonCopy(ccAddress);
			message.setBackgroundCopy(bccAddress);
			
			Part attachment = new MailPart();
			attachment.setContent(new File(fileName));
			message.addAttachment(attachment);
			MailServiceUtil.sendMail(message);

			msg = "Report email send successfully ";
			_log.info(msg);
		} catch (Exception e) {
			msg = "Report failed";
			_log.error(msg, e);
		} 
	}

	private final static String REPORT_EMAIL_SUBJECT = "report.email.subject";
	private final static String REPORT_EMAIL_TO_ADDRESS = "report.email.to.address";
	private final static String REPORT_EMAIL_CC_ADDRESS = "report.email.cc.address";
	private final static String REPORT_EMAIL_BCC_ADDRESS = "report.email.bcc.address";
	private final static String COMPANY_ID = "agloco.com";

	private static Log _log = LogFactory.getLog(ReportUtil.class);
}
