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
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectGetter extends ObjectMapper {
	static ObjectGetter oG = new ObjectGetter();
	private static Logger log = Logger.getLogger(ObjectGetter.class);
	boolean primekeyIncluded = false;

	public List<Object> createSimpleObject(MetaModel<?> model, ResultSet rs, String primaryKey,
			List<Object> listObjects) {
		try {
			List<ColumnField> columnFields = model.getColumns();
			Constructor<?> constuct = model.getConstructor();
			Object c = constuct.newInstance();
			log.info("new Instance created");
			Method m = model.getSetterMethod(primaryKey);
			m.invoke(c, rs.getInt(primaryKey));
			for (ColumnField cf : columnFields) {
				m = model.getSetterMethod(cf.getColumnName());
				String parType = model.getSetterMethod(cf.getColumnName()).getParameterTypes()[0].getSimpleName();
				Object output = getByType(parType, rs, cf.getColumnName());
				m.invoke(c, output);
			}
			listObjects.add(c);
			log.info(c + " added to list of objects in getListObjectFromDB() ");
			return listObjects;
		} catch (InstantiationException e1) {
			log.error("Instantiation Exception in getListObjectFromDB()");
		} catch (IllegalAccessException e1) {
			log.error("Illegal Access Exception in getListObjectFromDB()");
		} catch (InvocationTargetException e1) {
			log.error("Invocation Target Exception in getListObjectFromDB()");
		} catch (IllegalArgumentException e) {
			log.error("Invocation Target Exception in getListObjectFromDB()");
		} catch (SQLException e) {
			log.error("Invocation Target Exception in getListObjectFromDB()");
		}
		return listObjects;
	}

	public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz, Connection conn) {
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName();
		String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() + ";";
		List<Object> listObjects = new ArrayList<>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			log.info(sql + " has been executed against database");
			while (rs.next()) {
				listObjects = createSimpleObject(model, rs, primaryKey, listObjects);

			}
		} catch (SQLException e) {
			log.error("SQLException in getListObjectFromDB()");
		} catch (IllegalArgumentException e) {
			log.error("bad argument in getListObjectFromDB()");
		}
		return Optional.of(listObjects);

	}

	public Object getObjectFromDB(final Class<?> clazz, Connection conn, int id) {
		MetaModel<?> model = MetaModel.of(clazz);
		String primaryKey = model.getPrimaryKey().getColumnName();
		String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() + " WHERE " + primaryKey + "=?;";
		List<Object> listObjects = new ArrayList<>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			pstmt = setPreparedStatmentByType(pstmt, pd.getParameterTypeName(1), Integer.toString(id), 1);
			log.info("Prepared Statment " + pstmt + " is about to query the database");
			ResultSet rs = pstmt.executeQuery();
			log.info(sql + " has been executed against database");
			while (rs.next()) {
				listObjects = createSimpleObject(model, rs, primaryKey, listObjects);
			}
		} catch (SQLException e) {
			log.error("SQLException in getObjectFromDB()");
		} catch (IllegalArgumentException e) {
			log.error("bad argument in getObjectFromDB()");
		}
		return listObjects.get(0);

	}

	public String[] createColumnArray(String columns) {
		String[] columnArray = new String[0];
		if (columns != null && columns.length() > 0) {
			columnArray = columns.split(",");
		}
		return columnArray;
	}

	public String[] createConditionsArray(String condition) {
		String[] conditionsArray = new String[0];
		if (condition != null && condition.length() > 0) {
			conditionsArray = condition.split(",");
		}
		return conditionsArray;
	}

	public String[] createOperatorsArray(String operator) {
		String[] operatorsArray = new String[0];
		if (operator != null && operator.length() > 0) {
			operatorsArray = operator.split(",");
		}
		return operatorsArray;
	}

	public String createSQL(String[] columnArray, String[] conditionsArray, String[] operatorsArray, Class<?> clazz,
			String primaryKey) {
		String sql = "SELECT ";
		log.info("Columns:" + columnArray + " conditions: " + conditionsArray + " operators: " + operatorsArray);

		for (int i = 0; i < columnArray.length; i++) {
			// If the columnName isn't empty
			if (i > 0) {
				sql += ", ";
			}
			if (columnArray[i].trim().toLowerCase().equals(primaryKey)) {
				primekeyIncluded = true;
			}
			sql += columnArray[i].trim().toLowerCase();
		}
		if (!primekeyIncluded) {
			sql += ", " + primaryKey.trim().toLowerCase();
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
		return sql;
	}

	public List<Object> constructComplexObject(MetaModel<?> model, String primaryKey, ResultSet rs,
			String[] columnArray, List<Object> listObjects) {
		Object c = null;
		Constructor<?> constuct = model.getConstructor();
		try {
			c = constuct.newInstance();
			Method m = model.getSetterMethod(primaryKey);
			m.invoke(c, rs.getInt(primaryKey));
			for (String cf : columnArray) {
				m = model.getSetterMethod(cf);
				String parType = model.getSetterMethod(cf).getParameterTypes()[0].getSimpleName();
				Object output = getByType(parType, rs, cf);
				m.invoke(c, output);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error(
					c + " does not have access to the definition of the specified class, field, method or constructor");
		} catch (InstantiationException e) {
			log.error(constuct + "cannot be instantiated");
		} catch (SQLException e) {
			log.error(primaryKey + "was unable to be gotten");
			e.printStackTrace();
		}
		listObjects.add(c);
		log.info(c + " has been added to the list of objects grabbed from database");
		return listObjects;
	}

	public Optional<List<Object>> getListObjectFromDB(final Class<?> clazz, Connection conn, final String columns,
			final String conditions, final String operators) {
		MetaModel<?> model = MetaModel.of(clazz);
		String[] columnArray = createColumnArray(columns);
		String[] conditionsArray = createConditionsArray(conditions);
		String[] operatorsArray = createOperatorsArray(operators);
		String primaryKey = model.getPrimaryKey().getColumnName().toLowerCase();
		String sql = createSQL(columnArray, conditionsArray, operatorsArray, clazz, primaryKey);
		log.info(sql + " is about to query the database");
		List<Object> listObjects = new ArrayList<>();
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			for (int i3 = 0; i3 < conditionsArray.length; i3++) {
				pstmt = setPreparedStatmentByType(pstmt, pd.getParameterTypeName(i3 + 1), conditionsArray[i3], i3 + 1);
			}
			log.info("Prepared Statment " + pstmt + " is about to query the database");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				listObjects = constructComplexObject(model, primaryKey, rs, columnArray, listObjects);
			}
		} catch (SQLException e2) {
			log.error("SQLException exception from complex getObjectFromDB");
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException exception from complex getObjectFromDB");
		}
		log.info(listObjects + " is being returned from complex getListObjectFromDB");
		return Optional.of(listObjects);
	}

	public static ObjectGetter getInstance() {
		return oG;
	}

	protected Object getByType(String type, ResultSet rs, String columnName) {
		try {
			switch (type) {
			case "String":
				log.info("String found in ResultSet");
				return rs.getString(columnName);
			case "Integer":
				log.info("Integer found in ResultSet");
				return rs.getInt(columnName);
			case "Double":
				log.info("Double found in ResultSet");
				return rs.getDouble(columnName);
			case "int":
				log.info("int found in ResultSet");
				return rs.getInt(columnName);
			case "double":
				log.info("double found in ResultSet");
				return rs.getDouble(columnName);
			case "float":
				log.info("float found in ResultSet");
				return rs.getFloat(columnName);
			case "Float":
				log.info("Float found in ResultSet");
				return rs.getFloat(columnName);
			}
			log.error("unknown type found in ResultSet in getByType");
			return null;
		} catch (

		SQLException e) {
			log.error("SQLException exception from getByType");
			return null;
		}
	}
}
