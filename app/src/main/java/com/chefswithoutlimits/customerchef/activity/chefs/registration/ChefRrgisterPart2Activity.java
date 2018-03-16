package com.chefswithoutlimits.customerchef.activity.chefs.registration;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.customerchef.dataVO.ChefRegData;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.RegisterActivities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChefRrgisterPart2Activity extends AppCompatActivity implements OnClickListener,GoogleApiClient.OnConnectionFailedListener{

	EditText custmerEdtuser;
	EditText custmerEdtpass;
	Button btnRegister;
	ImageView btnRegisterStep2Pre;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	EditText chefAddress1Part2;
	EditText chefRegAddress2Part2;
	EditText chefRegStreetAddressPart2;
	EditText chefRegAptPart2;	
	EditText chefRegZipPart2;
	EditText chefReglatitudePart2;
	EditText chefReglongitudePart2;

	Spinner chefRegSpiCountryPart2;
	Spinner chefRegCityPart;
	Spinner chefRegStatePart2;
	Spinner chefRegtimezone;
	Spinner chefRegcurrency;

	Button pick_location;


	String chefAddress1 = "";
	String chefAddress2 = "";
	String chefRegStreetAddress = "";
	String chefRegApt = "";
	String chefRegCity = "";
	String chefRegState = "";
	String chefRegZip = "";
	String chefRegCountry = "";
	
	String countryid = "";
	String stateid = "";
	String cityid = "";
	String serviceoffer="";

	String selectcurrencyid="";
	String selectcurrency="";

	String timezoneid="";
	String latitute="";
	String longitude="";

	ChefRegData chefData;

	private ProgressDialog progressDialog;

	ArrayList<String> currencyList = new ArrayList<String>();
	ArrayList<String> currencyListID = new ArrayList<String>();

	ArrayList<String> countryList = new ArrayList<String>();
	ArrayList<String> countryId = new ArrayList<String>();
	
	ArrayList<String> stateList = new ArrayList<String>();
	ArrayList<String> stateId = new ArrayList<String>();
	
	ArrayList<String> cityList = new ArrayList<String>();
	ArrayList<String> cityId = new ArrayList<String>();

	ArrayList<String> timezoneList = new ArrayList<String>();
	ArrayList<String> timezoneId = new ArrayList<String>();

	ArrayAdapter<String> currencyAdapter;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> adapterState;
	ArrayAdapter<String> adapterCity;
	ArrayAdapter<String> adapterTimezone;

	private GoogleApiClient mGoogleApiClient;
	private int PLACE_PICKER_REQUEST = 1;
	final int LOCATION_PERMISSION = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_register_part2_activity);

		mGoogleApiClient = new GoogleApiClient
				.Builder(this)
				.addApi(Places.GEO_DATA_API)
				.addApi(Places.PLACE_DETECTION_API)
				.enableAutoManage(this, this)
				.build();

		//chefData = getIntent().getParcelableExtra("chefData");	   
		chefData = (ChefRegData) getIntent().getSerializableExtra("chefData");
		
		setWidget();
		//doCountryList();
		doCurrencyList();
		//getTimeZone();
	}

	public void setWidget()
	{
		btnRegister = (Button)findViewById(R.id.btnRegisterStep2Next);
		btnRegister.setOnClickListener(this);

		btnRegisterStep2Pre = (ImageView)findViewById(R.id.btnRegisterStep2Pre);
		btnRegisterStep2Pre.setRotation(180);
		btnRegisterStep2Pre.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);		
		headerTxt.setText("CHEF REGISTRATION");

		chefAddress1Part2 = (EditText)findViewById(R.id.chefAddress1Part2);
		chefRegAddress2Part2 = (EditText)findViewById(R.id.chefRegAddress2Part2);
		chefRegStreetAddressPart2 = (EditText)findViewById(R.id.chefRegStreetAddressPart2);
		chefRegAptPart2 = (EditText)findViewById(R.id.chefRegAptPart2);
		chefRegZipPart2 = (EditText)findViewById(R.id.chefRegZipPart2);
		chefRegZipPart2.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				if(s.length() != 0){

					if(!chefRegZipPart2.getText().toString().equalsIgnoreCase("")){
						/*String location=chefRegStreetAddressPart2.getText().toString()+","+
								chefRegCountry.toString()+","+
								chefRegState.toString()+","+
								chefRegCity.toString()+","+
								chefRegZipPart2.getText().toString();
						System.out.println("Location "+location.replace(" ", "%20").trim());
						getlatlongfromzipcode(location.replace(" ", "%20").trim());*/
					}
				}
			}
		});

		chefReglatitudePart2 = (EditText)findViewById(R.id.chefReglatitudePart2);
		chefReglongitudePart2 = (EditText)findViewById(R.id.chefReglongitudePart2);

		chefRegtimezone = (Spinner)findViewById(R.id.chefRegtimezone);
		chefRegtimezone.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				timezoneid = timezoneId.get(position);
				//chefRegCity = cityList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		chefRegCityPart = (Spinner)findViewById(R.id.chefRegCity);		
		chefRegCityPart.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub	
				
				cityid = cityId.get(position);				
				chefRegCity = cityList.get(position);
					
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});
		
		chefRegStatePart2 = (Spinner)findViewById(R.id.chefRegStatePart2);	
		chefRegStatePart2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				chefRegState = stateList.get(position);
				stateid = stateId.get(position);
				
				if(!stateid.equalsIgnoreCase("-1"))				
					doCityList(stateid);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});


		chefRegSpiCountryPart2 = (Spinner)findViewById(R.id.chefRegSpiCountryPart2);
		chefRegSpiCountryPart2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				chefRegCountry = countryList.get(position);
				countryid = countryId.get(position);
				
				if(!countryid.equalsIgnoreCase("-1"))				
				   doStateList(countryid);

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});


		chefRegcurrency = (Spinner)findViewById(R.id.chefRegcurrency);
		chefRegcurrency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				//kitchenOpenTime = openClostList.get(position);
				selectcurrency = currencyList.get(position);
				selectcurrencyid = currencyListID.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		pick_location=(Button)findViewById(R.id.pick_location);
		pick_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
						Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
				if (!hasPermissionLocation) {
					ActivityCompat.requestPermissions(ChefRrgisterPart2Activity.this,
							new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
							LOCATION_PERMISSION);
				}else {
					PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
					try {
						startActivityForResult(builder.build(ChefRrgisterPart2Activity.this), PLACE_PICKER_REQUEST);
					} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
						e.printStackTrace();
					}
				}

			}
		});
		currencyAdapter =new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, currencyList);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegcurrency.setAdapter(currencyAdapter);

		//adapter = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,android.R.layout.simple_spinner_item, countryList);
		adapter = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, countryList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegSpiCountryPart2.setAdapter(adapter);

		adapterState = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, stateList);
		adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegStatePart2.setAdapter(adapterState);

		adapterCity = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, cityList);
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegCityPart.setAdapter(adapterCity);

		adapterTimezone = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, timezoneList);
		adapterTimezone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefRegtimezone.setAdapter(adapterTimezone);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	

		case R.id.btnBack:

			if(InternetConnectivity.isConnectedFast(ChefRrgisterPart2Activity.this)){
				startActivity(new Intent(ChefRrgisterPart2Activity.this,ChefRrgisterActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btnLogout:

			break;

		case R.id.btnRegisterStep2Next:

			chefAddress1 = chefAddress1Part2.getText().toString();
			chefAddress2 = chefRegAddress2Part2.getText().toString();
			chefRegStreetAddress = chefRegStreetAddressPart2.getText().toString();
			chefRegApt = chefRegAptPart2.getText().toString();			
			chefRegZip = chefRegZipPart2.getText().toString();	

			/*if(chefAddress1.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Address1");

			}else if(chefAddress2.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Address2");

			}*/
			if(chefRegStreetAddress.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Street Address");

			}
			/*else if(chefRegApt.equalsIgnoreCase("")){
				CreateDialog.showDialog(this, "Enter Apt/suite/unit, building, floor, buzzer, booth, stand number");
			}*/
			else if(selectcurrency.equalsIgnoreCase("Select Currency")){

				CreateDialog.showDialog(this, "Select Currency");
			}else if(chefRegCountry.equalsIgnoreCase("") || chefRegCountry.equalsIgnoreCase("Select Country")){

				CreateDialog.showDialog(this, "Select Country");

			}else if(chefRegState.equalsIgnoreCase("") || chefRegState.equalsIgnoreCase("Select State")){

				CreateDialog.showDialog(this, "Select State");

			}			
			else if(chefRegCity.equalsIgnoreCase("") || chefRegCity.equalsIgnoreCase("Select City"))
			{
				CreateDialog.showDialog(this, "Select City");

			}else if(chefRegZip.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter Zip code");

			}else if(latitute.equalsIgnoreCase("") || latitute.equalsIgnoreCase("0"))
			{
				CreateDialog.showDialog(this, "pick location");

			}else if(longitude.equalsIgnoreCase("") || longitude.equalsIgnoreCase("0")){

				CreateDialog.showDialog(this, "pick location");

			}
			else{
                serviceoffer="";
				/*if(!chefData.getSeriveOffer1().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getSeriveOffer1();
				if(!chefData.getSeriveOffer2().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getSeriveOffer2();
				if(!chefData.getSeriveOffer3().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getSeriveOffer3();
				if(!chefData.getDeleveryBicycle().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getDeleveryBicycle();
				if(!chefData.getDeleveryVehicle().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getDeleveryVehicle();
				if(!chefData.getDeleveryWalking().equalsIgnoreCase(""))
					serviceoffer=serviceoffer+","+chefData.getDeleveryWalking();*/

				if(InternetConnectivity.isConnectedFast(ChefRrgisterPart2Activity.this)){
					//doChefRegister();
					doChefRegister();
					/*String location=chefRegStreetAddressPart2.getText().toString()+","+
							chefRegCountry.toString()+","+
							chefRegState.toString()+","+
							chefRegCity.toString()+","+
							chefRegZipPart2.getText().toString();
					System.out.println("Location "+location.replace(" ", "%20").trim());
					getlatlongfromzipcode(location.replace(" ", "%20").trim());*/
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.btnRegisterStep2Pre:
			finish();
			break;
		}
	}


	//===============Register chef Function===================
	void doChefRegister()
	{
		progressDialog=new ProgressDialog(ChefRrgisterPart2Activity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CHEF_REGISTER_URL;

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
						Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 
				//params.put("name",chefData.getVkName());
				params.put("FirstName",chefData.getFirstName());
				params.put("LastName",chefData.getLastName());		
				params.put("contact",chefData.getPhoneNo());				
				params.put("password",chefData.getPassword());	
				params.put("passconf",chefData.getConPassword());
				params.put("email",chefData.getEmail());
				params.put("KitchenName",chefData.getVkName());
				/*params.put("country",chefRegCountry);
				params.put("state",chefRegState);	
				params.put("city",chefRegCity);*/
				params.put("country",countryid);
				params.put("state",stateid);
				params.put("city",cityid);
				params.put("zipcode",chefRegZip);
				params.put("addresslineone",chefAddress1);	
				params.put("addresslinetwo",chefAddress2);	
				params.put("streetaddress",chefRegStreetAddress);	
				params.put("aptno",chefRegApt);					
				params.put("venue_type",chefData.getVenueTypeId());
				//params.put("service_offer",chefData.getSeriveOffer1());
				params.put("latitude",latitute);
				params.put("longitude",longitude);
				params.put("time_zonesID",timezoneid);
				params.put("service_offer",chefData.getServiceArray());
				params.put("currency",selectcurrencyid);
				params.put("status","closed");
				params.put("format","json");

				System.out.println(chefData.getFirstName()+"\n"+chefData.getLastName()+"\n"+
						chefData.getPhoneNo()+"\n"+chefData.getPassword()+"\n"+chefData.getConPassword()+""+chefData.getEmail()+"\n"+countryid+"\n"+
						"\n"+stateid+"\n"+"\n"+cityid+"\n"+"\n"+chefRegZip+"\n"+"\n"+chefAddress1+"\n"+"\n"+selectcurrency+
						"\n"+chefAddress2+"\n"+"\n"+chefRegStreetAddress+"\n"+"\n"+chefRegApt+"\n"+"\n"+chefData.getVenueType()+
				"\n"+chefData.getVenueTypeId()+"\n"+latitute+"\n"+longitude+"\n"+timezoneid);

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


	void parseResponse(String response){	

		String message = "";
		String verifyStatus="";
		System.out.println("Response :"+response);
		try {

			JSONObject obj = new JSONObject(response);
			verifyStatus = obj.getString("verifyStatus");
			message = obj.getString("message");


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

		if(verifyStatus.contains("1")){

			startActivity(new Intent(ChefRrgisterPart2Activity.this,ChefEmailVerifyActivity.class));
			finish();
			RegisterActivities.removeAllActivities();
		}


		Toast.makeText(ChefRrgisterPart2Activity.this, ""+message, Toast.LENGTH_LONG).show();
	}

	//=========================== Email varification ==============================================

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}



	void doCurrencyList()
	{
		final ProgressDialog progressDialog3=new ProgressDialog(ChefRrgisterPart2Activity.this);
		progressDialog3.setMessage("loading...");
		progressDialog3.show();
		progressDialog3.setCancelable(false);
		progressDialog3.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CURRENCY_LIST;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseCurrency(response);
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
						Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

	void parseCurrency(String response){
		try {

			if(currencyList.size()>0){

				currencyList.clear();
				currencyListID.clear();
			}


			JSONObject parentObj = new JSONObject(response);

			JSONArray jArray = parentObj.getJSONArray("Records");

			currencyList.add("Select Currency");
			currencyListID.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				currencyList.add(records.optString("code"));
				currencyListID.add(records.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currencyAdapter.notifyDataSetChanged();
		//int position = cityId.indexOf(citySelect);
		//chefKithenEditCity.setSelection(position);
		//doCurrencyList();
		getTimeZone();
	}


	//================================== Get country data ====================================

	void doCountryList()
	{
		progressDialog=new ProgressDialog(ChefRrgisterPart2Activity.this);
		progressDialog.setMessage("loading...");
		//progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.COUNTRY_LIST;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseCountry(response);
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
						Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			JSONObject parentObj = new JSONObject(response);

			JSONArray jArray = parentObj.getJSONArray("Records");

			countryList.add("Select Country");
			countryId.add("-1");

			for(int i=0;i<jArray.length();i++){

				JSONObject innerObj = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				countryList.add(innerObj.optString("name"));
				countryId.add(innerObj.optString("id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		adapter.notifyDataSetChanged();
		//getTimeZone();
	}
	
	
	//================================== Get state data ====================================

		void doStateList(final String countryId)
		{
			progressDialog=new ProgressDialog(ChefRrgisterPart2Activity.this);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			String url;
            System.out.println("CountryID "+countryId);
			url =WebServiceURL.STATE_LIST;    	

			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					new Response.Listener<String>() 
					{
				@Override
				public void onResponse(String response) {
					parseState(response);
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
							Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
			//AppController.getInstance().addToRequestQueue(postRequest);
			RequestQueue requestQueue = Volley.newRequestQueue(this);
			requestQueue.add(postRequest);
			AppController.getInstance().getRequestQueue().getCache().remove(url);
			AppController.getInstance().getRequestQueue().getCache().clear();
		}

		public void parseState(String response)
		{
			try {

				/* stateList = new ArrayList<String>();
				 stateId = new ArrayList<String>();*/
				stateList.clear();
				stateId.clear();

				JSONObject parentObj = new JSONObject(response);

				JSONArray jArray = parentObj.getJSONArray("Records");

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

			/*adapterState = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, stateList);
			adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			chefRegStatePart2.setAdapter(adapterState);*/

			adapterState.notifyDataSetChanged();
		}
		
		//================================== Get city data ====================================

		void doCityList(final String stateId)
		{
			progressDialog=new ProgressDialog(ChefRrgisterPart2Activity.this);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			String url;
			System.out.println("StateID "+stateId);

			url =WebServiceURL.CITY_LIST;    	

			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					new Response.Listener<String>() 
					{
				@Override
				public void onResponse(String response) {
					parseCity(response);
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
							Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
			//AppController.getInstance().addToRequestQueue(postRequest);
			RequestQueue requestQueue = Volley.newRequestQueue(this);
			requestQueue.add(postRequest);
			AppController.getInstance().getRequestQueue().getCache().remove(url);
			AppController.getInstance().getRequestQueue().getCache().clear();
		}

		public void parseCity(String response)
		{
			try {

				/*cityList = new ArrayList<String>();
				cityId = new ArrayList<String>();*/
				cityList.clear();
				cityId.clear();

				JSONObject parentObj = new JSONObject(response);

				JSONArray jArray = parentObj.getJSONArray("Records");

				cityList.add("Select City");
				cityId.add("-1");

				for(int i=0;i<jArray.length();i++){

					JSONObject innerObj = jArray.optJSONObject(i);
					//JSONObject records = innerObj.optJSONObject("records");

					cityList.add(innerObj.optString("name"));
					cityId.add(innerObj.optString("id"));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*adapterCity = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, cityList);
			adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			chefRegCityPart.setAdapter(adapterCity);*/

			adapterCity.notifyDataSetChanged();
		}


	void getlatlongfromzipcode(final String zipcode)
	{
		String  tag_string_req = "string_req";
		//String zip="700091";
		String url ="http://maps.googleapis.com/maps/api/geocode/json?address="+zipcode+"&sensor=false%22";

		final ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		//pDialog.show();

		StringRequest strReq = new StringRequest(Request.Method.GET,
				url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				parselatlong(response);
				Log.d("", response.toString());
				pDialog.hide();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d("", "Error: " + error.getMessage());
				pDialog.hide();
			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
		/*AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();*/
	}

	public void parselatlong(String response)
	{
		try {

			JSONObject parentObj = new JSONObject(response);


			String status=parentObj.getString("status");

			if(status.equalsIgnoreCase("OK")) {

				JSONArray jArray = parentObj.getJSONArray("results");


				JSONObject innerObj = jArray.optJSONObject(0);
				JSONObject geometry = innerObj.optJSONObject("geometry");
				JSONObject location = geometry.optJSONObject("location");

				chefReglatitudePart2.setText(location.optString("lat"));
				chefReglongitudePart2.setText(location.optString("lng"));
				latitute = location.optString("lat");
				longitude = location.optString("lng");
			}else {
				chefReglatitudePart2.setText("");
				chefReglongitudePart2.setText("");
				//Toast.makeText(getApplicationContext(),"Please provide proper street address and zip code",Toast.LENGTH_SHORT).show();
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doChefRegister();

	}


	void getTimeZone()
	{
		progressDialog=new ProgressDialog(ChefRrgisterPart2Activity.this);
		progressDialog.setMessage("loading...");
		//progressDialog.show();
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
						Toast.makeText(ChefRrgisterPart2Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			/*adapterState = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,R.layout.spinner_rows, stateList);
			adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			chefRegStatePart2.setAdapter(adapterState);*/

		adapterTimezone.notifyDataSetChanged();
		doCountryList();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				StringBuilder stBuilder = new StringBuilder();
				String placename = String.format("%s", place.getName());
				 latitute = String.valueOf(place.getLatLng().latitude);
				 longitude = String.valueOf(place.getLatLng().longitude);
				String address = String.format("%s", place.getAddress());
				stBuilder.append("Name: ");
				stBuilder.append(placename);
				stBuilder.append("\n");
				stBuilder.append("Latitude: ");
				stBuilder.append(latitute);
				stBuilder.append("\n");
				stBuilder.append("Logitude: ");
				stBuilder.append(longitude);
				stBuilder.append("\n");
				stBuilder.append("Address: ");
				stBuilder.append(address);
				System.out.println("Location Details : "+stBuilder.toString());
				chefReglatitudePart2.setText(latitute);
				chefReglongitudePart2.setText(longitude);

			}
		}
	}
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		//Snackbar.make(fabPickPlace, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
	}


	//======================================Permission==================================================//
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{

			case LOCATION_PERMISSION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					Toast.makeText(ChefRrgisterPart2Activity.this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
				} else
				{
					Toast.makeText(ChefRrgisterPart2Activity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
				break;
			}


		}

	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(ChefRrgisterPart2Activity.this)){
			startActivity(new Intent(ChefRrgisterPart2Activity.this,ChefRrgisterActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}

	}
}
