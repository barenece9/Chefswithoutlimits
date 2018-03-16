package com.chefswithoutlimits.customerchef.activity.other;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.KitchenVenue;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CreateDialog;
import com.squareup.picasso.Picasso;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChefKitchenEditActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener,OnMapReadyCallback {

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	Switch btn_switch;
	TextView tv_status;
	Button btn_map;

	ImageView kitchen_logo;

	EditText chefKithenEditVKName;
	EditText chefKithenEditAddress;
	EditText chefKithenEditAddressLine1;
	EditText chefKithenEditAddressline2;
	EditText chefKithenEditStreetAddress;
	EditText chefKithenEditApt;
	EditText chefKithenEditZip;
	EditText chefkitcheneditlatitude;
	EditText chefkitcheneditlongitute;

	EditText chefKithenEditkitchensescription,chefKithenEditLocalTaxRate,chefKithenEditMinimumOrder,chefKithenEditDeliveryFee,
			chefKithenEditDeliveryzonebyradiuskmmiles;

	Spinner chefKithenEditSpiCountry;
	Spinner chefKithenEditState;
	Spinner chefKithenEditCity;
	Spinner chefKitchenEditVType;
	Spinner chefKitchenEditOpenTime;
	Spinner chefKitchenEditCloseTime;

	Spinner chefKithenEditSelectDeliveryzonebyradiuskmmiles,chefKithenEditSelectCurrencyForTransaction;

	Button btnChefKithenEdit;	

	CheckBox checkBoxPickup;
	CheckBox checkBoxDelivery;
	CheckBox checkBoxMailOrder;
	CheckBox checkBoxVehicle;
	CheckBox checkBoxBicycle;
	CheckBox checkBoxWalking;

	RelativeLayout deleveryTypeLayout;

	private ProgressDialog progressDialog,progressDialog1,progressDialog2,progressDialog3,progressDialog4,progressDialog5,progressDialog6;
	UserInformation userInfo;

	ArrayList<KitchenVenue> venueArray = new ArrayList<KitchenVenue>();
	ArrayList<String> venueName = new ArrayList<String>();
	ArrayList<String> venueNameId=new ArrayList<>();

	ArrayList<String> countryList = new ArrayList<String>();
	ArrayList<String> countryId = new ArrayList<String>();

	ArrayList<String> stateList = new ArrayList<String>();
	ArrayList<String> stateId = new ArrayList<String>();

	ArrayList<String> cityList = new ArrayList<String>();
	ArrayList<String> cityId = new ArrayList<String>();

	ArrayList<String> currencyList = new ArrayList<String>();
	ArrayList<String> currencyListID = new ArrayList<String>();

	ArrayList<String> distanceUNIT = new ArrayList<String>();
	ArrayList<String> distanceUNITID = new ArrayList<String>();

	ArrayList<String> openClostList = new ArrayList<String>();

	ArrayAdapter<String> countryAdapter;
	ArrayAdapter<String> stateAdapter;
	ArrayAdapter<String> cityAdapter;

	ArrayAdapter<String> venueAdapter;
	ArrayAdapter<String> openTimeAdapter;
	ArrayAdapter<String> closeTimeAdapter;

	ArrayAdapter<String> currencyAdapter;
	ArrayAdapter<String> distanceunitAdapter;

	String venueSelect = "0";
	String countrySelect = "";
	String stateSelect = "";
	String citySelect = "";
	String serviceOffer = "";

	String chefRegState = "";
	String chefRegCountry = "";
	String countryid = "";
	String stateid = "";
	String cityid = "";
	String kitchenId = "";

	String serviceOffer1 = "";
	String serviceOffer2 = "";
	String serviceOffer3 = "";
	String deleveryType1 = "";
	String deleveryType2 = "";
	String deleveryType3 = "";

	String kitchenVName = "";
	String kitchenVType = "";
	String KitvhenVTypeid="";
	String kitchenAddress = "";
	String kitchenAddressLine1 = "";
	String kitchenAddressLine2 = "";
	String kitchenStreetAddress = "";
	String kitchenAPT = "";
	String kitchenCountry = "";
	String kitchenState = "";
	String kitchenCity = "";
	String kitchenZip = "";
	String kitchenOpenTime = "";
	String kitchenCloseTime = "";
	String serviceOfferValue;
	String selectcurrencyid="";
	String selectcurrency="";

	private GoogleMap mMap;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	private int REQUEST_ID_MULTIPLE_PERMISSIONS = 24;
	String latitude="",longitude="";
	String distancecoverbyvehicle="";
	String distanceunit="";
	String description="",locattaxrate="",minimumorder="",deliveryfee="";


	private static final int
			REQUEST_READ_STORAGE = 115;

	Button chefKithenuploadphoto;
	//Image request code
	private int PICK_IMAGE_REQUEST = 1;

	//storage permission code
	private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 123;

	//Bitmap to get image from gallery
	private Bitmap bitmap;

	//Uri to store the image uri
	private Uri filePath;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getSupportActionBar().hide();
		setContentView(R.layout.chef_kitchenedit_activity);

		userInfo = Sharepreferences.getSharePreferance(this);

		openClostList.add("1AM");
		openClostList.add("2AM");
		openClostList.add("3AM");
		openClostList.add("4AM");
		openClostList.add("5AM");
		openClostList.add("6AM");
		openClostList.add("7AM");
		openClostList.add("8AM");
		openClostList.add("9AM");
		openClostList.add("10AM");
		openClostList.add("11AM");
		openClostList.add("12AM");
		openClostList.add("13PM");
		openClostList.add("14PM");
		openClostList.add("15PM");
		openClostList.add("16PM");
		openClostList.add("17PM");
		openClostList.add("18PM");
		openClostList.add("19PM");
		openClostList.add("20PM");
		openClostList.add("21PM");
		openClostList.add("22PM");
		openClostList.add("23PM");
		openClostList.add("24PM");

		SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

		setWidget();
		doCurrencyList();
		//getKitchenValue();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		// Add a marker in Sydney, Australia, and move the camera.
		/*LatLng sydney = new LatLng(22.6735936, 88.387);
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in SRFoods"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.lnsel.chefswitchoutlimits/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.lnsel.chefswitchoutlimits/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	public void setWidget()
	{
		btn_switch=(Switch)findViewById(R.id.btn_switch);
		tv_status=(TextView)findViewById(R.id.tv_status);
		btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					changekitchenstatus("open");
					tv_status.setText("Kitchen Open");
				}
				else {
					changekitchenstatus("closed");
					tv_status.setText("Kitchen Closed");
				}
			}
		});



		btn_map=(Button)findViewById(R.id.btn_map);
		btn_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Construct an intent for the place picker
				try {
					PlacePicker.IntentBuilder intentBuilder =
							new PlacePicker.IntentBuilder();
					Intent intent = intentBuilder.build(ChefKitchenEditActivity.this);
					// Start the intent by requesting a result,
					// identified by a request code.
					startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

				} catch (GooglePlayServicesRepairableException e) {
					// ...
				} catch (GooglePlayServicesNotAvailableException e) {
					// ...
				}
			}
		});


		kitchen_logo=(ImageView)findViewById(R.id.kitchen_logo);
		chefKithenEditVKName = (EditText)findViewById(R.id.chefKithenEditVKName);
		chefKithenEditAddress = (EditText)findViewById(R.id.chefKithenEditAddress);
		chefKithenEditAddressLine1 = (EditText)findViewById(R.id.chefKithenEditAddressLine1);
		chefKithenEditAddressline2 = (EditText)findViewById(R.id.chefKithenEditAddressline2);
		chefKithenEditStreetAddress = (EditText)findViewById(R.id.chefKithenEditStreetAddress);
		chefKithenEditApt = (EditText)findViewById(R.id.chefKithenEditApt);
		chefKithenEditZip = (EditText)findViewById(R.id.chefKithenEditZip);
		chefKithenuploadphoto=(Button)findViewById(R.id.chefKithenuploadphoto);
		chefKithenuploadphoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//requestStoragePermission();
				boolean hasPermissionRead = (ContextCompat.checkSelfPermission(getApplicationContext(),
						android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
				if(hasPermissionRead) {
					showFileChooser();
				}else {
					ActivityCompat.requestPermissions(ChefKitchenEditActivity.this,
							new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
							REQUEST_READ_STORAGE);
				}
			}
		});
		chefKithenEditZip.addTextChangedListener(new TextWatcher() {

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

					/*if(!chefKithenEditZip.getText().toString().equalsIgnoreCase("")){
						getlatlongfromzipcode(chefKithenEditZip.getText().toString());
					}*/

					if(!chefKithenEditZip.getText().toString().equalsIgnoreCase("")){
						String location=chefKithenEditStreetAddress.getText().toString()+","+
								chefKithenEditApt.getText().toString()+","+ ","+
								chefKithenEditZip.getText().toString();
						System.out.println("Location "+location.replace(" ", "%20").trim());
						getlatlongfromzipcode(location.replace(" ", "%20").trim());
					}
				}
			}
		});

		chefkitcheneditlatitude = (EditText)findViewById(R.id.chefkitcheneditlatitude);
		chefkitcheneditlongitute = (EditText)findViewById(R.id.chefkitcheneditlongitute);

		//----------------////
		chefKithenEditkitchensescription = (EditText)findViewById(R.id.chefKithenEditkitchensescription);
		chefKithenEditLocalTaxRate = (EditText)findViewById(R.id.chefKithenEditLocalTaxRate);
		chefKithenEditMinimumOrder = (EditText)findViewById(R.id.chefKithenEditMinimumOrder);
		chefKithenEditDeliveryFee = (EditText)findViewById(R.id.chefKithenEditDeliveryFee);
		chefKithenEditDeliveryzonebyradiuskmmiles = (EditText)findViewById(R.id.chefKithenEditDeliveryzonebyradiuskmmiles);
		chefKithenEditDeliveryzonebyradiuskmmiles.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() != 0){

					if(!chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString().equalsIgnoreCase("")){
						//getlatlongfromzipcode(chefKithenEditZip.getText().toString());

						afteraddmarker();/*#################################################################################*/
					}
				}

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		chefKithenEditSpiCountry = (Spinner)findViewById(R.id.chefKithenEditSpiCountry);
		chefKithenEditSpiCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

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
		chefKithenEditState = (Spinner)findViewById(R.id.chefKithenEditState);
		chefKithenEditState.setOnItemSelectedListener(new OnItemSelectedListener() {

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
		chefKithenEditCity = (Spinner)findViewById(R.id.chefKithenEditCity);
		chefKithenEditCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub	

				kitchenCity=cityList.get(position);
				cityid=cityId.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		chefKitchenEditVType = (Spinner)findViewById(R.id.chefKitchenEditVType);
		chefKitchenEditVType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub		

				kitchenVType = venueName.get(position);
				KitvhenVTypeid=venueNameId.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		chefKitchenEditOpenTime = (Spinner)findViewById(R.id.chefKitchenEditOpenTime);
		chefKitchenEditOpenTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub		
				kitchenOpenTime = openClostList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		//spinner for currency....

		chefKithenEditSelectCurrencyForTransaction = (Spinner)findViewById(R.id.chefKithenEditSelectCurrencyForTransaction);
		chefKithenEditSelectCurrencyForTransaction.setOnItemSelectedListener(new OnItemSelectedListener() {

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
		currencyAdapter =new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, currencyList);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKithenEditSelectCurrencyForTransaction.setAdapter(currencyAdapter);
		// end spinner for currency....

		//spinner for mile/km....

		chefKithenEditSelectDeliveryzonebyradiuskmmiles = (Spinner)findViewById(R.id.chefKithenEditSelectDeliveryzonebyradiuskmmiles);
		chefKithenEditSelectDeliveryzonebyradiuskmmiles.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				//kitchenOpenTime = openClostList.get(position);
				 distanceunit=distanceUNIT.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});
		distanceUNIT.add("Km");
		distanceUNIT.add("Miles");
		distanceunitAdapter =new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, distanceUNIT);
		distanceunitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKithenEditSelectDeliveryzonebyradiuskmmiles.setAdapter(distanceunitAdapter);
		//end spinner for mile/km....

		openTimeAdapter =new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, openClostList);
		openTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKitchenEditOpenTime.setAdapter(openTimeAdapter); 

		chefKitchenEditCloseTime = (Spinner)findViewById(R.id.chefKitchenEditCloseTime);
		chefKitchenEditCloseTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub		

				kitchenCloseTime = openClostList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// TODO Auto-generated method stub

			}
		});

		closeTimeAdapter =new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, openClostList);
		closeTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKitchenEditCloseTime.setAdapter(closeTimeAdapter); 

		btnChefKithenEdit = (Button)findViewById(R.id.btnChefKithenEdit);
		btnChefKithenEdit.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("KITCHEN SETTING");


		venueAdapter =new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, venueName);
		venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKitchenEditVType.setAdapter(venueAdapter); 

		countryAdapter = new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, countryList);
		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKithenEditSpiCountry.setAdapter(countryAdapter);

		stateAdapter = new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, stateList);
		stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKithenEditState.setAdapter(stateAdapter);

		cityAdapter = new ArrayAdapter<String>(ChefKitchenEditActivity.this,R.layout.spinner_rows, cityList);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chefKithenEditCity.setAdapter(cityAdapter);

		deleveryTypeLayout = (RelativeLayout)findViewById(R.id.deleveryTypeLayout);


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

	}


	@Override
	public void onCheckedChanged(CompoundButton view, boolean check) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.checkBoxPickup:
			

			if(check){
				serviceOffer1 = "1";
			}else{
				serviceOffer1 = "";
			}

			break;

		case R.id.checkBoxDelivery:

			if(check)
			{
				deleveryTypeLayout.setVisibility(View.VISIBLE);
				serviceOffer2 = "2";
			}else{
				deleveryTypeLayout.setVisibility(View.INVISIBLE);
				serviceOffer2 = "";
				checkBoxWalking.setChecked(false);
				checkBoxVehicle.setChecked(false);
				checkBoxBicycle.setChecked(false);
			}

			break;

		case R.id.checkBoxMailOrder:


			if(check){
				serviceOffer3 = "3";
			}else{
				serviceOffer3 = "";
			}

			break;

		case R.id.checkBoxVehicle:



			if(check){
				deleveryType1 = "4";
			}else{
				deleveryType1 = "";
			}

			break;

		case R.id.checkBoxBicycle:



			if(check){
				deleveryType2 = "5";
			}else{
				deleveryType2 = "";
			}


			break;

		case R.id.checkBoxWalking:		

			if(check){
				deleveryType3 = "6";
			}else{
				deleveryType3 = "";
			}

			break;
		}
	}




	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {

		case R.id.btnBack:

			startActivity(new Intent(ChefKitchenEditActivity.this,ChefDashboardActivity.class));
			finish();
			break;

		case R.id.btnLogout:

			Logout.logOut(this);
			finish();
			break;	

		case R.id.btnChefKithenEdit:		

			kitchenVName = chefKithenEditVKName.getText().toString();		
			kitchenAddress = chefKithenEditAddress.getText().toString();
			kitchenAddressLine1 = chefKithenEditAddressLine1.getText().toString();
			kitchenAddressLine2 = chefKithenEditAddressline2.getText().toString();
			kitchenStreetAddress = chefKithenEditStreetAddress.getText().toString();
			kitchenAPT = chefKithenEditApt.getText().toString();		
			kitchenZip = chefKithenEditZip.getText().toString();
			latitude=chefkitcheneditlatitude.getText().toString();
			longitude=chefkitcheneditlongitute.getText().toString();
			description=chefKithenEditkitchensescription.getText().toString();
			locattaxrate=chefKithenEditLocalTaxRate.getText().toString();
			minimumorder=chefKithenEditMinimumOrder.getText().toString();
			deliveryfee=chefKithenEditDeliveryFee.getText().toString();
			distancecoverbyvehicle=chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString();

			if(kitchenVName.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter venue name");
			}else if(kitchenVType.equalsIgnoreCase("") || kitchenVType.equalsIgnoreCase("Select Venue Type")){

				CreateDialog.showDialog(this, "Select venue type");

			}else if(chefRegCountry.equalsIgnoreCase("Select Country")){

				CreateDialog.showDialog(this, "Select Country");
			}else if(chefRegState.equalsIgnoreCase("Select State")){

				CreateDialog.showDialog(this, "Select State");
			}else if(kitchenCity.equalsIgnoreCase("Select City")){

				CreateDialog.showDialog(this, "Select City");
			}else if(kitchenZip.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter Zip code");
			}
			else if(latitude.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter latitude");
			}else if(longitude.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter longitude");
			}else if(locattaxrate.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter local tax rate");
			}
			else if(selectcurrency.equalsIgnoreCase("Select Currency")){

				CreateDialog.showDialog(this, "Select Currency");
			}else if(minimumorder.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter minimum order");
			}else if(deliveryfee.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter delivery fee");
			}else if(distancecoverbyvehicle.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "delivery zone by radious km/miles");
			}


			else{

				doKitchenUpdate();
			}



			break;

		}
	}

	//========================================= Get Customer detail =========================================

	void getKitchenValue()
	{
		progressDialog=new ProgressDialog(ChefKitchenEditActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CHEF_KITCHEN_SETTING;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {

				parseData(response);

				if(progressDialog!=null)
					progressDialog.dismiss();				
				Log.d("Response", response);
				System.out.println("@@@@@@@@2 "+response);
			}
				}, 
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog!=null)
							progressDialog.dismiss();
						System.out.println("Error=========="+error);
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("kitchen_ID",userInfo.getKitchenId());
				//System.out.println("@@@@@@@@"+userInfo.getKitchenId());
				//params.put("kitchen_ID","147");
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}


	private void parseData(String response)
	{
		JSONObject parentJSON;
		try {

			parentJSON = new JSONObject(response);
			JSONArray parentArr = parentJSON.optJSONArray("Records");

			JSONObject record = parentArr.getJSONObject(0);
			//JSONObject record = child.getJSONObject("records");

			chefKithenEditVKName.setText(record.optString("kichen_name"));
			chefKithenEditAddress.setText(record.optString("address"));
			chefKithenEditAddressLine1.setText(record.optString("addresslineone"));
			chefKithenEditAddressline2.setText(record.optString("addresslinetwo"));
			chefKithenEditStreetAddress.setText(record.optString("streetaddress"));
			chefKithenEditApt.setText(record.optString("aptno"));
			chefKithenEditZip.setText(record.optString("zip"));
			chefkitcheneditlatitude.setText(record.optString("latitde"));
			chefkitcheneditlongitute.setText(record.optString("longitude"));
			latitude=record.optString("latitde");
			longitude=record.optString("longitude");
			distancecoverbyvehicle=record.optString("distance_cover_by_vehicle");
			distanceunit=record.optString("distance_unit");
			chefKithenEditDeliveryFee.setText(record.optString("delivery_fee"));
			chefKithenEditDeliveryzonebyradiuskmmiles.setText(record.optString("distance_cover_by_vehicle"));
			chefKithenEditkitchensescription.setText(record.optString("description"));
			chefKithenEditMinimumOrder.setText(record.optString("min_order"));
			chefKithenEditLocalTaxRate.setText(record.optString("tax_rate"));


			String kitchenstatus = record.optString("status");
			if (kitchenstatus.equalsIgnoreCase("closed")) {
				btn_switch.setChecked(false);
				tv_status.setText("Kitchen Closed");
				//imgChDashOff.setPressed(true);
				//imgChDashOn.setPressed(false);
			} else {
				btn_switch.setChecked(true);
				tv_status.setText("Kitchen Open");
				//imgChDashOn.setPressed(true);
				//imgChDashOff.setPressed(false);
			}



			kitchenId = record.optString("id");
			venueSelect = record.optString("venue_type");
			countrySelect = record.optString("country_code");
			stateSelect = record.optString("state_id");
			citySelect = record.optString("city_name");
			serviceOffer = record.optString("service_offer");

			int currencyidposition = currencyListID.indexOf(record.optString("currency"));
			chefKithenEditSelectCurrencyForTransaction.setSelection(currencyidposition);
			String distanceunit = record.optString("distance_unit");
			int position=-1;
			if(distanceunit.equalsIgnoreCase("Km"))
				position=0;
			else if (distanceunit.equalsIgnoreCase("Miles"))
				position=1;
			chefKithenEditSelectDeliveryzonebyradiuskmmiles.setSelection(position);


			Picasso.with(this)
					.load(record.optString("logo_image"))
					.placeholder(R.mipmap.user_image)   // optional
					.error(R.mipmap.user_image)      // optional
					.resize(400,400)                        // optional
					.into(kitchen_logo);

			if(serviceOffer.contains("1")){				

				checkBoxPickup.setChecked(true);
			}

			if(serviceOffer.contains("2")){				

				checkBoxDelivery.setChecked(true);
				deleveryTypeLayout.setVisibility(View.VISIBLE);
			}

			if(serviceOffer.contains("3")){				

				checkBoxMailOrder.setChecked(true);
			}

			if(serviceOffer.contains("4")){				

				checkBoxVehicle.setChecked(true);
			}

			if(serviceOffer.contains("5")){				

				checkBoxBicycle.setChecked(true);
			}

			if(serviceOffer.contains("6")){				

				checkBoxWalking.setChecked(true);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		//doKitchenOpenClose();

		addmarker();/*#################################################################################################*/

		doKitchenList();
		//doCountryList();


	}

	//================================== Get kitchen data ====================================

	void doKitchenList()
	{
		progressDialog1=new ProgressDialog(ChefKitchenEditActivity.this);
		progressDialog1.setMessage("loading...");
		progressDialog1.show(); 
		progressDialog1.setCancelable(false);
		progressDialog1.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.KITCHEN_LIST;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseKitchen(response);
				if(progressDialog1!=null)
					progressDialog1.dismiss();				
				Log.d("Response", response);                        
			}
				}, 
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog1!=null)
							progressDialog1.dismiss();
						System.out.println("Error=========="+error);
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
			venueNameId.add("-1");

			for(int i=1;i<jArray.length();i++){

				KitchenVenue venueObj = new KitchenVenue();

				JSONObject records = jArray.optJSONObject(i);
				//JSONObject records = innerObj.optJSONObject("records");

				venueObj.setVenueId(records.optString("venue_id"));
				venueObj.setVenueName(records.optString("venue_name"));

				venueArray.add(venueObj);

				venueName.add(records.optString("venue_name"));
				venueNameId.add(records.optString("venue_id"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		venueAdapter.notifyDataSetChanged();
		chefKitchenEditVType.setSelection(Integer.parseInt(venueSelect)-1);

		//doKitchenList();
		doCountryList();
	}
	//==================================== Get Country List ========================
	void doCountryList()
	{
		progressDialog2=new ProgressDialog(ChefKitchenEditActivity.this);
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
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			for(int i=1;i<jArray.length();i++){

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
		chefKithenEditSpiCountry.setSelection(position);
		//doStateList(countrySelect);
	}	

	//================================== Get state data ====================================

	void doStateList(final String countryId)
	{
		progressDialog4=new ProgressDialog(ChefKitchenEditActivity.this);
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
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			for(int i=1;i<jArray.length();i++){

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
		chefKithenEditState.setSelection(position);
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

			for(int i=1;i<jArray.length();i++){

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
		chefKithenEditCity.setSelection(position);
		//doCurrencyList();
	}


	//================================== Get open close time ====================================

	void doCityList(final String stateId)
	{
		progressDialog3=new ProgressDialog(ChefKitchenEditActivity.this);
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
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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


	//================================== Get city data ====================================

	void doKitchenOpenClose()
	{
		progressDialog5=new ProgressDialog(ChefKitchenEditActivity.this);
		progressDialog5.setMessage("loading...");
		progressDialog5.show(); 
		progressDialog5.setCancelable(false);
		progressDialog5.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_KITCHEN_SETTING_TIME_OPEN;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseKitchenTime(response);
				if(progressDialog5!=null)
					progressDialog5.dismiss();				
				Log.d("Response", response);                        
			}
				}, 
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog5!=null)
							progressDialog5.dismiss();
						System.out.println("Error=========="+error);
						//doCityList(stateid);
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 				
				params.put("kichen_id",kitchenId);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);           
	}

	public void parseKitchenTime(String response)
	{

		try {

			JSONObject parentObj = new JSONObject(response);
			JSONArray jArray = parentObj.getJSONArray("Records");

			JSONObject childObj = jArray.getJSONObject(1);
			JSONObject recordObj = childObj.optJSONObject("records");
			String openTime = recordObj.optString("open_time");
			String closeTime = recordObj.optString("close_time");

			if(!openTime.equalsIgnoreCase("")){

				int openPosition = openClostList.indexOf(openTime);
				chefKitchenEditOpenTime.setSelection(openPosition);
			}else{

				chefKitchenEditOpenTime.setSelection(5);
			}

			if(!closeTime.equalsIgnoreCase("")){

				int closePosition = openClostList.indexOf(closeTime);
				chefKitchenEditCloseTime.setSelection(closePosition);

			}else{

				chefKitchenEditCloseTime.setSelection(23);
			}




		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			chefKitchenEditOpenTime.setSelection(5);
			chefKitchenEditCloseTime.setSelection(23);
		}


	}


	//================================== Kitchen update ====================================

	void doKitchenUpdate()
	{
		progressDialog6=new ProgressDialog(ChefKitchenEditActivity.this);
		progressDialog6.setMessage("loading...");
		progressDialog6.show(); 
		progressDialog6.setCancelable(false);
		progressDialog6.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_KITCHEN_SETTING_UPATE;  
		
		

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseKitchenEdit(response);
				if(progressDialog6!=null)
					progressDialog6.dismiss();				
				Log.d("Response", response);                        
			}
				}, 
				new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog6!=null)
							progressDialog6.dismiss();
						System.out.println("Error=========="+error);
						//doCityList(stateid);
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   	
				serviceOfferValue = "";
				//getting the actual path of the image
				String image="";
				Map<String, String>  params = new HashMap<String, String>();
				if(bitmap!=null) {
					image = getStringImage2(bitmap);
				}
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

				params.put("kichen_id",userInfo.getKitchenId());
				params.put("kichen_name",kitchenVName);
				params.put("country",countryid);
				params.put("state",stateid);
				params.put("city",cityid);
				params.put("venue_type",KitvhenVTypeid);
				params.put("addresslineone",kitchenAddressLine1);
				params.put("addresslinetwo",kitchenAddressLine2);
				params.put("streetaddress",kitchenStreetAddress);
				params.put("aptno",kitchenAPT);
				params.put("address","");
				params.put("postalcode",kitchenZip);
				params.put("phone","");
				params.put("delivarymethod","");
				params.put("distance",distancecoverbyvehicle);
				params.put("distance_unit",distanceunit);
				params.put("description",description);
				params.put("service_offer",serviceOfferValue);
				params.put("latitude",latitude);
				params.put("longitude",longitude);
				params.put("tax_rate",locattaxrate);
				params.put("currency",selectcurrencyid);
				params.put("min_order",minimumorder);
				params.put("delivery_fee",deliveryfee);
				params.put("kichen_Logo",image);
				params.put("insurance_file","");
				params.put("format","json");

				System.out.println("#############$%%%%%%%%%%%%%%%%%%  "+userInfo.getKitchenId()+"\n"+
						kitchenVName+"\n"+"\n"+deliveryfee+"\n"+minimumorder+"\n"+locattaxrate+"\n"+serviceOfferValue);

				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);

		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}
	
	public void parseKitchenEdit(String response)
	{
		try{

			JSONObject parentObj = new JSONObject(response);
			//JSONArray jArray = parentObj.getJSONArray("output");
			JSONObject messageObj = parentObj.getJSONObject("output");
			String message = messageObj.optString("message");
			Toast.makeText(ChefKitchenEditActivity.this, message, Toast.LENGTH_LONG).show();

		}catch(JSONException e){
			System.out.println("Exception :"+e);
		}
	}


	void doCurrencyList()
	{
		progressDialog3=new ProgressDialog(ChefKitchenEditActivity.this);
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
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			for(int i=1;i<jArray.length();i++){

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
		getKitchenValue();
	}


	private void addmarker(){
		// Creating MarkerOptions
		LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
		CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(10).build();

		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		MarkerOptions options = new MarkerOptions();
		// Setting the position of the marker
		options.position(point);
		options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
		options.title("My Location");
		//options.snippet("A-312 Crystal plaza,Opp infinity mall,New link road,Andheri West,Mumbai-400053");
		// Add new marker to the Google Map Android API V2
		mMap.addMarker(options);

		double radious=0;
		if(distanceunit.equalsIgnoreCase("Miles")){
			radious= Integer.valueOf(distancecoverbyvehicle)*1000*1.60934;
		}else if(distanceunit.equalsIgnoreCase("Km")){
			radious= Integer.valueOf(distancecoverbyvehicle)*1000;
		}

		Circle circle = mMap.addCircle(new CircleOptions()
				.center(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
				.radius(radious)
				.strokeColor(Color.RED)
				.strokeWidth(1)
				.fillColor(Color.BLUE));
	}


	// lat log .....
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
		//AppController.getInstance().getRequestQueue().getCache().remove(url);
		//AppController.getInstance().getRequestQueue().getCache().clear();
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

				chefkitcheneditlatitude.setText(location.optString("lat"));
				chefkitcheneditlongitute.setText(location.optString("lng"));
				latitude = location.optString("lat");
				longitude = location.optString("lng");
				afteraddmarker();  /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void afteraddmarker(){
		// Creating MarkerOptions
		mMap.clear();
		LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
		CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(10).build();

		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		MarkerOptions options = new MarkerOptions();
		// Setting the position of the marker
		options.position(point);
		options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
		options.title("My Location");
		//options.snippet("A-312 Crystal plaza,Opp infinity mall,New link road,Andheri West,Mumbai-400053");
		// Add new marker to the Google Map Android API V2
		mMap.addMarker(options);

		double radious=0;
		if(distanceunit.equalsIgnoreCase("Miles")){
			radious= Integer.valueOf(Integer.valueOf(chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString()))*1000*1.60934;
		}else if(distanceunit.equalsIgnoreCase("Km")){
			radious= Integer.valueOf(Integer.valueOf(chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString()))*1000;
		}

		Circle circle = mMap.addCircle(new CircleOptions()
				.center(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
				.radius(radious)
				.strokeColor(Color.RED)
				.strokeWidth(1)
				.fillColor(Color.BLUE));
	}


	/*upload file.......*/
	//method to show file chooser
	private void showFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	}



	//method to get the file path from uri
	public String getPath(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = getContentResolver().query(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}

/*
	//Requesting permission
	private void requestStoragePermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			return;

		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			//If the user has denied the permission previously your code will come to this block
			//Here you can explain why you need this permission
			//Explain here why you need this permission
		}
		//And finally ask for the permission
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
	}


	//This method will be called when the user will tap on allow or deny
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		//Checking the request code of our request
		if (requestCode == STORAGE_PERMISSION_CODE) {

			//If permission is granted
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//Displaying a toast
				Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
			} else {
				//Displaying another toast if permission is not granted
				Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
			}
		}
	}*/

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
					Toast.makeText(ChefKitchenEditActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
					//reload my activity with permission granted or use the features what required the permission
					//finish();
					//startActivity(getIntent());
				} else
				{
					Toast.makeText(ChefKitchenEditActivity.this, "The app was not allowed to read to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}

	}


	public  void changekitchenstatus(final String status){
		final ProgressDialog progressDialog=new ProgressDialog(ChefKitchenEditActivity.this);
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
						Toast.makeText(ChefKitchenEditActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

				Log.d("kitchen_id",userInfo.getKitchenId());
				Log.d("kitchen_status",status);
				Log.d("format","json");

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
			JSONObject record = parentJSON.getJSONObject("output");
			String status=record.optString("status");
			String message=record.optString("message");
			Toast.makeText(ChefKitchenEditActivity.this, message, Toast.LENGTH_LONG).show();


			//getkitchenstatus();


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// for marker drah and drop ................................



	public void onPickButtonClick() {
		// Construct an intent for the place picker
		try {
			PlacePicker.IntentBuilder intentBuilder =
					new PlacePicker.IntentBuilder();
			Intent intent = intentBuilder.build(this);
			// Start the intent by requesting a result,
			// identified by a request code.
			startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

		} catch (GooglePlayServicesRepairableException e) {
			// ...
		} catch (GooglePlayServicesNotAvailableException e) {
			// ...
		}
	}

	// A place has been received; use requestCode to track the request.
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = PlaceAutocomplete.getPlace(this, data);
				Log.i("TAG", "Place: " + place.getName());
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(this, data);
				// TODO: Handle the error.
				Log.i("TAG", status.getStatusMessage());

			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}*/


	//handling the image chooser activity result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			filePath = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				kitchen_logo.setImageBitmap(bitmap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE
				&& resultCode == RESULT_OK) {

			Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
			// The user has selected a place. Extract the name and address.
			final Place place = PlacePicker.getPlace(data, this);

			final CharSequence name = place.getName();
			final CharSequence address = place.getAddress();
			String attributions = PlacePicker.getAttributions(data);
			if (attributions == null) {
				attributions = "";
			}

			//mViewName.setText(name);
			//mViewAddress.setText(address);
			//mViewAttributions.setText(Html.fromHtml(attributions));
			Toast.makeText(getApplicationContext(),name+" "+address+" "+Html.fromHtml(attributions),Toast.LENGTH_SHORT).show();

		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(ChefKitchenEditActivity.this,ChefDashboardActivity.class));
		finish();
	}
}
