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
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.adapter.ChefItemListAdapter;
import com.chefswithoutlimits.customerchef.dataVO.ChefOredrItem;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefOrderItem extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	ListView listViewOrderItem;

	private ProgressDialog progressDialog;

	String orderId = "";

	ArrayList<ChefOredrItem> orderItemData = new ArrayList<ChefOredrItem>();
	ChefItemListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_order_item);
		orderId = getIntent().getStringExtra("itemId");
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
		headerTxt.setText("ORDER");

		listViewOrderItem = (ListView)findViewById(R.id.listViewOrderItem);
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
		progressDialog=new ProgressDialog(ChefOrderItem.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CHEF_CUSTOMERORDERDETAIL;

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
						Toast.makeText(ChefOrderItem.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("oredrid",orderId);				
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

			for(int i=1;i<parentArr.length();i++){

				ChefOredrItem object = new ChefOredrItem();

				JSONObject childObj = parentArr.optJSONObject(i);
				JSONObject dataObj = childObj.optJSONObject("records");

				object.setItemId(dataObj.optString("item_id"));
				object.setItemImage(dataObj.optString("item_image"));
				object.setItemName(dataObj.optString("item_name"));
				object.setItemPrice(dataObj.optString("normal_price"));
				object.setItemCurrency(dataObj.optString("item_currency"));
				object.setItemAvailable(dataObj.optString("total_avail"));
				object.setItemQuantity(dataObj.optString("quantity"));

				orderItemData.add(object);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	public void setAdapter()
	{
		adapter = new ChefItemListAdapter(this, orderItemData);
		listViewOrderItem.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
