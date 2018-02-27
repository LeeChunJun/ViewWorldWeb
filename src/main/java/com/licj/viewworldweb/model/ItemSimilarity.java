package com.licj.viewworldweb.model;

public class ItemSimilarity {
	public static final String Item_ID1 = "itemID1";
	public static final String Item_ID2 = "itemID2";
	public static final String Similarity = "similarity";
	
	private long itemID1;
	private long itemID2;
	private double similarity;
	
	public ItemSimilarity() {

	}

	public ItemSimilarity(long itemID1, long itemID2, double similarity) {
		super();
		this.itemID1 = itemID1;
		this.itemID2 = itemID2;
		this.similarity = similarity;
	}

	public long getItemID1() {
		return itemID1;
	}

	public void setItemID1(long itemID1) {
		this.itemID1 = itemID1;
	}

	public long getItemID2() {
		return itemID2;
	}

	public void setItemID2(long itemID2) {
		this.itemID2 = itemID2;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Item_ID1: " + itemID1 + "  ");
		sb.append("Item_ID2: " + itemID2 + "  ");
		sb.append("Similarity: " + similarity);
		return sb.toString();
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"" + Item_ID1 + "\":" + itemID1 + ", ");
		sb.append("\"" + Item_ID2 + "\":\"" + itemID2 + "\", ");
		sb.append("\"" + Similarity + "\":\"" + similarity + "\"}");
		return sb.toString();
	}

}
