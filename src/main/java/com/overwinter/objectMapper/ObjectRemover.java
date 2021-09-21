package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.overwinter.util.MetaModel;

public class ObjectRemover extends ObjectMapper {
	static final ObjectRemover ob = new ObjectRemover();
	public boolean removeObjectFromDb(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey= model.getPrimaryKey().getColumnName();
		String sql = "DELETE FROM "+ model.getSimpleName()+" WHERE "+ primaryKey+" = ?;";
		System.out.print(sql);
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			System.out.println();
			System.out.println("PRIME: "+model.getGetterMethod(primaryKey));
			pstmt =	setStatement(pstmt, pd, model.getGetterMethod(primaryKey), obj, 1);
			ResultSet rs = pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	static public ObjectRemover getInstance() {
		return ob;
	}
}