package com.chefswithoutlimits.customerchef.activity.chefs.kitchen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefEditKitchenPart2 extends Activity implements View.OnClickListener {

    UserInformation userInfo;

    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> countryId = new ArrayList<String>();

    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> stateId = new ArrayList<String>();

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();

    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;

    ImageView imageView2;
    ImageView btnBack;
    ImageView btnLogout;
    TextView headerTxt;

    //EditText chefKithenEditAddress;
    EditText chefKithenEditAddressLine1;
    EditText chefKithenEditAddressline2;
    EditText chefKithenEditStreetAddress;
    EditText chefKithenEditApt;
    EditText chefKithenEditZip;


    Spinner chefKithenEditSpiCountry;
    Spinner chefKithenEditState;
    Spinner chefKithenEditCity;

    Button btn_submit;


    String countrySelect = "";
    String stateSelect = "";
    String citySelect = "";


    String chefRegState = "";
    String chefRegCountry = "";
    String countryid = "";
    String stateid = "";
    String cityid = "";
    String kitchenId = "";


    String kitchenAddressLine1 = "";
    String kitchenAddressLine2 = "";
    String kitchenStreetAddress = "";
    String kitchenAPT = "";
    String kitchenCountry = "";
    String kitchenState = "";
    String kitchenCity = "";
    String kitchenZip = "";
    String latitude="",longitude="";

    String kichen_name="",address="",serviceOffer="",currency="",venueSelect="",delivery_fee="",description="",min_order="",tax_rate="",logo_image="",
            distance_cover_by_vehicle="",distance_unit="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_edit_kitchen_part2);
        userInfo = Sharepreferences.getSharePreferance(this);
        setWidget();
        getKitchenValue();
    }

    public void setWidget()
    {

        chefKithenEditAddressLine1 = (EditText)findViewById(R.id.chefKithenEditAddressLine1);
        chefKithenEditAddressline2 = (EditText)findViewById(R.id.chefKithenEditAddressline2);
        chefKithenEditStreetAddress = (EditText)findViewById(R.id.chefKithenEditStreetAddress);
        chefKithenEditApt = (EditText)findViewById(R.id.chefKithenEditApt);
        chefKithenEditZip = (EditText)findViewById(R.id.chefKithenEditZip);

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
                       /* String location=chefKithenEditStreetAddress.getText().toString()+","+
                                chefKithenEditApt.getText().toString()+","+ ","+
                                chefKithenEditZip.getText().toString();
                        System.out.println("Location "+location.replace(" ", "%20").trim());
                        getlatlongfromzipcode(location.replace(" ", "%20").trim());*/
                    }
                }
            }
        });




        chefKithenEditSpiCountry = (Spinner)findViewById(R.id.chefKithenEditSpiCountry);
        chefKithenEditSpiCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        chefKithenEditState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        chefKithenEditCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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



        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Kitchen Address");

        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);


        countryAdapter = new ArrayAdapter<String>(ChefEditKitchenPart2.this,R.layout.spinner_rows, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKithenEditSpiCountry.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<String>(ChefEditKitchenPart2.this,R.layout.spinner_rows, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKithenEditState.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<String>(ChefEditKitchenPart2.this,R.layout.spinner_rows, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKithenEditCity.setAdapter(cityAdapter);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {

            case R.id.btnBack:

                if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart2.this)){
                    startActivity(new Intent(ChefEditKitchenPart2.this,ChefEditKitchen.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnLogout:

                Logout.logOut(this);
                finish();
                break;

            case R.id.btn_submit:

                kitchenAddressLine1 = chefKithenEditAddressLine1.getText().toString();
                kitchenAddressLine2 = chefKithenEditAddressline2.getText().toString();
                kitchenStreetAddress = chefKithenEditStreetAddress.getText().toString();
                kitchenAPT = chefKithenEditApt.getText().toString();
                kitchenZip = chefKithenEditZip.getText().toString();


                if(kitchenStreetAddress.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "Street address can not be blank ");
                }
                else if(chefRegCountry.equalsIgnoreCase("Select Country")){

                    CreateDialog.showDialog(this, "Select Country");
                }else if(chefRegState.equalsIgnoreCase("Select State")){

                    CreateDialog.showDialog(this, "Select State");
                }else if(kitchenCity.equalsIgnoreCase("Select City")){

                    CreateDialog.showDialog(this, "Select City");
                }else if(kitchenZip.equalsIgnoreCase("")){

                    CreateDialog.showDialog(this, "Enter Zip code");
                }
                else{
                    /*String location=chefKithenEditStreetAddress.getText().toString()+","+
                            chefKithenEditZip.getText().toString();
                    System.out.println("Location "+location.replace(" ", "%20").trim());
                    getlatlongfromzipcode(location.replace(" ", "%20").trim());*/
                    if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart2.this)){
                        doKitchenUpdate();
                    }else {
                        Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    }

                }
                break;

        }
    }


    void getKitchenValue()
    {
        final ProgressDialog  progressDialog=new ProgressDialog(ChefEditKitchenPart2.this);
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
                        Toast.makeText(ChefEditKitchenPart2.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("kitchen_ID",userInfo.getKitchenId());
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

            chefKithenEditAddressLine1.setText(record.optString("addresslineone"));
            chefKithenEditAddressline2.setText(record.optString("addresslinetwo"));
            chefKithenEditStreetAddress.setText(record.optString("streetaddress"));
            chefKithenEditApt.setText(record.optString("aptno"));
            chefKithenEditZip.setText(record.optString("zip"));
           // chefkitcheneditlatitude.setText(record.optString("latitde"));
           // chefkitcheneditlongitute.setText(record.optString("longitude"));



            kitchenId = record.optString("id");

            countrySelect = record.optString("country_code");
            stateSelect = record.optString("state_id");
            citySelect = record.optString("city_name");



             /**/
            kichen_name=(record.optString("kichen_name"));
            address=(record.optString("address"));
            serviceOffer = record.optString("service_offer");
            currency=(record.optString("currency"));
            venueSelect = record.optString("venue_type");
            delivery_fee=record.optString("delivery_fee");
            description=(record.optString("description"));
            min_order=(record.optString("min_order"));
            tax_rate=(record.optString("tax_rate"));
            logo_image=record.optString("logo_image");


            /*addresslineone=(record.optString("addresslineone"));
            addresslinetwo=(record.optString("addresslinetwo"));
            streetaddress=(record.optString("streetaddress"));
            aptno=(record.optString("aptno"));
            zip=(record.optString("zip"));
            latitde=(record.optString("latitde"));
            longitude=(record.optString("longitude"));
            stateSelect = record.optString("state_id");
            citySelect = record.optString("city_name");
            countrySelect = record.optString("country_code");*/


            distance_cover_by_vehicle=record.optString("distance_cover_by_vehicle");
            distance_unit=record.optString("distance_unit");
            latitude=(record.optString("latitde"));
            longitude=(record.optString("longitude"));



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        doCountryList();


    }


    //==================================== Get Country List ========================
    void doCountryList()
    {
        final ProgressDialog progressDialog2=new ProgressDialog(ChefEditKitchenPart2.this);
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
                        Toast.makeText(ChefEditKitchenPart2.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        chefKithenEditSpiCountry.setSelection(position);
        //doStateList(countrySelect);
    }

    //================================== Get state data ====================================

    void doStateList(final String countryId)
    {
        final ProgressDialog progressDialog4=new ProgressDialog(ChefEditKitchenPart2.this);
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
                        Toast.makeText(ChefEditKitchenPart2.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        chefKithenEditCity.setSelection(position);
        //doCurrencyList();
    }


    //================================== Get open close time ====================================

    void doCityList(final String stateId)
    {
        final ProgressDialog progressDialog3=new ProgressDialog(ChefEditKitchenPart2.this);
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
                        Toast.makeText(ChefEditKitchenPart2.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
    }

    public void parselatlong(String response)
    {
        latitude ="";
        longitude = "";
        try {
            JSONObject parentObj = new JSONObject(response);
            String status=parentObj.getString("status");
            if(status.equalsIgnoreCase("OK")) {
                JSONArray jArray = parentObj.getJSONArray("results");
                JSONObject innerObj = jArray.optJSONObject(0);
                JSONObject geometry = innerObj.optJSONObject("geometry");
                JSONObject location = geometry.optJSONObject("location");

                latitude = location.optString("lat");
                longitude = location.optString("lng");

                if(latitude.equalsIgnoreCase("")||longitude.equalsIgnoreCase("")){
                   Toast.makeText(getApplicationContext(),"Please provide proper address",Toast.LENGTH_LONG).show();
                }else {
                    doKitchenUpdate();
                    //Toast.makeText(getApplicationContext(),"Updated "+latitude+" "+longitude,Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getApplicationContext(),"Please provide proper address",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void doKitchenUpdate()
    {
        final ProgressDialog progressDialog6=new ProgressDialog(ChefEditKitchenPart2.this);
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
                        Toast.makeText(ChefEditKitchenPart2.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kichen_id",userInfo.getKitchenId());

                params.put("kichen_name",kichen_name);
                params.put("venue_type",venueSelect);
                params.put("address","");
                params.put("phone","");
                params.put("delivarymethod","");
                params.put("description",description);
                params.put("service_offer",serviceOffer);
                params.put("tax_rate",tax_rate);
                params.put("currency",currency);
                params.put("min_order",min_order);
                params.put("delivery_fee",delivery_fee);
                params.put("kichen_Logo","");
                params.put("insurance_file","");


                params.put("country",countryid);
                params.put("state",stateid);
                params.put("city",cityid);
                params.put("addresslineone",kitchenAddressLine1);
                params.put("addresslinetwo",kitchenAddressLine2);
                params.put("streetaddress",kitchenStreetAddress);
                params.put("aptno",kitchenAPT);
                params.put("postalcode",kitchenZip);


                params.put("distance",distance_cover_by_vehicle);
                params.put("distance_unit",distance_unit);
                params.put("latitude",latitude);
                params.put("longitude",longitude);

                params.put("format","json");

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
            JSONObject messageObj = parentObj.getJSONObject("output");
            String message = messageObj.optString("message");
            Toast.makeText(ChefEditKitchenPart2.this, message, Toast.LENGTH_LONG).show();

        }catch(JSONException e){
            System.out.println("Exception :"+e);
        }
    }


    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart2.this)){
            startActivity(new Intent(ChefEditKitchenPart2.this,ChefEditKitchen.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
