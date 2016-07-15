package com.stats.chinastats.model;

import java.io.Serializable;

public class ItemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public ItemInfo(String id, String itemNo, String itemName, String itemAnswer) {
		super();
		this.id = id;
		this.itemNo = itemNo;
		this.itemName = itemName;
		this.itemAnswer = itemAnswer;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public ItemInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ItemInfo [id=" + id + ", itemNo=" + itemNo + ", itemName="
				+ itemName + ", itemAnswer=" + itemAnswer + "]";
	}
	public String getItemAnswer() {
		return itemAnswer;
	}
	public void setItemAnswer(String itemAnswer) {
		this.itemAnswer = itemAnswer;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String itemNo;
	private String itemName;
	private String itemAnswer;

}
