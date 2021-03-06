package com.chefswithoutlimits.customerchef.activity.chefs.menu.option;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefEditMenuOption extends Activity {


    ImageView imageView2;
    TextView headerTxt;
    Button add_category,btn_close;
    EditText etn_option_name,etn_option_regular_price,etn_option_sales_price;
    Spinner spinner_category,spinner_available;
    String menuoptionname="";
    UserInformation userInfo;
    ImageView btnBack;

    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayList<String> categoryId = new ArrayList<String>();

    ArrayList<String> availablity=new ArrayList<>();

    ArrayAdapter<String> adaptercategory;
    ArrayAdapter<String> adapteravailablity;

    String selectcategoryList="",selectavailablity="";
    String menu_regular_price="",menu_sales_price="";
    String item_id="",item_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_edit_menu_option);
        Bundle bundle = getIntent().getExtras();
        item_id = bundle.getString("item_id");
        item_name = bundle.getString("item_name");
        setwidget();
       // userInfo = Sharepreferences.getSharePreferance(this);
    }

    public  void setwidget(){
        userInfo = Sharepreferences.getSharePreferance(this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Edit Menu Option Item");
        etn_option_name=(EditText)findViewById(R.id.etn_option_name);
        etn_option_name.setText(item_name);
        etn_option_regular_price=(EditText)findViewById(R.id.etn_option_regular_price);
        etn_option_sales_price=(EditText)findViewById(R.id.etn_option_sales_price);
        add_category=(Button)findViewById(R.id.btn_add);
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuoptionname=etn_option_name.getText().toString();

               // menu_regular_price=String.valueOf(round(Double.valueOf(etn_option_regular_price.getText().toString()), 2));
               // menu_sales_price=String.valueOf(round(Double.valueOf(etn_option_sales_price.getText().toString()), 2));

                menu_regular_price=etn_option_regular_price.getText().toString();
                menu_sales_price=etn_option_sales_price.getText().toString();

                if(menuoptionname.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChefEditMenuOption.this, "Enter Menu option name");
                }else if(selectcategoryList.equalsIgnoreCase("-1")||selectcategoryList.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChefEditMenuOption.this, "Select menu name");
                }else if(selectavailablity.equalsIgnoreCase("Select availability")||selectavailablity.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChefEditMenuOption.this, "Select availability");
                }
                else {
                    if(InternetConnectivity.isConnectedFast(ChefEditMenuOption.this)){
                        editmenuoption();
                    }else {
                        Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_close=(Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefEditMenuOption.this)){
                    startActivity(new Intent(ChefEditMenuOption.this,ChefMenuOptionActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefEditMenuOption.this)){
                    startActivity(new Intent(ChefEditMenuOption.this,ChefMenuOptionActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner_category=(Spinner)findViewById(R.id.spinner_category);
        spinner_available=(Spinner)findViewById(R.id.spinner_available);

        adaptercategory = new ArrayAdapter<String>(ChefEditMenuOption.this,R.layout.spinner_rows, categoryList);
        adaptercategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adaptercategory);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // selectcategoryList=categoryList.get(position);
                selectcategoryList=categoryId.get(position);
                if(!selectcategoryList.equalsIgnoreCase("-1")||selectcategoryList.equalsIgnoreCase("Select Menu"))
                checkpricestatus(selectcategoryList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        availablity.add("Select availability");
        availablity.add("Yes");
        availablity.add("No");

        adapteravailablity = new ArrayAdapter<String>(ChefEditMenuOption.this,R.layout.spinner_rows, availablity);
        adapteravailablity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_available.setAdapter(adapteravailablity);

        spinner_available.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String checkselectavailablity=availablity.get(position);
                if(checkselectavailablity.equalsIgnoreCase("Yes")){
                    selectavailablity="A";
                }else if(checkselectavailablity.equalsIgnoreCase("No")){
                    selectavailablity="IA";
                }else {
                    selectavailablity="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        domenulistingwithmultipleoption();
    }


    public  void domenulistingwithmultipleoption(){

        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuOption.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_ITEM_MULTIPLE_OPTION_LISTING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseDataMenuItem(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefEditMenuOption.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefEditMenuOption.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                // params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseDataMenuItem(String response)
    {

        try {
            categoryList.clear();
            categoryId.clear();

            categoryList.add("Select Menu");
            categoryId.add("-1");

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");
                for (int i = 0; i < parentArr.length(); i++) {
                    //  ChefMenuItem object = new ChefMenuItem();
                    JSONObject dataObj = parentArr.optJSONObject(i);
                    //JSONObject dataObj = childObj.optJSONObject("records");
                    categoryId.add(dataObj.optString("id"));
                    //object.setMenuImage(dataObj.optString("item_image"));
                    categoryList.add(dataObj.optString("menu_name"));
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        adaptercategory.notifyDataSetChanged();
        getMenuOptiondetailsforedit();

    }


    public  void editmenuoption(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuOption.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.EDIT_MENU_OPTION;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseData(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefEditMenuOption.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("item_id",item_id);
                params.put("item_name",menuoptionname);
                params.put("menu_id",selectcategoryList);
                params.put("item_sale_price",menu_sales_price);
                params.put("item_regular_price",menu_regular_price);
                params.put("item_status",selectavailablity);
                //System.out.println("selectcategoryList  @@@@@@@@@@@@@@@  "+selectcategoryList);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseData(String response)
    {
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("status");
            if(Status.equalsIgnoreCase("success")) {
                Toast.makeText(ChefEditMenuOption.this, "Menu Option Edited successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ChefEditMenuOption.this,ChefMenuOptionActivity.class));
                finish();
            }else {
                Toast.makeText(ChefEditMenuOption.this, "Failed to edit menu option", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public  void getMenuOptiondetailsforedit(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuOption.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_ITEM_OPTION_FOR_EDIT_DATA;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseOptiondetailsforedit(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefEditMenuOption.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("item_id",item_id);
                // params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseOptiondetailsforedit(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                JSONObject records=parentJSON.getJSONObject("records");

                etn_option_name.setText(records.optString("item_name"));
                etn_option_sales_price.setText(records.optString("item_sale_price"));
                etn_option_regular_price.setText(records.optString("item_regular_price"));

                String item_status=records.optString("item_status");

               // String available_for_delivery=dataObj.optString("available_for_delivery");
                if(item_status.equalsIgnoreCase("A")){
                    spinner_available.setSelection(1);
                }else if(item_status.equalsIgnoreCase("IA")){
                    spinner_available.setSelection(2);
                }
                String menu_name=records.optString("menu_name");
                System.out.println("$$$$$$$$$$$$ "+menu_name);

                for(int i=0;i<categoryList.size();i++){
                    // boolean isStringExists = categoryList.contains(cat_name);
                        /*if(categoryList.contains(cat_name)) {
                            spinner_category.setSelection(i);
                        }*/
                    if(categoryList.get(i).equalsIgnoreCase(menu_name)){
                        spinner_category.setSelection(i);
                    }
                }

            }else {
                //Toast.makeText(ChefEditMenuOption.this, "Failed to add menu option", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkpricestatus(final String menu_id){

        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuOption.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_ITEM_OPTION_PRICE_OPTION_STATUS;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parsePriceOptionStatus(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefEditMenuOption.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("menu_id",menu_id);
                // params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parsePriceOptionStatus(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
           // JSONObject Records=parentJSON.getJSONObject("Records");
            if(Status.equalsIgnoreCase("success")) {
                String priced_stat = parentJSON.optString("priced stat");
                if (priced_stat.equalsIgnoreCase("PR")) {

                    etn_option_regular_price.setVisibility(View.VISIBLE);
                    etn_option_sales_price.setVisibility(View.VISIBLE);
                } else if (priced_stat.equalsIgnoreCase("NP")) {
                    etn_option_regular_price.setVisibility(View.GONE);
                    etn_option_sales_price.setVisibility(View.GONE);
                    etn_option_regular_price.setText("");
                    etn_option_sales_price.setText("");
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditMenuOption.this)){
            startActivity(new Intent(ChefEditMenuOption.this,ChefMenuOptionActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
