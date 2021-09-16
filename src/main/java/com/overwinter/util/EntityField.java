package com.overwinter.util;

public class EntityField {
	private String tableName;

	public EntityField(String tableName) {
		if (tableName == null) {
			throw new IllegalStateException("Cannot create Entity Field Object! Provided field " + getName()
					+ " is not annotated with @Entity");
		}
		this.tableName = tableName;
	}
	public String getTableName() {
		return this.tableName;
	}
	public String getName() {
		return this.getName();
	}
}
