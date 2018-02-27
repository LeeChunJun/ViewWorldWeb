package com.licj.viewworldweb.model;

public class Rate {
	public static final String USER_ID = "userID";
	public static final String ITEM_ID = "itemID";
	public static final String PREFERENCE = "preference";
	public static final String TIMESTAMP = "timestamp";

	private long userId;
	private long itemId;
	private float preference;
	private int timestamp;
	
	public Rate(){
		
	}
	
	public Rate(long userId, long itemId, float preference, int timestamp) {
		super();
		this.userId = userId;
		this.itemId = itemId;
		this.preference = preference;
		this.timestamp = timestamp;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public float getPreference() {
		return preference;
	}

	public void setPreference(float preference) {
		this.preference = preference;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User ID: " + userId + "  ");
		sb.append("Item ID: " + itemId + "  ");
		sb.append("preference: " + preference + "  ");
		sb.append("Timestamp: " + timestamp);
		return sb.toString();
	}
	
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + USER_ID + "\":" + userId + ", ");
		sb.append("\"" + ITEM_ID + "\":\"" + itemId + "\", ");
		sb.append("\"" + PREFERENCE + "\":\"" + preference + "\", ");
		sb.append("\"" + TIMESTAMP + "\":\"" + timestamp + "\"}");
		return sb.toString();
	}

}
