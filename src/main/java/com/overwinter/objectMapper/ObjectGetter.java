package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectGetter extends ObjectMapper {
	static ObjectGetter oG = new ObjectGetter();
	public Object getObjectFromDb(Class<?> clazz,Connection conn) {
		Object c = new Object();
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey= model.getPrimaryKey().getName();
		String sql = "SELECT * FROM "+ clazz.getSimpleName()+"WHERE"+ primaryKey+"= ?;";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt=setStatement(pstmt, pd, null, c, 1);
			
			ArrayList<String> columns = new ArrayList<>();
			List<ColumnField> columnFields = model.getColumns();
			for (ColumnField cf : columnFields) {
				columns.add(cf.getColumnName());
				columns.forEach((cName) -> {
		            System.out.format("key: %s%n", cName);
		        });
			}
			ResultSet rs;
			int count=0;
			if ((rs = pstmt.executeQuery()) != null) {
				rs.next();
				HashMap<String,Object> s = new HashMap<>();
				for(String col : columns) {
					s.put(col,rs.getObject(col));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
		
	}

	public static ObjectGetter getInstance() {
		// TODO Auto-generated method stub
		return oG;
	}
}
