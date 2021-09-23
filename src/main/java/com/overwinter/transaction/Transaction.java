package com.overwinter.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.overwinter.OverWinterORM;

public class Transaction {
	static Logger log = Logger.getLogger(Transaction.class);
	static Transaction transaction = new Transaction();

	private Transaction() {
		super();
	}

	public Transaction beginTransaction(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("SQL error in transaction.beginTransaction");
		}
		return getInstance();
	}

	public Transaction commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			log.error("SQL error in transaction.commit");
		}
		return getInstance();
	}

	public static Transaction getInstance() {
		return transaction;
	}
}
