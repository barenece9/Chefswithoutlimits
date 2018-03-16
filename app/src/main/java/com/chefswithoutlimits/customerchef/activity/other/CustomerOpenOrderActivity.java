package com.chefswithoutlimits.customerchef.activity.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.customer.order.CustomerOrderDetailsActivity;
import com.chefswithoutlimits.customerchef.adapter.ChefNewOrderAdapter;
import com.chefswithoutlimits.customerchef.dataVO.ChefNewOrderItem;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.stripeconnect.StripeApp;
import com.chefswithoutlimits.stripeconnect.StripeButton;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.adapter.CustomerOpenOrderAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerOpenOrderActivity extends Activity implements OnClickListener{

	ImageView imageView2;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	ListView listViewNewOrder;

	private ProgressDialog progressDialog;
	UserInformation userInfo;
	ArrayList<ChefNewOrderItem> orderItemData = new ArrayList<ChefNewOrderItem>();
	ChefNewOrderAdapter adapter;
	ArrayList<HashMap<String,String>> alllist;


	private StripeApp mApp, mApp2;
	private TextView tvSummary;
	private StripeButton mStripeButton, mStripeButton2;
	Boolean connect=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customer_open_order);


		setWidget();
		getorderValue();
		//setAdapter();

	}

	public void setWidget()
	{
		userInfo = Sharepreferences.getSharePreferance(CustomerOpenOrderActivity.this);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+ userInfo.getUser_log())
				.placeholder(R.drawable.ic_person_black_24dp)   // optional
				.error(R.drawable.ic_person_black_24dp)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		TextView txtCusName = (TextView)findViewById(R.id.txtCusName);
		UserInformation userInfo = Sharepreferences.getSharePreferance(this);
		txtCusName.setText(userInfo.getFirstName()+" "+userInfo.getLastName());

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("OPEN ORDER");

		listViewNewOrder = (ListView)findViewById(R.id.listViewNewOrder);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {


		case R.id.btnBack:

			if(InternetConnectivity.isConnectedFast(CustomerOpenOrderActivity.this)){
				startActivity(new Intent(CustomerOpenOrderActivity.this,CustomerDashboardActivity.class));
				finish();
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

	//========================================= Get Order value =========================================

	void getorderValue()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CustomerOpenOrderActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CUSTOMER_ORDER_LIST;

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
						Toast.makeText(CustomerOpenOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CustomerOpenOrderActivity.this);
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("customer_id",userInfo.getUserId());
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

			alllist=new ArrayList<>();
			JSONObject parentJSON = new JSONObject(response);
			//JSONObject innerobj=parentJSON.getJSONObject("output");

			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				JSONArray parentArr = parentJSON.optJSONArray("ordersList");

				alllist.clear();

				for (int i = parentArr.length()-1; i >= 0; i--) {

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


					String order_status=dataObj.optString("order_status");
					if(order_status.equalsIgnoreCase("pending")){
						alllist.add(list);
					}


				}
			}else {
				Toast.makeText(CustomerOpenOrderActivity.this, "No order", Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//adapter.notifyDataSetChanged();
		listViewNewOrder.setAdapter(new CustomerOpenOrderAdapter(CustomerOpenOrderActivity.this,alllist));
	}

	//==================================== Set Adapter =====================================


	public void yourDesiredMethod(String id)
	{
		Intent intent=new Intent(CustomerOpenOrderActivity.this,CustomerOrderDetailsActivity.class);
		intent.putExtra("id",id);
		intent.putExtra("activity","CustomerOpenOrderActivity");
		System.out.println("############ id : "+id);
		startActivity(intent);
		finish();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(resultCode) {
			case StripeApp.RESULT_CONNECTED:
				//tvSummary.setText("Connected as " + mApp.getAccessToken());
				tvSummary.setText("Connected as " + mApp.getSUserId());
				System.out.println("Access Token : "+mApp.getAccessToken());
				break;
			case StripeApp.RESULT_ERROR:
				String error_description = data.getStringExtra("error_description");
				Toast.makeText(CustomerOpenOrderActivity.this, error_description, Toast.LENGTH_SHORT).show();
				break;
		}

	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(CustomerOpenOrderActivity.this)){
			startActivity(new Intent(CustomerOpenOrderActivity.this,CustomerDashboardActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}
	}
}
