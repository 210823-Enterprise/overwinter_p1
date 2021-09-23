package com.overwinter.objectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.overwinter.OverWinterORM;
import com.overwinter.dummyModels.Test;
import com.overwinter.util.Configuration;
import com.overwinter.util.MetaModel;

public class ObjectCache {

	private final HashMap<Class<?>, HashSet<Object>> cache;
	static final ObjectCache obj_cache = new ObjectCache();
//	OverWinterORM orm = OverWinterORM.getInstance();

	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}

	public ObjectCache putObjectInCache(Object o) {
		// set to that cache object
		HashSet<Object> hSet = cache.get(o.getClass());
		int pk = 0;

		MetaModel<?> model = MetaModel.of(o.getClass());
		String primaryKey = model.getPrimaryKey().getColumnName();
		Method m = model.getGetterMethod(primaryKey);
		try {
			pk = (int) m.invoke(o);
			// loop through hashset of the attach class
			for (Object theObj : hSet) {
				System.out.println("the obj is " + theObj);
				MetaModel<?> model2 = MetaModel.of(theObj.getClass());
				String primaryKey2 = model2.getPrimaryKey().getColumnName();
				Method m2 = model2.getGetterMethod(primaryKey2);

				int pk2 = (int) m2.invoke(theObj);
				// we looking for the id of one of the object in the hashset
				if (pk == pk2) {
					// perform update
					hSet.remove(theObj); // id matched
					hSet.add(o); // new object
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.cache.put(o.getClass(), hSet);

		return getInstance();
	}

	
	public boolean addAllFromDBToCache(final Class<?> clazz, OverWinterORM orm) {
		
		// new hashset every time user log in
		HashSet<Object> hSet = new HashSet<>();
		// pull our list of things
		Optional<List<Object>> list = orm.getListObjectFromDB(clazz, "test_username,test_password", "Sam,Boi", null);
		
		List<Object> list2 = list.get();
		// loop through
		for(Object theObj : list2) {
			hSet.add(theObj);
		}
		this.cache.put(clazz, hSet);
		cache.forEach((k, v) -> System.out.println("Key " + k + " Value " + v));
		return true;
	}
	
	
	
	public static ObjectCache getInstance() {
		return obj_cache;
	}
}