package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.overwinter.annotations.Entity;
import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectTabler extends ObjectMapper{
	static final ObjectTabler ob = new ObjectTabler();

	public <T> boolean addTabletoDb(Class<T> clazz, Connection conn) {
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getName();
		String sql = "CREATE TABLE " + model.getSimpleName() + "(" + primaryKey + " SERIAL PRIMARY KEY";
		for (ColumnField c : model.getColumns()) {
			switch(c.getType().getSimpleName()) {
			case "String" : sql += "," + c.getColumnName() + " VARCHAR(50) NOT NULL";
			break;
			case "Integer" : sql += "," + c.getColumnName() + " NUMERIC(50)";
			break;
			case "Boolean" : sql += "," + c.getColumnName() + "BOOLEAN";
			break;
			}
		}
		sql += ");";
		System.out.println(sql);
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			System.out.println("What? " + conn.prepareStatement(sql));
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt = setStatement(pstmt, pd, null, clazz, 1);
			ResultSet rs = pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	static public ObjectTabler getInstance() {
		return ob;
	}
}
