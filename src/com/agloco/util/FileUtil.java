package com.agloco.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {

	private static Log log = LogFactory.getLog(FileUtil.class);
	
	public static Set readFile(String fileName)throws IOException {
		File f = new File(fileName);
		return readFile(f);
	}
	
	public static Set readFile(File f) throws IOException {
		
		Set set = new HashSet();
		if(f == null){
			return set;
		}
		
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String email = null;
			while((email = br.readLine()) != null){
				if(StringUtils.isNotBlank(email.trim().toLowerCase())){
					set.add(email.trim().toLowerCase());	
				}
			}
		} catch (FileNotFoundException e) {
			if(log.isDebugEnabled()){
				log.error("file: " + f.getName() + " not found!");
			}
			throw e;
		} catch (IOException e) {
			if(log.isDebugEnabled()){
				log.error("read file: " + f.getName() + " failed!");
			}
			throw e;
		}
		return set;
		
	}
}
