package com.overwinter.config;

public class OverwinterCfg {
 
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private int poolSize;
	
	public OverwinterCfg(String url, String username, String password, int poolSize, String driver) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
		this.poolSize = poolSize;
		this.driver = driver;
	}

	public OverwinterCfg() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "OverwinterCfg []";
	}
	
	
}
