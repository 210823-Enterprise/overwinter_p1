package com.overwinter;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.overwinter.dummyModels.Tester;
import com.overwinter.objectMapper.ObjectGetter;
import com.overwinter.util.MetaModel;

public class ObjectGetterTest {
	private OverWinterORM mockorm;
	private ObjectGetter object_getter;
	private Connection mockconn;
	private ResultSet mockrs;
	private MetaModel<?> model;

	@Before
	public void setUp() {
		//mockorm = OverWinterORM.getInstance();
		object_getter = ObjectGetter.getInstance();
	}

	@After
	public void tearDown() {
		mockorm = null;
		object_getter = null;
		mockconn = null;
	}

	@Test
	public void testcreateSimpleObject() {
		Tester test_user = new Tester(0, "Kirk", "Hahn");
		String[] columnArray = new String[2],conditionsArray= new String[2];
		String[] operatorsArray =null;
		columnArray[0]= "test_id";
		columnArray[1]= "test_username";
		conditionsArray[0]= "Kirk";
		conditionsArray[1]= "Hahn";
		String primaryKey="test_id";
		String sql = object_getter.createSQL(columnArray, conditionsArray, operatorsArray, Test.class, primaryKey);
		String expected = "SELECT test_id ,test_username FROM test WHERE test_id=? AND test_username=?;";
		assertEquals(expected, sql);
	}
}
