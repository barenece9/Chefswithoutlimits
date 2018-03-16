package com.chefswithoutlimits.customerchef.activity.chefs.menu.optionsets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.option.ChefAddMenuOption;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by db on 2/27/2018.
 */

public class ChefOptionSetsAttToMenuActivity extends Activity {

    TextView headerTxt;
    ImageView btnLogout;
    Button btn_close,btn_submit;
    EditText etn_option_set_name;
    LinearLayout ll_parent_programmatically;
    ImageView btnBack;
    ImageView imageView2;
    UserInformation userInfo;
    ProgressDialog progressDialog;
    ListView list_view;
    ArrayList<HashMap<String,String>> setsName = new ArrayList<>();
    ArrayList<HashMap<String,String>> optionName = new ArrayList<>();
    ArrayList<Integer> ids=new ArrayList<>();
    Spinner spinner_option_set_name;

    ArrayAdapter adapterMenu;
    ArrayList<String> menuList = new ArrayList<String>();
    ArrayList<String> menuId = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_option_sets_att_to_menu);



        progressDialog=new ProgressDialog(ChefOptionSetsAttToMenuActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsAttToMenuActivity.this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Opten Sets");
        list_view=(ListView)findViewById(R.id.list_view);
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.logOut(ChefOptionSetsAttToMenuActivity.this);
                finish();
            }
        });


        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefOptionSetsAttToMenuActivity.this)){
                    startActivity(new Intent(ChefOptionSetsAttToMenuActivity.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        spinner_option_set_name=(Spinner)findViewById(R.id.spinner_option_set_name);


        adapterMenu = new ArrayAdapter<String>(ChefOptionSetsAttToMenuActivity.this,R.layout.spinner_rows, menuList);
        adapterMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_option_set_name.setAdapter(adapterMenu);

        spinner_option_set_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*String selectMenuId=menuId.get(position);
                String selectMenuName=menuList.get(position);
                if(!selectMenuName.equalsIgnoreCase("-1")||selectMenuName.equalsIgnoreCase("Select Menu")){

                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*ArrayList<String> menuArray = new ArrayList<String>();
        menuArray.add("-Select Menu-");
        ArrayAdapter<String> spinnerOptionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, menuArray);
        spinner_option_set_name.setAdapter(spinnerOptionAdapter);*/

        ll_parent_programmatically = (LinearLayout) findViewById(R.id.ll_parent_programmatically);
        for(int id=0;id<1;id++) {

            LinearLayout ll_parent_child = new LinearLayout(this);
            ll_parent_child.setId(id);
            ll_parent_child.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams ll_parms_child = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll_parms_child.gravity = Gravity.CENTER_VERTICAL;
            ll_parms_child.setMargins(0, 5, 0, 0);
            ll_parent_child.setLayoutParams(ll_parms_child);



            CheckBox option_name = new CheckBox(this);
            option_name.setId(id);
            option_name.setHeight(50);
           // option_name.setWidth(200);
            option_name.setText("Test,");
            option_name.setTextColor(Color.parseColor("#795548"));
            option_name.setTypeface(Typeface.DEFAULT_BOLD);
            option_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            option_name.setPadding(5,0,0,0);
            //option_name.setBackgroundResource(R.drawable.edit_bg);
            ll_parent_child.addView(option_name);

            TextView tv_use = new TextView(this);
            tv_use.setId(id);
            tv_use.setHeight(50);
            tv_use.setText("Use");
            tv_use.setTextColor(Color.parseColor("#795548"));
            tv_use.setTypeface(Typeface.DEFAULT_BOLD);
            tv_use.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tv_use.setPadding(5,0,0,0);
            ll_parent_child.addView(tv_use);


            EditText option_price = new EditText(this);
            option_price.setId(id);
            option_price.setHeight(50);
            option_price.setWidth(160);
            option_price.setHint("Price if any");
            option_price.setTextColor(Color.parseColor("#898C88"));
            option_price.setTypeface(Typeface.DEFAULT_BOLD);
            option_price.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            option_price.setPadding(5,0,0,0);
            option_price.setBackgroundResource(R.drawable.edit_bg);
            ll_parent_child.addView(option_price);

            TextView tv_times = new TextView(this);
            tv_times.setId(id);
            tv_times.setHeight(50);
            tv_times.setText("Times, As");
            tv_times.setTextColor(Color.parseColor("#795548"));
            tv_times.setTypeface(Typeface.DEFAULT_BOLD);
            tv_times.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tv_times.setPadding(5,0,0,0);
            ll_parent_child.addView(tv_times);


            Spinner spinner = new Spinner(this);
            spinner.setId(id);
            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.add("Multi-Select");
            spinnerArray.add("Single-Select");
            spinnerArray.add("Number-Select");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setPadding(5,0,0,0);
            spinner.setAdapter(spinnerArrayAdapter);
            // spinner.setBackgroundResource(R.drawable.edit_bg);
            ll_parent_child.addView(spinner);


            ll_parent_programmatically.addView(ll_parent_child);

        }

        getMenuItem();

    }





    public void getMenuItem()
    {
        progressDialog.show();
        String url;

        url = WebServiceURL.MENU_ITEM_LISTING;

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
                        Toast.makeText(ChefOptionSetsAttToMenuActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsAttToMenuActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("userid",userInfo.getUserId());
                params.put("format","json");
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
        if(menuList.size()>0){
            menuList.clear();
        }
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");

                for (int i = 0; i < parentArr.length(); i++) {

                    //  ChefMenuItem object = new ChefMenuItem();
                    HashMap<String,String> list=new HashMap<>();

                    JSONObject dataObj = parentArr.optJSONObject(i);

                    list.put("item_id",dataObj.optString("item_id"));
                    list.put("item_image",dataObj.optString("item_image"));
                    list.put("item_name",dataObj.optString("item_name"));
                    list.put("sale_price",dataObj.optString("sale_price"));
                    list.put("currency",dataObj.optString("currency"));
                    list.put("normal_price",dataObj.optString("normal_price"));
                    list.put("category",dataObj.optString("category"));


                    System.out.print("item_name : "+dataObj.optString("item_name"));
                    menuList.add(dataObj.optString("item_name"));
                    menuId.add(dataObj.optString("item_id"));
                }
            }else {
                Toast.makeText(ChefOptionSetsAttToMenuActivity.this, "No data", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        adapterMenu.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefOptionSetsAttToMenuActivity.this)){
            startActivity(new Intent(ChefOptionSetsAttToMenuActivity.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
