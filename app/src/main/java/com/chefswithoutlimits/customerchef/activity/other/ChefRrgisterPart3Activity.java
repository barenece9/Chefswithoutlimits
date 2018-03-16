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
import com.chefswithoutlimits.customerchef.activity.chefs.registration.ChefEmailVerifyActivity;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.WebServiceURL;


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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefRrgisterPart3Activity extends Activity implements OnClickListener{

	EditText custmerEdtuser;
	EditText custmerEdtpass;
	Button btnRegister;
	ImageView btnRegisterStep3Pre;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	EditText chefAddress1Part3;
	EditText chefRegAddress2Part3;
	EditText chefRegStreetAddressPart3;
	EditText chefRegAptPart3;
	EditText chefRegCityPart3;
	EditText chefRegStatePart3;	
	EditText chefRegZipPart3;

	Spinner chefRegSpiCountryPart3;

	

	String chefAddress1 = "";
	String chefAddress2 = "";
	String chefRegStreetAddress = "";
	String chefRegApt = "";
	String chefRegCity = "";
	String chefRegState = "";
	String chefRegZip = "";
	
	

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_register_part3_activity);

		setWidget();
	}

	public void setWidget()
	{
		btnRegister = (Button)findViewById(R.id.btnRegisterStep3Next);
		btnRegister.setOnClickListener(this);

		btnRegisterStep3Pre = (ImageView)findViewById(R.id.btnRegisterStep3Pre);
		btnRegisterStep3Pre.setRotation(180);
		btnRegisterStep3Pre.setOnClickListener(this);
		
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);		
		headerTxt.setText("CHEF REGISTRATION");

		chefAddress1Part3 = (EditText)findViewById(R.id.chefAddress1Part3);
		chefRegAddress2Part3 = (EditText)findViewById(R.id.chefRegAddress2Part3);
		chefRegStreetAddressPart3 = (EditText)findViewById(R.id.chefRegStreetAddressPart3);
		chefRegAptPart3 = (EditText)findViewById(R.id.chefRegAptPart3);
		chefRegCityPart3 = (EditText)findViewById(R.id.chefRegCityPart3);
		chefRegStatePart3 = (EditText)findViewById(R.id.chefRegStatePart3);	
		chefRegZipPart3 = (EditText)findViewById(R.id.chefRegZipPart3); 

		chefRegSpiCountryPart3 = (Spinner)findViewById(R.id.chefRegSpiCountryPart3);
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {	

		case R.id.btnBack:

			finish();

			break;

		case R.id.btnLogout:

			break;

		case R.id.btnRegisterStep3Next:

			chefAddress1 = chefAddress1Part3.getText().toString();
			chefAddress2 = chefRegAddress2Part3.getText().toString();
			chefRegStreetAddress = chefRegStreetAddressPart3.getText().toString();
			chefRegApt = chefRegAptPart3.getText().toString();
			chefRegCity = chefRegCityPart3.getText().toString();
			chefRegState = chefRegStatePart3.getText().toString();	
			chefRegZip = chefRegZipPart3.getText().toString();	
		
			if(chefAddress1.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Address1");

			}else
			if(chefAddress2.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Address2");

			}else if(chefRegStreetAddress.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Street Address");

			}else if(chefRegApt.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter Apt/suite/unit, building, floor, buzzer, booth, stand number");
			}			
			else if(chefRegCity.equalsIgnoreCase(""))
			{
				CreateDialog.showDialog(this, "Enter City");

			}else if(chefRegState.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter State");

			}else if(chefRegZip.equalsIgnoreCase("")){

				CreateDialog.showDialog(this, "Enter Zipcode");

			}
			else{

				//doChefRegister();
				startActivity(new Intent(ChefRrgisterPart3Activity.this,ChefEmailVerifyActivity.class));
			}


			//startActivity(new Intent(CustomerRrgisterActivity.this,ChefEmailVerifyActivity.class));
			break;
		case R.id.btnRegisterStep3Pre:
			
			//(new Intent(ChefRrgisterPart2Activity.this,ChefRrgisterPart3Activity.class));
			finish();
			
			break;
		}
	}


	//===============Register chef Function===================
	void doChefRegister()
	{
		progressDialog=new ProgressDialog(ChefRrgisterPart3Activity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.REGISTER_URL;

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
						Toast.makeText(ChefRrgisterPart3Activity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 
				/*params.put("FirstName",firstName);	
				params.put("LastName",lastName);	
				params.put("password",password);	
				params.put("email",email);	*/
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}


	void parseResponse(String response){	

		String message = "";
		System.out.println("Response :"+response);
		try {

			JSONObject parentObject = new JSONObject(response);
			JSONArray parentArray = parentObject.getJSONArray("output");
			JSONObject  obj = parentArray.optJSONObject(0);
			message = obj.getString("message");			


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

		if(message.contains("Successfull")){

			startActivity(new Intent(ChefRrgisterPart3Activity.this,ChefEmailVerifyActivity.class));
			finish();
		}


		Toast.makeText(ChefRrgisterPart3Activity.this, ""+message, Toast.LENGTH_LONG).show();
	}

	//=========================== Email varification ==============================================

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}
