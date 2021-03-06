package com.overwinter;

import org.apache.log4j.Logger;

import com.overwinter.dummyModels.Tester;
import com.overwinter.transaction.Transaction;
import com.overwinter.util.Configuration;

public class ORM_Driver {
	public static void main(String[] args) {
		Logger log = Logger.getLogger(ORM_Driver.class);
		log.info("ORM Driver Starting");
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Tester.class);

		// GET THE ORM UP AND RUNNING
		OverWinterORM orm = OverWinterORM.getInstance();
		orm.addTabletoDb(Tester.class); // <--
		Tester test = new Tester("Kirk", "Hahn");
		Tester test2 = new Tester("Thinh", "Pham");
		Tester test3 = new Tester("Joel", "Wiegand");
		orm.insertObjIntoDB(test); // KIRK inserted
		try {
			orm.getObjectFromDB(Tester.class, 1);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		orm.beginTransaction();
//		orm.insertObjIntoDB(test); // KIRK inserted
//		orm.setSavePointWithName("testing"); // set save point
//		orm.insertObjIntoDB(test2); // THINH <--- THIS ONE WILL BE INSERTED
//		orm.rollBackWithSpecificSavePoint(Transaction.getSavepoint()); // ROLL
//		orm.insertObjIntoDB(test3); // JOEL inserted
//		orm.commit(); // will have KIRK AND JOEL
//
//		orm.addAllFromDBToCache(Tester.class); // this will be call the first time so it makes senes but not the 2nd time
//		orm.updateObjFromDB(test2);
//		orm.putObjectInCache(test2); // THIS WILL BE SKINN BOI!!!!
//		log.info("ORM Driver Ending");
		// this SHOULD return the whole cache including update version of id2 = skinny
		// boi

	}
}