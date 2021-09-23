package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.overwinter.config.OverwinterDataSource;
import com.overwinter.util.MetaModel;

public class ObjectRemover extends ObjectMapper {
	static final ObjectRemover ob = new ObjectRemover();
	static Logger log = Logger.getLogger(ObjectRemover.class);
	public boolean removeObjectFromDb(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey= model.getPrimaryKey().getColumnName();
		String sql = "DELETE FROM "+ model.getSimpleName()+" WHERE "+ primaryKey+" = ?;";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt =	setStatement(pstmt, pd, model.getGetterMethod(primaryKey), obj, 1);
			log.error(pstmt+" executing in ObjectRemover");
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("SQLException error in ObjectRemover");
		}
		return false;
	}
	static public ObjectRemover getInstance() {
		return ob;
	}
}