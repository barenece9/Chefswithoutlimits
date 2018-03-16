package com.chefswithoutlimits.customerchef.activity.customer.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.circleimageview.CircleImageView;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineKitchenDetailsActivity;
import com.chefswithoutlimits.customerchef.activity.home.HomeActivity;
import com.chefswithoutlimits.customerchef.adapter.SearchKitchenListAdapter;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.GPSService;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class OfflineSearchKitchen extends Activity implements View.OnClickListener {


    ImageView btnBack;
    ImageView btnLogout;
    TextView headerTxt,txtCusName;
    ListView itemlistView;

    AutoCompleteTextView spinner_country,spinner_state,spinner_city;

    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> countryCode = new ArrayList<String>();
    ArrayList<String> countryId = new ArrayList<String>();

    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> stateCode = new ArrayList<String>();
    ArrayList<String> stateId = new ArrayList<String>();

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterState;
    ArrayAdapter<String> adapterCity;

    String countryid = "";
    String stateid = "";
    String cityid = "";

    String countryGps="";
    String stateGps= "";
    String cityGps = "";

    String lat="0",lon="0";

    ArrayList<HashMap<String,String>> kitchenlist;

    CircleImageView imageView2;



    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    ProgressDialog locationDialog;

    PlaceAutocompleteFragment places;
    String searchLat,searchLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_search_kitchen);
        setwidgets();
    }

    void setwidgets(){

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("SEARCH KITCHEN");
        txtCusName = (TextView)findViewById(R.id.txtCusName);
        txtCusName.setText("Guest User");

        imageView2=(CircleImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH)
                .placeholder(R.drawable.ic_person_black_24dp)   // optional
                .error(R.drawable.ic_person_black_24dp)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        itemlistView=(ListView)findViewById(R.id.itemlistView);
        itemlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@ You click");

                String kituserid=kitchenlist.get(position).get("id");
                String kitchenName=kitchenlist.get(position).get("kichen_name");
                String kitchen_id=kitchenlist.get(position).get("kitchen_id");

                KitchenData kitchenData=new KitchenData();

                kitchenData.setKitchen_id(kituserid);
                kitchenData.setKichen_name(kitchenlist.get(position).get("kichen_name"));
                kitchenData.setKichen_Logo(kitchenlist.get(position).get("kichen_Logo"));
                kitchenData.setDistance(kitchenlist.get(position).get("distance"));
                kitchenData.setDistance_cover_by_vehicle(kitchenlist.get(position).get("distance_cover_by_vehicle"));
                kitchenData.setMin_order(kitchenlist.get(position).get("min_order"));
                Sharepreferences.saveSharePreferanceKitchenData(OfflineSearchKitchen.this, kitchenData);

                ConstantUtils.kitchen_id=kitchen_id;
                ConstantUtils.kitchenName=kitchenName;

                if(InternetConnectivity.isConnectedFast(OfflineSearchKitchen.this)){
                    Intent intent=new Intent(OfflineSearchKitchen.this,OfflineKitchenDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


        /// spinner ===============================================================

       /* spinner_city = (Spinner)findViewById(R.id.spinner_city);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                cityid = cityId.get(position);

                if(!cityid.equalsIgnoreCase("-1"))
                    doSearchKitchenList(cityid);
               // chefRegCity = cityList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        spinner_state = (Spinner)findViewById(R.id.spinner_state);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
               // chefRegState = stateList.get(position);
                stateid = stateId.get(position);

                if(!stateid.equalsIgnoreCase("-1"))
                    doCityList(stateid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });


        spinner_country = (Spinner)findViewById(R.id.spinner_country);
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        //adapter = new ArrayAdapter<String>(ChefRrgisterPart2Activity.this,android.R.layout.simple_spinner_item, countryList);
        adapter = new ArrayAdapter<String>(SearchKitchen.this,R.layout.spinner_rows, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter);

        adapterState = new ArrayAdapter<String>(SearchKitchen.this,R.layout.spinner_rows, stateList);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapterState);

        adapterCity = new ArrayAdapter<String>(SearchKitchen.this,R.layout.spinner_rows, cityList);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapterCity);*/


        //end spinner ====================================================================================


        ////Start Auto complete text view=========================================


        ///for country =============================================================
        spinner_country=(AutoCompleteTextView) findViewById(R.id.spinner_country);
        adapter= new ArrayAdapter<String>(
                OfflineSearchKitchen.this, android.R.layout.simple_dropdown_item_1line,
                countryList);

        spinner_country.setAdapter(adapter);
        spinner_country.setThreshold(1);

        spinner_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                spinner_country.showDropDown();
            }
        });


        spinner_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                String country=spinner_country.getText().toString();
                for(int i=0;i<countryList.size();i++){
                    if( countryList.get(i).equalsIgnoreCase(country)){
                        countryid=countryId.get(i);

                        if(!countryid.equalsIgnoreCase("-1"))
                            doStateList(countryid);
                    }
                }

            }
        });

        ///for state =============================================================
        spinner_state=(AutoCompleteTextView) findViewById(R.id.spinner_state);
       /* adapterState= new ArrayAdapter<String>(
                SearchKitchen.this, android.R.layout.simple_dropdown_item_1line,
                stateList);

        spinner_state.setAdapter(adapterState);
        spinner_state.setThreshold(1);*/

        spinner_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                spinner_state.showDropDown();
            }
        });


        spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                String state=spinner_state.getText().toString();
                for(int i=0;i<stateList.size();i++){
                    if( stateList.get(i).equalsIgnoreCase(state)){
                        stateid=stateId.get(i);

                        if(!stateid.equalsIgnoreCase("-1"))
                            doCityList(stateid);
                    }
                }

            }
        });

        ///for city =============================================================
        spinner_city=(AutoCompleteTextView) findViewById(R.id.spinner_city);
        /*adapterCity= new ArrayAdapter<String>(
                SearchKitchen.this, android.R.layout.simple_dropdown_item_1line,
                cityList);

        spinner_city.setAdapter(adapterCity);
        spinner_city.setThreshold(1);*/

        spinner_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                spinner_city.showDropDown();
            }
        });


        spinner_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                String city=spinner_city.getText().toString();
                for(int i=0;i<cityList.size();i++){
                    if( cityList.get(i).equalsIgnoreCase(city)){
                        cityid=cityId.get(i);

                        if(!cityid.equalsIgnoreCase("-1"))
                            doSearchKitchenList(cityid);
                    }
                }

            }
        });


        //======= Location pick up===============================================================================

        /*GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            String country = gpsTracker.getCountryName(this);
            String city = gpsTracker.getLocality(this);
            String postalCode = gpsTracker.getPostalCode(this);
            String addressLine = gpsTracker.getAddressLine(this);
           *//* Log.d("country ",country);
            Log.d("city ",city);
            Log.d("postalCode ",postalCode);
            Log.d("addressLine ",addressLine);*//*
            Log.d("stringLatitude ",stringLatitude);
            Log.d("stringLongitude ",stringLongitude);
            GetLocation(stringLatitude,stringLongitude);
           // Toast.makeText(getApplicationContext(),stringLatitude+" "+stringLongitude,Toast.LENGTH_LONG).show();
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
*/




        /*try {

            locationDialog=new ProgressDialog(OfflineSearchKitchen.this);
            locationDialog.setMessage("loading...");
            locationDialog.show();
            locationDialog.setCancelable(false);
            locationDialog.setCanceledOnTouchOutside(false);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            listener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        }catch (Exception e){
            Log.d("Exception","Exception.....................");
            if(locationDialog!=null)
                locationDialog.dismiss();
        }*/





        /*if(!(ConstantUtils.Latitude.equalsIgnoreCase("0") || ConstantUtils.Longitude.equalsIgnoreCase("0"))){
            Log.e("Search Kitchen","Call Get Location ");
            GetLocation(ConstantUtils.Latitude,ConstantUtils.Longitude);

        }else {
            Log.e("Search Kitchen","Call Country List ");
            doCountryList();
        }*/


        places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setHint("Country, Place, Location");
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //iv_toggle.setVisibility(View.VISIBLE);
                LatLng latLng=(place.getLatLng());
                searchLat = String.valueOf(place.getLatLng().latitude);
                searchLng = String.valueOf(place.getLatLng().longitude);
                doKitchenList();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {
            System.out.println("Location not available, Open GPS");
        } else {
            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();

            ConstantUtils.Latitude=Double.toString(latitude);
            ConstantUtils.Longitude=Double.toString(longitude);

            //call by default
            searchLat = Double.toString(latitude);
            searchLng = Double.toString(longitude);
            doKitchenList();
        }
       // doCountryList();



    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.btnBack:

                startActivity(new Intent(OfflineSearchKitchen.this,HomeActivity.class));
                finish();

                break;

            case R.id.btnLogout:

                Logout.logOut(OfflineSearchKitchen.this);
                finish();

                break;
        }
    }


    //================================== Get country data ====================================

    void doCountryList()
    {
        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.COUNTRY_LIST;

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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

            String Status=parentObj.getString("Status");
            if(Status.equalsIgnoreCase("true")) {

                JSONArray jArray = parentObj.getJSONArray("Records");

                countryList.add("Select Country");
                countryId.add("-1");
                countryCode.add("");

                for (int i = 1; i < jArray.length(); i++) {

                    JSONObject innerObj = jArray.optJSONObject(i);
                    //JSONObject records = innerObj.optJSONObject("records");

                    countryList.add(innerObj.optString("name"));
                    countryId.add(innerObj.optString("id"));
                    countryCode.add(innerObj.optString("sortname"));
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();

        for(int i=0;i<countryCode.size();i++){
            if( countryCode.get(i).equalsIgnoreCase(countryGps)){
                countryid=countryId.get(i);

                if(!countryid.equalsIgnoreCase("-1")) {
                    doStateList(countryid);
                    int position = countryId.indexOf(i);
                    System.out.println("Country Position : "+i+1);
                    spinner_country.setText(countryList.get(i));
                }
            }
        }


        //getTimeZone();
    }


    //================================== Get state data ====================================

    void doStateList(final String countryId)
    {
        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(postRequest);
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
            stateCode.clear();

            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("Records");

            stateList.add("Select State");
            stateId.add("-1");
            stateCode.add("");

            for(int i=1;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                stateList.add(records.optString("name"));
                stateId.add(records.optString("id"));
                stateCode.add(records.optString("state_code"));
            }

            System.out.println("clear state list");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        adapterState= new ArrayAdapter<String>(
                OfflineSearchKitchen.this, android.R.layout.simple_dropdown_item_1line,
                stateList);

        spinner_state.setAdapter(adapterState);
        spinner_state.setThreshold(1);
        spinner_state.setText("");
        spinner_city.setText("");



        for(int i=0;i<stateCode.size();i++){
            if( stateCode.get(i).equalsIgnoreCase(stateGps)){
                stateid=stateId.get(i);

                if(!stateid.equalsIgnoreCase("-1")) {
                    doCityList(stateid);
                    int position = stateId.indexOf(i);
                    System.out.println("State Position : "+i+1);
                    spinner_state.setText(stateList.get(i));
                }
            }
        }

        //adapterState.notifyDataSetChanged();
    }

    //================================== Get city data ====================================

    void doCityList(final String stateId)
    {
        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(postRequest);
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

            for(int i=1;i<jArray.length();i++){

                JSONObject innerObj = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                cityList.add(innerObj.optString("name"));
                cityId.add(innerObj.optString("id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        adapterCity= new ArrayAdapter<String>(
                OfflineSearchKitchen.this, android.R.layout.simple_dropdown_item_1line,
                cityList);

        spinner_city.setAdapter(adapterCity);
        spinner_city.setThreshold(1);
        spinner_city.setText("");
        //adapterCity.notifyDataSetChanged();


        for(int i=0;i<cityList.size();i++){
            if( cityList.get(i).equalsIgnoreCase(cityGps)){
                cityid=cityId.get(i);

                if(!cityid.equalsIgnoreCase("-1")) {
                    doSearchKitchenList(cityid);
                    int position = cityId.indexOf(i);
                    System.out.println("State Position : "+i+1);
                    spinner_city.setText(cityList.get(i));
                }
            }
        }
    }

    public void doSearchKitchenList(final String cityid){

        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.SEARCH_KITCHEN_LIST_OFFLINE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseSearchKitchenList(response);
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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInformation userInfo = Sharepreferences.getSharePreferance(OfflineSearchKitchen.this);

                /*lat
              lon*/
                Map<String, String>  params = new HashMap<String, String>();
                params.put("lat",ConstantUtils.Latitude);
                params.put("lon",ConstantUtils.Longitude);
                params.put("city_id",cityid);
                params.put("format","json");

                Log.e("lat",lat);
                Log.e("lon",lon);
                Log.e("city_id",cityid);
                Log.e("format","json");

                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parseSearchKitchenList(String response)
    {
        try {

            kitchenlist=new ArrayList<>();
            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {

                JSONArray jArray = parentObj.getJSONArray("Records");

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject innerObj = jArray.optJSONObject(i);
                    HashMap<String,String> list=new HashMap<>();

                    list.put("kitchen_id",innerObj.optString("id"));
                    list.put("id",innerObj.optString("chef_id"));
                    list.put("kichen_name",innerObj.optString("kichen_name"));
                    list.put("address",innerObj.optString("address"));
                    list.put("status",innerObj.optString("status"));
                    list.put("phone",innerObj.optString("phone"));
                    list.put("description",innerObj.optString("description"));
                    list.put("kichen_Logo",innerObj.optString("kichen_Logo"));
                    list.put("kichen_rating",innerObj.optString("kichen_rating"));
                    list.put("venue_type",innerObj.optString("venue_type"));

                    list.put("distance_unit",innerObj.optString("distance_unit"));
                    list.put("distance",innerObj.optString("distance"));
                    list.put("distance_cover_by_vehicle",innerObj.optString("distance_cover_by_vehicle"));
                    list.put("ratting",innerObj.optString("ratting"));
                    list.put("venue_name",innerObj.optString("venue_name"));
                    list.put("min_order",innerObj.optString("min_order"));

                    kitchenlist.add(list);
                }
            }else {
                Toast.makeText(OfflineSearchKitchen.this, "No kitchen registered here", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        itemlistView.setAdapter(new SearchKitchenListAdapter(OfflineSearchKitchen.this,kitchenlist));
    }



    //=========================get location===============================================


    public void GetLocation(final String lat,final String lng){

        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        //&key=
        //AIzaSyAuUzzZMhY5vIBL8tMxX4SaZZbKdWhrpe4
        /*url ="http://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng + "&sensor=false";*/
        url ="https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng + "&key=AIzaSyAuUzzZMhY5vIBL8tMxX4SaZZbKdWhrpe4";

        Log.e("Search Kitchen : GetLocation URL ",url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseLocation(response);
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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInformation userInfo = Sharepreferences.getSharePreferance(OfflineSearchKitchen.this);
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("user_id",userInfo.getUserId());
                //params.put("city_id",cityid);
                //params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        //AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseLocation(final String response){
        String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";
            try {

                JSONObject jsonObj =new JSONObject(response);
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("short_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                Address1 = long_name + " ";
                            } else if (Type.equalsIgnoreCase("route")) {
                                Address1 = Address1 + long_name;
                            } else if (Type.equalsIgnoreCase("sublocality")) {
                                Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                            }
                        }

                        // JSONArray mtypes = zero2.getJSONArray("types");
                        // String Type = mtypes.getString(0);
                        // Log.e(Type,long_name);

                        Log.d("country ",Country);
                        Log.d("city ",City);
                        Log.d("postalCode ",PIN);
                        Log.d("State ",State);

                        countryGps=Country;
                        stateGps=State;
                        cityGps=City;
                    }

                    doCountryList();
                }else {

                    // if gps not provide any address.===========================
                    doCountryList();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


    }



    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {
            Log.d("******", "Location changed");
            //if(isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                System.out.println(loc.getLatitude()+"current location"+loc.getLongitude());
                // sendBroadcast(intent);
               // sendtoserver(loc.getLatitude(),loc.getLongitude());

                String stringLatitude = String.valueOf(loc.getLatitude());
                String stringLongitude = String.valueOf(loc.getLongitude());


                lat=String.valueOf(loc.getLatitude());
                lon=String.valueOf(loc.getLongitude());

                Log.e("stringLatitude ",stringLatitude);
                Log.e("stringLongitude ",stringLongitude);

            if(locationDialog!=null) {
                locationDialog.dismiss();
            }

            locationManager.removeUpdates(listener);
            GetLocation(stringLatitude,stringLongitude);



            Log.d("Location Post : ",loc.getLatitude()+" current location "+loc.getLongitude());

           // }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
            if(locationDialog!=null) {
                locationDialog.dismiss();
            }
            doCountryList();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            if(locationDialog!=null) {
                locationDialog.dismiss();
            }
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            if(locationDialog!=null) {
                locationDialog.dismiss();
            }
        }


    }



    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("STOP_SERVICE", "DONE");
       // locationManager.removeUpdates(listener);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OfflineSearchKitchen.this,HomeActivity.class));
        finish();
    }




    public void doKitchenList(){

        final ProgressDialog progressDialog=new ProgressDialog(OfflineSearchKitchen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.GET_ALL_KITCHEN_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseKitchenList(response);
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
                        Toast.makeText(OfflineSearchKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInformation userInfo = Sharepreferences.getSharePreferance(OfflineSearchKitchen.this);

                /*lat
              lon*/
                Map<String, String>  params = new HashMap<String, String>();
                params.put("format","json");

                Log.e("lat",lat);
                Log.e("lon",lon);
                Log.e("city_id",cityid);
                Log.e("format","json");

                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parseKitchenList(String response)
    {
        try {

            kitchenlist=new ArrayList<>();
            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {

                JSONArray jArray = parentObj.getJSONArray("Records");

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject innerObj = jArray.optJSONObject(i);
                    HashMap<String,String> list=new HashMap<>();

                    list.put("kitchen_id",innerObj.optString("id"));
                    list.put("id",innerObj.optString("chef_id"));
                    list.put("kichen_name",innerObj.optString("kichen_name"));
                    list.put("address",innerObj.optString("address"));
                    list.put("status",innerObj.optString("status"));
                    list.put("phone",innerObj.optString("phone"));
                    list.put("description",innerObj.optString("description"));
                    list.put("kichen_Logo",innerObj.optString("kichen_Logo"));
                    list.put("kichen_rating",innerObj.optString("kichen_rating"));
                    list.put("venue_type",innerObj.optString("venue_type"));

                    list.put("distance_unit",innerObj.optString("distance_unit"));
                    //list.put("distance",innerObj.optString("distance"));
                    list.put("distance_cover_by_vehicle",innerObj.optString("distance_cover_by_vehicle"));
                    list.put("ratting",innerObj.optString("ratting"));
                    list.put("venue_name",innerObj.optString("venue_name"));
                    list.put("min_order",innerObj.optString("min_order"));
                    //"latitde": "22.5801326",
                    //"longitude": "88.3904616",
                    Double dis=getDistance(Double.valueOf(ConstantUtils.Latitude),Double.valueOf(ConstantUtils.Longitude),
                            Double.valueOf(innerObj.optString("latitde")),Double.valueOf(innerObj.optString("longitude")));


                    Double diswithin=getDistance(Double.valueOf(searchLat),Double.valueOf(searchLng),
                            Double.valueOf(innerObj.optString("latitde")),Double.valueOf(innerObj.optString("longitude")));
                    //list.put("distance", String.valueOf(getDistance(Double.valueOf(searchLat),Double.valueOf(searchLng),Double.valueOf(innerObj.optString("latitde")),Double.valueOf(innerObj.optString("longitude")))));
                    //getDistance(Double.valueOf(searchLat),Double.valueOf(searchLng),Double.valueOf(innerObj.optString("latitde")),Double.valueOf(innerObj.optString("longitude")));
                    if(innerObj.optString("distance_unit").equalsIgnoreCase("Km")){
                        list.put("distance",String.valueOf(dis));
                        if(diswithin<=160.93){
                            kitchenlist.add(list);
                        }
                    }else if(innerObj.optString("distance_unit").equalsIgnoreCase("Miles")){
                        //miles
                        dis=round(0.621371*dis , 2);
                        diswithin=round(0.621371*diswithin , 2);
                        list.put("distance",String.valueOf(dis));
                        if(diswithin<=100){
                            kitchenlist.add(list);
                        }
                    }


                }
            }else {
                Toast.makeText(OfflineSearchKitchen.this, "No kitchen registered here", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        itemlistView.setAdapter(new SearchKitchenListAdapter(OfflineSearchKitchen.this,kitchenlist));
    }


    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (round(dist , 2));
        //return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
