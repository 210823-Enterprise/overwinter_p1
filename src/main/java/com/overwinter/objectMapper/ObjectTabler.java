package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectTabler extends ObjectMapper{
	static final ObjectTabler ob = new ObjectTabler();

	public <T> boolean addTabletoDb(Class<T> clazz, Connection conn) {
		MetaModel<?> model = MetaModel.of(clazz.getClass());
		String primaryKey = model.getPrimaryKey().getName();
		String sql = "CREATE TABLE " + model.getClassName() + "(" + primaryKey + " PRIMARY KEY";
		for (ColumnField c : model.getColumns()) {
			sql += "," + c.getColumnName() + " " + c.getType() + "";
		}
		sql += ");";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
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
