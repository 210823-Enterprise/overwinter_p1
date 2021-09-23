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
		String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() + ";";
		List<Object> listObjects = new ArrayList<>();
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

					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
				Method m = model.getSetterMethod(primaryKey);
				try {
					m.invoke(c, rs.getInt(primaryKey));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				for (ColumnField cf : columnFields) {
					m = model.getSetterMethod(cf.getColumnName());
					String parType = model.getSetterMethod(cf.getColumnName()).getParameterTypes()[0].getSimpleName();
					Object output = getByType(parType, rs, cf.getColumnName());
					try {
						m.invoke(c, output);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				listObjects.add(c);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return Optional.of(listObjects);

	}

	public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz, Connection conn, final String columns,
			final String conditions, final String operators) {
		String[] columnArray = new String[0];
		if (columns != null && columns.length() > 0) {
			columnArray = columns.split(",");
		}
		String[] conditionsArray = new String[0];
		if (conditions != null && conditions.length() > 0) {
			conditionsArray = conditions.split(",");
		}
		String[] operatorsArray = new String[0];
		if (operators != null && operators.length() > 0) {
			operatorsArray = operators.split(",");
		}
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName().toLowerCase();
		String sql = "SELECT ";
		boolean primekeyIncluded = false;
		for (int i = 0; i < columnArray.length; i++) {
			// If the columnName isn't empty
			if (i > 0) {
				sql += " ,";
			}
			if (columnArray[i].trim().toLowerCase().equals(primaryKey)) {
				primekeyIncluded = true;
			}
			sql += columnArray[i].trim().toLowerCase();
		}
		// 2,3,5,all,1
		sql += " FROM " + clazz.getSimpleName().toLowerCase();
		if (conditionsArray.length > 0)
			sql += " WHERE ";
		for (int i2 = 0; i2 < conditionsArray.length; i2++) {
			// If the columnName isn't empty
			if (i2 > 0) {
				sql += " AND ";
			}
			sql += columnArray[i2].trim().toLowerCase() + "=" + "?";
		}
		sql += ";";
		List<Object> listObjects = new ArrayList<>();
		PreparedStatement pstmt = null;
		Constructor<?> constuct = model.getConstructor();
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			//pstmt = setPreparedStatmentByType(pstmt, pd.getParameterTypeName(1), conditionsArray[0], 1);
			for (int i3 = 0; i3 < conditionsArray.length; i3++) {
				pstmt = setPreparedStatmentByType(pstmt, pd.getParameterTypeName(i3+1), conditionsArray[i3], i3+1);
			}
			System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			Object c = null;
			while (rs.next()) {
				c = constuct.newInstance();
				Method m = model.getSetterMethod(primaryKey);
				if (primekeyIncluded)
					m.invoke(c, rs.getInt(primaryKey));

				for (String cf : columnArray) {
					m = model.getSetterMethod(cf);
					String parType = model.getSetterMethod(cf).getParameterTypes()[0].getSimpleName();
					Object output = getByType(parType, rs, cf);
					m.invoke(c, output);
				}
				listObjects.add(c);
				System.out.println(c);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
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
