package com.overwinter;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.overwinter.config.OverwinterCfg;
import com.overwinter.config.OverwinterDataSource;
import com.overwinter.objectMapper.ObjectGetter;
import com.overwinter.objectMapper.ObjectRemover;
import com.overwinter.objectMapper.ObjectTabler;
import com.overwinter.objectMapper.ObjectUpdate;

public class OverWinterORM {

	final private static OverWinterORM overWinterORM = new OverWinterORM();
	private static Connection conn = null;
	private final ObjectRemover obj_remover = ObjectRemover.getInstance();
	private final ObjectGetter obj_getter = ObjectGetter.getInstance();
	private final ObjectTabler obj_table = ObjectTabler.getInstance();
	private final ObjectUpdate obj_updater = ObjectUpdate.getInstance();
	// obj getter, etc.....

	private OverWinterORM() {
		OverwinterCfg config = new OverwinterCfg();
		System.out.println(System.getProperty("user.dir"));
		config.configure(".\\src\\test\\resources\\test_application.properties");
		
		OverwinterDataSource pool = new OverwinterDataSource(config);
		try {
			DataSource dataSource = pool.setUpPool();
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// return a a static instance of this singleton class
	public static OverWinterORM getInstance() {
		return overWinterORM;
	}

	// when someone wants to delete an object from their database
	// DIYORM.deleteObjFromDB
	public boolean deleteObjFromDB(Object obj) {

		return obj_remover.removeObjectFromDb(obj, conn);

	}

	public Object getObjFromDB(Object obj) {

		return obj_getter.getObjectFromDb(obj.getClass(), conn);

	}
	public Object addTabletoDb(Class<?> clazz) {

		return obj_table.addTabletoDb(clazz, conn);

	}
	public Object updateObjFromDB(Object obj) {

		return obj_updater.updateObjectFromDB(obj.getClass(), conn);

	}

}
