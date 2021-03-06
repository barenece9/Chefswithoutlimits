package com.chefswithoutlimits.customerchef.activity.chefs.menu.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChefEditMenuCategory extends Activity {

    ImageView imageView2;
    TextView headerTxt;
    Button add_category;
    EditText etn_menu_cat;
    String menucategory="";
    UserInformation userInfo;
    String menu_catid="",cat_name="";
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_edit_menu_category);
        Bundle bundle = getIntent().getExtras();
        menu_catid = bundle.getString("menu_catid");
        cat_name = bundle.getString("cat_name");
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
        headerTxt.setText("Edit Menu Category");
        etn_menu_cat=(EditText)findViewById(R.id.etn_menu_cat);
        etn_menu_cat.setText(cat_name);
        add_category=(Button)findViewById(R.id.add_category);
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menucategory=etn_menu_cat.getText().toString();
                if(menucategory.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ChefEditMenuCategory.this, "Enter Menu category");
                }else {
                    if(InternetConnectivity.isConnectedFast(ChefEditMenuCategory.this)){
                        editmenucategory();
                    }else {
                        Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefEditMenuCategory.this)){
                    startActivity(new Intent(ChefEditMenuCategory.this,ChefMenuCategoryActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public  void editmenucategory(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditMenuCategory.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.EDIT_MENU_CATEGORY;

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
                        Toast.makeText(ChefEditMenuCategory.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefEditMenuCategory.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                params.put("cat_name",menucategory);
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

    public void parseData(String response)
    {
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                Toast.makeText(ChefEditMenuCategory.this, "Menu Category Edited successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,ChefMenuCategoryActivity.class));
                finish();
            }else {
                Toast.makeText(ChefEditMenuCategory.this, "Failed to edit menu category", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditMenuCategory.this)){
            startActivity(new Intent(ChefEditMenuCategory.this,ChefMenuCategoryActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
