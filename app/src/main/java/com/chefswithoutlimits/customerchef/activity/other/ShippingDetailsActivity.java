package com.chefswithoutlimits.customerchef.activity.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.customerchef.activity.customer.cart.CartListActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShippingDetailsActivity extends Activity {


    TextView txt_customer_name;
    TextView headerTxt;
    Button editBtnUSetting,editBtnUPass,chefeditBtndeliveryUpdate;
    ImageView btnBack;
    ImageView btnLogout;

    EditText deliveryFName,deliveryMName,deliveryLName,deliveryEmail,deliveryPhone,deliveryAddress,deliveryZip;
    Spinner spinnerdeliveryCountry,spinnerdeliveryState,spinnerdeliveryCity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shipping_details);
        setwidgets();
    }
    void setwidgets(){

        userInfo = Sharepreferences.getSharePreferance(this);
        txt_customer_name=(TextView)findViewById(R.id.txt_customer_name);
        txt_customer_name.setText(userInfo.getFirstName()+" "+userInfo.getLastName());

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Shipping Details");

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ShippingDetailsActivity.this)){
                    startActivity(new Intent(ShippingDetailsActivity.this,CartListActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.logOut(ShippingDetailsActivity.this);
                finish();
            }
        });

        deliveryFName = (EditText)findViewById(R.id.deliveryFName);
        deliveryMName = (EditText)findViewById(R.id.deliveryMName);
        deliveryLName = (EditText)findViewById(R.id.deliveryLName);
        deliveryEmail = (EditText)findViewById(R.id.deliveryEmail);
        deliveryPhone = (EditText)findViewById(R.id.deliveryPhone);
        deliveryAddress = (EditText)findViewById(R.id.deliveryAddress);
        deliveryZip = (EditText)findViewById(R.id.deliveryZip);


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

        countryAdapter = new ArrayAdapter<String>(ShippingDetailsActivity.this,R.layout.spinner_rows, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryCountry.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<String>(ShippingDetailsActivity.this,R.layout.spinner_rows, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryState.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<String>(ShippingDetailsActivity.this,R.layout.spinner_rows, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryCity.setAdapter(cityAdapter);

        getalldetails();
    }



    public void getalldetails(){
        final ProgressDialog progressDialog=new ProgressDialog(ShippingDetailsActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.GET_CUSTOMER_DELIVERY_SETTING;

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
                        Toast.makeText(ShippingDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ShippingDetailsActivity.this);
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
            Toast.makeText(ShippingDetailsActivity.this, message, Toast.LENGTH_LONG).show();

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

           /* custmerFName.setText(user_details.getString("FirstName"));
            custmerLName.setText(user_details.getString("LastName"));
            custmerEmail.setText(user_details.getString("email"));
            custmerMName.setText(user_details.getString("MidName"));
            custmerContact.setText(user_details.getString("contact"));*/

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
        final ProgressDialog progressDialog2=new ProgressDialog(ShippingDetailsActivity.this);
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
                        Toast.makeText(ShippingDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        spinnerdeliveryCountry.setSelection(position);
        //doStateList(countrySelect);
    }

    //================================== Get state data ====================================

    void doStateList(final String countryId)
    {
        final ProgressDialog progressDialog4=new ProgressDialog(ShippingDetailsActivity.this);
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
                        Toast.makeText(ShippingDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
        spinnerdeliveryCity.setSelection(position);
        //doCurrencyList();
    }


    //================================== Get open close time ====================================

    void doCityList(final String stateId)
    {
        final ProgressDialog progressDialog3=new ProgressDialog(ShippingDetailsActivity.this);
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
                        Toast.makeText(ShippingDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ShippingDetailsActivity.this)){
            startActivity(new Intent(ShippingDetailsActivity.this,CartListActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
