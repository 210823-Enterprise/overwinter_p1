package com.overwinter.objectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.overwinter.OverWinterORM;
import com.overwinter.util.MetaModel;

public class ObjectCache {
	private static Logger log = Logger.getLogger(ObjectCache.class);
	private final HashMap<Class<?>, HashSet<Object>> cache;
	static final ObjectCache obj_cache = new ObjectCache();
	private OverWinterORM orm = OverWinterORM.getInstance();

	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}

	// we call this method after the first time
	public HashMap<Class<?>, HashSet<Object>> putObjectInCache(Object o) {
		// set to that cache object
		HashSet<Object> hSet = cache.get(o.getClass());
		int pk = 0;
		MetaModel<?> model = MetaModel.of(o.getClass());
		String primaryKey = model.getPrimaryKey().getColumnName();
		Method m = model.getGetterMethod(primaryKey);
		try {
			pk = (int) m.invoke(o);
			// loop through hashset of the attach class
			System.out.println("Probably here: " + hSet);
			for (Object theObj : hSet) {
				System.out.println("Is it here: " + theObj);
				MetaModel<?> model2 = MetaModel.of(theObj.getClass());
				String primaryKey2 = model2.getPrimaryKey().getColumnName();
				Method m2 = model2.getGetterMethod(primaryKey2);
				int pk2 = (int) m2.invoke(theObj);
				// we looking for the id of one of the object in the hashset
				if (pk == pk2) {
					// perform update
					hSet.remove(theObj); // id matched
					break;
				} 
			}
			hSet.add(o); // new object
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

		cache.forEach((k, v) -> log.info("UPDATE PER CRUD Key " + k + " Value " + v));
		return cache;
	}

	public HashMap<Class<?>, HashSet<Object>> addAllFromDBToCache(final Class<?> clazz, Optional<List<Object>> list) {
		// new hashset every time user log in
		HashSet<Object> hSet = new HashSet<>();
		// loop through
		for (Object theObj : list.get()) {
			hSet.add(theObj);
		}
		this.cache.put(clazz, hSet);
		cache.forEach((k, v) -> log.info("FIRST TIME CACHE Key " + k + " Value " + v));
		return cache;
	}
	

	public HashMap<Class<?>, HashSet<Object>> getCache() {
		return cache;
	}

	public static ObjectCache getInstance() {
		return obj_cache;
	}
}