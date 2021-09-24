package com.overwinter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.overwinter.config.OverwinterCfg;
import com.overwinter.config.OverwinterDataSource;
import com.overwinter.objectMapper.ObjectCache;
import com.overwinter.objectMapper.ObjectGetter;
import com.overwinter.objectMapper.ObjectInsert;
import com.overwinter.objectMapper.ObjectMapper;
import com.overwinter.objectMapper.ObjectRemover;
import com.overwinter.objectMapper.ObjectTabler;
import com.overwinter.objectMapper.ObjectUpdate;
import com.overwinter.transaction.Transaction;

import jdk.internal.org.jline.utils.Log;

public class OverWinterORM {
	static Logger log = Logger.getLogger(ObjectRemover.class);
	final private static OverWinterORM overWinterORM = new OverWinterORM();
	Connection conn = null;
	DataSource dataSource = null;
	private final ObjectRemover obj_remover = ObjectRemover.getInstance();
	private final ObjectGetter obj_getter = ObjectGetter.getInstance();
	private final ObjectTabler obj_table = ObjectTabler.getInstance();
	private final ObjectUpdate obj_updater = ObjectUpdate.getInstance();
	private final ObjectInsert obj_insert = ObjectInsert.getInstance();
	private final Transaction transaction = Transaction.getInstance();
	private final ObjectCache obj_cache = ObjectCache.getInstance();
	// obj getter, etc.....
	OverwinterDataSource pool = new OverwinterDataSource(new OverwinterCfg().configure("./src/test/resources/test_application.properties"));
	
	private OverWinterORM() {
		try {
			dataSource = pool.setUpPool();
			conn = dataSource.getConnection();
			log.info("\nNew OverWinterORM launched:"+this);
		} catch (SQLException e) {
			log.info("\nSQLException thrown in OverwinterDataSource");
		} catch (Exception e) {
			log.info("\nGeneral Exception thrown in OverwinterDataSource");
		}
	}

	// return a a static instance of this singleton class
	public static OverWinterORM getInstance() {
		return overWinterORM;
	}

	// when someone wants to delete an object from their database
	// DIYORM.deleteObjFromDB
	public boolean deleteObjFromDB(Object obj) {

		return obj_remover.removeObjectFromDb(obj, conn);

	}

	public Optional<List<Object>> getListObjectFromDB(Object obj) throws InstantiationException, IllegalAccessException {
		return obj_getter.getListObjectFromDB(obj.getClass(), conn);

	}
	public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions){
		return obj_getter.getListObjectFromDB(clazz, conn, columns, conditions, null);
	}

	public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators){
		return obj_getter.getListObjectFromDB(clazz, conn, columns, conditions, operators);
	}

	public Object addTabletoDb(Class<?> clazz) {
		return obj_table.addTabletoDb(clazz, conn);

	}

	public Object updateObjFromDB(Object obj) {

		return obj_updater.updateObjectFromDB(obj, conn);

	}

	public void insertObjIntoDB(Object obj) {
		obj_insert.insertObjectIntoDB(obj, conn);
	}
	
	public Transaction beginTransaction() {
		return transaction.beginTransaction(conn);
	}
	
	public Transaction commit() {
		return transaction.commit(conn);
	}

	public Transaction rollBack() {
		return transaction.rollBack(conn);
	}
	
	public Transaction rollBackWithSpecificSavePoint(Savepoint savepoint) {
		return transaction.rollBackWithSpecificSavePoint(conn, savepoint);
	}
	
	public Transaction setSavePoint() {
		
		return transaction.setSavePoint(conn);
	}
	
	public Transaction setSavePointWithName(String name) {
		return transaction.setSavePointWithName(conn, name);
	}
	
	public boolean addAllFromDBToCache(final Class<?> clazz) {
		// this method will call the first time user login
		Optional<List<Object>> list = obj_getter.getListObjectFromDB(clazz, conn);
		return obj_cache.addAllFromDBToCache(clazz, list);
	}
	
	public ObjectCache putObjectInCache(Object obj) {
		return obj_cache.putObjectInCache(obj);
	}

	public Transaction getTransaction() {
		return transaction;
	}
	
	
	
}
