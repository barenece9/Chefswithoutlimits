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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

/**
 * Created by db on 2/27/2018.
 */

public class ChefOptionSetsAddActivity extends Activity {

    TextView headerTxt;
    ImageView btnLogout;
    Button btn_close,btn_submit,btn_add,btn_subtract;
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
    List<EditText> allNameEds = new ArrayList<EditText>();
    List<EditText> allPriceEds = new ArrayList<EditText>();
    List<Spinner> allSpinnerEds = new ArrayList<Spinner>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_add_option_sets);



        progressDialog=new ProgressDialog(ChefOptionSetsAddActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsAddActivity.this);
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
                Logout.logOut(ChefOptionSetsAddActivity.this);
                finish();
            }
        });
        btn_add=(Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ids.add(ids.size()+1);//1,2,3
                addView(ids.size());
            }
        });

        btn_subtract=(Button)findViewById(R.id.btn_subtract);
        btn_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ids.size()>1) {
                    removeView(ids.size());
                    ids.remove(ids.size() - 1);
                }
            }
        });
        ll_parent_programmatically = (LinearLayout) findViewById(R.id.ll_parent_programmatically);
        etn_option_set_name=(EditText)findViewById(R.id.etn_option_set_name);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option_set_name=etn_option_set_name.getText().toString();

                String allData="";
                for(int i=0;i<ids.size();i++){
                    if(allData.equalsIgnoreCase("")){
                        allData = allNameEds.get(i).getText().toString();
                    }else {
                        allData = allData+"***"+allNameEds.get(i).getText().toString();
                    }
                    allData =allData+"@"+allPriceEds.get(i).getText().toString();

                    if(allSpinnerEds.get(i).getSelectedItem().toString().equalsIgnoreCase("Available")){
                        allData =allData+"@"+"A";
                    }else {
                        allData =allData+"@"+"IA";
                    }

                    System.out.print("AllData : "+allData);
                    Log.e("AllData",allData);
                }


                if(option_set_name.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"set name can not be blank", Toast.LENGTH_SHORT).show();
                }else if(allNameEds.get(0).getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"option name can not be blank", Toast.LENGTH_SHORT).show();
                }else if(!InternetConnectivity.isConnectedFast(ChefOptionSetsAddActivity.this)){
                    Toast.makeText(getApplicationContext(),"check your internet connection", Toast.LENGTH_SHORT).show();
                }else {
                    System.out.print("AllData : "+allData);
                    Log.e("AllData",allData);
                    addOptionSet(option_set_name,allData);
                }

            }
        });

        btn_close=(Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnectivity.isConnectedFast(ChefOptionSetsAddActivity.this)){
                    startActivity(new Intent(ChefOptionSetsAddActivity.this,ChefOptionSetsActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefOptionSetsAddActivity.this)){
                    startActivity(new Intent(ChefOptionSetsAddActivity.this,ChefOptionSetsActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ids.add(ids.size()+1);
        addView(ids.size());

        /*for(int i=0;i<=2;i++) {

            LinearLayout ll_parent_child = new LinearLayout(this);
            ll_parent_child.setId(i);
            ll_parent_child.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams ll_parms_child = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll_parms_child.gravity = Gravity.CENTER_VERTICAL;
            ll_parms_child.setMargins(0, 5, 0, 0);
            ll_parent_child.setLayoutParams(ll_parms_child);



            EditText option_name = new EditText(this);
            option_name.setHint("Option Name");
            option_name.setTextColor(Color.parseColor("#795548"));
            option_name.setTypeface(Typeface.DEFAULT_BOLD);
            option_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            ll_parent_child.addView(option_name);


            EditText option_price = new EditText(this);
            option_price.setHint("Price if any");
            option_price.setTextColor(Color.parseColor("#898C88"));
            option_price.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            option_price.setPadding(5,0,0,0);
            ll_parent_child.addView(option_price);


            Spinner spinner = new Spinner(this);
            ArrayList<String> spinnerArray = new ArrayList<String>();
            spinnerArray.add("Available");
            spinnerArray.add("Not Available");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setPadding(5,0,0,0);
            spinner.setAdapter(spinnerArrayAdapter);
            ll_parent_child.addView(spinner);


            ll_parent_programmatically.addView(ll_parent_child);
        }*/

    }

    public void addView(int id){
        LinearLayout ll_parent_child = new LinearLayout(this);
        ll_parent_child.setId(id);
        ll_parent_child.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams ll_parms_child = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_parms_child.gravity = Gravity.CENTER_VERTICAL;
        ll_parms_child.setMargins(0, 5, 0, 0);
        ll_parent_child.setLayoutParams(ll_parms_child);



        EditText option_name = new EditText(this);
        allNameEds.add(option_name);
        option_name.setId(id);
        option_name.setHeight(50);
        option_name.setWidth(200);
        option_name.setHint("Option Name");
        option_name.setTextColor(Color.parseColor("#795548"));
        option_name.setTypeface(Typeface.DEFAULT_BOLD);
        option_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        option_name.setPadding(5,0,0,0);
        option_name.setBackgroundResource(R.drawable.edit_bg);
        ll_parent_child.addView(option_name);


        EditText option_price = new EditText(this);
        allPriceEds.add(option_price);
        option_price.setId(id);
        option_price.setHeight(50);
        option_price.setWidth(180);
        option_price.setHint("Price if any");
        option_price.setTextColor(Color.parseColor("#898C88"));
        option_price.setTypeface(Typeface.DEFAULT_BOLD);
        option_price.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        option_price.setPadding(5,0,0,0);
        option_price.setBackgroundResource(R.drawable.edit_bg);
        ll_parent_child.addView(option_price);


        Spinner spinner = new Spinner(this);
        allSpinnerEds.add(spinner);
        spinner.setId(id);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Available");
        spinnerArray.add("Not Available");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setPadding(5,0,0,0);
        spinner.setAdapter(spinnerArrayAdapter);
       // spinner.setBackgroundResource(R.drawable.edit_bg);
        ll_parent_child.addView(spinner);


        ll_parent_programmatically.addView(ll_parent_child);
    }

    public void removeView(int id){
        ll_parent_programmatically.removeViewAt(id-1);
    }


    public void addOptionSet(final String option_set_name,final String allData){
        progressDialog.show();
        String url;

        url = WebServiceURL.OPTION_SET_ADD;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseAddData(response);
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
                        Toast.makeText(ChefOptionSetsAddActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefOptionSetsAddActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                /*set_name:XAOMI  all_data:y1@100@A***Z1@200@IA  userID:85  action:edit  itemID:1  format:json*/

                params.put("userID",userInfo.getUserId());
                params.put("set_name",option_set_name);
                params.put("all_data",allData);
                params.put("action","add");
                params.put("itemID","");
                params.put("format","json");

                Log.e("userID",userInfo.getUserId());
                Log.e("set_name",option_set_name);
                Log.e("all_data",allData);
                Log.e("action","add");
                Log.e("itemID","");
                Log.e("format","json");

                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parseAddData(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                Toast.makeText(ChefOptionSetsAddActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                if(InternetConnectivity.isConnectedFast(ChefOptionSetsAddActivity.this)){
                    startActivity(new Intent(ChefOptionSetsAddActivity.this,ChefOptionSetsActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(ChefOptionSetsAddActivity.this, "Failed to add", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefOptionSetsAddActivity.this)){
            startActivity(new Intent(ChefOptionSetsAddActivity.this,ChefOptionSetsActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
