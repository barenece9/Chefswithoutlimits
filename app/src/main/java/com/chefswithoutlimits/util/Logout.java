package com.chefswithoutlimits.util;


import android.content.Context;
import android.content.Intent;

import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.customerchef.activity.home.HomeActivity;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.stripeconnect.StripeSession;


public class Logout {


	public static void logOut(Context  context)
	{

		// logout from stripe =========================================
		RememberData rememberData = Sharepreferences.getRememberStripe(context);
		if(rememberData.isLogin()){
			Sharepreferences.setRememberStripe(context, false, "chef", 0);
			StripeSession.Stripelogout();
		}else{

		}

		// logout from chef or customer =========================================
		ConstantUtils.onlineCartStatus=false;

		Sharepreferences.setRememberLogin(context, false, "", 0);
		//KitchenData kitchenData=new KitchenData();
		//Sharepreferences.saveSharePreferanceKitchenData(context,kitchenData);
		UserInformation userInformation=new UserInformation();
		Sharepreferences.saveSharePreferance(context,userInformation);
		context.startActivity(new Intent(context,HomeActivity.class));
	}
}
