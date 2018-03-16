package com.chefswithoutlimits.customerchef.activity.customer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.circleimageview.CircleImageView;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CommonMethodes;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerDeliveryAddressActivity extends Activity implements OnClickListener{

	CircleImageView imageView2;
	Button btnBrowse;
	Button btnEdit;
	FrameLayout general_setting;
	FrameLayout password_setting;
	FrameLayout delivery_setting;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	TextView txt_customer_name;
	LinearLayout linear_general;
	LinearLayout linear_password;
	LinearLayout linear_delivery;
	ImageView imageViewGeneral;
	ImageView imageViewPass;
	ImageView imageViewdelivery;

	Button editBtnUSetting,editBtnUPass,chefeditBtndeliveryUpdate;

	EditText custmerFName,custmerMName,custmerLName,custmerContact,custmerEmail,custmerOlsPass,custmerNewPass,custmerNewConPass;

	EditText deliveryFName,deliveryMName,deliveryLName,deliveryEmail,deliveryPhone,deliveryAddress,deliveryZip,delivery_spl_instn,delivery_unit_no,delivery_buzzer_no;
	Spinner spinnerdeliveryCountry,spinnerdeliveryState,spinnerdeliveryCity;

	boolean generalSetting = false;
	boolean passwordSetting = false;
	boolean deliverySetting = false;

	UserInformation userInfo;
	RememberData rememberData;

	ArrayList<String> countryList = new ArrayList<String>();
	ArrayList<String> countryId = new ArrayList<String>();

	ArrayList<String> stateList = new ArrayList<String>();
	ArrayList<String> stateId = new ArrayList<String>();

	ArrayList<String> cityList = new ArrayList<String>();
	ArrayList<String> cityId = new ArrayList<String>();

	ArrayAdapter<String> countryAdapter;
	ArrayAdapter<String> stateAdapter;
	ArrayAdapter<String> cityAdapter;

	String countryid = "";
	String stateid = "";
	String cityid = "";

	String countrySelect="";
	String stateSelect="";
	String citySelect="";

	private static final int
			REQUEST_READ_STORAGE = 115;

	private int PICK_IMAGE_REQUEST = 1;
	//Bitmap to get image from gallery
	private Bitmap bitmap;
	//Uri to store the image uri
	private Uri filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customer_delivery_address);

		rememberData = Sharepreferences.getRememberMe(this);

		setWidget();
	}


	public void setWidget()
	{
		imageView2=(CircleImageView)findViewById(R.id.imageView2);
		btnBrowse = (Button)findViewById(R.id.btnBrowse);
		btnBrowse.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(this);

		general_setting = (FrameLayout)findViewById(R.id.general_setting);
		general_setting.setOnClickListener(this);
		password_setting = (FrameLayout)findViewById(R.id.password_setting);
		password_setting.setOnClickListener(this);
		delivery_setting = (FrameLayout)findViewById(R.id.delivery_setting);
		delivery_setting.setOnClickListener(this);

		linear_general = (LinearLayout)findViewById(R.id.linear_general);
		linear_password = (LinearLayout)findViewById(R.id.linear_password);
		linear_delivery = (LinearLayout)findViewById(R.id.linear_delivery);

		imageViewGeneral = (ImageView)findViewById(R.id.imageViewGeneral);
		imageViewPass = (ImageView)findViewById(R.id.imageViewPass);
		imageViewdelivery = (ImageView)findViewById(R.id.imageViewdelivery);

		imageViewGeneral.setRotation(270);
		imageViewPass.setRotation(270);
		imageViewdelivery.setRotation(270);


		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("Delivery Address");
		userInfo = Sharepreferences.getSharePreferance(this);
		txt_customer_name=(TextView)findViewById(R.id.txt_customer_name);
		txt_customer_name.setText(userInfo.getFirstName()+" "+userInfo.getLastName());

		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+ userInfo.getUser_log())
				.placeholder(R.drawable.ic_person_black_24dp)   // optional
				.error(R.drawable.ic_person_black_24dp)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		custmerFName = (EditText)findViewById(R.id.custmerFName);
		custmerMName = (EditText)findViewById(R.id.custmerMName);
		custmerLName = (EditText)findViewById(R.id.custmerLName);
		custmerContact = (EditText)findViewById(R.id.custmerContact);
		custmerEmail = (EditText)findViewById(R.id.custmerEmail);
		custmerOlsPass = (EditText)findViewById(R.id.custmerOlsPass);
		custmerNewPass = (EditText)findViewById(R.id.custmerNewPass);
		custmerNewConPass = (EditText)findViewById(R.id.custmerNewConPass);
        /*deli..................................*/
		deliveryFName = (EditText)findViewById(R.id.deliveryFName);
		deliveryMName = (EditText)findViewById(R.id.deliveryMName);
		deliveryLName = (EditText)findViewById(R.id.deliveryLName);
		deliveryEmail = (EditText)findViewById(R.id.deliveryEmail);
		deliveryPhone = (EditText)findViewById(R.id.deliveryPhone);
		deliveryAddress = (EditText)findViewById(R.id.deliveryAddress);
		deliveryZip = (EditText)findViewById(R.id.deliveryZip);

		delivery_unit_no = (EditText)findViewById(R.id.delivery_unit_no);
		delivery_buzzer_no = (EditText)findViewById(R.id.delivery_buzzer_no);
		delivery_spl_instn = (EditText)findViewById(R.id.delivery_spl_instn);
		/*custmerFName.setText(userInfo.getFirstName());
		custmerLName.setText(userInfo.getLastName());
		custmerEmail.setText(userInfo.getEmail());
		custmerMName.setText(userInfo.getMidName());
		custmerContact.setText(userInfo.getContact());*/

		custmerOlsPass.setText(rememberData.getPassword());

		editBtnUSetting = (Button)findViewById(R.id.editBtnUSetting);
		editBtnUSetting.setOnClickListener(this);
		editBtnUPass = (Button)findViewById(R.id.chefeditBtnUpdate);
		editBtnUPass.setOnClickListener(this);
		chefeditBtndeliveryUpdate = (Button)findViewById(R.id.chefeditBtndeliveryUpdate);
		chefeditBtndeliveryUpdate.setOnClickListener(this);



		spinnerdeliveryCountry = (Spinner)findViewById(R.id.spinnerdeliveryCountry);
		spinnerdeliveryCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				//chefRegCountry = countryList.get(position);
				countryid = countryId.get(position);

				if(!countryid.equalsIgnoreCase("-1"))
					doStateList(countryid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});
		spinnerdeliveryState = (Spinner)findViewById(R.id.spinnerdeliveryState);
		spinnerdeliveryState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				//chefRegState = stateList.get(position);
				stateid = stateId.get(position);

				if(!stateid.equalsIgnoreCase("-1"))
					doCityList(stateid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});
		spinnerdeliveryCity = (Spinner)findViewById(R.id.spinnerdeliveryCity);
		spinnerdeliveryCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub

				//kitchenCity=cityList.get(position);
				cityid=cityId.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		countryAdapter = new ArrayAdapter<String>(CustomerDeliveryAddressActivity.this,R.layout.spinner_rows, countryList);
		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerdeliveryCountry.setAdapter(countryAdapter);

		stateAdapter = new ArrayAdapter<String>(CustomerDeliveryAddressActivity.this,R.layout.spinner_rows, stateList);
		stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerdeliveryState.setAdapter(stateAdapter);

		cityAdapter = new ArrayAdapter<String>(CustomerDeliveryAddressActivity.this,R.layout.spinner_rows, cityList);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerdeliveryCity.setAdapter(cityAdapter);

		getalldetails();
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.btnBrowse:
			//startActivity(new Intent(CustomerProfileSettingActivity.this,MapActivity.class));

			boolean hasPermissionRead = (ContextCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
			if(hasPermissionRead) {
				chosefile();
			}else {
				ActivityCompat.requestPermissions(CustomerDeliveryAddressActivity.this,
						new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						REQUEST_READ_STORAGE);
			}
			break;

		case R.id.btnEdit:

			//startActivity(new Intent(CustomerProfileSettingActivity.this,LogintypeActivity.class));
			if(InternetConnectivity.isConnectedFast(CustomerDeliveryAddressActivity.this)){
				submitphoto();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}


			break;

		case R.id.btnRegister:

			break;

		case R.id.btnBack:

			if(InternetConnectivity.isConnectedFast(CustomerDeliveryAddressActivity.this)){
				startActivity(new Intent(CustomerDeliveryAddressActivity.this,CustomerDashboardActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}


			break;

		case R.id.btnLogout:

			Logout.logOut(this);
			finish();

			break;

		case R.id.general_setting:

			//boolean generalSetting = false;
			//boolean passwordSetting = false;

			if(generalSetting){

				generalSetting = false;
				linear_general.setVisibility(View.GONE);
				linear_password.setVisibility(View.GONE);
				linear_delivery.setVisibility(View.GONE);

				imageViewGeneral.setRotation(270);
				imageViewPass.setRotation(270);
				imageViewdelivery.setRotation(270);

			}else{

				generalSetting = true;

				linear_general.setVisibility(View.VISIBLE);
				linear_password.setVisibility(View.GONE);
				linear_delivery.setVisibility(View.GONE);

				imageViewGeneral.setRotation(360);
				imageViewPass.setRotation(270);
				imageViewdelivery.setRotation(270);
			}	

			break;
		case R.id.password_setting:

			if(passwordSetting){

				passwordSetting= false;

				linear_general.setVisibility(View.GONE);
				linear_password.setVisibility(View.GONE);
				linear_delivery.setVisibility(View.GONE);

				imageViewPass.setRotation(270);
				imageViewGeneral.setRotation(270);
				imageViewdelivery.setRotation(270);

			}else{

				passwordSetting = true;

				linear_general.setVisibility(View.GONE);
				linear_delivery.setVisibility(View.GONE);
				linear_password.setVisibility(View.VISIBLE);

				imageViewPass.setRotation(360);
				imageViewGeneral.setRotation(270);
				imageViewdelivery.setRotation(270);
			}

			break;

			case R.id.delivery_setting:

				if(deliverySetting){

					deliverySetting= false;

					linear_general.setVisibility(View.GONE);
					linear_password.setVisibility(View.GONE);
					linear_delivery.setVisibility(View.GONE);

					imageViewPass.setRotation(270);
					imageViewGeneral.setRotation(270);
					imageViewdelivery.setRotation(270);

				}else{

					deliverySetting = true;

					linear_general.setVisibility(View.GONE);
					linear_password.setVisibility(View.GONE);
					linear_delivery.setVisibility(View.VISIBLE);

					imageViewPass.setRotation(270);
					imageViewGeneral.setRotation(270);
					imageViewdelivery.setRotation(360);
				}

				break;

		case R.id.editBtnUSetting:

			editUserSetting();
			break;

		case R.id.chefeditBtnUpdate:

			String newPass = custmerNewPass.getText().toString();
			String NewConPass = custmerNewConPass.getText().toString();
			String oldPass = custmerOlsPass.getText().toString();
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
				editPassSetting();
			}
			break;

			case R.id.chefeditBtndeliveryUpdate:
				editDeliverySetting();
				break;
		}

	}

	// ========================== Edit delivery setting ===================================

	void editDeliverySetting()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.EDIT_CUSTOMER_DELIVERY_SETTING;

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
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CustomerDeliveryAddressActivity.this);
				Map<String, String>  params = new HashMap<String, String>();

				String fName = custmerFName.getText().toString();
				String lName = custmerLName.getText().toString();
				String email = custmerEmail.getText().toString();

				params.put("userID",userInfo.getUserId());
				params.put("first_name",deliveryFName.getText().toString());
				params.put("middle_name",deliveryMName.getText().toString());
				params.put("last_name",deliveryLName.getText().toString());
				params.put("phone_no",deliveryPhone.getText().toString());
				params.put("email_address",deliveryEmail.getText().toString());
				params.put("address",deliveryAddress.getText().toString());
				params.put("zipcode",deliveryZip.getText().toString());

				params.put("unit_no",delivery_unit_no.getText().toString());
				params.put("buzzer_no",delivery_buzzer_no.getText().toString());
				params.put("spl_instn",delivery_spl_instn.getText().toString());

				params.put("country",countryid);
				params.put("state",stateid);
				params.put("city",cityid);

				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	// ========================== Edit user setting ===================================

	void editUserSetting()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.EDIT_USER_SETTING;

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
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CustomerDeliveryAddressActivity.this);
				Map<String, String>  params = new HashMap<String, String>(); 	

				String fName = custmerFName.getText().toString();
				String lName = custmerLName.getText().toString();
				String email = custmerEmail.getText().toString();

				params.put("user_id",userInfo.getUserId());
				params.put("FirstName",custmerFName.getText().toString());
				params.put("MidName",custmerMName.getText().toString());
				params.put("LastName",custmerLName.getText().toString());
				params.put("contact",custmerContact.getText().toString());
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
	
	//======================================== Pass setting =============================================
	
	void editPassSetting()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.EDIT_CUSTOMER_PASS_SETTING;

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
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>();

				userInfo = Sharepreferences.getSharePreferance(CustomerDeliveryAddressActivity.this);
				
				params.put("user_id",userInfo.getUserId());
				params.put("new_pass",custmerNewPass.getText().toString());
				params.put("old_pass",custmerOlsPass.getText().toString());
				return params;
			}
		};
		// Adding request to volley request queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void getalldetails(){
		final ProgressDialog progressDialog=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.GET_CUSTOMER_DELIVERY_SETTING;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseALLDetails(response);
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
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CustomerDeliveryAddressActivity.this);
				Map<String, String>  params = new HashMap<String, String>();

				params.put("userID",userInfo.getUserId());
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}
	
	

	public void parse(String parse)
	{
		try {

			JSONObject parentObject = new JSONObject(parse);

			//JSONArray parentArray = parentObject.getJSONArray("output");
			//JSONObject  obj = parentArray.optJSONObject(0);
			String status = parentObject.getString("status");
			String message = parentObject.getString("message");
			if(status.equalsIgnoreCase("success")){
				if(InternetConnectivity.isConnectedFast(CustomerDeliveryAddressActivity.this)){
					startActivity(new Intent(CustomerDeliveryAddressActivity.this,CustomerDashboardActivity.class));
					finish();
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}
			}



			Toast.makeText(CustomerDeliveryAddressActivity.this, message, Toast.LENGTH_LONG).show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseALLDetails(String parse)
	{
		try {

			JSONObject parentObject = new JSONObject(parse);
			JSONObject user_details=parentObject.getJSONObject("user_details");

			custmerFName.setText(user_details.getString("FirstName"));
			custmerLName.setText(user_details.getString("LastName"));
			custmerEmail.setText(user_details.getString("email"));
			custmerMName.setText(user_details.getString("MidName"));
			custmerContact.setText(user_details.getString("contact"));

			String Status = parentObject.getString("status");
			if(Status.equalsIgnoreCase("true")){
				deliveryFName.setText(parentObject.getString("first_name"));
				deliveryMName.setText(parentObject.getString("middle_name"));
				deliveryLName.setText(parentObject.getString("last_name"));
				deliveryEmail.setText(parentObject.getString("email_address"));
				deliveryPhone.setText(parentObject.getString("phone_no"));
				deliveryAddress.setText(parentObject.getString("address"));
				deliveryZip.setText(parentObject.getString("zipcode"));
				countrySelect=parentObject.getString("country");
				stateSelect=parentObject.getString("state");
				citySelect=parentObject.getString("city");

				delivery_unit_no.setText(parentObject.getString("aptno"));
				delivery_buzzer_no.setText(parentObject.getString("buzzerno"));
				delivery_spl_instn.setText(parentObject.getString("sp_instructions"));

			}
			//String message = parentObject.getString("message");
			//Toast.makeText(CustomerProfileSettingActivity.this, message, Toast.LENGTH_LONG).show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doCountryList();
	}



	void doCountryList()
	{
		final ProgressDialog progressDialog2=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog2.setMessage("loading...");
		progressDialog2.show();
		progressDialog2.setCancelable(false);
		progressDialog2.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.COUNTRY_LIST;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseCountry(response);
						if(progressDialog2!=null)
							progressDialog2.dismiss();
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog2!=null)
							progressDialog2.dismiss();
						System.out.println("Error=========="+error);
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parseCountry(String response)
	{
		try {

			if(countryList.size()>0){

				countryList.clear();
				countryId.clear();
			}

			JSONObject parentObj = new JSONObject(response);

			JSONArray jArray = parentObj.getJSONArray("Records");

			countryList.add("Select Country");
			countryId.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				countryList.add(records.optString("name"));
				countryId.add(records.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		countryAdapter.notifyDataSetChanged();
		int position = countryId.indexOf(countrySelect);
		spinnerdeliveryCountry.setSelection(position);
		//doStateList(countrySelect);
	}

	//================================== Get state data ====================================

	void doStateList(final String countryId)
	{
		final ProgressDialog progressDialog4=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog4.setMessage("loading...");
		progressDialog4.show();
		progressDialog4.setCancelable(false);
		progressDialog4.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.STATE_LIST;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseState(response);
						if(progressDialog4!=null)
							progressDialog4.dismiss();
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog4!=null)
							progressDialog4.dismiss();
						System.out.println("Error=========="+error);
						//doStateList(countryid);
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("country_id",countryId);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parseState(String response)
	{
		try {

			JSONObject parentObj = new JSONObject(response);

			JSONArray jArray = parentObj.getJSONArray("Records");

			if(stateList.size()>0){

				stateList.clear();
				stateId.clear();
			}

			stateList.add("Select State");
			stateId.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				stateList.add(records.optString("name"));
				stateId.add(records.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stateAdapter.notifyDataSetChanged();
		int position = stateId.indexOf(stateSelect);
		spinnerdeliveryState.setSelection(position);
		//doCityList(stateSelect);
	}



	public void parseCity(String response)
	{
		try {

			if(cityList.size()>0){

				cityList.clear();
				cityId.clear();
			}


			JSONObject parentObj = new JSONObject(response);

			JSONArray jArray = parentObj.getJSONArray("Records");

			cityList.add("Select City");
			cityId.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				cityList.add(records.optString("name"));
				cityId.add(records.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cityAdapter.notifyDataSetChanged();
		int position = cityId.indexOf(citySelect);
		spinnerdeliveryCity.setSelection(position);
		//doCurrencyList();
	}


	//================================== Get open close time ====================================

	void doCityList(final String stateId)
	{
		final ProgressDialog progressDialog3=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog3.setMessage("loading...");
		progressDialog3.show();
		progressDialog3.setCancelable(false);
		progressDialog3.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CITY_LIST;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseCity(response);
						if(progressDialog3!=null)
							progressDialog3.dismiss();
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog3!=null)
							progressDialog3.dismiss();
						System.out.println("Error=========="+error);
						//doCityList(stateid);
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("state_id",stateId);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}


	/*upload file.......*/
	//method to show file chooser
	private void chosefile() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	}

	//handling the image chooser activity result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			filePath = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				imageView2.setImageBitmap(bitmap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int byteSizeOf(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return bitmap.getAllocationByteCount();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}

	public String getStringImage(Bitmap bmp){
		if(byteSizeOf(bmp)<=1000000){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] imageBytes = baos.toByteArray();

			long lengthbmp = imageBytes.length;
			System.out.println(" File size 1 : " + lengthbmp +" KB");

			String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
			return encodedImage;
		}else if(byteSizeOf(bmp)<=9000000){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] imageBytes = baos.toByteArray();

			long lengthbmp = imageBytes.length;
			System.out.println(" File size 2 : " + lengthbmp +" KB");

			String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
			return encodedImage;
		}else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
			byte[] imageBytes = baos.toByteArray();

			long lengthbmp = imageBytes.length;
			System.out.println(" File size 3 : " + lengthbmp +" KB");

			String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
			return encodedImage;
		}
	}

	public void submitphoto(){

		final ProgressDialog progressDialog=new ProgressDialog(CustomerDeliveryAddressActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.UPLOAD_CUSTOMER_PF;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						responsePhoto(response);
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
						//doCityList(stateid);
						Toast.makeText(CustomerDeliveryAddressActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				String image="";
				if(bitmap!=null) {
					//image = getStringImage2(bitmap);
					image= CommonMethodes.getStringImage(bitmap);
				}
				params.put("user_id",userInfo.getUserId());
				params.put("client","A");
				params.put("user_profile_pic",image);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
				0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();

	}


	public void responsePhoto(String response)
	{
		try {

			JSONObject parentJSON = new JSONObject(response);
			JSONObject output=parentJSON.getJSONObject("output");
			String Status=output.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				Toast.makeText(CustomerDeliveryAddressActivity.this, "Profile pic is updated sucessfully", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(CustomerDeliveryAddressActivity.this, "Failed to upload", Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public String getStringImage2(Bitmap bmp){

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;

		int actualHeight = bmp.getHeight();
		int actualWidth = bmp.getWidth();

		float imgRatio = (float) actualWidth / (float) actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;
			}
		}

		int inSampleSize = calculateInSampleSize(bmp, actualWidth, actualHeight);

		actualHeight=actualHeight/inSampleSize;
		actualWidth=actualWidth/inSampleSize;
		bmp=Bitmap.createScaledBitmap(bmp, actualWidth,actualHeight , true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}


	public static int calculateInSampleSize(Bitmap bmp, int reqWidth, int reqHeight) {
		final int height = bmp.getHeight();
		final int width = bmp.getWidth();
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}
		return inSampleSize;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{
			case REQUEST_READ_STORAGE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					Toast.makeText(CustomerDeliveryAddressActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
					//reload my activity with permission granted or use the features what required the permission
					//finish();
					//startActivity(getIntent());
				} else
				{
					Toast.makeText(CustomerDeliveryAddressActivity.this, "The app was not allowed to read to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(CustomerDeliveryAddressActivity.this)){
			startActivity(new Intent(CustomerDeliveryAddressActivity.this,CustomerDashboardActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}

	}
}
