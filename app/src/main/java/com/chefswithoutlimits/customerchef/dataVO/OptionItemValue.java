package com.chefswithoutlimits.customerchef.dataVO;

public class OptionItemValue {

	String ItemId = "";
	String ItemStatus = "";
	String ItemName="";
	String ItemSalePrice="";
	String ItemRegularPrice="";
	Boolean ItemSelect = false;



	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public String getItemSalePrice() {
		return ItemSalePrice;
	}

	public void setItemSalePrice(String itemSalePrice) {
		ItemSalePrice = itemSalePrice;
	}

	public String getItemRegularPrice() {
		return ItemRegularPrice;
	}

	public void setItemRegularPrice(String itemRegularPrice) {
		ItemRegularPrice = itemRegularPrice;
	}

	public String getItemID() {
		return ItemId;
	}
	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}
	public String getItemStatus() {
		return ItemStatus;
	}
	public void setItemStatus(String ItemStatus) {
		this.ItemStatus = ItemStatus;
	}

	public Boolean getItemSelect() {
		return ItemSelect;
	}

	public void setItemSelect(Boolean itemSelect) {
		ItemSelect = itemSelect;
	}

	
	
}
