package com.chefswithoutlimits.customerchef.activity.chefs.kitchen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.util.Sharepreferences;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefEditKitchenPart3 extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback{

    ArrayList<String> distanceUNIT = new ArrayList<String>();
    ArrayList<String> distanceUNITID = new ArrayList<String>();
    ArrayAdapter<String> distanceunitAdapter;

    ImageView btnBack;
    ImageView btnLogout;
    TextView headerTxt;
    Button btn_submit;

    Spinner chefKithenEditSelectDeliveryzonebyradiuskmmiles;
    EditText chefKithenEditDeliveryzonebyradiuskmmiles;

    UserInformation userInfo;
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

    String kichen_name="",address="",serviceOffer="",currency="",venueSelect="",delivery_fee="",description="",min_order="",tax_rate="",logo_image="",
            addresslineone="",addresslinetwo="",streetaddress="",aptno="",zip="",stateSelect="",citySelect="",countrySelect="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_edit_kitchen_part3);
        userInfo = Sharepreferences.getSharePreferance(this);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setWidget();
        getKitchenValue();
    }

    public void setWidget(){

        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Location in Map");

        chefKithenEditDeliveryzonebyradiuskmmiles = (EditText)findViewById(R.id.chefKithenEditDeliveryzonebyradiuskmmiles);
        chefKithenEditDeliveryzonebyradiuskmmiles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    if(!chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString().equalsIgnoreCase("")){
                        afteraddmarker();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //spinner for mile/km....

        chefKithenEditSelectDeliveryzonebyradiuskmmiles = (Spinner)findViewById(R.id.chefKithenEditSelectDeliveryzonebyradiuskmmiles);
        chefKithenEditSelectDeliveryzonebyradiuskmmiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        distanceunitAdapter =new ArrayAdapter<String>(ChefEditKitchenPart3.this,R.layout.spinner_rows, distanceUNIT);
        distanceunitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKithenEditSelectDeliveryzonebyradiuskmmiles.setAdapter(distanceunitAdapter);
        //end spinner for mile/km....

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {

            case R.id.btnBack:

                if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart3.this)){
                    startActivity(new Intent(ChefEditKitchenPart3.this,ChefEditKitchen.class));
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

                distancecoverbyvehicle=chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString();
                if(latitude.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "set kitchen location");
                }else if(longitude.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "set kitchen location");
                }else if(distancecoverbyvehicle.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "delivery zone by radious km/miles");
                }
                else{

                    if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart3.this)){
                        doKitchenUpdate();
                    }else {
                        Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
		/*LatLng sydney = new LatLng(22.6735936, 88.387);
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in SRFoods"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                LatLng dragPosition = arg0.getPosition();
                double dragLat = dragPosition.latitude;
                double dragLong = dragPosition.longitude;
                Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
               // Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
                latitude=String.valueOf(dragLat);
                longitude=String.valueOf(dragLong);
                afteraddmarker();

            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });
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
                Uri.parse("android-app://com.chefswithoutlimits/http/host/path")
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
                Uri.parse("android-app://com.chefswithoutlimits/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    void getKitchenValue()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchenPart3.this);
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
                        Toast.makeText(ChefEditKitchenPart3.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

            latitude=record.optString("latitde");
            longitude=record.optString("longitude");
            distancecoverbyvehicle=record.optString("distance_cover_by_vehicle");
            distanceunit=record.optString("distance_unit");
            chefKithenEditDeliveryzonebyradiuskmmiles.setText(record.optString("distance_cover_by_vehicle"));


            String distanceunit = record.optString("distance_unit");
            int position=-1;
            if(distanceunit.equalsIgnoreCase("Km"))
                position=0;
            else if (distanceunit.equalsIgnoreCase("Miles"))
                position=1;
            chefKithenEditSelectDeliveryzonebyradiuskmmiles.setSelection(position);



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



            addresslineone=(record.optString("addresslineone"));
            addresslinetwo=(record.optString("addresslinetwo"));
            streetaddress=(record.optString("streetaddress"));
            aptno=(record.optString("aptno"));
            zip=(record.optString("zip"));
            stateSelect = record.optString("state_id");
            citySelect = record.optString("city_name");
            countrySelect = record.optString("country_code");


            /*distancecoverbyvehicle=record.optString("distance_cover_by_vehicle");
            distanceunit=record.optString("distance_unit");
            latitude=(record.optString("latitde"));
            longitude=(record.optString("longitude"));*/


            /*Picasso.with(this)
                    .load(record.optString("logo_image"))
                    .placeholder(R.mipmap.user_image)   // optional
                    .error(R.mipmap.user_image)      // optional
                    .resize(400,400)                        // optional
                    .into(kitchen_logo);
*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        addmarker();
    }

    private void addmarker(){
        // Creating MarkerOptions
        LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(point);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        options.title("My Kitchen");
        options.draggable(true);
        //options.snippet("A-312 Crystal plaza,Opp infinity mall,New link road,Andheri West,Mumbai-400053");
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        double radious=0;
        if(distanceunit.equalsIgnoreCase("Miles")){
            radious= Double.valueOf(distancecoverbyvehicle)*1000*1.60934;
        }else if(distanceunit.equalsIgnoreCase("Km")){
            radious= Double.valueOf(distancecoverbyvehicle)*1000;
        }

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
                .radius(radious)
                .strokeColor(Color.GREEN)
                .strokeWidth(1)
                .fillColor(Color.parseColor("#80B3F9A2")));
    }

    private void afteraddmarker(){
        // Creating MarkerOptions
        mMap.clear();
        LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(point);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        options.title("My Kitchen");
        options.draggable(true);
        //options.snippet("A-312 Crystal plaza,Opp infinity mall,New link road,Andheri West,Mumbai-400053");
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        double radious=0;
        if(distanceunit.equalsIgnoreCase("Miles")){
            radious= Double.valueOf(Float.valueOf(chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString()))*1000*1.60934;
        }else if(distanceunit.equalsIgnoreCase("Km")){
            radious= Double.valueOf(Float.valueOf(chefKithenEditDeliveryzonebyradiuskmmiles.getText().toString()))*1000;
        }

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)))
                .radius(radious)
                .strokeColor(Color.GREEN)
                .strokeWidth(1)
                .fillColor(Color.parseColor("#80B3F9A2")));
    }


    //================================== Kitchen update ====================================

    void doKitchenUpdate()
    {
        final ProgressDialog progressDialog6=new ProgressDialog(ChefEditKitchenPart3.this);
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
                        Toast.makeText(ChefEditKitchenPart3.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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


                params.put("country",countrySelect);
                params.put("state",stateSelect);
                params.put("city",citySelect);
                params.put("addresslineone",addresslineone);
                params.put("addresslinetwo",addresslinetwo);
                params.put("streetaddress",streetaddress);
                params.put("aptno",aptno);
                params.put("postalcode",zip);



                params.put("distance",distancecoverbyvehicle);
                params.put("distance_unit",distanceunit);
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
            Toast.makeText(ChefEditKitchenPart3.this, message, Toast.LENGTH_LONG).show();

        }catch(JSONException e){
            System.out.println("Exception :"+e);
        }
    }

    @Override
    public void onBackPressed() {
        if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart3.this)){
            startActivity(new Intent(ChefEditKitchenPart3.this,ChefEditKitchen.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
