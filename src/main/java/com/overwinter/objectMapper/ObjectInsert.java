package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectInsert extends ObjectMapper {
	static ObjectInsert objInsert = new ObjectInsert();
	
	public boolean insertObjectIntoDB(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey= model.getPrimaryKey().getName();
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
		String sql = "INSERT " + model.getEntity().getTableName().toLowerCase() + "(";
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
		// WHERE username = ?
		columnCounter = 0;
		sql += "\nVALUES(";
		for(ColumnField field : model.getColumns()) {
			if(numberOfColumn-- > columnCounter) {
				columnCounter++;
				sql += "?, ";
			} else {
				sql += "?)\nRETURNING *;";
				break;
			}
		}
		System.out.println(sql);
		PreparedStatement statement;
//		try {
//			statement = conn.prepareStatement(sql);
//			ParameterMetaData pd = statement.getParameterMetaData();
//			statement =	setStatement(statement, pd, model.getGetterMethod(model.getPrimaryKey().getName()), obj, 1);
//			ResultSet rs = statement.executeQuery();
//			return true;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return false;
	}
	
	// Get instance of objUpdate
	public static ObjectInsert getInstance() {
		return objInsert;
	}
}
