package com.chefswithoutlimits.customerchef.activity.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.adapter.ChefNewOrderAdapter;
import com.chefswithoutlimits.customerchef.dataVO.ChefNewOrderItem;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefNewOrderActivity extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	ListView listViewNewOrder;

	private ProgressDialog progressDialog;
	UserInformation userInfo;
	ArrayList<ChefNewOrderItem> orderItemData = new ArrayList<ChefNewOrderItem>();
	ChefNewOrderAdapter adapter;
	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_neworder);
		userInfo = Sharepreferences.getSharePreferance(this);

		setWidget();
		getorderValue();
		setAdapter();
	}

	public void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("NEW ORDER");
		textView1 = (TextView)findViewById(R.id.textView1);
		textView1.setText("NEW ORDER");

		listViewNewOrder = (ListView)findViewById(R.id.listViewNewOrder);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {


		case R.id.btnBack:

			finish();

			break;

		case R.id.btnLogout:

			Logout.logOut(this);
			finish();
			break;	



		}
	}

	//========================================= Get Order value =========================================

	void getorderValue()
	{
		progressDialog=new ProgressDialog(ChefNewOrderActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CHEF_ORDERS;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {

				parseData(response);
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
						Toast.makeText(ChefNewOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("userid",userInfo.getUserId());				
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}


	public void parseData(String response)
	{
		try {

			JSONObject parentJSON = new JSONObject(response);
			JSONArray parentArr = parentJSON.optJSONArray("output");

			for(int i=0;i<parentArr.length();i++){

				ChefNewOrderItem object = new ChefNewOrderItem();

				JSONObject childObj = parentArr.optJSONObject(i);
				JSONObject dataObj = childObj.optJSONObject("records");
				
				if(dataObj !=null){
					
					object.setOrderId(dataObj.optString("id"));
					object.setInvoiceNo(dataObj.optString("invoiceno"));
					object.setSecreteKey(dataObj.optString("secrete_key"));
					object.setCustomerId(dataObj.optString("customer_id"));
					object.setCurrency(dataObj.optString("order_currency"));
					object.setItemPrice(dataObj.optString("total_amont"));
					object.setItemDiscount(dataObj.optString("discount"));
					object.setDicountCoupnId(dataObj.optString("discount_coupon_id"));
					object.setOrderStatus(dataObj.optString("order_status"));
					object.setOrderDate(dataObj.optString("datetime_added"));

					orderItemData.add(object);
				}else{
					
					String message = childObj.getString("message");	
					Toast.makeText(ChefNewOrderActivity.this, message, Toast.LENGTH_LONG).show();
				}

				
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	//==================================== Set Adapter =====================================

	private void setAdapter()
	{
		adapter = new ChefNewOrderAdapter(this,orderItemData);
		listViewNewOrder.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
