package com.overwinter.objectMapper;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.overwinter.util.ColumnField;
import com.overwinter.util.MetaModel;

public class ObjectUpdate extends ObjectMapper {
	static ObjectUpdate objUpdate = new ObjectUpdate();
	static Logger log = Logger.getLogger(ObjectUpdate.class);

	public boolean updateObjectFromDB(Object obj, Connection conn) {
		MetaModel<?> model = MetaModel.of(obj.getClass());
		String primaryKey = model.getPrimaryKey().getColumnName();
		int numberOfColumn = 0;
		// Loop through to see how many columns do we need to update
		for (ColumnField field : model.getColumns()) {
			// If the columnName isn't empty
			if (field.getColumnName() != "") {
				numberOfColumn++;
			}
		}
		// Start the sql string
		// Make sure everything is lowercase
		// as long as we are consistance
		String sql = "UPDATE " + model.getEntity().getTableName().toLowerCase() + " SET ";
		// loop through everything
		int columnCounter = 0;
		for (ColumnField field : model.getColumns()) {
			// If the columnName isn't empty
			if (field.getColumnName() != "") {
				columnCounter++;
				// set the column = value
				// SET username = 'thinh'
				sql += field.getColumnName() + " = ? "; // WHERE ARE THE VALUE? GRAB IT FROM THE META MODEL
														// .getMethod("")
				if (numberOfColumn > columnCounter) {
					sql += ","; // if more than one column the add ,
				}
			}
		}
		// WHERE username = ?
		sql += " WHERE " + primaryKey + "= ?";
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(sql);
			ParameterMetaData pd = statement.getParameterMetaData();
			int counter = 1;
			for (ColumnField field : model.getColumns()) {
				statement = setStatement(statement, pd, (model.getGetterMethod(field.getColumnName())), obj, counter);
				counter++;
			}
			statement = setStatement(statement, pd, (model.getGetterMethod(model.getPrimaryKey().getColumnName())), obj,
					counter);
			log.error(sql+"updated the database");
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("SQLException error in ObjectUpdate");
		}
		return false;
	}
	// Get instance of objUpdate
	public static ObjectUpdate getInstance() {
		return objUpdate;
	}
}
