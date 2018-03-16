package com.chefswithoutlimits.customerchef.activity.chefs.kitchen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.stripeconnect.StripeApp;
import com.chefswithoutlimits.stripeconnect.StripeButton;
import com.chefswithoutlimits.stripeconnect.StripeConnectListener;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.stripeconnect.StripeSession;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;
import com.stripe.Stripe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChefEditKitchen extends Activity {

    TextView headerTxt;
    ImageView btnLogout;
    Button btn_edit_part_1,btn_edit_part_2,btn_edit_part_3;
    ImageView btnBack;
    ImageView imageView2;
    UserInformation userInfo;

    Switch btn_switch;
    TextView tv_status;

    Boolean kitchen_status=false;

    private StripeApp mApp, mApp2;
    private TextView tvSummary;
    private StripeButton mStripeButton, mStripeButton2;
    Boolean connect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_edit_kitchen);
        setwidget();
    }
    public  void setwidget(){

        userInfo = Sharepreferences.getSharePreferance(ChefEditKitchen.this);

        btn_switch=(Switch)findViewById(R.id.btn_switch);
        tv_status=(TextView)findViewById(R.id.tv_status);

        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                userInfo = Sharepreferences.getSharePreferance(ChefEditKitchen.this);
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
        });

        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Kitchen Setting");
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.logOut(ChefEditKitchen.this);
                finish();
            }
        });
        btn_edit_part_1=(Button)findViewById(R.id.btn_edit_part_1);
        btn_edit_part_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnectivity.isConnectedFast(ChefEditKitchen.this)){
                    startActivity(new Intent(ChefEditKitchen.this,ChefEditKitchenPart1.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_edit_part_2=(Button)findViewById(R.id.btn_edit_part_2);
        btn_edit_part_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnectivity.isConnectedFast(ChefEditKitchen.this)){
                    startActivity(new Intent(ChefEditKitchen.this,ChefEditKitchenPart2.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_edit_part_3=(Button)findViewById(R.id.btn_edit_part_3);
        btn_edit_part_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnectivity.isConnectedFast(ChefEditKitchen.this)){
                    startActivity(new Intent(ChefEditKitchen.this,ChefEditKitchenPart3.class));
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
                if(InternetConnectivity.isConnectedFast(ChefEditKitchen.this)){
                    startActivity(new Intent(ChefEditKitchen.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


        /*stripe connect.........*/
        mApp = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
                ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);

        tvSummary = (TextView) findViewById(R.id.tvSummary);
		/*if (mApp.isConnected()) {
			//tvSummary.setText("Connected as " + mApp.getAccessToken());
			tvSummary.setText("Connected as " + mApp.getSUserId());
			System.out.println("Access Token : "+mApp.getAccessToken());
			connect=true;
			Sharepreferences.setRememberStripe(ChefOrderActivity.this, true, "chef", 1);
		}*/

        mStripeButton = (StripeButton) findViewById(R.id.btnConnect1);
        mStripeButton.setStripeApp(mApp);
        mStripeButton.addStripeConnectListener(new StripeConnectListener() {

            @Override
            public void onConnected() {
                //tvSummary.setText("Connected as " + mApp.getAccessToken());
                tvSummary.setText("Connected as " + mApp.getSUserId());
                System.out.println("Access Token : "+mApp.getAccessToken());
                System.out.println("Stripe onConnected : =========================>>");
                connect=true;
                Sharepreferences.setRememberStripe(ChefEditKitchen.this, true, "chef", 1);


                updateStripeUserId(mApp.getSUserId());

            }

            @Override
            public void onDisconnected() {
                tvSummary.setText("Disconnected");
                System.out.println("Stripe onDisconnected : =========================>>");
                connect=false;
                Sharepreferences.setRememberStripe(ChefEditKitchen.this, false, "chef", 1);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChefEditKitchen.this, error, Toast.LENGTH_SHORT).show();
                connect=false;
                System.out.println("Stripe onError : =========================>>");
                Sharepreferences.setRememberStripe(ChefEditKitchen.this, false, "chef", 1);
            }

        });

        mApp2 = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
                ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);
        mStripeButton2 = (StripeButton) findViewById(R.id.btnConnect2);
        mStripeButton2.setStripeApp(mApp2);
        mStripeButton2.setConnectMode(StripeApp.CONNECT_MODE.ACTIVITY);

        Stripe.apiKey = mApp.getAccessToken();




        if(!userInfo.getOffline_stripe_user_id().contains("acct_")){
            mStripeButton.setVisibility(View.VISIBLE);
            connect=false;
        }else {
            mStripeButton.setVisibility(View.GONE);
            tvSummary.setText("Connected as " + userInfo.getOffline_stripe_user_id());
            //System.out.println("Access Token : "+mApp.getAccessToken());
            connect=true;
        }


        getKitchenValue();
    }



    public  void updateStripeUserId(final String offline_stripe_user_id){

        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.UPDATED_USER_STRIPE_ID;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parserStripeUpdateResponse(response,offline_stripe_user_id);

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

                        RememberData rememberData = Sharepreferences.getRememberStripe(ChefEditKitchen.this);
                        if(rememberData.isLogin()){
                            Sharepreferences.setRememberStripe(ChefEditKitchen.this, false, "chef", 0);
                            StripeSession.Stripelogout();
                        }

                        Toast.makeText(ChefEditKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                params.put("offline_stripe_user_id",offline_stripe_user_id);
                params.put("format","json");

                Log.e("user_id",userInfo.getUserId());
                Log.e("offline_stripe_user_id",offline_stripe_user_id);
                Log.e("format","json");

                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();

    }

    public void parserStripeUpdateResponse(String response,String offline_stripe_user_id){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("status");
            if(Status.equalsIgnoreCase("success")) {
                Sharepreferences.updateStripeIdSharePreferance(ChefEditKitchen.this,offline_stripe_user_id);

                mStripeButton.setVisibility(View.GONE);
                //tvSummary.setText("Connected as " + userInfo.getOffline_stripe_user_id());
                //System.out.println("Access Token : "+mApp.getAccessToken());
                //connect=true;

                Toast.makeText(ChefEditKitchen.this, "successfully", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(ChefEditKitchen.this, "Failed", Toast.LENGTH_LONG).show();

                RememberData rememberData = Sharepreferences.getRememberStripe(ChefEditKitchen.this);
                if(rememberData.isLogin()){
                    Sharepreferences.setRememberStripe(ChefEditKitchen.this, false, "chef", 0);
                    StripeSession.Stripelogout();
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public  void changekitchenstatus(final String status){
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchen.this);
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
                        Toast.makeText(ChefEditKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ChefEditKitchen.this, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void getKitchenValue()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefEditKitchen.this);
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
                        Toast.makeText(ChefEditKitchen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
            String kitchenstatus = record.optString("status");
            if (kitchenstatus.equalsIgnoreCase("closed")) {
                btn_switch.setChecked(false);
                tv_status.setText("Kitchen Closed");
            } else {
                btn_switch.setChecked(true);
                tv_status.setText("Kitchen Open");
            }
            kitchen_status=true;
            String distanceunit = record.optString("distance_unit");

            Picasso.with(this)
                    .load(record.optString("logo_image"))
                    .placeholder(R.mipmap.user_image)   // optional
                    .error(R.mipmap.user_image)      // optional
                    .resize(400,400)                        // optional
                    .into(imageView2);


            /**/

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefEditKitchen.this)){
            startActivity(new Intent(ChefEditKitchen.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
