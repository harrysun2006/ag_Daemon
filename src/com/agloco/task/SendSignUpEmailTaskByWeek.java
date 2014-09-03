package com.agloco.task;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.agloco.Constants;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.MailService;
import com.agloco.service.util.MemberServiceUtil;
import com.liferay.portal.spring.util.SpringUtil;

public class SendSignUpEmailTaskByWeek
{
	private final static String MAIL_SERVICE_NAME_SUFFIX = ".task";
	
	public SendSignUpEmailTaskByWeek()
	{
		System.out.println("SendSignUpEmailTaskByWeek init!");
		_log.info("SendSignUpEmailTaskByWeek init!");
	}

	public void sendMail()
	{
		int sendMailCount = 0;
		int faildMailCount = 0;

		try
		{
			Calendar now;
			Calendar sendMailTime;
			int count = 0;
			while (true)
			{
				now = Calendar.getInstance();
				now.set(Calendar.HOUR, 0);
				now.set(Calendar.SECOND, 0);
				now.set(Calendar.MINUTE, 0);

				List list = MemberServiceUtil.listAgMemberTempSendEmail(
						Constants.DEF_MAIL_SIGNUP_MAXSEND, Constants.DEF_MAIL_SIGNUP_INTERVAL_WEEKLY,now);

				if (list == null || list.size() < 1)
				{
					if(sendMailCount == 0)
						_log
							.info("No sign up email should to be sent,complete task successfully");
					return;
				}

				for (Iterator it = list.iterator(); it.hasNext();)
				{
					sendMailCount ++;
					AGMemberTemp temp = (AGMemberTemp) it.next();
					sendMailTime = Calendar.getInstance();
					try
					{
//						MailServiceUtil.sendSignupMail(temp, Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TIMING);
//						MailService mailService = MailServiceUtil.getMailService();
//						MailExcluder mailExcluder = (MailExcluder)SpringUtil.getContext().getBean(WeekTaskMailExcluder.class.getName());
//						mailService.setExcluder(mailExcluder);
						MailService mailService = (MailService)SpringUtil.getContext().getBean(MailService.class.getName()+MAIL_SERVICE_NAME_SUFFIX);
						mailService.sendSignupMail(temp, null, Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TIMING);
						
						//modified in mailservice
//						temp.setLastMailTime(sendMailTime);
//						temp.setMailCount(new Integer(temp.getMailCount()
//								.intValue() + 1));
						MemberServiceUtil.updateAGMemberTemp(temp);
						_log.info("Secceed: " + temp.getEmailAddress());
					}
					catch (Exception e) {
						// TODO: handle exception
						faildMailCount++;
						temp.setLastMailTime(sendMailTime);
						try
						{
							MemberServiceUtil.updateAGMemberTemp(temp);
						}
						catch(Exception e2)
						{
							_log.info("Failed to update lastMailTime: " + temp.getEmailAddress());
						}
						_log.info("Failed: " + temp.getEmailAddress());
					}
				}
				count++;
				if(count % 15 == 0) {
					long sleepTime = 1000 * 60 * 10;
					_log.info("SendSignUpEmailTaskByWeek sleep: " + sleepTime/60000 + " minutes!  Times:" + count/15);
					Thread.sleep(sleepTime);	// sleep 5 minutes
					count = 0;
				}
			}
		}
		catch (Throwable t)
		{
			_log.info("Send sign up email failure");
			t.printStackTrace();
		}
		finally
		{
			_log.info("Mail count: " + sendMailCount + ", Failed count: " + faildMailCount);
		}

	}

	private static Log _log = LogFactory.getLog(SendSignUpEmailTaskByWeek.class);
}
