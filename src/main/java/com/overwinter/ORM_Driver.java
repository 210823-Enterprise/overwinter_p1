package com.overwinter;

import com.overwinter.dummyModels.Test;
import com.overwinter.util.Configuration;

public class ORM_Driver {
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Test.class);

		// GET THE ORM UP AND RUNNING
		OverWinterORM orm = OverWinterORM.getInstance();
		orm.addTabletoDb(Test.class); // <--
		Test test = new Test("Kirk", "Hahn");

		Test test2 = new Test("Sam", "Boi");
		orm.insertObjIntoDB(test);
		orm.insertObjIntoDB(test2);
		Test testUpdate = new Test(1, "Thinh", "Pham");
		orm.transaction();
		orm.insertObjIntoDB(test);
		orm.updateObjFromDB(testUpdate);
		orm.getListObjectFromDB(Test.class, "test_id,test_username,test_password", null, null);
		orm.getListObjectFromDB(Test.class, "test_username,test_password", "Sam,Boi", null);
		orm.deleteObjFromDB(test);
		orm.commit();
	}
}