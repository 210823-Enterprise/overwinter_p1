package com.overwinter.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Id;
import com.overwinter.exceptions.NoEnityException;
import com.overwinter.exceptions.NoPrimaryKeyException;

public class MetaModel<T> {
	private Class<T> clazz;
	// private IdField primaryKeyField;
	private List<ColumnField> columnFields;
	private IdField idField;
	private EntityField entity;
	// private List<ForeignKeyField> foreignKeyFields;
	
	public static <T> MetaModel<T> of(Class<T> clazz) {
		if(clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException("Cannot Create MetaModel for " + clazz.getName());
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
	
	public EntityField getEntity(){
		Field[] fields = clazz.getDeclaredFields();//get all fields
		for (Field field : fields) {
			Entity ent = field.getAnnotation(Entity.class);
			if (ent != null) {
				this.entity= new EntityField(field);
				return this.entity;
			}
		}
		throw new NoEnityException("No Entity found for "+ clazz.getSimpleName());
	}
	//TO_DO: public IdField getPrimaryKey() .. need new class IdField
	public IdField getPrimaryKey(){
		Field[] fields = clazz.getDeclaredFields();//get all fields
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				idField= new IdField(field);
				return idField;
			}
		}
		throw new NoPrimaryKeyException("No primary key found for "+ clazz.getSimpleName());
	}
	
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