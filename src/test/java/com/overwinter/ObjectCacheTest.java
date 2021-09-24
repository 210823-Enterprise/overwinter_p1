package com.overwinter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
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

public class ObjectCacheTest {
	private ObjectCache oC;
	private OverWinterORM mockorm;
	private MetaModel<?> mockmeta;
	private Connection mockconn;
	HashSet<Object> hash = new HashSet<Object>();
	@Before
	public void setUp() {
		oC = ObjectCache.getInstance();
		mockorm=mock(OverWinterORM.class);
		mockmeta=mock(MetaModel.class);
		oC.cache.put(Test.class, hash);
		//oC.model=mockmeta;
		oC.orm=mockorm;
	}
	@After
	public void tearDown() {
		oC = null;
		mockorm=null;
		mockmeta=null;
		mockconn = null;
	}
	@Test
	public void testputObjectInCache() {
		Object test = new Tester(1, "Kirk", "Hahn");
		hash.add(new Tester(2, "Sam", "Sammy"));
		when(mockmeta.getGetterMethod("test_id").thenReturn(getter);
		
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
