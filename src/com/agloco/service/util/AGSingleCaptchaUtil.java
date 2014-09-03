package com.agloco.service.util;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.portal.captcha.CaptchaTextException;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.GetterUtil;
import com.liferay.util.ParamUtil;

/**
 * <a href="CaptchaUtil.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Brian Wing Shun Chan
 * 
 */
public class AGSingleCaptchaUtil
{

	public static void check(PortletRequest req) throws CaptchaTextException
	{
		PortletSession ses = req.getPortletSession();

		String captchaText = (String) ses.getAttribute(WebKeys.CAPTCHA_TEXT);
		ses.setAttribute(WebKeys.CAPTCHA_TEXT,null);

		// Captcha should never be null, but on the rare occasion it is,
		// just let people register.

		if (captchaText == null
				|| !captchaText.equals(ParamUtil.getString(req, "captchaText")))
			throw new CaptchaTextException();
	}

}