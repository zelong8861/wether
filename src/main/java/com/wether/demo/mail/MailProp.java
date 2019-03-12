package com.wether.demo.mail;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Zhangchen on 2016/11/23.
 */
public class MailProp {
	private static Properties prop = null;
	private static String propFile = "mail.properties";
	private static boolean isInitialized = false;
	private static Executor executor;

	public static Properties getProp() {
		if (prop != null)
			return prop;
		return prop = new Properties();
	}

	private static String getPropertyFileName() {
		if (System.getProperty("baseDir") == null || System.getProperty("baseDir").trim().equals(""))
			System.setProperty("baseDir", new File("").getAbsolutePath());
		String baseDir = System.getProperty("baseDir");

		return baseDir + "/src/main/resources/" +propFile;
	}

	private static synchronized void init() {
		if (isInitialized)
			return;
		FileInputStream configStream = null;
		try {
			File configFile = new File(getPropertyFileName());
			configStream = new FileInputStream(configFile);
			getProp().load(configStream);
			isInitialized = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (configStream != null) {
					configStream.close();
					configStream = null;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	static String getProperty(String name) {
		init();
		return prop.getProperty(name);
	}
	
	public static Executor getExecutorPool(){
		if(executor != null){
			return executor;	
		}
		String poolSize = getProperty("emailSetting.poolSize");
		if (StringUtils.isNotBlank(poolSize)) {
			executor = Executors.newFixedThreadPool(Integer.parseInt(poolSize));
		} else {
			executor = Executors.newCachedThreadPool();
		}		
		return executor;
	}
}
