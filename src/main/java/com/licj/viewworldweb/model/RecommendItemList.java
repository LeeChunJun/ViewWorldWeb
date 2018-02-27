package com.licj.viewworldweb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.licj.viewworldweb.model.table.ItemTable;


public class RecommendItemList {
	private List<RecommendItem> recommendItems = new ArrayList<RecommendItem>();

	public RecommendItemList() {

	}

	public RecommendItemList(List<RecommendedItem> items) {
		List<String> itemIDList = new ArrayList<String>();
		for (RecommendedItem item : items) {
			itemIDList.add(String.valueOf(item.getItemID()));
		}

		Map<String, Item> itemsMap = new ItemTable().getItemMap(itemIDList);

		for (RecommendedItem item : items) {
			String itemID = String.valueOf(item.getItemID());
			Item itemTmp = itemsMap.get(itemID);
			if (itemTmp != null) {
				RecommendItem ri = new RecommendItem(itemTmp, item.getValue());
				recommendItems.add(ri);
			}
		}
	}

	public List<RecommendItem> getRecommendItems() {
		return recommendItems;
	}

	public void setRecommendItems(List<RecommendItem> recommendItems) {
		this.recommendItems = recommendItems;
	}
	
	public int getTotalSum(){
		return this.recommendItems.size();
	}

	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean flag = false;
		for (RecommendItem item : recommendItems) {
			if (flag) {
				sb.append(", ");
			} else {
				flag = true;
			}
			sb.append(item.toJSON());
		}
		sb.append("]");
		return sb.toString();
	}

}
