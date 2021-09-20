package com.overwinter.objectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ObjectMapper {
	
	protected PreparedStatement setStatement(PreparedStatement pstmt, ParameterMetaData pd, Method getter, Object obj, int index) {
		try {
			return setPreparedStatmentByType(pstmt, pd.getParameterTypeName(index), String.valueOf(getter.invoke(obj, index)), index);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pstmt;
	}
	/**
	 * @param prepeares  statment to set
	 * @param paramenter type
	 * @param input      that represents the value to be placed in prepared
	 * @param index      to place the value at
	 */
	protected PreparedStatement setPreparedStatmentByType(PreparedStatement pstmt, String type, String input, int index) {
		try {
			switch (type) {
			case "text":
				pstmt.setString(index, input);
				break;
			case "String":
				pstmt.setString(index, input);
				break;
			case "varchar":
				pstmt.setString(index, input);
				break;
			case "int":
				pstmt.setInt(index, Integer.parseInt(input));
				break;
			case "double":
				pstmt.setDouble(index, Double.parseDouble(input));
				break;
				//TODO:add timestamp, float, and other types
			}
			return pstmt;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
