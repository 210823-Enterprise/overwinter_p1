package com.overwinter.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;


public class Transaction {
	static Transaction transaction = new Transaction();
	static Savepoint savepoint = null;

	private Transaction() {
		super();

	}

	public static Savepoint getSavepoint() {
		return savepoint;
	}

//	public static void setSavepoint(Savepoint savepoint) {
//		Transaction.savepoint = savepoint;
//	}

	public Transaction beginTransaction(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}

	public Transaction commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}

	public Transaction rollBack(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}

	public Transaction rollBackWithSpecificSavePoint(Connection conn, Savepoint savepoint) {
		try {
			conn.rollback(savepoint);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getInstance();
	}

	public Transaction setSavePoint(Connection conn) {
		try {
			savepoint = conn.setSavepoint();
//			return savepoint;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		throw new NoSavePoint("Can't make a save point");
		return getInstance();
	}

	public Transaction setSavePointWithName(Connection conn, String name) {
		try {

			savepoint = conn.setSavepoint(name);
//			return savepoint;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		throw new NoSavePoint("Can't make a save point");
		return getInstance();
	}

	public static Transaction getInstance() {
		return transaction;
	}
}
