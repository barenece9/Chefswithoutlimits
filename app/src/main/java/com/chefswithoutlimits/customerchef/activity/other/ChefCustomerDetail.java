package com.chefswithoutlimits.customerchef.activity.other;

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
import android.widget.TextView;
import android.widget.Toast;

public class ChefCustomerDetail extends Activity implements OnClickListener{

	TextView txtCustomerFName;
	TextView txtCustomerMName;
	TextView txtCustomerLName;
	TextView txtCustomerEmail;
	TextView txtCustomerContact;
	TextView txtCustomerCustomertype;
	private ProgressDialog progressDialog;
	
	public String customerId="";
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_customerdetail);
		
		customerId = getIntent().getStringExtra("customerId");

		setWidget();
		getCustomerValue();
	}

	private void setWidget()
	{
		txtCustomerFName = (TextView)findViewById(R.id.txtCustomerFName);
		txtCustomerMName = (TextView)findViewById(R.id.txtCustomerMName);
		txtCustomerLName = (TextView)findViewById(R.id.txtCustomerLName);
		txtCustomerEmail = (TextView)findViewById(R.id.txtCustomerEmail);
		txtCustomerContact = (TextView)findViewById(R.id.txtCustomerContact);
		txtCustomerCustomertype = (TextView)findViewById(R.id.txtCustomerCustomertype);
		
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("CUSTOMER DETAIL");
	}
	
	//========================================= Get Customer detail =========================================
	
			void getCustomerValue()
			{
				progressDialog=new ProgressDialog(ChefCustomerDetail.this);
				progressDialog.setMessage("loading...");
				progressDialog.show(); 
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				String url;

				url = WebServiceURL.CHEF_CUSTOMERDETAIL;

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
								Toast.makeText(ChefCustomerDetail.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
							}
						}
						){     
					@Override
					protected Map<String, String> getParams()
					{   						
						Map<String, String>  params = new HashMap<String, String>(); 	
						
						params.put("customer_id",customerId);				
						params.put("format","json");
						return params;
					}
				};
				// Adding request to volley request queue
				AppController.getInstance().addToRequestQueue(postRequest);
			}
			
			
			private void parseData(String response)
			{
				JSONObject parentJSON;
				try {
					
					parentJSON = new JSONObject(response);
					JSONArray parentArr = parentJSON.optJSONArray("output");
					
					JSONObject child = parentArr.getJSONObject(0);
					JSONObject record = child.getJSONObject("records");
					
					txtCustomerFName.setText(record.optString("FirstName"));
					txtCustomerMName.setText(record.optString("MidName"));
					txtCustomerLName.setText(record.optString("LastName"));
					txtCustomerEmail.setText(record.optString("email"));
					txtCustomerContact.setText(record.optString("contact"));
					txtCustomerCustomertype.setText(record.optString("usertype"));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
