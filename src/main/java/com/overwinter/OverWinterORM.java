package com.overwinter;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.overwinter.config.OverwinterCfg;
import com.overwinter.config.OverwinterDataSource;
import com.overwinter.objectMapper.ObjectRemover;

public class OverWinterORM {
	
	final private static OverWinterORM overWinterORM = new OverWinterORM();
	private final Connection conn;
	private final ObjectRemover obj_remover= ObjectRemover.getInstance();
	// obj getter, etc.....
	
	private OverWinterORM() {
		this.conn = null;
		OverwinterCfg config = new OverwinterCfg();
		config.configure("..\\..\\..\\test\\resources\\test_application.properties");
		OverwinterDataSource pool = new OverwinterDataSource(config);
		try {
			DataSource dataSource = pool.setUpPool();
			Connection conn = dataSource.getConnection();
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

}

