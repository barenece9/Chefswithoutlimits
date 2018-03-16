package com.chefswithoutlimits.util;


import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Sharepreferences {

	private static String preferanceStripe = "Stripe";
	private static String preferanceLogin = "Login";
	private static String preferanceName = "SaveUserInfo"; 
	private static String preferanceRemember = "RememberMe";
	private static String preferanceRememberPass = "RememberPass";
	private static String preferancekitchendata="RememberKitchenData";
	private static String saveCard="saveCard";

	public static void saveSharePreferance(Context context,UserInformation userInfo)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceName, context.MODE_PRIVATE);
		Editor editor = pref.edit();


		editor.putString("id", userInfo.getUserId());
		editor.putString("kitchenID", userInfo.getKitchenId());
		editor.putString("FirstName",userInfo.getFirstName());
		editor.putString("MidName", userInfo.getMidName());
		editor.putString("LastName", userInfo.getLastName());
		editor.putString("contact", userInfo.getContact());
		editor.putString("email", userInfo.getEmail());
		editor.putString("usertype", userInfo.getUsertype());

		editor.putString("User_log",userInfo.getUser_log());
		editor.putString("kichen_Logo",userInfo.getKichen_Logo());
		editor.putString("insurance_file",userInfo.getInsurance_file());
		editor.putString("offline_stripe_user_id",userInfo.getOffline_stripe_user_id());
		editor.putBoolean("isLogIn",userInfo.getIs_Login());

		// Save the changes in SharedPreferences
		editor.commit(); // commit changes
	}


	public static void saveEditSharePreferance(Context context,UserInformation userInfo)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceName, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		//editor.putString("User_log",userInfo.getUser_log());
		editor.putString("kichen_Logo",userInfo.getKichen_Logo());
		editor.putString("insurance_file",userInfo.getInsurance_file());

		// Save the changes in SharedPreferences
		editor.commit(); // commit changes
	}

	public static void updateStripeIdSharePreferance(Context context,String offline_stripe_user_id)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceName, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putString("offline_stripe_user_id",offline_stripe_user_id);

		// Save the changes in SharedPreferences
		editor.commit(); // commit changes
	}

	public static UserInformation getSharePreferance(Context context)
	{
		UserInformation userInfo = new UserInformation();

		SharedPreferences pref = context.getSharedPreferences(preferanceName, context.MODE_PRIVATE); 

		userInfo.setUserId( pref.getString("id", ""));
		userInfo.setKitchenId( pref.getString("kitchenID", ""));
		userInfo.setFirstName(pref.getString("FirstName", ""));
		userInfo.setMidName(pref.getString("MidName", ""));
		userInfo.setLastName(pref.getString("LastName", ""));
		userInfo.setContact(pref.getString("contact", ""));
		userInfo.setEmail(pref.getString("email", ""));
		userInfo.setUsertype(pref.getString("usertype", ""));

		userInfo.setUser_log(pref.getString("User_log",""));
		userInfo.setKichen_Logo(pref.getString("kichen_Logo",""));
		userInfo.setInsurance_file(pref.getString("insurance_file",""));
		userInfo.setOffline_stripe_user_id(pref.getString("offline_stripe_user_id",""));
		userInfo.setIs_Login(pref.getBoolean("isLogIn",false));

		return userInfo;   
	}


	public static void setRememberMe(Context context,String userName , String password , int remember)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceRemember, context.MODE_PRIVATE); 
		Editor editor = pref.edit(); 

		editor.putString("userName", userName); 
		editor.putString("password",password); 
		editor.putInt("remember", remember);   

		editor.commit();
	}

	public static RememberData getRememberMe(Context context)
	{
		RememberData rememberData = new RememberData(); 

		SharedPreferences pref = context.getSharedPreferences(preferanceRemember, context.MODE_PRIVATE);

		rememberData.setUserName(pref.getString("userName", ""));
		rememberData.setPassword(pref.getString("password", ""));
		rememberData.setRemember(pref.getInt("remember", 0)); 

		return rememberData; 
	}
	
	public static void setPasswordSetting(Context context,String password)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceRememberPass, context.MODE_PRIVATE); 
		Editor editor = pref.edit(); 	    

		editor.putString("password",password); 	   

		editor.commit();
	}
	
	public static RememberData getPasswordSetting(Context context)
	{
		RememberData rememberData = new RememberData(); 
		SharedPreferences pref = context.getSharedPreferences(preferanceRememberPass, context.MODE_PRIVATE);
		
		rememberData.setPassword(pref.getString("password", ""));		

		return rememberData; 
	}



	/*for kitchen data.............*/
	public static void saveSharePreferanceKitchenData(Context context,KitchenData kitchenData)
	{
		SharedPreferences pref = context.getSharedPreferences(preferancekitchendata, context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("kitchen_id",kitchenData.getKitchen_id());
		editor.putString("kitchenName", kitchenData.getKichen_name());
		editor.putString("kichen_Logo",kitchenData.getKichen_Logo());
		editor.putString("insurance_file",kitchenData.getInsurance_file());
		editor.putString("distance",kitchenData.getDistance());
		editor.putString("distance_cover_by_vehicle",kitchenData.getDistance_cover_by_vehicle());
		editor.putString("min_order",kitchenData.getMin_order());
		// Save the changes in SharedPreferences
		editor.commit(); // commit changes
	}

	public static KitchenData getSharePreferanceKitchenData(Context context)
	{
		KitchenData kitchenData = new KitchenData();
		SharedPreferences pref = context.getSharedPreferences(preferancekitchendata, context.MODE_PRIVATE);
		kitchenData.setKitchen_id(pref.getString("kitchen_id",""));
		kitchenData.setKichen_name( pref.getString("kitchenName", ""));
		kitchenData.setKichen_Logo(pref.getString("kichen_Logo",""));
		kitchenData.setInsurance_file(pref.getString("insurance_file",""));
		kitchenData.setDistance(pref.getString("distance",""));
		kitchenData.setDistance_cover_by_vehicle(pref.getString("distance_cover_by_vehicle",""));
		kitchenData.setMin_order(pref.getString("min_order",""));
		return kitchenData;
	}

    /// remember login ==============================================================================
	public static void setRememberLogin(Context context,boolean login , String usertype , int remember)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceLogin, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putBoolean("login", login);
		editor.putString("usertype",usertype);
		editor.putInt("remember", remember);

		editor.commit();
	}

	public static RememberData getRememberLogin(Context context)
	{
		RememberData rememberData = new RememberData();

		SharedPreferences pref = context.getSharedPreferences(preferanceLogin, context.MODE_PRIVATE);

		rememberData.setLogin(pref.getBoolean("login",false));
		rememberData.setUserType(pref.getString("usertype", ""));
		rememberData.setRemember(pref.getInt("remember", 0));

		return rememberData;
	}

	// remember stripe login =======================================================================

	public static void setRememberStripe(Context context,boolean login , String usertype , int remember)
	{
		SharedPreferences pref = context.getSharedPreferences(preferanceStripe, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putBoolean("login", login);
		editor.putInt("remember", remember);

		editor.commit();
	}

	public static RememberData getRememberStripe(Context context)
	{
		RememberData rememberData = new RememberData();

		SharedPreferences pref = context.getSharedPreferences(preferanceStripe, context.MODE_PRIVATE);

		rememberData.setLogin(pref.getBoolean("login",false));
		rememberData.setRemember(pref.getInt("remember", 0));

		return rememberData;
	}

	// card information store................
	public static void setCardData(Context context,boolean save_card , String card_number ,String cvv_number, int exp_date,int exp_year,String last4)
	{
		SharedPreferences pref = context.getSharedPreferences(saveCard, context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putBoolean("save_card", save_card);
		editor.putString("card_number",card_number);
		editor.putString("cvv_number",cvv_number);
		editor.putString("last4",last4);
		editor.putInt("exp_date", exp_date);
		editor.putInt("exp_year", exp_year);

		editor.commit();
	}

	public static RememberData getCardData(Context context)
	{
		RememberData rememberData = new RememberData();

		SharedPreferences pref = context.getSharedPreferences(saveCard, context.MODE_PRIVATE);

		rememberData.setSavecard(pref.getBoolean("save_card",false));
		rememberData.setCardnumber(pref.getString("card_number", ""));
		rememberData.setCvvnumber(pref.getString("cvv_number", ""));
		rememberData.setLast4(pref.getString("last4",""));
		rememberData.setExpdate(pref.getInt("exp_date", 0));
		rememberData.setExpyear(pref.getInt("exp_year", 0));

		return rememberData;
	}
}
