package com.overwinter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.overwinter.dummyModels.Tester;
import com.overwinter.objectMapper.ObjectCache;
import com.overwinter.objectMapper.ObjectGetter;
import com.overwinter.objectMapper.ObjectInsert;
import com.overwinter.objectMapper.ObjectRemover;
import com.overwinter.objectMapper.ObjectTabler;
import com.overwinter.objectMapper.ObjectUpdate;
import com.overwinter.transaction.Transaction;
import com.overwinter.util.MetaModel;

public class ObjectGetterTest {
	private OverWinterORM mockorm;
	private ObjectGetter object_getter;
	private Connection mockconn;
	private ResultSet mockrs;
	private MetaModel<?> model;
	@Before
	public void setUp() {
		mockorm = mock(OverWinterORM.class);
		object_getter = ObjectGetter.getInstance();
		mockconn = mock(Connection.class);
		 = mock(ResultSet.class);
	}
	
	@After 
	public void tearDown() {
		mockorm = null;
		object_getter = null;
		mockconn = null;
	}
	
	@Test
	public void testcreateSimpleObject() {
		Tester test = new Tester(0, "Kirk", "Hahn");
		String primarykey="test_id";
		when(mockrs.getInt(primarykey)).thenReturn(1);
		Method m = mock(object_getter.get)
		boolean t = mockobject_remover.removeObjectFromDb(test, mockconn);
		
		assertEquals(true, t);
	}
	
	@Test
	public void testGetListObjectFromDB() {
		Tester test = new Tester(1, "Kirk", "Hahn");
		Tester test2 = new Tester(2, "Joel", "Wiegand");
		List<Object> testList = new ArrayList<Object>();
		testList.add(test);
		testList.add(test2);
		Optional<List<Object>> ob = Optional.of(testList);
		when(mockobject_getter.getListObjectFromDB(test.getClass(), mockconn)).thenReturn(ob);
		System.out.println(ob);
		assertEquals(ob, orm.getListObjectFromDB(test));
	}
}
