package com.chefswithoutlimits.customerchef.dataVO;

public class RememberData {
	
	private String userName = "";
	private String password = "";
	private int remember = 0;
	private boolean login=false;
	private String userType="";


	private boolean savecard=false;
	private String cardnumber="";
	private String cvvnumber="";

	public boolean isSavecard() {
		return savecard;
	}

	public void setSavecard(boolean savecard) {
		this.savecard = savecard;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getCvvnumber() {
		return cvvnumber;
	}

	public void setCvvnumber(String cvvnumber) {
		this.cvvnumber = cvvnumber;
	}

	public int getExpdate() {
		return expdate;
	}

	public void setExpdate(int expdate) {
		this.expdate = expdate;
	}

	public int getExpyear() {
		return expyear;
	}

	public void setExpyear(int expyear) {
		this.expyear = expyear;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	private int expdate=0;
	private int expyear=0;
	private String last4="";

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRemember() {
		return remember;
	}
	public void setRemember(int remember) {
		this.remember = remember;
	}
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}

}
