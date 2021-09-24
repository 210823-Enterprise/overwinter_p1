package com.overwinter.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class OverwinterDataSource {
	// If you were doing this in your own project, or in a REAL application use the application.properties file
	// and the Properties object in Java which you use to READ from that file and supply the credentials
	static Logger log = Logger.getLogger(OverwinterDataSource.class);
	private static String driver = "";
	private static String url = "";
	private static String username = "";
	private static String password = "";
	private static int poolsize = 5;
	private static GenericObjectPool gPool = null;
	
	public OverwinterDataSource() {
		super();
	}

	public OverwinterDataSource(OverwinterCfg config) {
		super();
		driver = config.getDriver();
		url = config.getUrl();
		username = config.getUsername();
		password = config.getPassword();
		poolsize = config.getPoolSize();
		log.info("new OverwitnerDataSource created: "+this);
	}
 	// Apache Common dbcp gives us the functionality to create a connection pool. But we have to do so
	// by using its specific class and functionality called GenericObjectPool
	public DataSource setUpPool () throws Exception {
		// We use the DataSource Interface to create a connection object that
		// participates in Connection Pooling
		Class.forName(driver);

		// create an instance of GernericObjectPool that holds our Pool of connections
		// objects
		gPool = new GenericObjectPool();
		gPool.setMaxActive(poolsize);

		// Create a connectionFactory Object which will be used by the pool object to
		// creates the connections (which are all objects)
		ConnectionFactory cf = new DriverManagerConnectionFactory(url, username, password);

		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
		log.info(gPool+ " used to create a pool");
		return new PoolingDataSource(gPool);
	}

	public GenericObjectPool getConnectionPool() {
		return gPool;
	}

	// for our own benefit lets create a method to print the connection pool status
	// for now 
	public void printDbStatus() {
		System.out.println("Max: " + getConnectionPool().getMaxActive() + "; Active: "
				+ getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
	}
}
