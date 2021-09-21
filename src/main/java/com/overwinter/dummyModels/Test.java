package com.overwinter.dummyModels;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Getter;
import com.overwinter.annotations.Id;
import com.overwinter.annotations.Setter;

@Entity(tableName = "test_table")
public class Test {
	@Id(columnName = "test_id")
	private int id;

	@Column(columnName = "test_username")
	private String testUserName;

	@Column(columnName = "test_password")
	private String testPassword;
	
	@Getter(name = "test_password")
	public String getTestPassword() {
		return testPassword;
	}

	public Test(String testUserName, String testPassword) {
		super();
		this.testUserName = testUserName;
		this.testPassword = testPassword;
	}
	
}
