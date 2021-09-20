package com.overwinter.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class OverwinterCfg {
 
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static int poolSize;
//	private static String poolSize;

	// Modified Kirk's file by adding few more fields


	// No args constructor
	public OverwinterCfg() {
		super();
	}

	/**
	 * This constructor is for when user want to input the specific or want to at
	 * the very least want to override
	 * 
	 * @param driver   model,
	 * @param url      database,
	 * @param username for database,
	 * @param password for database,
	 * @param poolSize for connection pool
	 */
	public OverwinterCfg(String driver, String url, String username, String password, int poolSize) {
		super();
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.poolSize = poolSize;
		this.driver = driver;
	}

	// Im not sure what OverwinterCfg.poolSize means

//	public static void setPoolSize(int poolSize) {
//		OverwinterCfg.poolSize = poolSize;
//	}
//	
//	public static void setDriver(String driver) {
//	OverwinterCfg.driver = driver;
//}
//	
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}

	// Commented out toString() method since we only use this class to extract info
//	@Override
//	public String toString() {
//		return "OverwinterCfg []";
//	}

	/**
	 * This will allow us to extract properties values from file path
	 * 
	 * @param filePath
	 */
	public OverwinterCfg configure(String filePath) {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader(filePath));
			if (prop.getProperty("driver").isEmpty() && prop.getProperty("poolsize").isEmpty()) {
				OverwinterCfg cfg = new OverwinterCfg("org.postgresql.Driver", prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"), 5);
				return cfg;
			}
			else if (prop.getProperty("poolsize").isEmpty()) {
				OverwinterCfg cfg = new OverwinterCfg(prop.getProperty("driver"), prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"), 5);
				return cfg;
			} else if (prop.getProperty("driver").isEmpty()) {
				OverwinterCfg cfg = new OverwinterCfg("org.postgresql.Driver", prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"), Integer.valueOf(prop.getProperty("poolsize")));
				return cfg;
			} else {
				OverwinterCfg cfg = new OverwinterCfg(prop.getProperty("driver"), prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"), Integer.valueOf(prop.getProperty("poolsize")));
				return cfg;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getPoolSize() {
		return poolSize;
	}

}
