package com.overwinter.dummyModels;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Id;

@Entity(tableName = "test_table")
public class Test {
	@Id(columnName = "test_id")
	private int id;

	@Column(columnName = "test_username")
	private String testUserName;

	@Column(columnName = "test_password")
	private String testPassword;

}
