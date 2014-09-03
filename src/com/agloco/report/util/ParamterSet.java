package com.agloco.report.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.agloco.exception.DateFormatInvalidException;

public class ParamterSet
{
	public static Map getParamter(Map params)
	{
		// TODO Auto-generated method stub
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timezone = params.get("timezone").toString();
		if(timezone != null && timezone.length() > 0)
			df.setTimeZone(TimeZone.getTimeZone(timezone));
		Date beginDate = null;
		Date endDate = null;
			try
			{
				if(params.get("beginDate")!=null)
					beginDate = df.parse(params.get("beginDate").toString());
				if(params.get("endDate")!=null)
					endDate = df.parse(params.get("endDate").toString());
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				
				throw new DateFormatInvalidException();
				// TODO Auto-generated catch block
			}

		params.put("param1", beginDate);
		params.put("param2", endDate);
		
		return params;
	}
}
