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

	public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz, Connection conn, final String columns,
			final String conditions, final String operators) {
		String[] columnArray = new String[0];
		if (columns!=null&&columns.length() > 0) {
			columnArray = columns.split(",");
		}
		String[] conditionsArray = new String[0];
		if (conditions!=null&&conditions.length() > 0) {
			conditionsArray = conditions.split(",");
		}
		String[] operatorsArray = new String[0];
		if (operators!=null&&operators.length() > 0) {
			operatorsArray = operators.split(",");
		}
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName().toLowerCase();
		String sqltemp = "SELECT * FROM " + clazz.getSimpleName() + ";";
		String sql = "SELECT ";
		boolean primekeyIncluded =false;
		for (int i= 0; i<columnArray.length; i++) {
			// If the columnName isn't empty
			if (i > 0) {
				sql += " ,";
			}
				if(columnArray[i].trim().toLowerCase().equals(primaryKey)) {
					primekeyIncluded=true;
				}
				sql += columnArray[i].trim().toLowerCase();
		}
		//2,3,5,all,1
		sql+=" FROM "+clazz.getSimpleName();
		if(conditionsArray.length>0)sql+=" WHERE ";
		for (int i2 = 0; i2<conditionsArray.length; i2++) {
			// If the columnName isn't empty
			if (i2 > 0) {
				sql += " ,";
			}
				sql += columnArray[i2].trim().toLowerCase()+"="+conditionsArray[i2];
		}
		sql+=";";
		List<Object> listObjects = new ArrayList<>();
		Optional<List<Object>> outputList = Optional.of(listObjects);
		PreparedStatement pstmt;
		try {
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
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
					if(primekeyIncluded)m.invoke(c, rs.getInt(primaryKey));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (String cf: columnArray) {
					m = model.getSetterMethod(cf);
					String parType = model.getSetterMethod(cf).getParameterTypes()[0].getSimpleName();
					Object output = getByType(parType, rs, cf);
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
