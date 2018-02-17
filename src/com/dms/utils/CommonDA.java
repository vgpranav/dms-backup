package com.dms.utils;

import java.io.InputStream;
import java.util.Properties;

public class CommonDA {
	private static Properties properties;
	
	public static synchronized Properties getProperties() {
		if(properties != null)
			return properties;
		try {
			 
			InputStream input = null;
			String filename = "config.properties";
			properties = new Properties();
			input = CommonDA.class.getClassLoader().getResourceAsStream(filename);
    		if(input==null){
    	            System.out.println("Sorry, unable to find " + filename);
    		}

    		//load a properties file from class path, inside static method
    		properties.load(input);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	 
	 
}
