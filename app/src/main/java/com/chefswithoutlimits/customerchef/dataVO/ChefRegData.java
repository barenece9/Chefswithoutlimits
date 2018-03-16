package com.chefswithoutlimits.customerchef.dataVO;

import java.io.Serializable;

public class ChefRegData implements Serializable{

	String vkName = "";
	String firstName = "";
	String lastName = "";
	String email = "";
	String password = "";
	String conPassword = "";	
	String phoneNo = "";
	String venueType = "";
	String venueTypeid = "";
	String seriveOffer1 = "";
	String seriveOffer2 = "";
	String seriveOffer3 = "";
	String deleveryVehicle = "";

	public String getServiceArray() {
		return serviceArray;
	}

	public void setServiceArray(String serviceArray) {
		this.serviceArray = serviceArray;
	}

	String deleveryBicycle = "";
	String deleveryWalking = "";
	String serviceArray="";
	
	public String getVenueType() {
		return venueType;
	}
	public void setVenueType(String venueType) {
		this.venueType = venueType;
	}
	public String getVenueTypeId() {
		return venueTypeid;
	}
	public void setVenueTypeId(String venueTypeid) {
		this.venueTypeid = venueTypeid;
	}
	public String getSeriveOffer1() {
		return seriveOffer1;
	}
	public void setSeriveOffer1(String seriveOffer1) {
		this.seriveOffer1 = seriveOffer1;
	}
	public String getSeriveOffer2() {
		return seriveOffer2;
	}
	public void setSeriveOffer2(String seriveOffer2) {
		this.seriveOffer2 = seriveOffer2;
	}
	public String getSeriveOffer3() {
		return seriveOffer3;
	}
	public void setSeriveOffer3(String seriveOffer3) {
		this.seriveOffer3 = seriveOffer3;
	}
	public String getDeleveryVehicle() {
		return deleveryVehicle;
	}
	public void setDeleveryVehicle(String deleveryVehicle) {
		this.deleveryVehicle = deleveryVehicle;
	}
	public String getDeleveryBicycle() {
		return deleveryBicycle;
	}
	public void setDeleveryBicycle(String deleveryBicycle) {
		this.deleveryBicycle = deleveryBicycle;
	}
	public String getDeleveryWalking() {
		return deleveryWalking;
	}
	public void setDeleveryWalking(String deleveryWalking) {
		this.deleveryWalking = deleveryWalking;
	}
	public String getVkName() {
		return vkName;
	}
	public void setVkName(String vkName) {
		this.vkName = vkName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConPassword() {
		return conPassword;
	}
	public void setConPassword(String conPassword) {
		this.conPassword = conPassword;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}	
	
	
}
