package com.chefswithoutlimits.customerchef.activity.chefs.menu.category;

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
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.ChefMenuActivity;
import com.chefswithoutlimits.customerchef.adapter.ChefMenuCatAdapter;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefMenuCategoryActivity extends Activity {

    TextView headerTxt;
    Button btnaddnew;
    ListView list_view;
    ImageView btnBack;
    ImageView imageView2;

    private ProgressDialog progressDialog;
    String userId = "";

    UserInformation userInfo;
    ArrayList<HashMap<String,String>> menucategoryitem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_menu_category);
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
        headerTxt.setText("Menu Category");
        btnaddnew=(Button)findViewById(R.id.btnaddnew);
        btnaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefMenuCategoryActivity.this)){
                    startActivity(new Intent(ChefMenuCategoryActivity.this,ChefAddMenuCategory.class));
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
                startActivity(new Intent(ChefMenuCategoryActivity.this,ChefMenuActivity.class));
                finish();
            }
        });
        list_view=(ListView)findViewById(R.id.list_view);
        getMenuCategoryItem();
    }

    void getMenuCategoryItem()
    {
        progressDialog=new ProgressDialog(ChefMenuCategoryActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.MENU_CATEGORY_LISTING;

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
                        Toast.makeText(ChefMenuCategoryActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefMenuCategoryActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
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
        if(menucategoryitem.size()>0)
            menucategoryitem.clear();
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArr = parentJSON.optJSONArray("Records");

                for (int i = 0; i < parentArr.length(); i++) {

                    HashMap<String,String> list=new HashMap<>();

                    JSONObject dataObj = parentArr.optJSONObject(i);
                    list.put("menu_catid",dataObj.optString("menu_catid"));
                    list.put("cat_name",dataObj.optString("cat_name"));

                    menucategoryitem.add(list);
                }
            }else {
                Toast.makeText(ChefMenuCategoryActivity.this, "No data", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        list_view.setAdapter(new ChefMenuCatAdapter(ChefMenuCategoryActivity.this, menucategoryitem));
    }

    public void yourDesiredMethod(final String menu_catid){

        final ProgressDialog progressDialog=new ProgressDialog(ChefMenuCategoryActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.DELETE_MENU_CATEGORY;

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
                        Toast.makeText(ChefMenuCategoryActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefMenuCategoryActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                params.put("menu_catid",menu_catid);
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
                //String message=parentJSON.getString("message");
                Toast.makeText(ChefMenuCategoryActivity.this, "Menu Category deleted successfully", Toast.LENGTH_LONG).show();
                getMenuCategoryItem();
            }else {
                Toast.makeText(ChefMenuCategoryActivity.this, "Failed to Delete", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChefMenuCategoryActivity.this,ChefMenuActivity.class));
        finish();
    }
}
