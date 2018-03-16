package com.chefswithoutlimits.customerchef.activity.chefs.kitchen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.KitchenVenue;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CommonMethodes;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.chefswithoutlimits.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefEditKitchenPart1 extends Activity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    ImageView imageView2;
    ImageView btnBack;
    ImageView btnLogout;
    TextView headerTxt;
    Switch btn_switch;
    TextView tv_status;
    ImageView kitchen_logo;
    EditText chefKithenEditkitchensescription,chefKithenEditLocalTaxRate,chefKithenEditMinimumOrder,
            chefKithenEditDeliveryFee,chefKithenEditVKName;

    Spinner chefKitchenEditVType;

    RelativeLayout deleveryTypeLayout;

    Spinner chefKithenEditSelectCurrencyForTransaction;

    Button btn_submit;

    CheckBox checkBoxPickup;
    CheckBox checkBoxDelivery;
    CheckBox checkBoxMailOrder;
    CheckBox checkBoxVehicle;
    CheckBox checkBoxBicycle;
    CheckBox checkBoxWalking;

    UserInformation userInfo;

    ArrayList<KitchenVenue> venueArray = new ArrayList<KitchenVenue>();
    ArrayList<String> venueName = new ArrayList<String>();
    ArrayList<String> venueNameId=new ArrayList<>();

    ArrayList<String> currencyList = new ArrayList<String>();
    ArrayList<String> currencyListID = new ArrayList<String>();

    ArrayAdapter<String> venueAdapter;
    ArrayAdapter<String> currencyAdapter;

    String venueSelect = "0";

    String serviceOffer = "";
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
    String serviceOfferValue;
    String selectcurrencyid="";
    String selectcurrency="";
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

    Boolean kitchen_status=false;

    String addresslineone="",addresslinetwo="",streetaddress="",aptno="",zip,latitde="",longitude="",stateSelect="",citySelect="",countrySelect="",
            distance_cover_by_vehicle="",distance_unit="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_edit_kitchen_part1);
        userInfo = Sharepreferences.getSharePreferance(this);
        kitchen_status=false;
        setWidget();
        doCurrencyList();
    }

    public void setWidget(){

        btn_switch=(Switch)findViewById(R.id.btn_switch);
        tv_status=(TextView)findViewById(R.id.tv_status);




        /*btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((Switch) v).isChecked()) {
                    //Case 1
                    if(!userInfo.getOffline_stripe_user_id().contains("acct_")){
                        btn_switch.setChecked(false);
                        Toast.makeText(getApplicationContext(),"Please Connect a Stripe Account to Open & Activate Your Venue Online",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        tv_status.setText("Kitchen Open");
                        changekitchenstatus("open");
                        if(kitchen_status) {

                        }
                    }

                }
                else{
                    //case 2
                    tv_status.setText("Kitchen Closed");
                    changekitchenstatus("closed");
                    if(kitchen_status) {

                    }
                }
            }
        });*/







        deleveryTypeLayout = (RelativeLayout)findViewById(R.id.deleveryTypeLayout);

        kitchen_logo=(ImageView)findViewById(R.id.kitchen_logo);
        chefKithenEditVKName = (EditText)findViewById(R.id.chefKithenEditVKName);

        chefKithenuploadphoto=(Button)findViewById(R.id.chefKithenuploadphoto);
        chefKithenuploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermissionRead = (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if(hasPermissionRead) {
                    showFileChooser();
                }else {
                    ActivityCompat.requestPermissions(ChefEditKitchenPart1.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_READ_STORAGE);
                }
            }
        });

        //----------------////
        chefKithenEditkitchensescription = (EditText)findViewById(R.id.chefKithenEditkitchensescription);
        chefKithenEditLocalTaxRate = (EditText)findViewById(R.id.chefKithenEditLocalTaxRate);
        chefKithenEditMinimumOrder = (EditText)findViewById(R.id.chefKithenEditMinimumOrder);
        chefKithenEditDeliveryFee = (EditText)findViewById(R.id.chefKithenEditDeliveryFee);


        chefKitchenEditVType = (Spinner)findViewById(R.id.chefKitchenEditVType);
        chefKitchenEditVType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                kitchenVType = venueName.get(position);
                KitvhenVTypeid=venueNameId.get(position);
                System.out.println(kitchenVType+"  +++++++++  "+KitvhenVTypeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        //spinner for currency....

        chefKithenEditSelectCurrencyForTransaction = (Spinner)findViewById(R.id.chefKithenEditSelectCurrencyForTransaction);
        chefKithenEditSelectCurrencyForTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        currencyAdapter =new ArrayAdapter<String>(ChefEditKitchenPart1.this,R.layout.spinner_rows, currencyList);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKithenEditSelectCurrencyForTransaction.setAdapter(currencyAdapter);
        // end spinner for currency....


        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Kitchen Details");

        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);


        venueAdapter =new ArrayAdapter<String>(ChefEditKitchenPart1.this,R.layout.spinner_rows, venueName);
        venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chefKitchenEditVType.setAdapter(venueAdapter);



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

                if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart1.this)){
                    startActivity(new Intent(ChefEditKitchenPart1.this,ChefEditKitchen.class));
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

                kitchenVName = chefKithenEditVKName.getText().toString();
                description=chefKithenEditkitchensescription.getText().toString();
                locattaxrate=chefKithenEditLocalTaxRate.getText().toString();
                minimumorder=chefKithenEditMinimumOrder.getText().toString();
                deliveryfee=chefKithenEditDeliveryFee.getText().toString();


                if(kitchenVName.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "Enter venue name");
                }else if(kitchenVType.equalsIgnoreCase("") || kitchenVType.equalsIgnoreCase("Select Venue Type")){
                    CreateDialog.showDialog(this, "Select venue type");
                }
                else if(locattaxrate.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "Enter local tax rate");
                }
                else if(selectcurrency.equalsIgnoreCase("Select Currency")){
                    CreateDialog.showDialog(this, "Select Currency");
                }else if(minimumorder.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "Enter minimum order");
                }else if(deliveryfee.equalsIgnoreCase("")){
                    CreateDialog.showDialog(this, "Enter delivery fee");
                }
                else{

                    serviceOfferValue = "";
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
                        if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart1.this)){
                            doKitchenUpdate();
                        }else {
                            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                        }
                    }



                }
                break;

        }
    }


    //================================== Kitchen update ====================================

    void doKitchenUpdate()
    {
        final ProgressDialog progressDialog6=new ProgressDialog(ChefEditKitchenPart1.this);
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
                        Toast.makeText(ChefEditKitchenPart1.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {

                //getting the actual path of the image
                String image="";
                Map<String, String>  params = new HashMap<String, String>();
                if(bitmap!=null) {
                    //image = getStringImage2(bitmap);
                    image= CommonMethodes.getStringImage(bitmap);
                }

                params.put("kichen_id",userInfo.getKitchenId());


                params.put("kichen_name",kitchenVName);
                params.put("venue_type",KitvhenVTypeid);//venue_type
                params.put("address","");
                params.put("phone","");
                params.put("delivarymethod","");
                params.put("description",description);
                params.put("service_offer",serviceOfferValue);
                params.put("tax_rate",locattaxrate);
                params.put("currency",selectcurrencyid);
                params.put("min_order",minimumorder);
                params.put("delivery_fee",deliveryfee);
                params.put("kichen_Logo",image);
                params.put("insurance_file","");


                params.put("country",countrySelect);
                params.put("state",stateSelect);
                params.put("city",citySelect);
                params.put("addresslineone",addresslineone);
                params.put("addresslinetwo",addresslinetwo);
                params.put("streetaddress",streetaddress);
                params.put("aptno",aptno);
                params.put("postalcode",zip);
                params.put("latitude",latitde);
                params.put("longitude",longitude);


                params.put("distance",distance_cover_by_vehicle);
                params.put("distance_unit",distance_unit);


                params.put("format","json");

                System.out.println(kitchenVType+"  2+++++++++2  "+KitvhenVTypeid);

                System.out.println("#############$%%%%%%%%%%%%%%%%%%  "+userInfo.getKitchenId()+"\n"+
                        kitchenVName+"\n"+"\n"+deliveryfee+"\n"+minimumorder+"\n"+locattaxrate+"\n"+serviceOfferValue);

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

    public void parseKitchenEdit(String response)
    {
        try{

            JSONObject parentObj = new JSONObject(response);
            JSONObject messageObj = parentObj.getJSONObject("output");
            String message = messageObj.optString("message");
            Toast.makeText(ChefEditKitchenPart1.this, message, Toast.LENGTH_LONG).show();

        }catch(JSONException e){
            System.out.println("Exception :"+e);
        }
    }

    void doCurrencyList()
    {
        final ProgressDialog progressDialog3=new ProgressDialog(ChefEditKitchenPart1.this);
        progressDialog3.setMessage("loading...");
        progressDialog3.show();
        progressDialog3.setCancelable(false);
        progressDialog3.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.CURRENCY_LIST;

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
                        Toast.makeText(ChefEditKitchenPart1.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        getKitchenValue();
    }


    void getKitchenValue()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchenPart1.this);
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
                        Toast.makeText(ChefEditKitchenPart1.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
            chefKithenEditVKName.setText(record.optString("kichen_name"));
            chefKithenEditDeliveryFee.setText(record.optString("delivery_fee"));
            chefKithenEditkitchensescription.setText(record.optString("description"));
            chefKithenEditMinimumOrder.setText(record.optString("min_order"));
            chefKithenEditLocalTaxRate.setText(record.optString("tax_rate"));
            String kitchenstatus = record.optString("status");
            if (kitchenstatus.equalsIgnoreCase("closed")) {
                btn_switch.setChecked(false);
                tv_status.setText("Kitchen Closed");
            } else {
                btn_switch.setChecked(true);
                tv_status.setText("Kitchen Open");
            }
            kitchen_status=true;
            kitchenId = record.optString("id");
            venueSelect = record.optString("venue_type");
            countrySelect = record.optString("country_code");
            stateSelect = record.optString("state_id");
            citySelect = record.optString("city_name");
            serviceOffer = record.optString("service_offer");
            int currencyidposition = currencyListID.indexOf(record.optString("currency"));
            chefKithenEditSelectCurrencyForTransaction.setSelection(currencyidposition);
            String distanceunit = record.optString("distance_unit");

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





            /**/
           /* String kichen_name=(record.optString("kichen_name"));
            String address=(record.optString("address"));
            serviceOffer = record.optString("service_offer");
            String currency=(record.optString("currency"));
            venueSelect = record.optString("venue_type");
            String delivery_fee=record.optString("delivery_fee");
            description=(record.optString("description"));
            String min_order=(record.optString("min_order"));
            String tax_rate=(record.optString("tax_rate"));
            String logo_image=record.optString("logo_image");
           */



            addresslineone=(record.optString("addresslineone"));
            addresslinetwo=(record.optString("addresslinetwo"));
            streetaddress=(record.optString("streetaddress"));
            aptno=(record.optString("aptno"));
            zip=(record.optString("zip"));
            latitde=(record.optString("latitde"));
            longitude=(record.optString("longitude"));
            stateSelect = record.optString("state_id");
            citySelect = record.optString("city_name");
            countrySelect = record.optString("country_code");


            distance_cover_by_vehicle=record.optString("distance_cover_by_vehicle");
            distance_unit=record.optString("distance_unit");
            latitde=(record.optString("latitde"));
            longitude=(record.optString("longitude"));


            /**/

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        doKitchenList();
    }

    void doKitchenList()
    {
        final ProgressDialog progressDialog1=new ProgressDialog(ChefEditKitchenPart1.this);
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
                        Toast.makeText(ChefEditKitchenPart1.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
            KitchenVenue venueObjSelect = new KitchenVenue();
            venueObjSelect.setVenueId("-1");
            venueObjSelect.setVenueName("Select Venue Type");
            venueArray.add(venueObjSelect);
            venueName.add("Select Venue Type");
            venueNameId.add("-1");

            for(int i=0;i<jArray.length();i++){
                KitchenVenue venueObj = new KitchenVenue();
                JSONObject records = jArray.optJSONObject(i);
                venueObj.setVenueId(records.optString("venue_id"));
                venueObj.setVenueName(records.optString("venue_name"));
                venueArray.add(venueObj);
                venueName.add(records.optString("venue_name"));
                venueNameId.add(records.optString("venue_id"));
            }

            venueAdapter.notifyDataSetChanged();
            if(venueName.size()>Integer.parseInt(venueSelect)){
                chefKitchenEditVType.setSelection(Integer.parseInt(venueSelect));
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        /*if(venueName.size()>Integer.parseInt(venueSelect)) {
            if(Integer.parseInt(venueSelect)>0){
                chefKitchenEditVType.setSelection(Integer.parseInt(venueSelect) - 1);
            }
        }*/
    }



    public  void changekitchenstatus(final String status){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchenPart1.this);
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
                        Toast.makeText(ChefEditKitchenPart1.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ChefEditKitchenPart1.this, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
                    Toast.makeText(ChefEditKitchenPart1.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(ChefEditKitchenPart1.this, "The app was not allowed to read to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

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
            Toast.makeText(getApplicationContext(),name+" "+address+" "+ Html.fromHtml(attributions),Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditKitchenPart1.this)){
            startActivity(new Intent(ChefEditKitchenPart1.this,ChefEditKitchen.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }


    }
}
