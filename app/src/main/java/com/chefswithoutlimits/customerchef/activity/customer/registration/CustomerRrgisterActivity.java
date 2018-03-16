package com.chefswithoutlimits.customerchef.activity.customer.registration;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ScheduledDeliveryActivity;
import com.chefswithoutlimits.customerchef.activity.home.LoginActivity;
import com.chefswithoutlimits.customerchef.activity.home.HomeActivity;
import com.chefswithoutlimits.customerchef.activity.home.RegisterTypeActivity;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ShippingScreen;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.InternetConnectivity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerRrgisterActivity extends Activity implements OnClickListener{

	EditText custmerEdtuser;
	EditText custmerEdtpass;
	Button btnRegister;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	EditText custmerRegFName;
	EditText custmerRegMName;
	EditText custmerRegLName;
	EditText custmerRegEmail;
	EditText custmerRegPass;
	EditText custmerRegConPass;
	CheckBox rememberme;
	
	String firstName = "";
	String middleName="";
	String lastName = "";
	String email = "";
	String password = "";
	String conPassword = "";
	private ProgressDialog progressDialog;
	Boolean isGuestUserLogin=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_activity);

		Bundle getBundle = null;
		getBundle = this.getIntent().getExtras();
		if(getBundle!=null){
			String guestOrder=getBundle.getString("guestOrder");
			if(guestOrder.equalsIgnoreCase("1")){
				isGuestUserLogin=true;
				//cart_session_id= ConstantUtils.sessionUserId;
			}else {
				isGuestUserLogin=false;
				//cart_session_id= "";
			}
		}

		setWidget();
	}

	public void setWidget()
	{
		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("REGISTER");

		custmerRegFName = (EditText)findViewById(R.id.custmerRegFName);
		custmerRegMName=(EditText)findViewById(R.id.custmerRegMName);
		custmerRegLName = (EditText)findViewById(R.id.custmerRegLName);
		custmerRegEmail = (EditText)findViewById(R.id.custmerRegEmail);
		custmerRegPass = (EditText)findViewById(R.id.custmerRegPass);
		custmerRegConPass = (EditText)findViewById(R.id.custmerRegConPass);
		rememberme = (CheckBox)findViewById(R.id.rememberme);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	

		case R.id.btnBack:

			startActivity(new Intent(CustomerRrgisterActivity.this, RegisterTypeActivity.class));
			finish();

			break;

		case R.id.btnLogout:

			break;

		case R.id.btnRegister:

			firstName = custmerRegFName.getText().toString();
			middleName = custmerRegMName.getText().toString();
			lastName = custmerRegLName.getText().toString();
			email = custmerRegEmail.getText().toString();
			password = custmerRegPass.getText().toString();
			conPassword = custmerRegConPass.getText().toString();

			if(firstName.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter first name");
				
			}else if(lastName.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter last name");
				
			}else if(email.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter email");
				
			}else if(!isValidEmail(email)){
				
				CreateDialog.showDialog(this, "Email is not valid");
				
			}else if(password.equalsIgnoreCase("")){
				
				CreateDialog.showDialog(this, "Enter password");
				
			}else if(conPassword.equalsIgnoreCase("")){
				
				CreateDialog.showDialog(this, "Enter confirm password");
				
			}else if(!password.equalsIgnoreCase(conPassword)){
				
				CreateDialog.showDialog(this, "Password & confirm password is not match");
				
			}else{

				if(InternetConnectivity.isConnectedFast(CustomerRrgisterActivity.this)){
					doUserRegister();
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}
				

			}


			//startActivity(new Intent(CustomerRrgisterActivity.this,ChefEmailVerifyActivity.class));
			break;
		}
	}


	//===============Register Customer Function===================
	void doUserRegister()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerRrgisterActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CUSTOMER_REGISTER_URL;

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
						Toast.makeText(CustomerRrgisterActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 
				params.put("FirstName",firstName);
				params.put("MiddleName",middleName);
				params.put("LastName",lastName);
				params.put("password",password);
				params.put("passconf",password);
				params.put("email",email);
				//params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}


	void parseResponse(String response){	

		String message = "";
		String status="";
		System.out.println("Response :"+response);
		try {

			JSONObject parentObject = new JSONObject(response);
			//JSONArray parentArray = parentObject.getJSONArray("output");
			//JSONObject  obj = parentArray.optJSONObject(0);
			status=parentObject.getString("status");
			message = parentObject.getString("message");
			if(status.contains("success")){
				/*startActivity(new Intent(CustomerRrgisterActivity.this,ChefEmailVerifyActivity.class));
				finish();*/

				if(isGuestUserLogin){
					doUserLogin();
				}else {
					startActivity(new Intent(CustomerRrgisterActivity.this,LoginActivity.class));
					finish();
				}

			}
			Toast.makeText(CustomerRrgisterActivity.this, ""+message, Toast.LENGTH_LONG).show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//===============Register Customer Function===================
	void doUserLogin()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerRrgisterActivity.this);
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
						parseLoginResponse(response);
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
						Toast.makeText(CustomerRrgisterActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("username",email);
				params.put("password",password);
				params.put("cart_session_id",ConstantUtils.sessionUserId);
				params.put("format","json");


				Log.e("username",email);
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


	void parseLoginResponse(String response){

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

					if (userType.equalsIgnoreCase("customer")) {
						userInfoObj.setUser_log(innerObj.getString("User_log"));
						Sharepreferences.saveSharePreferance(this, userInfoObj);
						Sharepreferences.setRememberLogin(CustomerRrgisterActivity.this, true, "customer", 1);
					}else {

					}

					if (verifyStatus.contains("1")) {

						if (userType.equalsIgnoreCase("customer")) {

							// for customer======================
							if(!isGuestUserLogin){
								startActivity(new Intent(CustomerRrgisterActivity.this, CustomerDashboardActivity.class));
								finish();
							}else {
								if(ConstantUtils.activityLunch.equalsIgnoreCase("ShippingScreen")){
									Intent intent2=new Intent(CustomerRrgisterActivity.this,ShippingScreen.class);
									startActivity(intent2);
									finish();
								}else if(ConstantUtils.activityLunch.equalsIgnoreCase("ScheduledDeliveryActivity")){
									Intent intent1 = new Intent(CustomerRrgisterActivity.this, ScheduledDeliveryActivity.class);
									intent1.putExtra("selectcategory",ConstantUtils.selectcategory);
									startActivity(intent1);
									finish();
								}else {
									startActivity(new Intent(CustomerRrgisterActivity.this, CustomerDashboardActivity.class));
									finish();
								}

							}

						} else {
							Toast.makeText(CustomerRrgisterActivity.this, "Open Login Screen and try again" + message, Toast.LENGTH_LONG).show();
						}
					}

				}

			}else {
				Toast.makeText(CustomerRrgisterActivity.this, "Open Login Screen and try again" + message, Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_LONG).show();
	}





	
	//=========================== Email varification ==============================================
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(CustomerRrgisterActivity.this, HomeActivity.class));
		finish();
	}
}
