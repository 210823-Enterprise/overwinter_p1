package com.overwinter.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
	static Transaction transaction = new Transaction();
	
	private Transaction() {
		super();
		
	}
	
	public static Transaction beginTransaction(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}
	
	public static Transaction commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}
	public static Transaction getInstance() {
		return transaction;
	}
}
