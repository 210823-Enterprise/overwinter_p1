package com.overwinter.objectMapper;

import java.util.HashMap;
import java.util.HashSet;

public class ObjectCache {
	private final HashMap<Class<?>,HashSet<Object>> cache;
	static final ObjectCache obj_cache = new ObjectCache();
	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}
	private static ObjectCache getInstance() {
		return obj_cache;
	}
	
	public ObjectCache putObjectInCache(Object o) {
		return getInstance();
	}
	
}