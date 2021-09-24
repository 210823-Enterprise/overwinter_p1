package com.overwinter;

import java.sql.Savepoint;

import com.overwinter.dummyModels.Test;
import com.overwinter.transaction.Transaction;
import com.overwinter.util.Configuration;

public class ORM_Driver {
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Test.class);
		// GET THE ORM UP AND RUNNING
		OverWinterORM orm = OverWinterORM.getInstance();
		orm.addTabletoDb(Test.class); // <-- 
		Test test = new Test("Kirk", "Hahn");
		Test test2 = new Test("Thinh", "Pham");
		Test test3 = new Test("Joel", "Wiegand");
		
		orm.beginTransaction();
		orm.insertObjIntoDB(test); // KIRK inserted
		orm.setSavePointWithName("testing"); // set save point
		orm.insertObjIntoDB(test2); // THINH <--- THIS ONE WILL BE INSERTED
		orm.rollBackWithSpecificSavePoint(Transaction.getSavepoint()); // ROLL
		orm.insertObjIntoDB(test3); // JOEL inserted
		orm.commit(); // will have KIRK AND JOEL
		
		
		
		
		
		
		
//		Test testUpdate = new Test(1, "Thinh", "Pham");

//		orm.transaction();
//		orm.insertObjIntoDB(test);
		
//		orm.getListObjectFromDB(Test.class, "test_id,test_username,test_password", null, null);
//		orm.getListObjectFromDB(Test.class, "test_username,test_password", "Sam,Boi", null);
//		orm.deleteObjFromDB(test);
//		orm.commit();
		
		// Thinny Boi HERE !!! SHOULD BE V
//		orm.addAllFromDBToCache(Test.class); // this will be call the first time so it makes senes but not the 2nd time
//		orm.updateObjFromDB(test2);
//		orm.putObjectInCache(test2); // THIS WILL BE SKINN BOI!!!!
		// this SHOULD return the whole cache including update version of id2 = skinny boi
		
		// This part was repeated but we will delete later
		
//		orm.addTabletoDb(Test.class); // <--
//		Test test = new Test("Kirk", "Hahn");
//		Test test2 = new Test("Sam", "Boi");
//		orm.insertObjIntoDB(test);
//		orm.insertObjIntoDB(test2);
//		Test testUpdate = new Test(1, "Thinh", "Pham");
//		orm.transaction();
//		orm.updateObjFromDB(testUpdate);
//		orm.getListObjectFromDB(Test.class, "test_id,test_username,test_password", null, null);
//		orm.getListObjectFromDB(Test.class, "test_username,test_password", "Sam,Boi", null);
//		orm.deleteObjFromDB(test);
//		orm.commit();
//		orm.addAllFromDBToCache(Test.class, orm);
		
		
		
		
	}
}