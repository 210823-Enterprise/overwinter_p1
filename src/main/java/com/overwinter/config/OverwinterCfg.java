package com.overwinter.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class OverwinterCfg {
	static Logger log = Logger.getLogger(OverwinterCfg.class);
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
		log.info("new OverwinterCfg created: "+this);
	}
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
		log.warn("OverwinterCfg.configure() returned null");
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
