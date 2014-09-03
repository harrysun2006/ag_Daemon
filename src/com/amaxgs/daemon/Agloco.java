package com.amaxgs.daemon;

import org.springframework.context.ApplicationContext;

import com.agloco.service.MemberService;
import com.liferay.portal.spring.util.SpringUtil;

public class Agloco {

	public static void main(String[] args) throws Exception {
		try {
			ContextHelper.addResource("jdbc.properties");
			ContextHelper.addResource("mail.properties");
			ApplicationContext ctx = SpringUtil.getContext();
			MemberService service = (MemberService) ctx.getBean(MemberService.class.getName());
			if (service != null) {
				System.out.println("Daemon started successfully!");
			} else {
				System.out.println("Daemon started failed!");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
}
