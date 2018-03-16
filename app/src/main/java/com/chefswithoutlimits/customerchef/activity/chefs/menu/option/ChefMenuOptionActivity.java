package com.chefswithoutlimits.customerchef.activity.chefs.menu.option;

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
import com.chefswithoutlimits.customerchef.activity.chefs.menu.ChefMenuActivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.adapter.ChefMenuOptionAdapter;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefMenuOptionActivity extends Activity {

    ImageView imageView2;
    TextView headerTxt;
    Button btnaddnew;
    ListView list_view;
    ImageView btnBack;


    UserInformation userInfo;
    ArrayList<HashMap<String,String>> menucategoryitem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_menu_option_item);
        setwidget();
    }
    public void setwidget(){

        userInfo = Sharepreferences.getSharePreferance(this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Menu Option");
        btnaddnew=(Button)findViewById(R.id.btnaddnew);
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefMenuOptionActivity.this)){
                    startActivity(new Intent(ChefMenuOptionActivity.this,ChefAddMenuOption.class));
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
                startActivity(new Intent(ChefMenuOptionActivity.this,ChefMenuActivity.class));
                finish();
            }
        });
        list_view=(ListView)findViewById(R.id.list_view);
        getMenuOptionItem();
    }

    void getMenuOptionItem()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefMenuOptionActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_OPTION_LISTING;

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
                        Toast.makeText(ChefMenuOptionActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefMenuOptionActivity.this);
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

    public void parseData(String response)
    {
        if(menucategoryitem.size()>0)
            menucategoryitem.clear();
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");

                for (int i = 0; i < parentArr.length(); i++) {

                  //  ChefMenuItem object = new ChefMenuItem();
                    HashMap<String,String> list=new HashMap<>();

                    JSONObject dataObj = parentArr.optJSONObject(i);
                    //JSONObject dataObj = childObj.optJSONObject("records");

                    list.put("item_id",dataObj.optString("item_id"));
                    list.put("item_name",dataObj.optString("item_name"));
                    list.put("menu_id",dataObj.optString("menu_id"));
                    list.put("menu_name",dataObj.optString("menu_name"));
                    list.put("item_status",dataObj.optString("item_status"));
                    list.put("item_sale_price",dataObj.optString("item_sale_price"));
                    list.put("item_regular_price",dataObj.optString("item_regular_price"));

                    menucategoryitem.add(list);
                }
            }else {
                Toast.makeText(ChefMenuOptionActivity.this, "No data", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        list_view.setAdapter(new ChefMenuOptionAdapter(ChefMenuOptionActivity.this, menucategoryitem));
    }

    public void yourDesiredMethod(final String item_id){

        final ProgressDialog progressDialog=new ProgressDialog(ChefMenuOptionActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.DELETE_MENU_OPTION;

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
                        Toast.makeText(ChefMenuOptionActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                //params.put("user_id",userInfo.getUserId());
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

    public void parseDeleteData(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("status");
            if(Status.equalsIgnoreCase("success")) {
                //String message=parentJSON.getString("message");
                Toast.makeText(ChefMenuOptionActivity.this, "Menu Option deleted successfully", Toast.LENGTH_LONG).show();
                getMenuOptionItem();
            }else {
                Toast.makeText(ChefMenuOptionActivity.this, "Failed to Delete", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChefMenuOptionActivity.this,ChefMenuActivity.class));
        finish();
    }
}
