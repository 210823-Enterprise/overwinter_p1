package com.overwinter.util;

import java.lang.reflect.Field;

import com.overwinter.annotations.Column;

public class EntityField {
	private Field field;
	public EntityField(Field field) {
		if( field.getAnnotation(Column.class)==null) {
			throw new IllegalStateException("Cannot create Entity Feild Object! Provided field "+getName()+ " is not annotated with @Entity");
		}
		this.field=field;
	}
	public String getName() {
		return field.getName();
	}
}
