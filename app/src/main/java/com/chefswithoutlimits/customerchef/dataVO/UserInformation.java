package com.chefswithoutlimits.customerchef.dataVO;

public class UserInformation {

	private String userId = "";
	private String kitchen_ID = "";
	private String firstName = "";
	private String midName = "";
	private String lastName = "";
	private String contact = "";
	private String email = "";
	private String usertype = "";
	private String kichen_Logo="";
	private String insurance_file="";
	private String offline_stripe_user_id="";
	private String User_log="";
	private Boolean is_Login=false;


	public String getKichen_Logo() {
		return kichen_Logo;
	}

	public void setKichen_Logo(String kichen_Logo) {
		this.kichen_Logo = kichen_Logo;
	}

	public String getInsurance_file() {
		return insurance_file;
	}

	public void setInsurance_file(String insurance_file) {
		this.insurance_file = insurance_file;
	}

	public String getUser_log() {
		return User_log;
	}

	public void setUser_log(String user_log) {
		User_log = user_log;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKitchenId() {
		return kitchen_ID;
	}
	public void setKitchenId(String kitchen_ID) {
		this.kitchen_ID = kitchen_ID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public Boolean getIs_Login() {
		return is_Login;
	}
	public void setIs_Login(Boolean is_Login) {
		this.is_Login = is_Login;
	}
	public String getOffline_stripe_user_id() {
		return offline_stripe_user_id;
	}

	public void setOffline_stripe_user_id(String offline_stripe_user_id) {
		this.offline_stripe_user_id = offline_stripe_user_id;
	}
	
}
