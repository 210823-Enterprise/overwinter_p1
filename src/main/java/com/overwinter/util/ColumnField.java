package com.overwinter.util;

import java.lang.reflect.Field;

import com.overwinter.annotations.Column;

public class ColumnField {
	//for this
//	@Column(columnName = "name")
//	private String name;
	private Field field;
	public ColumnField(Field field) {
		if( field.getAnnotation(Column.class)==null) {
			throw new IllegalStateException("Cannot create ColumnFeild Object! Provided field "+getName()+ " is not annotated with @Column");
		}
		this.field=field;
	}
	public String getName() {
		return field.getName();
	}
	public Class<?> getType(){
		return field.getType();
	}
	public String getColumnName() {
		return field.getAnnotation(Column.class).columnName();
	
	}
	
}
