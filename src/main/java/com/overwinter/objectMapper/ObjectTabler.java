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

	public boolean AddTabletoDb(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey = model.getPrimaryKey().getName();
		String sql = "CREATE TABLE " + model.getClassName() + "(" + model.getPrimaryKey() + " PRIMARY KEY,";
		for (ColumnField c : model.getColumns()) {
			sql += "," + c.getColumnName() + " " + c.getType() + "";
		}
		sql += ");";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt = setStatement(pstmt, pd, null, obj, 1);
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
