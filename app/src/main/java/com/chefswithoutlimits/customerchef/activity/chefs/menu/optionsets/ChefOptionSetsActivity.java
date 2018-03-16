package com.chefswithoutlimits.customerchef.activity.chefs.menu.optionsets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.adapter.ChefMenuOptionSetAdapter;
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

public class ChefOptionSetsActivity extends Activity implements View.OnClickListener{

    TextView headerTxt;
    ImageView btnLogout;
    Button add_option_set,att_to_menu_item,att_to_category;
    ImageView btnBack;
    ImageView imageView2;
    UserInformation userInfo;
    ProgressDialog progressDialog;
    ListView list_view;
    ArrayList<HashMap<String,String>> setsName = new ArrayList<>();
    ArrayList<HashMap<String,String>> optionName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_option_sets);



        progressDialog=new ProgressDialog(ChefOptionSetsActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsActivity.this);
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
        btnLogout.setOnClickListener(this);
        att_to_category=(Button)findViewById(R.id.att_to_category);
        att_to_category.setOnClickListener(this);
        att_to_menu_item=(Button)findViewById(R.id.att_to_menu_item);
        att_to_menu_item.setOnClickListener(this);
        add_option_set=(Button)findViewById(R.id.add_option_set);
        add_option_set.setOnClickListener(this);
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefOptionSetsActivity.this)){
                    startActivity(new Intent(ChefOptionSetsActivity.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getOptionSets();

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {
            case R.id.add_option_set:
                if(InternetConnectivity.isConnectedFast(ChefOptionSetsActivity.this)){

                    startActivity(new Intent(ChefOptionSetsActivity.this,ChefOptionSetsAddActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.att_to_menu_item:

                if(InternetConnectivity.isConnectedFast(ChefOptionSetsActivity.this)){
                    startActivity(new Intent(ChefOptionSetsActivity.this,ChefOptionSetsAttToMenuActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.att_to_category:

                if(InternetConnectivity.isConnectedFast(ChefOptionSetsActivity.this)){

                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLogout:
                Logout.logOut(this);
                finish();
                break;
        }
    }



    public void getOptionSets()
    {
        progressDialog.show();
        String url;

        url = WebServiceURL.OPTION_SET_LISTING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseOptionSetData(response);
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
                        Toast.makeText(ChefOptionSetsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("userID",userInfo.getUserId());
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseOptionSetData(String response)
    {
        setsName.clear();
        optionName.clear();
        ArrayList<String> setIds=new ArrayList<>();

        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArr = parentJSON.optJSONArray("data");

                for (int i = 0; i < parentArr.length(); i++) {

                    HashMap<String,String> list=new HashMap<>();

                    JSONObject dataObj = parentArr.optJSONObject(i);
                    String set_id=dataObj.optString("set_id");

                    list.put("set_id",dataObj.optString("set_id"));
                    list.put("set_name",dataObj.optString("set_name"));

                    list.put("option_item_id",dataObj.optString("option_item_id"));
                    list.put("option_name",dataObj.optString("option_name"));

                    list.put("selection_type",dataObj.optString("selection_type"));
                    list.put("item_status",dataObj.optString("item_status"));

                    list.put("opt_reg_price",dataObj.optString("opt_reg_price"));
                    list.put("opt_sale_price",dataObj.optString("opt_sale_price"));

                    if(!setIds.contains(set_id)){
                        setsName.add(list);
                        setIds.add(set_id);
                    }
                    optionName.add(list);
                }
            }else {
                Toast.makeText(ChefOptionSetsActivity.this, "No data", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        list_view.setAdapter(new ChefMenuOptionSetAdapter(ChefOptionSetsActivity.this, setsName,optionName));
    }

    public void editSet(final String set_id,final String set_name){
        Intent intent=new Intent(ChefOptionSetsActivity.this,ChefOptionSetsEditActivity.class);
        intent.putExtra("set_id",set_id);
        intent.putExtra("set_name",set_name);
        startActivity(intent);
        finish();
    }
    public void deleteSet(final String set_id){
        progressDialog.show();
        String url;

        url = WebServiceURL.OPTION_SET_DELETE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseDeleteData(response);
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
                        Toast.makeText(ChefOptionSetsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("userID",userInfo.getUserId());
                params.put("set_id",set_id);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parseDeleteData(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                getOptionSets();
                Toast.makeText(ChefOptionSetsActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(ChefOptionSetsActivity.this, "Failed to delete", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefOptionSetsActivity.this)){
            startActivity(new Intent(ChefOptionSetsActivity.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
