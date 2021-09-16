package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.overwinter.util.MetaModel;

public class ObjectRemover extends ObjectMapper {
	public boolean removeObjectFromDb(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey= model.getPrimaryKey().getName();
		String sql = "DELETE FROM "+ model.getEntity().getTableName()+"WHERE"+ primaryKey+"= ?;";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt =	setStatement(pstmt, pd, null, obj, 1);
			ResultSet rs = pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	static public ObjectRemover getInstance() {
		return this;
	}
}
