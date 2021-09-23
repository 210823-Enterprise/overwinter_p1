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

import org.apache.log4j.Logger;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Getter;
import com.overwinter.annotations.Id;
import com.overwinter.annotations.JoinColumn;
import com.overwinter.annotations.Setter;
import com.overwinter.exceptions.NoEnityException;
import com.overwinter.exceptions.NoPrimaryKeyException;

public class MetaModel<T> {
	static Logger log = Logger.getLogger(MetaModel.class);
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
		log.info("New MetaModel created for class:"+clazz);
		return new MetaModel<>(clazz);
	}

	public MetaModel(Class<T> clazz) {
		this.clazz = clazz;
		this.columnFields = new LinkedList<>();
		this.getters = new ArrayList<Method>();
		this.setters = new ArrayList<Method>();
		setColumns();
		setMethods();
		log.info("New MetaModel created for class:"+clazz);
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
		log.error("No Entity found for " + clazz.getSimpleName());
		throw new NoEnityException("No Entity found for " + clazz.getSimpleName());
	}
	public IdField getPrimaryKey() {
		Field[] fields = clazz.getDeclaredFields();// get all fields
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				primaryKeyField = new IdField(field);
				log.info("Primary Key found:"+primaryKeyField);
				return primaryKeyField;
				
			}
		}
		log.error("No primary key found for " + clazz.getSimpleName());
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
		log.info("Columns found:"+columnFields);
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
		log.info("Methods found:"+columnFields);
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
		log.info("Getter found:"+getter);
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
		log.info("Foreign keys found:"+columnFields);
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
		log.info("Setter found:"+setter);
		return setter;
	}
	public Constructor<T> getConstructor(){
		try {
			Constructor<T> c = clazz.getConstructor();
			log.info("Constructor found:"+c);
			return c;
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException in getConstructor");
		} catch (SecurityException e) {
			log.error("SecurityException in getConstructor");
		}
		return null;
	}

}
