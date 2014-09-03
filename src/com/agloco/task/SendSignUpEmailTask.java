package com.agloco.task;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.agloco.Constants;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.MailService;
import com.agloco.service.util.MailServiceUtil;
import com.agloco.service.util.MemberServiceUtil;
import com.liferay.portal.spring.util.SpringUtil;

public class SendSignUpEmailTask
{
	
	private final static String MAIL_SERVICE_NAME_SUFFIX = ".task";
	public static final int LES_MAIL_COUNT = 1;

	public SendSignUpEmailTask()
	{
		System.out.println("SendSignUpEmailTask init!");
		_log.info("SendSignUpEmailTask init!");
	}

	protected SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public void sendMail()
	{

		Session session = null;
		int sendMailCount = 0;
		int faildMailCount = 0;

		try
		{
			Calendar now;
			Calendar sendMailTime;

			session = SessionFactoryUtils.getSession(sessionFactory, true);
			session.setFlushMode(FlushMode.NEVER);
			TransactionSynchronizationManager.bindResource(sessionFactory,
					new SessionHolder(session));

			while (true)
			{
				now = Calendar.getInstance();
				now.set(Calendar.SECOND, 0);
				now.set(Calendar.MINUTE, 0);

				List list = MemberServiceUtil.listNewAgMemberTempSendEmail(
						LES_MAIL_COUNT, Constants.DEF_MAIL_SIGNUP_INTERVAL,
						Constants.DEF_MAIL_SIGNUP_AGO, now);

				if (list == null || list.size() < 1)
				{
					if (sendMailCount == 0)
						_log
								.info("No sign up email should to be sent,complete task successfully");
					return;
				}

				for (Iterator it = list.iterator(); it.hasNext();)
				{
					sendMailCount++;
					AGMemberTemp temp = (AGMemberTemp) it.next();
					sendMailTime = Calendar.getInstance();
					try
					{
						MailService mailService = (MailService)SpringUtil.getContext().getBean(MailService.class.getName()+MAIL_SERVICE_NAME_SUFFIX);
						mailService.sendSignupMail(temp, null, Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1);
						
//						MailServiceUtil.sendSignupMail(temp, Locale
//								.getDefault(),
//								Constants.ARTICLEID_AGS_SIGNUP_EMAIL_TEMP1);

//						temp.setLastMailTime(sendMailTime);
//						temp.setMailCount(new Integer(temp.getMailCount()
//								.intValue() + 1));
						MemberServiceUtil.updateAGMemberTemp(temp);
						_log.info("Secceed: " + temp.getEmailAddress());
					}
					catch (Exception e)
					{
						// TODO: handle exception
						faildMailCount++;
						temp.setLastMailTime(sendMailTime);
						try
						{
							MemberServiceUtil.updateAGMemberTemp(temp);
						}
						catch (Exception e2)
						{
							_log.info("Failed to update lastMailTime: "
									+ temp.getEmailAddress());
						}
						_log.info("Failed: " + temp.getEmailAddress());
					}
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
			_log.info("Mail count:"+sendMailCount+"    Failed count:"+faildMailCount);
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.releaseSession(session, sessionFactory);
		}
	}

	private static Log _log = LogFactory.getLog(SendSignUpEmailTask.class);
}
