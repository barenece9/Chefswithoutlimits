package com.chefswithoutlimits.customerchef.activity.chefs.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.home.HomeActivity;
import com.chefswithoutlimits.customerchef.activity.home.RegisterTypeActivity;
import com.chefswithoutlimits.customerchef.dataVO.ChefRegData;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.KitchenVenue;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefRrgisterActivity extends Activity implements OnClickListener, OnCheckedChangeListener{

	EditText custmerEdtuser;
	EditText custmerEdtpass;
	Button btnRegister;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	EditText chefRegVKName;
	EditText chefRegFName;
	EditText chefRegLName;
	EditText chefRegEmail;
	EditText chefRegPass;
	EditText chefRegConPass;	
	EditText chefRegPhone;

	Spinner chefRegSpiCountry;
	Spinner chefRegVType;

	CheckBox rememberme;
	CheckBox checkBoxPickup;
	CheckBox checkBoxDelivery;
	CheckBox checkBoxMailOrder;
	CheckBox checkBoxVehicle;
	CheckBox checkBoxBicycle;
	CheckBox checkBoxWalking;

	RelativeLayout deleveryTypeLayout;

	String vkName = "";
	String firstName = "";
	String lastName = "";
	String email = "";
	String password = "";
	String conPassword = "";
	String phoneNo = "";
	String selectVenueType = "";
	String selectVenueTypeid = "";

	String serviceOffer1 = "";
	String serviceOffer2 = "";
	String serviceOffer3 = "";
	String deleveryType1 = "";
	String deleveryType2 = "";
	String deleveryType3 = "";
	ArrayList<String> arrayServiceOffer=new ArrayList<>();


	//ArrayList<String> venueArray = new ArrayList<String>();
	ArrayList<KitchenVenue> venueArray = new ArrayList<KitchenVenue>();
	
	ArrayList<String> venueName = new ArrayList<String>();
	ArrayList<String> venueId = new ArrayList<String>();

	
	
	ChefRegData chefData = new ChefRegData();


	private ProgressDialog progressDialog;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_register_part1_activity);

		setWidget();
		doKitchenList();
	}

	public void setWidget()
	{
		deleveryTypeLayout = (RelativeLayout)findViewById(R.id.deleveryTypeLayout);
		chefRegVType = (Spinner)findViewById(R.id.chefRegVType);
		chefRegVType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selectVenueType = venueName.get(position);
				selectVenueTypeid=venueId.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);		
		headerTxt.setText("CHEF REGISTRATION");

		chefRegVKName = (EditText)findViewById(R.id.chefRegVKName);
		chefRegFName = (EditText)findViewById(R.id.chefRegFName);
		chefRegLName = (EditText)findViewById(R.id.chefRegLName);
		chefRegEmail = (EditText)findViewById(R.id.chefRegEmail);
		chefRegPass = (EditText)findViewById(R.id.chefRegPass);
		chefRegConPass = (EditText)findViewById(R.id.chefRegConPass);	
		chefRegPhone = (EditText)findViewById(R.id.chefRegPhone); 

		chefRegSpiCountry = (Spinner)findViewById(R.id.chefRegSpiCountry);
		rememberme = (CheckBox)findViewById(R.id.rememberme);

		checkBoxPickup = (CheckBox)findViewById(R.id.checkBoxPickup);
		checkBoxPickup.setOnCheckedChangeListener(this);

		checkBoxDelivery = (CheckBox)findViewById(R.id.checkBoxDelivery);
		checkBoxDelivery.setOnCheckedChangeListener(this);

		checkBoxMailOrder = (CheckBox)findViewById(R.id.checkBoxMailOrder);
		checkBoxMailOrder.setOnCheckedChangeListener(this);

		checkBoxVehicle = (CheckBox)findViewById(R.id.checkBoxVehicle);
		checkBoxVehicle.setOnCheckedChangeListener(this);

		checkBoxBicycle = (CheckBox)findViewById(R.id.checkBoxBicycle);
		checkBoxBicycle.setOnCheckedChangeListener(this);

		checkBoxWalking = (CheckBox)findViewById(R.id.checkBoxWalking);
		checkBoxWalking.setOnCheckedChangeListener(this);
		
		//adapter =new ArrayAdapter<String>(ChefRrgisterActivity.this,android.R.layout.simple_spinner_item, venueName);
		adapter =new ArrayAdapter<String>(ChefRrgisterActivity.this,R.layout.spinner_rows, venueName);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegVType.setAdapter(adapter); 
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	

		case R.id.btnBack:

			startActivity(new Intent(ChefRrgisterActivity.this, RegisterTypeActivity.class));
			finish();

			break;

		case R.id.btnLogout:

			break;

		case R.id.btnRegister:

			vkName = chefRegVKName.getText().toString();
			firstName = chefRegFName.getText().toString();
			lastName = chefRegLName.getText().toString();
			email = chefRegEmail.getText().toString();
			password = chefRegPass.getText().toString();
			conPassword = chefRegConPass.getText().toString();	
			phoneNo = chefRegPhone.getText().toString();	

			if(vkName.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter venue name");

			}else if(selectVenueType.equalsIgnoreCase("") || selectVenueType.equalsIgnoreCase("Select Venue Type")){
				
				CreateDialog.showDialog(this, "Select venue type");
				
			}else if(firstName.equalsIgnoreCase(""))			
			{
				CreateDialog.showDialog(this, "Enter first name");

			}else if(lastName.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter last name");

			}else if(phoneNo.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Phoneno");
			}			
			else if(email.equalsIgnoreCase(""))
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

			}
			else{

				//doChefRegister();
				
				String serviceOfferValue = "";
				
				if(checkBoxPickup.isChecked()){

					serviceOfferValue = serviceOffer1;
				}

				if(checkBoxDelivery.isChecked()){

					if(serviceOfferValue.equalsIgnoreCase("")){

						serviceOfferValue = serviceOffer2;

					}else{

						serviceOfferValue = serviceOfferValue+","+ serviceOffer2;
					}
				}

				if(checkBoxMailOrder.isChecked()){

					if(serviceOfferValue.equalsIgnoreCase("")){

						serviceOfferValue = serviceOffer3;

					}else{

						serviceOfferValue = serviceOfferValue+","+ serviceOffer3;
					}
				}
				
				if(checkBoxVehicle.isChecked()){
					
					if(serviceOfferValue.equalsIgnoreCase("")){

						serviceOfferValue = deleveryType1;

					}else{

						serviceOfferValue = serviceOfferValue+","+ deleveryType1;
					}
				}
				
				if(checkBoxBicycle.isChecked()){
					
					if(serviceOfferValue.equalsIgnoreCase("")){

						serviceOfferValue = deleveryType2;

					}else{

						serviceOfferValue = serviceOfferValue+","+ deleveryType2;
					}
				}
				
				if(checkBoxWalking.isChecked()){
					
					if(serviceOfferValue.equalsIgnoreCase("")){

						serviceOfferValue = deleveryType3;

					}else{

						serviceOfferValue = serviceOfferValue+","+ deleveryType3;
					}
				}


				if(serviceOfferValue.equalsIgnoreCase("")){
					CreateDialog.showDialog(this, "Please select at-lest one service");
				}else {
					chefData.setFirstName(firstName);
					chefData.setLastName(lastName);
					chefData.setPassword(password);
					chefData.setConPassword(conPassword);
					chefData.setPhoneNo(phoneNo);
					chefData.setVkName(vkName);
					chefData.setEmail(email);
					chefData.setVenueType(selectVenueType);
					chefData.setVenueTypeId(selectVenueTypeid);
					//chefData.setDeleveryWalking(serviceOfferValue);
					chefData.setServiceArray(serviceOfferValue);
					System.out.println("@@@@@@@@@@@@@@#$$$$$$$$$$$ "+chefData.getVenueTypeId());


					if(InternetConnectivity.isConnectedFast(ChefRrgisterActivity.this)){
						Intent intent = new Intent(ChefRrgisterActivity.this,ChefRrgisterPart2Activity.class);
						intent.putExtra("chefData", chefData);
						startActivity(intent);
						finish();
					}else {
						Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
					}
				}



			}


			//startActivity(new Intent(CustomerRrgisterActivity.this,ChefEmailVerifyActivity.class));
			break;
		}
	}


	//===============Register chef Function===================
	void doChefRegister()
	{
		progressDialog=new ProgressDialog(ChefRrgisterActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.REGISTER_URL;

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
						Toast.makeText(ChefRrgisterActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 
				params.put("FirstName",firstName);	
				params.put("LastName",lastName);	
				params.put("password",password);	
				params.put("email",email);	
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}


	void parseResponse(String response){	

		String message = "";
		System.out.println("Response :"+response);
		try {

			JSONObject parentObject = new JSONObject(response);
			JSONArray parentArray = parentObject.getJSONArray("output");
			JSONObject  obj = parentArray.optJSONObject(0);
			message = obj.getString("message");			


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

		if(message.contains("Successfull")){

			startActivity(new Intent(ChefRrgisterActivity.this,ChefEmailVerifyActivity.class));
			finish();
		}


		Toast.makeText(ChefRrgisterActivity.this, ""+message, Toast.LENGTH_LONG).show();
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
	public void onCheckedChanged(CompoundButton view, boolean check) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.checkBoxPickup:
			
			
			 if(check){

			 	 //arrayServiceOffer.add(serviceOffer1);
				 serviceOffer1 = "1";
			 }else{
				 serviceOffer1 = "";
				 //arrayServiceOffer.remove(serviceOffer1);
			 }
			
			 chefData.setSeriveOffer1(serviceOffer1);
			 
			break;

		case R.id.checkBoxDelivery:

			if(check)
			{
				deleveryTypeLayout.setVisibility(View.VISIBLE);
				serviceOffer2 = "2";
				//arrayServiceOffer.add(serviceOffer2);
			}else{
				deleveryTypeLayout.setVisibility(View.INVISIBLE);
				serviceOffer2 = "";
				//arrayServiceOffer.add(serviceOffer2);
				
				checkBoxWalking.setChecked(false);
				checkBoxVehicle.setChecked(false);
				checkBoxBicycle.setChecked(false);
			}
			chefData.setSeriveOffer2(serviceOffer2);
			break;

		case R.id.checkBoxMailOrder:
			
			
			if(check){
				 serviceOffer3 = "3";
			 }else{
				 serviceOffer3 = "";
			 }
			chefData.setSeriveOffer3(serviceOffer3);
			break;

		case R.id.checkBoxVehicle:
			
			
			
			if(check){
				deleveryType1 = "4";
			 }else{
				 deleveryType1 = "";
			 }
			chefData.setDeleveryVehicle(deleveryType1);
			break;

		case R.id.checkBoxBicycle:
			
			
			
			if(check){
				deleveryType2 = "5";
			 }else{
				 deleveryType2 = "";
			 }
			
			chefData.setDeleveryBicycle(deleveryType2);

			break;

		case R.id.checkBoxWalking:
			
			
			
			if(check){
				deleveryType3 = "6";
			 }else{
				 deleveryType3 = "";
			 }			
			
			chefData.setDeleveryWalking(deleveryType3);

			break;
		}
	}
	
	
	//================================== Get kitchen data ====================================
	
	void doKitchenList()
	{
		progressDialog=new ProgressDialog(ChefRrgisterActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.KITCHEN_LIST;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseKitchen(response);
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
						Toast.makeText(ChefRrgisterActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
	}
	
	public void parseKitchen(String response)
	{
		try {
			
			JSONObject parentObj = new JSONObject(response);
			
			JSONArray jArray = parentObj.getJSONArray("Records");
			
			//JSONObject childObj = jArray.getJSONObject(0);
			
			KitchenVenue venueObjSelect = new KitchenVenue();
			venueObjSelect.setVenueId("-1");
			venueObjSelect.setVenueName("Select Venue Type");
			venueArray.add(venueObjSelect);
			venueName.add("Select Venue Type");
			venueId.add("-1");
			
			for(int i=0;i<jArray.length();i++){
				
				KitchenVenue venueObj = new KitchenVenue();
				
				JSONObject innerObj = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");
				
				venueObj.setVenueId(innerObj.optString("venue_id"));
				venueObj.setVenueName(innerObj.optString("venue_name"));
				
				venueArray.add(venueObj);

				
				venueName.add(innerObj.optString("venue_name"));
				venueId.add(innerObj.optString("venue_id"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(ChefRrgisterActivity.this, HomeActivity.class));
		finish();
	}
}
