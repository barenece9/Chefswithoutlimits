package com.chefswithoutlimits.customerchef.activity.chefs;

import java.util.ArrayList;
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
import com.chefswithoutlimits.customerchef.activity.other.MapActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefProfileSettingActivity extends Activity implements OnClickListener{


	ImageView imageView2;
	FrameLayout account_setting;
	//FrameLayout password_setting;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	LinearLayout linear_account;
	//LinearLayout linear_password;
	ImageView imageViewGeneral;
	ImageView imageViewPass;

	Spinner chefRegtimezone;

	Button editBtnUSetting,editBtnUPass;	

	boolean generalSetting = false;
	boolean passwordSetting = false;

	UserInformation userInfo;
	RememberData rememberData;

	private ProgressDialog progressDialog;
	String newPass = "";
	String timezoneid="";

	Button chefpassBtnUpdate,imgChDashOn,imgChDashOff;


	EditText chefOlsPass,chefNewPass,chefNewConPass;
	EditText chefFirstName,chefLastName,chefMidName,chefEmail,chefContact,chefGoogleId,chefFbId;

	ArrayList<String> timezoneList = new ArrayList<String>();
	ArrayList<String> timezoneId = new ArrayList<String>();

	ArrayAdapter<String> adapterTimezone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_setting);

		rememberData = Sharepreferences.getPasswordSetting(this);
		userInfo = Sharepreferences.getSharePreferance(this);

		setWidget();
		//geteditValue();
		getTimeZone();
	}


	public void setWidget()
	{

		imageView2=(ImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
				.placeholder(R.mipmap.logo_box)   // optional
				.error(R.mipmap.logo_box)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		account_setting = (FrameLayout)findViewById(R.id.account_setting);
		account_setting.setOnClickListener(this);
		//account_setting = (FrameLayout)findViewById(R.id.password_setting);
		//account_setting.setOnClickListener(this);

		linear_account = (LinearLayout)findViewById(R.id.linear_account);
		//linear_password = (LinearLayout)findViewById(R.id.linear_password);

		imageViewGeneral = (ImageView)findViewById(R.id.imageViewGeneral);
		imageViewPass = (ImageView)findViewById(R.id.imageViewPass);

		imageViewGeneral.setRotation(270);
		imageViewPass.setRotation(270);


		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("CHEF SETTING");

		chefFirstName = (EditText)findViewById(R.id.chefFirstName);
		chefMidName = (EditText)findViewById(R.id.chefMidName);
		chefLastName = (EditText)findViewById(R.id.chefLastName);
		chefEmail = (EditText)findViewById(R.id.chefEmail);
		chefContact = (EditText)findViewById(R.id.chefContact);
		chefOlsPass = (EditText)findViewById(R.id.chefOlsPass);
		chefNewPass = (EditText)findViewById(R.id.chefNewPass);
		chefNewConPass = (EditText)findViewById(R.id.chefNewConPass);		

		//chefOlsPass.setText(rememberData.getPassword());

		editBtnUSetting = (Button)findViewById(R.id.chefeditBtnUpdate);
		editBtnUSetting.setOnClickListener(this);

		chefpassBtnUpdate = (Button)findViewById(R.id.chefpassBtnUpdate);
		chefpassBtnUpdate.setOnClickListener(this);

		imgChDashOn=(Button)findViewById(R.id.imgChDashOn);
		imgChDashOn.setOnClickListener(this);

		imgChDashOff=(Button)findViewById(R.id.imgChDashOff);
		imgChDashOff.setOnClickListener(this);


		chefRegtimezone = (Spinner)findViewById(R.id.cheftimezone);
		chefRegtimezone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				timezoneid = timezoneId.get(position);
				//chefRegCity = cityList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		adapterTimezone = new ArrayAdapter<String>(ChefProfileSettingActivity.this,R.layout.spinner_rows, timezoneList);
		adapterTimezone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegtimezone.setAdapter(adapterTimezone);

	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.btnBrowse:

			if(InternetConnectivity.isConnectedFast(ChefProfileSettingActivity.this)){
				startActivity(new Intent(ChefProfileSettingActivity.this,MapActivity.class));
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btnEdit:

			//startActivity(new Intent(CustomerProfileSettingActivity.this,LogintypeActivity.class));

			break;

		case R.id.btnRegister:

			break;

		case R.id.btnBack:



			if(InternetConnectivity.isConnectedFast(ChefProfileSettingActivity.this)){
				startActivity(new Intent(ChefProfileSettingActivity.this,ChefDashboardActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btnLogout:

			Logout.logOut(this);
			finish();
			break;

		case R.id.account_setting:

			//boolean generalSetting = false;
			//boolean passwordSetting = false;

			if(generalSetting){

				generalSetting = false;
				linear_account.setVisibility(View.GONE);
				//linear_password.setVisibility(View.GONE);

				//imageViewGeneral.setRotation(270);
				imageViewPass.setRotation(270);

			}else{

				generalSetting = true;

				linear_account.setVisibility(View.VISIBLE);
				//linear_password.setVisibility(View.GONE);

				//imageViewGeneral.setRotation(360);
				imageViewPass.setRotation(360);
			}	

			break;
			/*case R.id.password_setting:

			if(passwordSetting){

				passwordSetting= false;

				linear_general.setVisibility(View.GONE);
				linear_password.setVisibility(View.GONE);

				imageViewPass.setRotation(270);
				imageViewGeneral.setRotation(270);

			}else{

				passwordSetting = true;

				linear_general.setVisibility(View.GONE);
				linear_password.setVisibility(View.VISIBLE);

				imageViewPass.setRotation(360);
				imageViewGeneral.setRotation(270);
			}

			break;*/

			case R.id.imgChDashOn:
                changekitchenstatus("open");

				break;
			case R.id.imgChDashOff:
				changekitchenstatus("closed");

				break;

		case R.id.chefeditBtnUpdate:

			if(InternetConnectivity.isConnectedFast(ChefProfileSettingActivity.this)){
				editUserSetting();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

			case R.id.chefpassBtnUpdate:

				String newPass = chefNewPass.getText().toString();
				String NewConPass = chefNewConPass.getText().toString();
				String oldPass = chefOlsPass.getText().toString();
				if(oldPass.equalsIgnoreCase("")){
					CreateDialog.showDialog(this, "Enter old password");
				}else if(newPass.equalsIgnoreCase("")){
					CreateDialog.showDialog(this, "Enter enter new password");
				}
				else if(NewConPass.equalsIgnoreCase("")){
					CreateDialog.showDialog(this, "Enter confirm new password");
				}
				else if(!NewConPass.equalsIgnoreCase(newPass)){
					CreateDialog.showDialog(this, "confirm new password mismatch");
				}else {

					if(InternetConnectivity.isConnectedFast(ChefProfileSettingActivity.this)){
						editPassSetting();
					}else {
						Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
					}
				}
				break;


		}
	}

	// ========================== Edit user setting ===================================

	void editUserSetting()
	{
		progressDialog=new ProgressDialog(ChefProfileSettingActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CHEF_PROIFLEEDIT;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseuserinfo(response);
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
						Toast.makeText(ChefProfileSettingActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				String fName = chefFirstName.getText().toString();
				String mName = chefMidName.getText().toString();
				String lName = chefLastName.getText().toString();
				String email = chefEmail.getText().toString();
				String contact = chefContact.getText().toString();


				params.put("user_id",userInfo.getUserId());
				params.put("FirstName",fName);
				params.put("MidName",mName);
				params.put("LastName",lName);
				//params.put("email",email);
				params.put("contact",contact);
				params.put("time_zonesID",timezoneid);
				/*params.put("google_id","");
				params.put("fb_id","");*/

				/*if(!newPass.equalsIgnoreCase(""))
					params.put("password",newPass);
				else
					params.put("password",oldPass); */

				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}

	//======================================== Pass setting =============================================

	void editPassSetting()
	{
		progressDialog=new ProgressDialog(ChefProfileSettingActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.EDIT_PASS_SETTING;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parse(response);
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
						Toast.makeText(ChefProfileSettingActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>();


				String oldPass = chefOlsPass.getText().toString();
				newPass = chefNewPass.getText().toString();
				String newconPass = chefNewConPass.getText().toString();

				params.put("user_id",userInfo.getUserId());
				params.put("old_pass",oldPass);
				params.put("new_pass",newPass);
				params.put("confirm_pass",newconPass);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	//========================================= Get edit value =========================================

	void geteditValue()
	{
		final  ProgressDialog progressDialog=new ProgressDialog(ChefProfileSettingActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_PROIFLEDETAIL;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseGetdata(response);
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
						System.out.println("Error============"+error);
						Toast.makeText(ChefProfileSettingActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("user_id",userInfo.getUserId());				
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}


	public void parseGetdata(String response)
	{
		try {

			JSONObject parentObject = new JSONObject(response);

			System.out.println("Chef detail Response =====================:"+response);

			JSONArray parentArray = parentObject.getJSONArray("Records");
			JSONObject  records = parentArray.optJSONObject(0);
			//JSONObject  records = obj.optJSONObject("records");

			chefFirstName.setText(records.optString("FirstName"));
			chefMidName.setText(records.optString("MidName"));
			chefLastName.setText(records.optString("LastName"));
			chefEmail.setText(records.optString("email"));
			chefContact.setText(records.optString("contact"));
			timezoneid=records.optString("time_zonesID");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//getTimeZone();
		int position = timezoneId.indexOf(timezoneid);
		chefRegtimezone.setSelection(position);
	}


	//==================================================================================================

	public void parseuserinfo(String parse)
	{
		try {

			JSONObject parentObject = new JSONObject(parse);

			//JSONArray parentArray = parentObject.getJSONArray("output");
			//JSONObject  obj = parentArray.optJSONObject(0);
			String message = parentObject.getString("message");
			String Status=parentObject.getString("Status");
			Toast.makeText(ChefProfileSettingActivity.this, message, Toast.LENGTH_LONG).show();

			if(Status.contains("true")){

				if(!newPass.equalsIgnoreCase("")){
					Sharepreferences.setPasswordSetting(ChefProfileSettingActivity.this,newPass);
					rememberData = Sharepreferences.getPasswordSetting(this);
					chefOlsPass.setText(rememberData.getPassword());
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void parse(String parse)
	{
		try {

			JSONObject parentObject = new JSONObject(parse);

			//JSONArray parentArray = parentObject.getJSONArray("output");
			//JSONObject  obj = parentArray.optJSONObject(0);
			String message = parentObject.getString("message");
			String Status=parentObject.getString("Status");
			Toast.makeText(ChefProfileSettingActivity.this, message, Toast.LENGTH_LONG).show();

			if(Status.contains("true")){

				/*if(!newPass.equalsIgnoreCase("")){
					Sharepreferences.setPasswordSetting(ChefProfileSettingActivity.this,newPass);
					rememberData = Sharepreferences.getPasswordSetting(this);
					chefOlsPass.setText(rememberData.getPassword());
				}*/
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void getTimeZone()
	{
		final  ProgressDialog progressDialog=new ProgressDialog(ChefProfileSettingActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;
		//System.out.println("CountryID "+countryId);
		//url =WebServiceURL.TIME_ZONE;
		url="https://chefswithoutlimits.com/webservice_ios/get_timezone.php";

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parsetimezone(response);
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
						Toast.makeText(ChefProfileSettingActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		//AppController.getInstance().addToRequestQueue(postRequest);
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parsetimezone(String response)
	{
		try {

				/* stateList = new ArrayList<String>();
				 stateId = new ArrayList<String>();*/
			timezoneList.clear();
			timezoneId.clear();

			JSONObject parentObj = new JSONObject(response);


			JSONArray jArray = parentObj.getJSONArray("Records");

			timezoneList.add("Select timezone");
			timezoneId.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				timezoneList.add(records.optString("time_zone"));
				timezoneId.add(records.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapterTimezone.notifyDataSetChanged();
		//int position = timezoneId.indexOf(timezoneid);
		//chefRegtimezone.setSelection(position);
		geteditValue();
	}

	public  void changekitchenstatus(final String status){
		progressDialog=new ProgressDialog(ChefProfileSettingActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_KITCHEN_STATUS;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						updatekitchenstatusparse(response);
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
						Toast.makeText(ChefProfileSettingActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("kitchen_id",userInfo.getKitchenId());
				params.put("kitchen_status",status);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void updatekitchenstatusparse(String response){
		JSONObject parentJSON;
		try {

			parentJSON = new JSONObject(response);
			//JSONArray parentArr = parentJSON.optJSONArray("Records");

			JSONObject record = parentJSON.getJSONObject("output");
			//JSONObject record = child.getJSONObject("records");
			String status=record.optString("status");
			String message=record.optString("message");
			Toast.makeText(ChefProfileSettingActivity.this, message, Toast.LENGTH_LONG).show();
			//System.out.println("@@@@@@@@@@@@@   "+kitchenstatus);
			//chefKithenEditAddress.setText(record.optString("status"));


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(ChefProfileSettingActivity.this)){
			startActivity(new Intent(ChefProfileSettingActivity.this,ChefDashboardActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}

	}
}
