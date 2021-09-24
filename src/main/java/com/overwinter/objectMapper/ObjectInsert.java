package com.overwinter.objectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectInsert extends ObjectMapper {
	static ObjectInsert objInsert = new ObjectInsert();
	static Logger log = Logger.getLogger(ObjectInsert.class);
	public boolean insertObjectIntoDB(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey= model.getPrimaryKey().getColumnName();
		int numberOfColumn = 0;
		// Loop through to see how many columns do we need to update
		for(ColumnField field : model.getColumns()) {
			// If the columnName isn't empty 
			if(field.getColumnName() != "") {
				numberOfColumn++;
			}
		}
		// Start the sql string
		// Make sure everything is lowercase
		// as long as we are consistance
		String sql = "INSERT INTO " + model.getEntity().getTableName().toLowerCase() + "(";
		// loop through everything
		int columnCounter = 0;
		for(ColumnField field : model.getColumns()) {
			// If the columnName isn't empty 
			if(field.getColumnName() != "") {
				columnCounter++;
				// set the column = value
				// SET username = 'thinh'
				sql += field.getColumnName(); // WHERE ARE THE VALUE? GRAB IT FROM THE META MODEL .getMethod("")
				if(numberOfColumn > columnCounter) {
					sql += ", "; // if more than one column the add ,
				} else {
					sql += ")";
					break;
				}
			}
		}
		int testing = columnCounter;
		// WHERE username = ?
		columnCounter = 0; // this will = 1 instead of 2
		sql += "\nVALUES(";
		for(ColumnField field : model.getColumns()) {
			if(numberOfColumn-- > columnCounter) {
				columnCounter++;
				sql += "?, ";
			} else {
				sql += "?)\nRETURNING " + primaryKey + ";";
				break;
			}
		}
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(sql);
			ParameterMetaData pd = statement.getParameterMetaData();
			
			int counter = 1;
			for(ColumnField field : model.getColumns()) {
				// If the columnName isn't empty 
//				for(int i = 1; i < testing + 1; i++) {
					
					statement =	setStatement(statement, pd, (model.getGetterMethod(field.getColumnName())), obj, counter);
					counter++;
//				}
			}
			log.info(sql+" is updating the database");
			ResultSet rs = statement.executeQuery();
			rs.next();
			int pk = rs.getInt(primaryKey);
			Method m = model.getSetterMethod(model.getPrimaryKey().getColumnName());
			try {
				m.invoke(obj, pk);
			} catch (IllegalAccessException e) {
				log.info("IllegalAccessException error in ObjectInsert");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				log.info("IllegalArgumentException error in ObjectInsert");
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				log.info("InvocationTargetException error in ObjectInsert");
			}
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("SQLException error in ObjectInsert");
		}
		return false;
	}
	// Get instance of objUpdate
	public static ObjectInsert getInstance() {
		return objInsert;
	}
}
