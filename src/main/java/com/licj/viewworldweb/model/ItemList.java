package com.licj.viewworldweb.model;

import java.util.HashMap;
import java.util.Map;

public class ItemList {
	public static final String ITEM = "item";
	public static final String VALUE = "score";
	
	private Map<Item, Float> items = new HashMap<>();
	
	public int size(){
		return items.size();
	}
	
	public void add(Item i, Float f){
		items.put(i, f);
	}
	
	public Float get(Item i){
		return items.get(i);
	}
	
	public Float remove(Item i){
		return items.remove(i);
	}
	
	public String toJSON(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean flag = false;
		for(Map.Entry<Item, Float> item : items.entrySet()){
			
			if(flag){
				sb.append(", ");
			} else {
				flag = true;
			}
			sb.append("{\"" + ITEM + "\":" + item.getKey().toJSON() + ", ");
			sb.append("\"" + VALUE + "\":" + item.getValue() + "}");
			
		}
		sb.append("]");
		return sb.toString();
	}
}
