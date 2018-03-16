package com.chefswithoutlimits.customerchef.activity.customer.checkout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.home.HomeActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentResponseActivity extends AppCompatActivity {


    TextView tv_order_message;
    RatingBar rating_bar;
    Button buttonSubmit;
    String SERVER_KEY="AAAA-Eyn37o:APA91bHCjgueNeASFUuBtbrv_F9QvDHB0ZzujUGqhF5kBrlxPu2KmMdvjItmOLE5FPhtfyygc0Ck7eRg9Q04IcOG4CBXAd7c9sVN28DiFDx1JyMM1dqJS_pF3zw-OYvvcLJZValIUD27";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_response);
        TextView headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Payment Response");
        tv_order_message=(TextView)findViewById(R.id.tv_order_message);
        ImageView btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnectivity.isConnectedFast(PaymentResponseActivity.this)){

                    RememberData rememberData = Sharepreferences.getRememberLogin(PaymentResponseActivity.this);
                    if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                        startActivity(new Intent(PaymentResponseActivity.this,CustomerDashboardActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(PaymentResponseActivity.this,HomeActivity.class));
                        finish();
                    }


                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        rating_bar=(RatingBar)findViewById(R.id.rating_bar);
        buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
        //Performing action on Button Click
        buttonSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating=String.valueOf(rating_bar.getRating());

                if(InternetConnectivity.isConnectedFast(PaymentResponseActivity.this)){
                    sendrating(rating);
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }

        });


        Toast.makeText(getApplicationContext(),"Payment Success",Toast.LENGTH_SHORT).show();
        Checkout();
       // sendnotification();
    }


    public  void Checkout(){
        final ProgressDialog progressDialog=new ProgressDialog(PaymentResponseActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.CHECKOUT;

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
                        Toast.makeText(PaymentResponseActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInformation userInfo = Sharepreferences.getSharePreferance(PaymentResponseActivity.this);
                RememberData rememberData = Sharepreferences.getRememberLogin(PaymentResponseActivity.this);
                String customer_id="";
                if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                    customer_id=userInfo.getUserId();
                }else {
                    customer_id=ConstantUtils.sessionUserId;
                }

                Map<String, String>  params = new HashMap<String, String>();
                //ConstantUtils.sessionUserId
                params.put("customer_id", customer_id);
                params.put("kitchenID", Checkout.kitchenID);
                params.put("order_currency",Checkout.order_currency);
                params.put("total_amont",Checkout.total_amont);
                params.put("tip_amount",Checkout.tip_amount);
                params.put("order_status",Checkout.order_status);
                params.put("payment_stat",Checkout.payment_stat);
                params.put("delivery_type",Checkout.delivery_type);
                params.put("order_type",Checkout.order_type);
                params.put("schedule_time",Checkout.schedule_time);
                params.put("schedule_date",Checkout.schedule_date);

                params.put("shipping_first_name",Checkout.shipping_first_name);
                params.put("shipping_middle_name",Checkout.shipping_middle_name);
                params.put("shipping_last_name",Checkout.shipping_last_name);
                params.put("shipping_address",Checkout.shipping_address);

                /*params.put("email",Checkout.email);
                params.put("phone",Checkout.phone);*/
                params.put("shipping_email",Checkout.email);
                params.put("shipping_phone",Checkout.phone);
                params.put("shipping_zip",Checkout.shipping_zip);

                params.put("shipping_country",Checkout.shipping_country);
                params.put("shipping_state",Checkout.shipping_state);
                params.put("shipping_city",Checkout.shipping_city);
                //params.put("shipping_zip",Checkout.shipping_zip);

                params.put("billing_first_name",Checkout.billing_first_name);
                params.put("billing_middle_name",Checkout.billing_middle_name);
                params.put("billing_last_name",Checkout.billing_last_name);
                params.put("billing_email",Checkout.billing_email);
                params.put("billing_address",Checkout.billing_address);
                params.put("billing_phone",Checkout.billing_phone);
                params.put("billing_country",Checkout.billing_country);
                params.put("billing_zip",Checkout.billing_zip);


                params.put("unit_no",Checkout.shipping_unit_no);
                params.put("buzzer_no",Checkout.shipping_buzzer_no);
                params.put("spl_instn",Checkout.shipping_spl_instn);

                params.put("pay_amount",Checkout.pay_amount);
                params.put("escrow_amount",Checkout.escrow_amount);
                params.put("stripe_charge_id",Checkout.stripe_charge_id);
                params.put("format","json");

                //////////////////////////////////////////////////////////////////////////////////////
                System.out.println("@@@@@@@@@@@@\n"+customer_id+" \n  "+Checkout.kitchenID+" \n"+Checkout.payment_stat+"\n"+Checkout.stripe_charge_id);

                Log.e("customer_id",userInfo.getUserId() +"  "+ ConstantUtils.sessionUserId);
                Log.e("kitchenID", Checkout.kitchenID);
                Log.e("order_currency",Checkout.order_currency);
                Log.e("total_amont",Checkout.total_amont);
                Log.e("tip_amount",Checkout.tip_amount);
                Log.e("order_status",Checkout.order_status);
                Log.e("payment_stat",Checkout.payment_stat);
                Log.e("delivery_type",Checkout.delivery_type);
                Log.e("order_type",Checkout.order_type);
                Log.e("schedule_time",Checkout.schedule_time);
                Log.e("schedule_date",Checkout.schedule_date);
                Log.e("shipping_first_name",Checkout.shipping_first_name);
                Log.e("shipping_middle_name",Checkout.shipping_middle_name);
                Log.e("shipping_last_name",Checkout.shipping_last_name);
                Log.e("shipping_address",Checkout.shipping_address);
                Log.e("shipping_email",Checkout.email);
                Log.e("shipping_phone",Checkout.phone);
                Log.e("shipping_country",Checkout.shipping_country);
                Log.e("shipping_state",Checkout.shipping_state);
                Log.e("shipping_city",Checkout.shipping_city);
                Log.e("shipping_zip",Checkout.shipping_zip);
                Log.e("billing_first_name",Checkout.billing_first_name);
                Log.e("billing_middle_name",Checkout.billing_middle_name);
                Log.e("billing_last_name",Checkout.billing_last_name);
                Log.e("billing_email",Checkout.billing_email);
                Log.e("billing_address",Checkout.billing_address);
                Log.e("billing_phone",Checkout.billing_phone);
                Log.e("billing_country",Checkout.billing_country);
                Log.e("billing_zip",Checkout.billing_zip);
                Log.e("pay_amount",Checkout.pay_amount);
                Log.e("escrow_amount",Checkout.escrow_amount);
                Log.e("stripe_charge_id",Checkout.stripe_charge_id);



                System.out.println(Checkout.billing_first_name+" \n  "+Checkout.billing_middle_name+" \n"+Checkout.billing_last_name);
                System.out.println(Checkout.pay_amount+"  %%%%%%%&&&&7%%%%%%%%%%%%%%%%%%  "+Checkout.escrow_amount);
                System.out.println("Checkout.stripe_charge_id === ========>>>  "+Checkout.stripe_charge_id);

                System.out.println("ConstantUtils.sessionUserId === ========>>>  "+ConstantUtils.sessionUserId);

                return params;
            }
        };
        // Adding request to volley request queue
        // Adding request to volley request queue
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                String secrete_key=parentJSON.getString("secrete_key");
                tv_order_message.setText("Order placed successfully \n Please Provide Order Code "+secrete_key+" to Receive Your Order");
                String sortname=parentJSON.getString("sortname");
                String contact=parentJSON.getString("contact");
                //showSuccessDialog("Order placed successfully \n Please Provide Order Code "+secrete_key+" to Receive Your Order");
                Toast.makeText(PaymentResponseActivity.this, "Order placed successfully \n Please Provide Order Code "+secrete_key+" to Receive Your Order", Toast.LENGTH_LONG).show();
               // sendnotification();
                sendSMSToVendor(secrete_key,sortname,contact);
            }else {
                Toast.makeText(PaymentResponseActivity.this, "Failed", Toast.LENGTH_LONG).show();
                CreateDialog.showDialog(PaymentResponseActivity.this,"Failed");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //send sms=============================================

    public  void sendSMSToVendor(final String orderCode,final String sortname,final String contact){
        final ProgressDialog progressDialog=new ProgressDialog(PaymentResponseActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;
        url = WebServiceURL.SEND_SMS;

        //vendor : 'Body' => "ChefsWithoutLimits.com Msg: You have a new order! Please open orders in the App or Online to view & fill the order. Remember to enter the order code from the customer to complete payment.",
        final String message="ChefsWithoutLimits.com Msg: You have a new order! Please open orders in the App or Online to view & fill the order. Remember to enter the order code from the customer to complete payment.";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        // {"response":"success","message":"SMS Successfully Sent"}
                        sendSMSToCustomer(orderCode,sortname);
                        System.out.println("@@@@@@@@@@@@   "+response);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        sendSMSToCustomer(orderCode,sortname);
                        System.out.println("Error=========="+error);
                        Toast.makeText(PaymentResponseActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("mobile",contact);
                params.put("country_id",sortname);
                params.put("message",message);
                params.put("format","json");

                Log.e("mobile",contact);
                Log.e("country_id",sortname);
                Log.e("message",message);
                Log.e("format","json");



                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }


    public  void sendSMSToCustomer(final String orderCode,final String sortname){
        final ProgressDialog progressDialog=new ProgressDialog(PaymentResponseActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;
        url = WebServiceURL.SEND_SMS;

        //cust: 'Body' => "ChefsWithoutLimits.com Msg: Thank you for your order! Please provide Order Code $o_code to the Vendor.",
        final String message="ChefsWithoutLimits.com Msg: Thank you for your order!\nPlease provide Order Code "+orderCode+" to the Vendor.";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        // {"response":"success","message":"SMS Successfully Sent"}
                        sendnotification();
                        System.out.println("@@@@@@@@@@@@   "+response);
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
                        Toast.makeText(PaymentResponseActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("mobile",Checkout.phone);
                params.put("country_id",sortname);
                params.put("message",message);
                params.put("format","json");

                Log.e("mobile",Checkout.phone);
                Log.e("country_id",sortname);
                Log.e("message",message);
                Log.e("format","json");



                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    // send push notification....========================================================================

    public  void sendnotification(){
        final ProgressDialog progressDialog=new ProgressDialog(PaymentResponseActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;
        url = WebServiceURL.SEND_NOTIFICATION;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        //CreateDialog.showDialog(PaymentResponseActivity.this,"Order placed successfully");
                        System.out.println("@@@@@@@@@@@@   "+response);
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
                        Toast.makeText(PaymentResponseActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("apikey",SERVER_KEY);
                params.put("kitchen_id",Checkout.kitchenID);
               // params.put("kitchen_id","147");
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }


    // send rating....========================================================================

    public  void sendrating(final String rating){
        final ProgressDialog progressDialog=new ProgressDialog(PaymentResponseActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;
        url = WebServiceURL.SEND_RATING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        //CreateDialog.showDialog(PaymentResponseActivity.this,"Order placed successfully");
                        Toast.makeText(getApplicationContext(),"Rating Successfully Received", Toast.LENGTH_LONG).show();
                        /*startActivity(new Intent(PaymentResponseActivity.this,CustomerDashboardActivity.class));
                        finish();*/

                        RememberData rememberData = Sharepreferences.getRememberLogin(PaymentResponseActivity.this);
                        if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                            startActivity(new Intent(PaymentResponseActivity.this,CustomerDashboardActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(PaymentResponseActivity.this,HomeActivity.class));
                            finish();
                        }

                        System.out.println("@@@@@@@@@@@@   "+response);
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
                        Toast.makeText(PaymentResponseActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("rating",rating);
                params.put("kitchenID",Checkout.kitchenID);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }


   /* public void sendSMS(){
        String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        String AUTH_TOKEN = "your_auth_token";


            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message
                    .creator(new PhoneNumber("+15558675309"), new PhoneNumber("+14158141829"),
                            "Jenny please?! I love you <3")
                    .setMediaUrl("http://www.example.com/hearts.png")
                    .create();

            System.out.println(message.getSid());

    }*/





    public  void showSuccessDialog(String message){
        final Dialog dialog = new Dialog(PaymentResponseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_success);

        final Button btnDisplay;
        final TextView tv_body;

        btnDisplay = (Button) dialog.findViewById(R.id.btn_ok);
        tv_body=(TextView)dialog.findViewById(R.id.tv_body);
        tv_body.setText(message);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }

        });

        dialog.show();

    }



    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(PaymentResponseActivity.this)){
            RememberData rememberData = Sharepreferences.getRememberLogin(PaymentResponseActivity.this);
            if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                startActivity(new Intent(PaymentResponseActivity.this,CustomerDashboardActivity.class));
                finish();
            }else {
                startActivity(new Intent(PaymentResponseActivity.this,HomeActivity.class));
                finish();
            }

        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
