package com.overwinter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.checkerframework.common.reflection.qual.ForName;
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

public class OverWinterORMTest {
	private OverWinterORM orm;
	private ObjectRemover mockobject_remover;
	private ObjectGetter mockobject_getter;
	private ObjectTabler mockobject_tabler;
	private ObjectUpdate mockobject_update;
	private ObjectInsert mockobject_insert;
	private Transaction mocktransaction;
	private ObjectCache mockobject_cache;
	private Connection mockconn;
	
	@Before
	public void setUp() {
		orm = OverWinterORM.getInstance();
		mockobject_remover = mock(ObjectRemover.class);
		mockobject_getter = mock(ObjectGetter.class);
		mockobject_tabler = mock(ObjectTabler.class);
		mockobject_update = mock(ObjectUpdate.class);
		mockobject_insert = mock(ObjectInsert.class);
		mocktransaction = mock(Transaction.class);
		mockobject_cache = mock(ObjectCache.class);
		mockconn = mock(Connection.class);
		orm.conn=mockconn;
	}
	
	@After 
	public void tearDown() {
		orm = null;
		mockobject_remover = null;
		mockobject_getter = null;
		mockobject_tabler = null;
		mockobject_update = null;
		mockobject_insert = null;
		mocktransaction = null;
		mockobject_cache = null;
	}
	
	@Test
	public void testDeleteObjFromDB() {
		Tester test = new Tester(1, "Kirk", "Hahn");
		
		when(mockobject_remover.removeObjectFromDb(test, mockconn)).thenReturn(true);
		
		boolean t = mockobject_remover.removeObjectFromDb(test, mockconn);
		
		assertEquals(true, t);
	}
	
//	@Test
//	public void testGetListObjectFromDB() {
//		Tester test = new Tester(1, "Kirk", "Hahn");
//		Tester test2 = new Tester(2, "Joel", "Wiegand");
//		List<Object> testList = new ArrayList<Object>();
//		testList.add(test);
//		testList.add(test2);
//		Optional<List<Object>> ob = Optional.of(testList);
//		when(mockobject_getter.getListObjectFromDB(test.getClass(), mockconn)).thenReturn(ob);
//		System.out.println(ob);
//		assertEquals(ob, orm.getListObjectFromDB(test));
//	}
}
