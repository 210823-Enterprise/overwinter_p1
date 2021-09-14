package com.overwinter.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Configuration {
	private String dbUrl;
	private String Username;
	private String password;
	private List<MetaModel<Class<?>>> metaModelList;

	//does what Hibernate mapping does
	public Configuration addAnnotatedClass(Class annotatedClass) {

		if (metaModelList == null) {
			metaModelList = new LinkedList<MetaModel<Class<?>>>();

		}
		metaModelList.add(MetaModel.of(annotatedClass));
		// of model should transform class into data model to be transposed
		return this;
	}

	public List<MetaModel<Class<?>>> getMetaModels() {
		return (List<MetaModel<Class<?>>>) (metaModelList == null ? Collections.emptyList() : metaModelList);
	}
}
