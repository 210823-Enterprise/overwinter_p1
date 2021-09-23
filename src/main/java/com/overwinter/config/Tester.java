package com.overwinter.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		OverwinterCfg config = new OverwinterCfg();
//		// I am able to get resources from the path
//		config.configure("C:\\Users\\Ethan\\Desktop\\overwinter_p1\\src\\test\\resources\\test_application.properties");
//		System.out.println("the driver should be " + config.getDriver());
//		System.out.println("the url should be " + config.getUrl());
//		System.out.println("the username should be " + config.getUsername());
//		System.out.println("the password should be " + config.getPassword());
//		System.out.println("the poolsize should be " + config.getPoolSize());
		
		// let make one where user input the values themselves
//		OverwinterCfg config = new OverwinterCfg("driver", "url", "name", "pass", 5);
//		System.out.println("the driver should be " + config.getDriver());
//		System.out.println("the url should be " + config.getUrl());
//		System.out.println("the username should be " + config.getUsername());
//		System.out.println("the password should be " + config.getPassword());
//		System.out.println("the poolsize should be " + config.getPoolSize());
		
		// Ok I was able to extract the values let's input those in the database
		OverwinterCfg config = new OverwinterCfg();
		config.configure(".\\src\\test\\resources\\test_application.properties");
		//System.out.println(System.getProperty("user.dir"));
		OverwinterDataSource pool = new OverwinterDataSource(config);
		ResultSet rs = null;
		Connection connObj = null;
		PreparedStatement stmt = null;
		
		// surround everything in try/catch
		try {
			DataSource dataSource = pool.setUpPool();
			pool.printDbStatus();
			
			// get the connection (from the pool)
			System.out.println("============= Making a new connection OBject for a DB operation! =============");
			connObj = dataSource.getConnection();
			
			// print the dbStatus()
			pool.printDbStatus();
			
			// use the fetch connection to execute a query (like select * from heroes)
			stmt = connObj.prepareStatement("SELECT * FROM heroes");
			
			rs = stmt.executeQuery();
			// iterate over the result set returned
			while(rs.next()) {
				System.out.println("Hero name is " + rs.getString("hero_name"));
			}
			
			pool.printDbStatus();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// release the db connection object back to the pool
			System.out.println("\n ============== RELEASING THE DB CONNECTION OBJECT BACK TO THE POOL ==============");
			System.out.println("Failed to do so");
//			try {
//				connObj.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // close the specific connection so it can be sent back to the pool
			// this sends the connection to the IDLE state...it can used again
		}
	}

}
