package com.chefswithoutlimits.customerchef.activity.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.Notification.Config;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ScheduledDeliveryActivity;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ShippingScreen;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.registration.ChefEmailVerifyActivity;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{

	EditText custmerEdtuser;
	EditText custmerEdtpass;

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	Button customerLogin;
	CheckBox rememberme;

	private ProgressDialog progressDialog;
	String userName = "";
	String password = "";
	String cart_session_id="";
	Boolean isGuestUserLogin=false;
	
	TextView forgotPassword;
	ProgressDialog progressDialog5;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);

		Bundle getBundle = null;
		getBundle = this.getIntent().getExtras();
		if(getBundle!=null){
			String guestOrder=getBundle.getString("guestOrder");
			if(guestOrder.equalsIgnoreCase("1")){
				isGuestUserLogin=true;
				cart_session_id= ConstantUtils.sessionUserId;
			}else {
				isGuestUserLogin=false;
				cart_session_id= "";
			}
		}



		customerLogin = (Button)findViewById(R.id.customerLogin);
		//customerLogin.setOnClickListener(this);
		customerLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				userName = custmerEdtuser.getText().toString();
				password = custmerEdtpass.getText().toString();

				if(userName.equalsIgnoreCase(""))
				{
					CreateDialog.showDialog(LoginActivity.this, "Enter username");

				}else if(password.equalsIgnoreCase(""))
				{
					CreateDialog.showDialog(LoginActivity.this, "Enter password");

				}
				else{

					if(rememberme.isChecked()){
						Sharepreferences.setRememberMe(LoginActivity.this, userName, password, 1);
					}

					Sharepreferences.setPasswordSetting(LoginActivity.this,password);
					if(InternetConnectivity.isConnectedFast(LoginActivity.this)){
						doUserLogin();
					}else {
						Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
					}

				}


			}
		});

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("LOGIN");

		forgotPassword = (TextView)findViewById(R.id.forgotPassword);
		forgotPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		forgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});

		custmerEdtuser = (EditText)findViewById(R.id.custmerEdtuser);
		custmerEdtpass = (EditText)findViewById(R.id.custmerEdtpass);
		rememberme = (CheckBox)findViewById(R.id.rememberme);
		rememberme.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean check) {
				// TODO Auto-generated method stub

				if(check){

					userName = custmerEdtuser.getText().toString();
					password = custmerEdtpass.getText().toString();

					Sharepreferences.setRememberMe(LoginActivity.this, userName, password, 1);

				}else{

					Sharepreferences.setRememberMe(LoginActivity.this, "", "", 0);
				}
			}
		});

		RememberData rememberData = Sharepreferences.getRememberMe(this);

		if(rememberData.getRemember() == 1){

			custmerEdtuser.setText(rememberData.getUserName());
			custmerEdtpass.setText(rememberData.getPassword());
			rememberme.setChecked(true);
		}else{

			custmerEdtuser.setText("");
			custmerEdtpass.setText("");
			rememberme.setChecked(false) ;
		}


	}




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	

		case R.id.btnBack:

			startActivity(new Intent(LoginActivity.this, HomeActivity.class));
			finish();

			break;

		case R.id.btnLogout:

			break;
		}
	}

	//===============Register Customer Function===================
	void doUserLogin()
	{
		final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url="";

		url = WebServiceURL.LOGIN_URL;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseResponse(response);
				if(progressDialog!=null)
					progressDialog.dismiss();
				Log.d("Response", response);                        
			}
				}, 
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog!=null)
							progressDialog.dismiss();
						System.out.println("Error=========="+error);
						Toast.makeText(LoginActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 
				params.put("username",userName);
				params.put("password",password);
				params.put("cart_session_id",ConstantUtils.sessionUserId);
				params.put("format","json");


				Log.e("username",userName);
				Log.e("password",password);
				Log.e("cart_session_id",ConstantUtils.sessionUserId);
				Log.e("format","json");

				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();


	}


	void parseResponse(String response){	

		UserInformation userInfoObj = new UserInformation();
		String verifyStatus = "";
		String message="";
		System.out.println("Response :"+response);
		try {

			JSONObject parentObject = new JSONObject(response);
			verifyStatus = parentObject.getString("verifyStatus");
			message=parentObject.getString("message");
			if (verifyStatus.contains("1")) {

			JSONArray parentArray = parentObject.getJSONArray("Records");
				if (parentArray.length() > 0) {
					JSONObject innerObj = parentArray.optJSONObject(0);

					//JSONObject innerObj = userObj.optJSONObject("records");

					userInfoObj.setUserId(innerObj.getString("id"));
					userInfoObj.setFirstName(innerObj.getString("FirstName"));
					userInfoObj.setMidName(innerObj.getString("MidName"));
					userInfoObj.setLastName(innerObj.getString("LastName"));
					userInfoObj.setContact(innerObj.getString("contact"));
					userInfoObj.setEmail(innerObj.getString("email"));
					userInfoObj.setUsertype(innerObj.getString("usertype"));
					userInfoObj.setIs_Login(true);

					String userType = innerObj.getString("usertype");

					if (userType.equalsIgnoreCase("chef")) {
						userInfoObj.setKitchenId(innerObj.getString("kitchenID"));
						userInfoObj.setOffline_stripe_user_id(innerObj.getString("offline_stripe_user_id"));
						userInfoObj.setKichen_Logo(innerObj.getString("kichen_Logo"));
						userInfoObj.setInsurance_file(innerObj.getString("insurance_file"));
						Sharepreferences.saveSharePreferance(this, userInfoObj);
						Sharepreferences.setRememberLogin(LoginActivity.this, true, "chef", 1);
					}else {
						userInfoObj.setUser_log(innerObj.getString("User_log"));
						Sharepreferences.saveSharePreferance(this, userInfoObj);
						Sharepreferences.setRememberLogin(LoginActivity.this, true, "customer", 1);
					}

					if (verifyStatus.contains("1")) {

						if (userType.equalsIgnoreCase("chef")) {
							//userInfoObj.setKitchenId(innerObj.getString("kitchenID"));
							SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
							String regId=pref.getString("regId","");

							if(!regId.equalsIgnoreCase("")) {
								INSERT_FIREBASE_REG_ID(regId);
							}else {
								startActivity(new Intent(LoginActivity.this, ChefDashboardActivity.class));
								finish();
							}

						} else {
							//ConstantUtils.offlineCartStatus=false;
							// for customer======================
							if(!isGuestUserLogin){
								startActivity(new Intent(LoginActivity.this, CustomerDashboardActivity.class));
								finish();
							}else {
								if(ConstantUtils.activityLunch.equalsIgnoreCase("ShippingScreen")){
									Intent intent2=new Intent(LoginActivity.this,ShippingScreen.class);
									startActivity(intent2);
									finish();
								}else if(ConstantUtils.activityLunch.equalsIgnoreCase("ScheduledDeliveryActivity")){
									Intent intent1 = new Intent(LoginActivity.this, ScheduledDeliveryActivity.class);
									intent1.putExtra("selectcategory",ConstantUtils.selectcategory);
									startActivity(intent1);
									finish();
								}else {
									startActivity(new Intent(LoginActivity.this, CustomerDashboardActivity.class));
									finish();
								}

							}

						}


						//RegisterActivities.removeAllActivities();
						//finish();
					}

				}

		    }else {
				Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_LONG).show();
				Sharepreferences.setRememberLogin(LoginActivity.this, false, "", 0);
				if(message.equalsIgnoreCase("Please enter the verification code")){
					startActivity(new Intent(LoginActivity.this, ChefEmailVerifyActivity.class));
					finish();
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		//Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_LONG).show();
	}
	
	 AlertDialog b;
	public void showDialog() {
		
	    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
	    dialogBuilder.setView(dialogView);
	   
	    final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

	    dialogBuilder.setTitle("Forgot Password");
	    dialogBuilder.setMessage("Enter email below");
	    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	           String email =  edt.getText().toString();
	        	
	        	forgotPassword(email);
	        	 
	        }
	    });
	    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            //pass
	        	 b.dismiss();
	        }
	    });
	    b = dialogBuilder.create();
	    b.show();
	   
	}
	    

	//===============Forgot password===================
		void forgotPassword(final String email)
		{
			progressDialog=new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			String url;

			url =WebServiceURL.FORGOT_PASS;    	

			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					new Response.Listener<String>() 
					{
				@Override
				public void onResponse(String response) {
					parseForgotPass(response);
					if(progressDialog!=null)
						progressDialog.dismiss();				
					Log.d("Response", response);    
					
				}
					}, 
					new Response.ErrorListener() 
					{
						@Override
						public void onErrorResponse(VolleyError error) {
							if(progressDialog!=null)
								progressDialog.dismiss();
							System.out.println("Error=========="+error);
							Toast.makeText(LoginActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
						}
					}
					){     
				@Override
				protected Map<String, String> getParams()
				{   						
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("email",email);	
					params.put("format","json");
					return params;
				}
			};
			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(postRequest);
			AppController.getInstance().getRequestQueue().getCache().remove(url);
			AppController.getInstance().getRequestQueue().getCache().clear();
		}
		
		
		public void parseForgotPass(String response)
		{
			try {
				
				JSONObject parentObj = new JSONObject(response);
				
				//JSONArray jArray = parentObj.getJSONArray("output");
				
				//JSONObject childObj = jArray.getJSONObject(0);

				String status = parentObj.getString("status");
				if(status.equalsIgnoreCase("true")){
					String message = parentObj.getString("message");
					Toast.makeText(LoginActivity.this, message , Toast.LENGTH_LONG).show();
				}else if(status.equalsIgnoreCase("false")){
					String message = parentObj.getString("message");
					Toast.makeText(LoginActivity.this, message , Toast.LENGTH_LONG).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			b.dismiss();
			
			
		}


	void INSERT_FIREBASE_REG_ID(final  String regId)
	{
		final  ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.INSERT_FIREBASE_REG_ID;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {

						parseData(response);
						System.out.println("@@@@@@@@@@@@@ "+response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
						System.out.println("Error=========="+error);
						// Toast.makeText(this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				UserInformation userInfo = Sharepreferences.getSharePreferance(LoginActivity.this);
				Map<String, String>  params = new HashMap<String, String>();

				params.put("user_id",userInfo.getUserId());
				params.put("regId",regId);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}


	public void parseData(String response)
	{
		try {
			JSONObject parentJSON = new JSONObject(response);

			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				startActivity(new Intent(LoginActivity.this, ChefDashboardActivity.class));
				finish();
				String message=parentJSON.getString("message");
				System.out.println("+++++++++++++++++++++++= "+message);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(LoginActivity.this, HomeActivity.class));
		finish();
	}
}
