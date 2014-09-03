package com.agloco.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.agloco.service.util.CommonServiceUtil;

/**
 * 
 * @author terry_zhao
 *
 */
public class DictionaryUtil {
	
	private static Map objMap = null;
	private static Map strMap = null;
	private static String CONNECT_STRING = "-";
	private static String EMPTY_STRING = "";
	
	private synchronized static List getModelList(String code,String language,String country){
		
		if(objMap == null){
			objMap = new HashMap();
		}
		
		List list = (List)objMap.get(code+CONNECT_STRING+language+CONNECT_STRING+country); 
		if(list != null){
			return list;
		}
		
		list = CommonServiceUtil.listAGDictionaryModel(code, language,country);
		if(list != null){
			objMap.put(code+CONNECT_STRING+language+CONNECT_STRING+country, list);
			return list;
		}
		return null;
		
	}
	
	private synchronized static String getDictionaryText(String code,String value,String language,String country){
		
		if(strMap == null){
			strMap = new HashMap();
		}
		
		String text = getTextFromMap(code, value, language, country);
		if(text != null){
			return text;
		}
		
		List list = CommonServiceUtil.listAGDictionaryText(code);
		
		if(list != null){
			for(Iterator it = list.iterator(); it.hasNext();){
				Object[] str = (Object[])it.next();
				String l = StringUtils.isBlank((String)str[2]) ? EMPTY_STRING:(String)str[2];
				String c = StringUtils.isBlank((String)str[3]) ? EMPTY_STRING:(String)str[3];
				strMap.put(code+CONNECT_STRING+(String)str[0]+CONNECT_STRING+l+CONNECT_STRING+c, CharsetUtil.Unicode2UTF8((String)str[1]));
			}
		}
		
		text = getTextFromMap(code, value, language, country);
		if(text != null){
			return text;
		}
		return null;
		
	}

	private static String getTextFromMap(String code, String value, String language, String country) {
		
		String s = (String)strMap.get(code+CONNECT_STRING+value+CONNECT_STRING+language+CONNECT_STRING+country); 
		if( s!= null){
			return s;
		}
		s = (String)strMap.get(code+CONNECT_STRING+value+CONNECT_STRING+language+CONNECT_STRING+EMPTY_STRING);;
		if(s != null){
			return s; 
		}
		s = (String)strMap.get(code+CONNECT_STRING+value+CONNECT_STRING+EMPTY_STRING+CONNECT_STRING+EMPTY_STRING); 
		if(s != null){
			return s; 
		}
		return null;
	}
	
	public static void refresh(){
		
		if(objMap != null && objMap.size() > 0){
			objMap.clear();
		}
		if(strMap != null && strMap.size() > 0){
			strMap.clear();
		}
		
	}

	public static List listDictionaryModel(String code,Locale locale){
		return getModelList(code,locale.getLanguage(),locale.getCountry());
	}
	
	public static List listDictionaryModel(String code){
		return listDictionaryModel(code, Locale.getDefault());
	}
	
	public static String ListDictionaryText(String code,String value,Locale locale){
		return getDictionaryText(code, value, locale.getLanguage(), locale.getCountry());
	} 
	
	public static String ListDictionaryText(String code,String value){
		return ListDictionaryText(code, value, Locale.getDefault());
	}
	
	public static class DictionaryResourceBundle extends ResourceBundle {

		public DictionaryResourceBundle() {
			
		}

    /**
     * Gets an object for the given key from this resource bundle.
     * Returns null if this resource bundle does not contain an
     * object for the given key.
     *
     * @param key the key for the desired object
     * @exception NullPointerException if <code>key</code> is <code>null</code>
     * @return the object for the given key, or null
     */
    protected Object handleGetObject(String key) {
    	return null;
    }

    /**
     * Returns an enumeration of the keys.
     *
     */
    public Enumeration getKeys() {
    	return null;
    }
	}
}
