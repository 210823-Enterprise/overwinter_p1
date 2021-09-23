package com.overwinter.dummyModels;

import com.overwinter.annotations.Column;
import com.overwinter.annotations.Entity;
import com.overwinter.annotations.Getter;
import com.overwinter.annotations.Id;
import com.overwinter.annotations.Setter;

@Entity(tableName = "test")
public class Tester {
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

	@Setter(name = "test_password")
	public void setTestPassword(String password) {
		this.testPassword = password;
	}

	@Getter(name = "test_username")
	public String getTestUserName() {
		return testUserName;
	}

	@Setter(name = "test_username")
	public void setTestUserName(String username) {
		this.testUserName = username;
	}

	@Getter(name = "test_id")
	public int getTestUserId() {
		return id;
	}

	@Setter(name = "test_id")
	public void setTestUserId(int id) {
		this.id=id;
	}

	public Tester() {
		super();
	}
	public Tester(String testUserName, String testPassword) {
		super();
		this.testUserName = testUserName;
		this.testPassword = testPassword;
	}

	public Tester(int id, String testUserName, String testPassword) {
		super();
		this.id = id;
		this.testUserName = testUserName;
		this.testPassword = testPassword;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + ", testUserName=" + testUserName + ", testPassword=" + testPassword + "]";
	} 
	
}
