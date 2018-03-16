package com.chefswithoutlimits.customerchef.activity.chefs.registration;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.customerchef.activity.home.LoginActivity;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.util.InternetConnectivity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChefEmailVerifyActivity extends Activity{

	Button buttonVeryfy;
	EditText editTextCode;
	String code="";
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.emailvarify_activity);
		
		editTextCode = (EditText)findViewById(R.id.editTextCode);
		
		buttonVeryfy = (Button)findViewById(R.id.buttonVeryfy);
		buttonVeryfy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				
				code = editTextCode.getText().toString();
				if(!code.equalsIgnoreCase("")){
					if(InternetConnectivity.isConnectedFast(ChefEmailVerifyActivity.this)){
						doEmailVerify(code);
					}else {
						Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(getApplicationContext(),"please enter code",Toast.LENGTH_SHORT).show();
				}



			}
		});
		
	}

	//================================== Email Verification ====================================
	
	void doEmailVerify(final String token)
	{
		progressDialog=new ProgressDialog(ChefEmailVerifyActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CONFIRM_REG;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseResponse(response);
				
				
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
						Toast.makeText(ChefEmailVerifyActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 				
				params.put("emailtoken",token);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		//AppController.getInstance().addToRequestQueue(postRequest);
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}
	
	void parseResponse(String response){	

		String message = "";
		String verifyStatus="";
		System.out.println("Response :"+response);
		try {

			JSONObject parentObject = new JSONObject(response);
			message = parentObject.getString("message");
			verifyStatus=parentObject.getString("verifyStatus");


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

		if(verifyStatus.equalsIgnoreCase("1")){

			startActivity(new Intent(ChefEmailVerifyActivity.this,LoginActivity.class));
			finish();
		}

		Toast.makeText(ChefEmailVerifyActivity.this, ""+message, Toast.LENGTH_LONG).show();
	}
	
}
