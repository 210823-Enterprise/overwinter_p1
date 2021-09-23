package com.overwinter.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.management.modelmbean.ModelMBean;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Getter;
import com.overwinter.annotations.Id;
import com.overwinter.annotations.JoinColumn;
import com.overwinter.annotations.Setter;
import com.overwinter.exceptions.NoEnityException;
import com.overwinter.exceptions.NoPrimaryKeyException;

public class MetaModel<T> {
	private Class<T> clazz;
	private IdField primaryKeyField;
	private ArrayList<Method> setters;
	private ArrayList<Method> getters;
	private List<ColumnField> columnFields;
	private List<ForeignKeyField> foreignKeyFields;
	private EntityField entity;

	public static <T> MetaModel<T> of(Class<T> clazz) {
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException("Cannot Create MetaModel for " + clazz.getName());
		}
		return new MetaModel<>(clazz);
	}

	public MetaModel(Class<T> clazz) {
		this.clazz = clazz;
		this.columnFields = new LinkedList<>();
		this.getters = new ArrayList<Method>();
		this.setters = new ArrayList<Method>();
		setColumns();
		setMethods();
	}

	public String getClassName() {
		return clazz.getName();
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public EntityField getEntity() {
		Annotation[] fields = clazz.getAnnotations();// get all fields
		for (Annotation field : fields) {
			String s = field.toString();
			String tName = s.substring(s.indexOf("tableName=") + 10, s.length() - 1);
			if (tName != null) {
				// EntityField e= new EntityField(field);
				return new EntityField(tName);
			}
		}
		throw new NoEnityException("No Entity found for " + clazz.getSimpleName());
	}

	// TO_DO: public IdField getPrimaryKey() .. need new class IdField
	public IdField getPrimaryKey() {
		
		Field[] fields = clazz.getDeclaredFields();// get all fields
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				primaryKeyField = new IdField(field);
				return primaryKeyField;
			}
		}
		throw new NoPrimaryKeyException("No primary key found for " + clazz.getSimpleName());
	}

	public List<ColumnField> getColumns() {
		
		return columnFields;
	}
	
	public List<ColumnField> setColumns() {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				columnFields.add(new ColumnField(field));
			}
		}
		return columnFields;
	} 

	public Method[] setMethods() {
		Method[] mArray = clazz.getMethods();
		for (Method m : mArray) {

			Setter s = m.getAnnotation(Setter.class);
			if (s != null) {
				setters.add(m);
			}
			Getter g = m.getAnnotation(Getter.class);
			if (g != null) {
				getters.add(m);
			}
		}
		return mArray;
	}

	public Method getGetterMethod(String methodName) {
		Method getter = null;
		for (Method m : getters) {
			Getter g = m.getAnnotation(Getter.class);
			if (g != null && g.name().equals(methodName)) {
				getter = m;
			}
		}
		return getter;
	}

	public List<ForeignKeyField> getForeignKeys() {

		List<ForeignKeyField> foreignKeyFields = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			JoinColumn column = field.getAnnotation(JoinColumn.class);

			if (column != null) {
				foreignKeyFields.add(new ForeignKeyField(field));
			}
		}
		return foreignKeyFields;

	}

	public Method getSetterMethod(String methodName) {
		// TODO Auto-generated method stub
		Method setter = null;
		for (Method m : setters) {
			Setter s = m.getAnnotation(Setter.class);
			if (s != null && s.name().equals(methodName)) {
				setter = m;
			}
		}
		return setter;
	}
	public Constructor<T> getConstructor(){
		try {
			Constructor<T> c = clazz.getConstructor();
			return c;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
