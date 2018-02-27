package com.licj.viewworldweb.model;

import java.util.List;

import com.licj.viewworldweb.utils.StringUtil;

public class User {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String PHONE = "phone";
	public static final String TAGS = "tags";

	private long id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private List<String> tags;

	public User() {

	}

	public User(long id, String name, String email, String password, String phone, List<String> tags) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.tags = tags;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + ID + "\":" + id + ", ");
		sb.append("\"" + NAME + "\":\"" + name + "\", ");
		sb.append("\"" + EMAIL + "\":\"" + email + "\", ");
		sb.append("\"" + PASSWORD + "\":\"" + password + "\", ");
		sb.append("\"" + PHONE + "\":\"" + phone + "\", ");
		sb.append("\"" + TAGS + "\":\"" + StringUtil.connectString(tags, " ") + "\"}");
		return sb.toString();
	}
}
