package com.overwinter;

import java.util.List;

import com.overwinter.annotations.Entity;
import com.overwinter.dummyModels.Test;
import com.overwinter.util.ColumnField;
import com.overwinter.util.Configuration;
import com.overwinter.util.EntityField;
import com.overwinter.util.IdField;
import com.overwinter.util.MetaModel;

public class ORM_Driver {
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Test.class);
		
		// GET THE ORM UP AND RUNNING
		OverWinterORM orm = OverWinterORM.getInstance();
		orm.addTabletoDb(Test.class); // <-- 
		Test test = new Test("Kirk", "Hahn");
		orm.insertObjIntoDB(test);
		Test testUpdate = new Test(1, "Thinh", "Pham");
		orm.updateObjFromDB(testUpdate);
		orm.deleteObjFromDB(test);
//		System.out.println(System.getProperty("user.dir"));
		
		
		
//		for (MetaModel<?> metamodel : cfg.getMetaModels()) {
//			//metamodel.getMethods();
//			System.out.printf("Printing metamodel for class %s\n", metamodel.getClassName());
//			List<ColumnField> columnFields = metamodel.getColumns();
//			for (ColumnField cf : columnFields) {
//				System.out.printf("Found a column fielded named %s of type %s which maps to the DB column %s\n",
//						cf.getName(), cf.getType(), cf.getColumnName());
//			}
//			IdField iF = metamodel.getPrimaryKey();
//			System.out.printf("Found a primary key fielded named %s of type %s which maps to the DB column %s\n",
//					iF.getName(), iF.getType(), iF.getColumnName());
//			EntityField ent = metamodel.getEntity();
//			System.out.printf("Found a table named %s\n", ent.getTableName());
//		}
	}
}