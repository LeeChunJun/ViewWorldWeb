package com.licj.viewworldweb.model;

public class RecommendItem {
	private static final String ITEM = "item";
	private static final String VALUE = "score";

	private Item item;
	private float value;

	public RecommendItem(Item item, float value) {
		this.item = item;
		this.value = value;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Item:\t" + item.toString() + "\t");
		sb.append("Score:\t" + value);
		return sb.toString();
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + ITEM + "\":" + item.toJSON() + ",");
		sb.append("\"" + VALUE + "\":" + value + "}");
		return sb.toString();
	}

}
