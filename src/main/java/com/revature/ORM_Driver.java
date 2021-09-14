package com.revature;

import java.util.List;

import com.revature.dummyModels.Test;
import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.MetaModel;

public class ORM_Driver {

	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Test.class);
		for (MetaModel<?> metamodel : cfg.getMetaModels()) {
			System.out.printf("Printing metamodel for class %s\n", metamodel.getClassName());
			List<ColumnField> columnFields = metamodel.getColumns();
			for (ColumnField cf : columnFields) {
				System.out.printf("Found a column fielded named %s of type %s which maps to the DB column %s\n",
						cf.getName(), cf.getType(), cf.getColumnName());
			}
		}
	}
}
