package com.overwinter.objectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

	public Object getObjectFromDb(Class<?> clazz, Connection conn) {
		Object c = new Object();
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getName();
		String sql = "SELECT * FROM " + clazz.getSimpleName() + "WHERE" + primaryKey + "= ?;";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt = setStatement(pstmt, pd, model.getGetterMethod(primaryKey), c, 1);
			List<ColumnField> columnFields = model.getColumns();
			// assume empty constructor
			Constructor<?> constuct = model.getConstructor();
			c = constuct.newInstance();// create new instance
			ResultSet rs;
			if ((rs = pstmt.executeQuery()) != null) {
				rs.next();
				for (ColumnField cf : columnFields) {
					Method m = model.getSetterMethod(cf.getColumnName());
					String parType = model.getSetterMethod(cf.getColumnName()).getParameterTypes()[0].getTypeName();
					System.out.print(parType);
					Object output = getByType(parType, rs, sql);
					m.invoke(c, output);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return c;

	}

	public static ObjectGetter getInstance() {
		return oG;
	}

	protected Object getByType(String type, ResultSet rs, String columnName) {
		try {
			switch (type) {
			case "String":
				rs.getString(columnName);
				break;
			case "Integer":
				rs.getInt(columnName);
				break;
			case "Double":
				rs.getDouble(columnName);
				break;
			case "int":
				rs.getInt(columnName);
				break;
			case "double":
				rs.getDouble(columnName);
				break;
			case "float":
				rs.getFloat(columnName);
				break;
			case "Float":
				rs.getFloat(columnName);
				break;
			}
			return null;
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
