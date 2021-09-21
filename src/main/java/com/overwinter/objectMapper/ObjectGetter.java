package com.overwinter.objectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectGetter extends ObjectMapper {
	static ObjectGetter oG = new ObjectGetter();

	public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz, Connection conn) {
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName();
		String sql = "SELECT * FROM " + clazz.getSimpleName() + ";";
		List<Object> listObjects = new ArrayList<>();
		Optional<List<Object>> outputList = Optional.of(listObjects);
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Constructor<?> constuct = model.getConstructor();
			Object c = null;
			List<ColumnField> columnFields = model.getColumns();
			while (rs.next()) {
				try {
					c = constuct.newInstance();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Method m = model.getSetterMethod(primaryKey);
				try {
					m.invoke(c, rs.getInt(primaryKey));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (ColumnField cf : columnFields) {
					m = model.getSetterMethod(cf.getColumnName());
					String parType = model.getSetterMethod(cf.getColumnName()).getParameterTypes()[0].getSimpleName();
					// System.out.println("Type:" + parType);
					Object output = getByType(parType, rs, cf.getColumnName());
					try {
						m.invoke(c, output);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(c);
				listObjects.add(c);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.of(listObjects);

	}

	public static ObjectGetter getInstance() {
		return oG;
	}

	protected Object getByType(String type, ResultSet rs, String columnName) {
		try {
			switch (type) {
			case "String":
				return rs.getString(columnName);
			case "Integer":
				return rs.getInt(columnName);
			case "Double":
				return rs.getDouble(columnName);
			case "int":
				return rs.getInt(columnName);
			case "double":
				return rs.getDouble(columnName);
			case "float":
				return rs.getFloat(columnName);
			case "Float":
				return rs.getFloat(columnName);
			}
			return null;
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
