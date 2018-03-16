package com.chefswithoutlimits.customerchef.dataVO;

public class ItemListData {
	
	
	private String itemName = "";
	private String itemQuantity = "";
	private int quantity = 0;
	private String imageUrl = "";
	private String itemId = "";
	private String itemPrice = "";
	private String itemCurrency = "";
	
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemCurrency() {
		return itemCurrency;
	}
	public void setItemCurrency(String itemCurrency) {
		this.itemCurrency = itemCurrency;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemQuantity() {
		return itemQuantity;
	}
	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	
	

}
