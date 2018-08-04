package com.licj.viewworldweb.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.licj.viewworldweb.utils.StringUtil;

public class Item {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PUBLISH_TIME = "published_year";
	public static final String TAGS = "tags";

	private long id;
	private String name;
	private String publish_time;
	private List<String> tags;

	public Item() {

	}
	
	public Item(long id, String name, String publish_time, List<String> tags) {
		super();
		this.id = id;
		this.name = name;
		this.publish_time = publish_time;
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

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	// 相关性
	public double relevance(Item m) {
		String patternString = StringUtil.connectString(tags, "|");
		Pattern pattern = Pattern.compile(patternString);
		int count = 0;
		for (String mTag : m.getTags()) {
			Matcher matcher = pattern.matcher(mTag);
			if (matcher.matches()) {
				count++;
			}
		}
		return Math.log10(count + 1);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: " + id + "  ");
		sb.append("NAME: " + name + "  ");
		sb.append("PUBLISH_TIME: " + publish_time + "  ");
		sb.append("TAGS: " + StringUtil.connectString(tags, ", "));
		return sb.toString();
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + ID + "\":" + id + ", ");
		sb.append("\"" + NAME + "\":\"" + name + "\", ");
		sb.append("\"" + PUBLISH_TIME + "\":\"" + publish_time + "\", ");
		sb.append("\"" + TAGS + "\":\"" + StringUtil.connectString(tags, " ") + "\"}");
		return sb.toString();
	}
}
