package com.chefswithoutlimits.customerchef.activity.chefs;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.chefs.order.ChefCompleteOrderActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.kitchen.ChefEditKitchen;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.ChefMenuActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.order.ChefOrderActivity;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.Notification.NotificationActivity;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.updatehandler.app.UpdateHandler;

public class ChefDashboardActivity extends AppCompatActivity implements OnClickListener{

	ImageView imgNeworder;
	ImageView imgKitchenSetting;
	ImageView imgProfileSetting;
	ImageView imgOrder;
	ImageView imgMenue;
	ImageView imgRecipes;
	TextView headerTxt;
	ImageView btnLogout;
	ImageView imageView2;


	RatingBar ratingBar1;

	Button imgChDashOn,imgChDashOff;
	UserInformation userInfo;
	private ProgressDialog progressDialog;


	private static final String TAG = NotificationActivity.class.getSimpleName();
	private BroadcastReceiver mRegistrationBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_dashboard);
		userInfo = Sharepreferences.getSharePreferance(this);


		setWidget();

	}

	public void setWidget()
	{
		imgNeworder = (ImageView)findViewById(R.id.imgNeworder);
		imgNeworder.setOnClickListener(this);
		imgKitchenSetting = (ImageView)findViewById(R.id.imgKitchenSetting);
		imgKitchenSetting.setOnClickListener(this);
		imgProfileSetting = (ImageView)findViewById(R.id.imgProfileSetting);
		imgProfileSetting.setOnClickListener(this);
		imgOrder = (ImageView)findViewById(R.id.imgOrder);
		imgOrder.setOnClickListener(this);
		imgMenue = (ImageView)findViewById(R.id.imgMenue);
		imgMenue.setOnClickListener(this);
		imgRecipes = (ImageView)findViewById(R.id.imgRecipes);
		imgRecipes.setOnClickListener(this);

		
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("HOME");
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);

		imgChDashOn=(Button)findViewById(R.id.imgChDashOn);
		imgChDashOn.setOnClickListener(this);
		imgChDashOff=(Button)findViewById(R.id.imgChDashOff);
		imgChDashOff.setOnClickListener(this);

		ratingBar1=(RatingBar)findViewById(R.id.ratingBar1);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
				.placeholder(R.mipmap.logo_box)   // optional
				.error(R.mipmap.logo_box)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		getkitchenstatus();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) { 
		case R.id.imgNeworder:


           // new orders .........................
			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){
				startActivity(new Intent(this,ChefOrderActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}



			break;

		case R.id.imgKitchenSetting:
			

			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){

				checkStripeMode();

			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.imgProfileSetting:
			

			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){
				startActivity(new Intent(this,ChefProfileSettingActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.imgOrder:

			//complete order........................

			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){
				startActivity(new Intent(this,ChefCompleteOrderActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;	
		case R.id.imgMenue:
			

			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){
				startActivity(new Intent(this,ChefMenuActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;	
		case R.id.imgRecipes:


			if(InternetConnectivity.isConnectedFast(ChefDashboardActivity.this)){
				startActivity(new Intent(this,ChefSchedular.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;	
		case R.id.btnLogout:
			Logout.logOut(this);
			finish();
			break;

		case R.id.imgChDashOn:
			//changekitchenstatus("open");

			break;
		case R.id.imgChDashOff:
			//changekitchenstatus("closed");

			break;

		}

	}

	public  void getkitchenstatus(){

		{
			progressDialog=new ProgressDialog(ChefDashboardActivity.this);
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
							Toast.makeText(ChefDashboardActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
	}

	private void parseData(String response)
	{
		JSONObject parentJSON;
		try {

			parentJSON = new JSONObject(response);
			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				JSONArray parentArr = parentJSON.optJSONArray("Records");
				JSONObject record = parentArr.getJSONObject(0);
				String kitchenstatus = record.optString("status");
				if (kitchenstatus.equalsIgnoreCase("closed")) {
					imgChDashOn.setVisibility(View.GONE);
					imgChDashOff.setVisibility(View.VISIBLE);
					//imgChDashOff.setPressed(true);
					//imgChDashOn.setPressed(false);
				} else {
					imgChDashOn.setVisibility(View.VISIBLE);
					imgChDashOff.setVisibility(View.GONE);
					//imgChDashOn.setPressed(true);
					//imgChDashOff.setPressed(false);
				}
				System.out.println("@@@@@@@@@@@@@   " + kitchenstatus);

				String ratting=record.getString("ratting");
				if(!ratting.equalsIgnoreCase("null") && !ratting.equalsIgnoreCase("")){
					ratingBar1.setRating(Float.valueOf(ratting));
					String rated_by=record.getString("rated_by");
				}



			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateCheck();

	}



	public void checkStripeMode() {
		final ProgressDialog progressDialog2=new ProgressDialog(ChefDashboardActivity.this);
		progressDialog2.setMessage("loading...");
		progressDialog2.show();
		progressDialog2.setCancelable(false);
		progressDialog2.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHECK_STRIPE_MODE;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseStripeMode(response);
						if(progressDialog2!=null)
							progressDialog2.dismiss();
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						if(progressDialog2!=null)
							progressDialog2.dismiss();
						System.out.println("Error=========="+error);
						Toast.makeText(ChefDashboardActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("type","stripe");
				params.put("status","1");
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parseStripeMode(String response)
	{
		try {

			JSONObject parentObj = new JSONObject(response);
			String Status=parentObj.getString("Status");

			if(Status.equalsIgnoreCase("true")){

				JSONObject innerObj=parentObj.getJSONObject("data");

				String id=innerObj.getString("id");
				String type=innerObj.getString("type");
				String url=innerObj.getString("url");
				String client_id=innerObj.getString("client_id");
				String private_key=innerObj.getString("private_key");
				String public_key=innerObj.getString("public_key");
				String status=innerObj.getString("status");
				String mode=innerObj.getString("mode");

				//for stripe connect==============================
				ApplicationData.CLIENT_ID = client_id;
				ApplicationData.SECRET_KEY = private_key;
				ApplicationData.CALLBACK_URL = url;
				//for stripe payment==================================
				ApplicationData.PUBLISHABLE_KEY=public_key;

				ApplicationData.STRIPE_MODE=mode;

				if(mode.equalsIgnoreCase("test")){
					Toast.makeText(getApplicationContext(),"stripe is in test mode",Toast.LENGTH_SHORT).show();
				}else if(mode.equalsIgnoreCase("live")){
					Toast.makeText(getApplicationContext(),"stripe is in live mode",Toast.LENGTH_SHORT).show();
				}

				startActivity(new Intent(this,ChefEditKitchen.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_SHORT).show();
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//doStateList(countrySelect);
	}


	public void updateCheck(){
		/**
		 * This library works in release mode only with the same JKS key used for
		 * your Previous Version
		 */
		new UpdateHandler.Builder(ChefDashboardActivity.this)
				.setContent("New Version Found")
				.setTitle("Update Found")
				.setUpdateText("Yes")
				.setCancelText("No")
				.showDefaultAlert(true)
				.showWhatsNew(true)
				.setCheckerCount(2)
				.setOnUpdateFoundLister(new UpdateHandler.Builder.UpdateListener() {
					@Override
					public void onUpdateFound(boolean newVersion, String whatsNew) {
						//tv.setText(tv.getText() + "\n\nUpdate Found : " + newVersion + "\n\nWhat's New\n" + whatsNew);
					}
				})
				.setOnUpdateClickLister(new UpdateHandler.Builder.UpdateClickListener() {
					@Override
					public void onUpdateClick(boolean newVersion, String whatsNew) {
						Log.v("onUpdateClick", String.valueOf(newVersion));
						Log.v("onUpdateClick", whatsNew);
					}
				})
				.setOnCancelClickLister(new UpdateHandler.Builder.UpdateCancelListener() {
					@Override
					public void onCancelClick() {
						Log.v("onCancelClick", "Cancelled");
					}
				})
				.build();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
