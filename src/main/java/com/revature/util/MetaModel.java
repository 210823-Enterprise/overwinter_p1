package com.revature.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;

public class MetaModel<T> {
	private Class<T> clazz;
	// private IdField primaryKeyField;
	private List<ColumnField> columnFields;
	// private List<ForeignKeyField> foreignKeyFields;
	
	public static <T> MetaModel<T> of(Class<T> clazz) {
		if(clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException("Cannot Create NetaModel for " + clazz.getName());
		}
		return new MetaModel<>(clazz);
	}
	
	public MetaModel(Class<T> clazz) {
		this.clazz = clazz;
		this.columnFields = new LinkedList<>();
	}

	public String getClassName() {
		return clazz.getName();
	}
	
	public String getSimpleName(){
		return clazz.getSimpleName();
	}
	
	//TO_DO: public IdField getPrimaryKey() .. need new class IdField
	
	
	public List<ColumnField> getColumns() {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				columnFields.add(new ColumnField(field));
			}
		}
		return columnFields;
	}

	
}
