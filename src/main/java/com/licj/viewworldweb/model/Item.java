package com.licj.viewworldweb.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.licj.viewworldweb.utils.StringUtil;

public class Item {
	public static final String ID = "id";
	public static final String SONG_NAME = "song_name";
	public static final String SINGER_NAME = "singer_name";
	public static final String PIC_URL = "pic_url";
	public static final String PUBLISH_TIME = "publish_time";
	public static final String LYRIC = "lyric";
	public static final String TAGS = "tags";

	private long id;
	private String song_name;
	private String singer_name;
	private String pic_url;
	private String publish_time;
	private String lyric;
	private List<String> tags;

	public Item() {

	}

	public Item(long id, String song_name, String singer_name, String pic_url, String publish_time, String lyric,
			List<String> tags) {
		super();
		this.id = id;
		this.song_name = song_name;
		this.singer_name = singer_name;
		this.pic_url = pic_url;
		this.publish_time = publish_time;
		this.lyric = lyric;
		this.tags = tags;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSong_name() {
		return song_name;
	}

	public void setSong_name(String song_name) {
		this.song_name = song_name;
	}

	public String getSinger_name() {
		return singer_name;
	}

	public void setSinger_name(String singer_name) {
		this.singer_name = singer_name;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
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
		sb.append("SONG_NAME: " + song_name + "  ");
		sb.append("SINGER_NAME: " + singer_name + "  ");
		sb.append("PIC_URL: " + pic_url + "  ");
		sb.append("PUBLISH_TIME: " + publish_time + "  ");
		sb.append("LYRIC: " + lyric + "  ");
		sb.append("TAGS: " + StringUtil.connectString(tags, ", "));
		return sb.toString();
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + ID + "\":" + id + ", ");
		sb.append("\"" + SONG_NAME + "\":\"" + song_name + "\", ");
		sb.append("\"" + SINGER_NAME + "\":\"" + singer_name + "\", ");
		sb.append("\"" + PIC_URL + "\":\"" + pic_url + "\", ");
		sb.append("\"" + PUBLISH_TIME + "\":\"" + publish_time + "\", ");
//		sb.append("\"" + LYRIC + "\":\"" + lyric + "\", ");
		sb.append("\"" + TAGS + "\":\"" + StringUtil.connectString(tags, " ") + "\"}");
		return sb.toString();
	}
}
