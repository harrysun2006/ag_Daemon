package com.agloco.report.util;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agloco.Constants;
import com.agloco.model.AGQuery;

/**
 * 
 * @author Erick Kong
 * 
 */
public class ReportScriptUtil
{
	public static Map prefixScript(AGQuery aq)
	{
		if(aq.getPreScript() == null || aq.getPreScript().length()<1)
		{
			return null;
		}
		
		ClassLoader parent = ReportScriptUtil.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class groovyClass;

		try
		{
			groovyClass = loader.parseClass(aq.getPreScript());
			GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
			Map params = new HashMap(); 
				
			groovyObject.invokeMethod("getParamter",params);
			
			params.put("pwdparam", Constants.AGLOCO_AESKEY.toString());
//			Map params = new ParamterSet().getParamter();
			
			return params;
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static AGReportResultList suffixScript(AGQuery aq,List list)
	{
		AGReportResultList rl = new AGReportResultList();

		//If AGQuery has no result process script, set list to AGReportResultList simply
		if(aq.getPostScript() == null || aq.getPostScript().length()<1)
		{
			rl.setList(list);
			return rl;
		}
//		ClassLoader parent = ReportScriptUtil.class.getClassLoader();
//		GroovyClassLoader loader = new GroovyClassLoader(parent);
//		Class groovyClass;

//		try
//		{
////			System.out.println(aq.getResultProcessScript());
//			groovyClass = loader.parseClass(aq.getPostScript());
//			GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
//			rl = (AGReportResultList)groovyObject.invokeMethod("processResult", list);
////			rl = new ResultProcess().processResult(list);
//			return rl;
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		rl = (AGReportResultList)executeScript(aq.getPostScript(),"processResult",list);
		return rl;
	}
	
	public static Object executeScript(String script,String method,Object param)
	{
		ClassLoader parent = ReportScriptUtil.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class groovyClass;
		Object obj = null;

		groovyClass = loader.parseClass(script);
		GroovyObject groovyObject;
		try
		{
			groovyObject = (GroovyObject) groovyClass.newInstance();
			obj = groovyObject.invokeMethod(method, param);
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;

	}

}
