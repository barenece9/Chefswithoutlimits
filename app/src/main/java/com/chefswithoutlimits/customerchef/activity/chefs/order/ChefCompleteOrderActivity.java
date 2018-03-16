package com.chefswithoutlimits.customerchef.activity.chefs.order;

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
import com.chefswithoutlimits.customerchef.adapter.ChefCompleteOrderAdapter;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChefCompleteOrderActivity extends Activity {

   /* private static final String TAG = NotificationActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;*/
    private TextView txtRegId, txtMessage;
    UserInformation userInfo;
    ArrayList<HashMap<String,String>> alllist;
    ListView listViewNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);

        userInfo = Sharepreferences.getSharePreferance(ChefCompleteOrderActivity.this);
        ImageView imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        TextView headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("COMPLETED ORDERS");
        listViewNewOrder = (ListView)findViewById(R.id.listViewNewOrder);
        ImageView btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefCompleteOrderActivity.this)){
                    startActivity(new Intent(ChefCompleteOrderActivity.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btn_refresh=(Button)findViewById(R.id.btn_refresh);
        btn_refresh.setVisibility(View.GONE);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getorderValue();
            }
        });

        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);



        getorderValue();
    }











    void getorderValue()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ChefCompleteOrderActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.CHEF_ORDER_LISTING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseData(response);
                        System.out.println("@@@@@@@@@@@@@ "+response);
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
                        Toast.makeText(ChefCompleteOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefCompleteOrderActivity.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("userid",userInfo.getUserId());
                //params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }


    public void parseData(String response)
    {
        try {

            alllist=new ArrayList<>();
            JSONObject parentJSON = new JSONObject(response);
            JSONObject innerobj=parentJSON.getJSONObject("output");

            String Status=innerobj.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                JSONArray parentArr = innerobj.optJSONArray("Records");

                alllist.clear();

                for (int i = 0; i < parentArr.length(); i++) {

                    //ChefNewOrderItem object = new ChefNewOrderItem();
                    HashMap<String, String> list = new HashMap<>();

                    JSONObject dataObj = parentArr.getJSONObject(i);

                    list.put("id", dataObj.optString("id"));
                    list.put("invoiceno", dataObj.optString("invoiceno"));
                    list.put("secrete_key", dataObj.optString("secrete_key"));
                    list.put("order_currency", dataObj.optString("order_currency"));
                    list.put("total_amont", dataObj.optString("total_amont"));
                    list.put("discount", dataObj.optString("discount"));
                    list.put("discount_coupon_id", dataObj.optString("discount_coupon_id"));
                    list.put("order_status", dataObj.optString("order_status"));
                    list.put("datetime_added", dataObj.optString("datetime_added"));
                    list.put("datetime_modified", dataObj.optString("datetime_modified"));
                    list.put("delivery_type", dataObj.optString("delivery_type"));
                    list.put("payment_stat", dataObj.optString("payment_stat"));
                    list.put("order_type", dataObj.optString("order_type"));
                    list.put("schedule_time", dataObj.optString("schedule_time"));
                    list.put("schedule_date", dataObj.optString("schedule_date"));
                    if (dataObj.optString("order_status").equalsIgnoreCase("delivered")) {
                        alllist.add(list);
                    }

                }
            }else {
                Toast.makeText(ChefCompleteOrderActivity.this, "No order", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //adapter.notifyDataSetChanged();
        listViewNewOrder.setAdapter(new ChefCompleteOrderAdapter(ChefCompleteOrderActivity.this,alllist));
    }

    public void yourDesiredMethod(String id)
    {

        if(InternetConnectivity.isConnectedFast(ChefCompleteOrderActivity.this)){
            Intent intent=new Intent(ChefCompleteOrderActivity.this,ChefOrderDetailsActivity.class);
            intent.putExtra("id",id);
            System.out.println("############ id : "+id);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefCompleteOrderActivity.this)){
            startActivity(new Intent(ChefCompleteOrderActivity.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
