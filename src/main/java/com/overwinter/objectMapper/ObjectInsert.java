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
				sql += "?)\nRETURNING *;";
				break;
			}
		}
		
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(sql);
			System.out.println(statement);
			ParameterMetaData pd = statement.getParameterMetaData();
			
			
			int counter = 1;
			for(ColumnField field : model.getColumns()) {
				System.out.println("field is " + field.getColumnName());
				// If the columnName isn't empty 
				System.out.println("obj " + field.getColumnName());
				System.out.println("get obj value " + model.getGetterMethod(field.getColumnName()));
//				for(int i = 1; i < testing + 1; i++) {
					
					statement =	setStatement(statement, pd, (model.getGetterMethod(field.getColumnName())), obj, counter);
					counter++;
//					System.out.println("Hello"+ i);
//				}
			}
			
			
			
			
			
			
			System.out.println("statement here " + statement);
			ResultSet rs = statement.executeQuery();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	// Get instance of objUpdate
	public static ObjectInsert getInstance() {
		return objInsert;
	}
}
