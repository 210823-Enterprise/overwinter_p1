package com.overwinter;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.overwinter.objectMapper.ObjectCache;
import com.overwinter.objectMapper.ObjectGetter;
import com.overwinter.objectMapper.ObjectInsert;
import com.overwinter.objectMapper.ObjectRemover;
import com.overwinter.objectMapper.ObjectTabler;
import com.overwinter.objectMapper.ObjectUpdate;
import com.overwinter.transaction.Transaction;

public class OverWinterORM {
	static Logger log = Logger.getLogger(ObjectRemover.class);
	final private static OverWinterORM overWinterORM = new OverWinterORM();
	Connection conn = null;
	private final ObjectRemover obj_remover = ObjectRemover.getInstance();
	private final ObjectGetter obj_getter = ObjectGetter.getInstance();
	private final ObjectTabler obj_table = ObjectTabler.getInstance();
	private final ObjectUpdate obj_updater = ObjectUpdate.getInstance();
	private final ObjectInsert obj_insert = ObjectInsert.getInstance();
	private final Transaction transaction = Transaction.getInstance();
	private final ObjectCache obj_cache = ObjectCache.getInstance();

	private OverWinterORM() {
		try {
			conn = getConn();
			log.info("New OverWinterORM launched:"+this);
		}
		catch (Exception e) {
			log.info("General Exception thrown in OverwinterDataSource");
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
	public Object getObjectFromDB(Class<?> clazz,int id) throws InstantiationException, IllegalAccessException {
		return obj_getter.getObjectFromDB(clazz, conn, id);

	}
	public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions){
		return obj_getter.getListObjectFromDB(clazz, conn, columns, conditions, null);
	}

	public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators){
		return obj_getter.getListObjectFromDB(clazz, conn, columns, conditions, operators);
	}

	public boolean addTabletoDb(Class<?> clazz) {
		return obj_table.addTabletoDb(clazz, conn);
	}

	public Object updateObjFromDB(Object obj) {
		return obj_updater.updateObjectFromDB(obj, conn);
	}

	public boolean insertObjIntoDB(Object obj) {
		return obj_insert.insertObjectIntoDB(obj, conn);
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

	public HashMap<Class<?>, HashSet<Object>> addAllFromDBToCache(final Class<?> clazz) {
		// this method will call the first time user login
		Optional<List<Object>> list = obj_getter.getListObjectFromDB(clazz, conn);
		return obj_cache.addAllFromDBToCache(clazz, list);
	}

	public HashMap<Class<?>, HashSet<Object>> putObjectInCache(Object obj) {
		return obj_cache.putObjectInCache(obj);
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public HashSet<Object> getCache(Class<?> clazz) {
		return obj_cache.getCache().get(clazz);
	}
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection connection) {
		this.conn = connection;
	}

}
