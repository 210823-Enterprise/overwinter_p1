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
	static Logger log = Logger.getLogger(ObjectGetter.class);

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
			Constructor<?> constuct = model.getConstructor();
			Object c = null;
			List<ColumnField> columnFields = model.getColumns();
			while (rs.next()) {
				try {
					c = constuct.newInstance();
					log.info("new Instance created");
				} catch (InstantiationException e1) {
					log.error("Instantiation Exception in getListObjectFromDB()");
				} catch (IllegalAccessException e1) {
					log.error("Illegal Access Exception in getListObjectFromDB()");
				} catch (InvocationTargetException e1) {
					log.error("Invocation Target Exception in getListObjectFromDB()");
				}
				Method m = model.getSetterMethod(primaryKey);
				try {
					m.invoke(c, rs.getInt(primaryKey));
				} catch (IllegalAccessException e) {
					log.error("Illegal Access Exception in getListObjectFromDB() for model.getSetterMethod(primaryKey)");
				} catch (InvocationTargetException e) {
					log.error("Invocation Target Exception in getListObjectFromDB() for model.getSetterMethod(primaryKey)");
				}
				for (ColumnField cf : columnFields) {
					m = model.getSetterMethod(cf.getColumnName());
					String parType = model.getSetterMethod(cf.getColumnName()).getParameterTypes()[0].getSimpleName();
					Object output = getByType(parType, rs, cf.getColumnName());
						try {
							m.invoke(c, output);
						} catch (IllegalAccessException | InvocationTargetException e) {
							log.error("Exception in getListObjectFromDB() for model.getSetterMethod(c,output)");
						}
				}
				listObjects.add(c);
				log.info(c+" added to list of objects in getListObjectFromDB() ");
			}
		} catch (SQLException e) {
			log.error("SQLException in getListObjectFromDB()");
		} catch (IllegalArgumentException e) {
			log.error("bad argument in getListObjectFromDB()");
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
		log.info("Columns:"+columnArray+" conditions: "+conditionsArray+" operators: "+operatorsArray);
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
		log.info(sql+" is about to query the database");
		List<Object> listObjects = new ArrayList<>();
		PreparedStatement pstmt = null;
		Constructor<?> constuct = model.getConstructor();
		try {
			pstmt = conn.prepareStatement(sql);
			ParameterMetaData pd = pstmt.getParameterMetaData();
			for (int i3 = 0; i3 < conditionsArray.length; i3++) {
				pstmt = setPreparedStatmentByType(pstmt, pd.getParameterTypeName(i3+1), conditionsArray[i3], i3+1);
			}
			System.out.println(pstmt);
			log.info("Prepared Statment "+pstmt+" is about to query the database");
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
				log.info(c+" has been added to the list of objects grabbed from database");
			}
		} catch (SQLException e2) {
			log.error("SQLException exception from complex getObjectFromDB");
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException exception from complex getObjectFromDB");
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException exception from complex getObjectFromDB");
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException exception from complex getObjectFromDB");
		} catch (InstantiationException e) {
			log.error("InstantiationException exception from complex getObjectFromDB");
		}
		log.info(listObjects+" is being returned from complex getListObjectFromDB");
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
