package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectTabler extends ObjectMapper{
	static final ObjectTabler ob = new ObjectTabler();
	private static Logger log = Logger.getLogger(ObjectTabler.class);
	public <T> boolean addTabletoDb(Class<T> clazz, Connection conn) {
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName();
		PreparedStatement pstmt;
		String sql = "DROP TABLE IF EXISTS " + model.getSimpleName().toLowerCase() + " Cascade;\n CREATE TABLE " + model.getSimpleName().toLowerCase() + "(" + primaryKey + " SERIAL PRIMARY KEY";
		for (ColumnField c : model.getColumns()) {
			switch(c.getType().getSimpleName()) {
			case "String" : sql += "," + c.getColumnName() + " VARCHAR(50) NOT NULL";
			break;
			case "Integer" : sql += "," + c.getColumnName() + " INT";
			break;
			case "Boolean" : sql += "," + c.getColumnName() + " BOOLEAN";
			break;
			case "double" : sql += "," + c.getColumnName() + " NUMERIC";
			break;
			}
		}
		sql += ");";
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt.execute();
			log.info("\nTable created using "+pstmt);
			return true;
		} catch (SQLException e) {
			log.error("\nSQL error in ObjectTabler");
			e.printStackTrace();
		}
		return false;
	}

	static public ObjectTabler getInstance() {
		return ob;
	}
}
