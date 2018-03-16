package com.chefswithoutlimits.customerchef.activity.chefs.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.Notification.NotificationUtils;
import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.adapter.ChefOrderListingAdapter;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.stripeconnect.StripeApp;
import com.chefswithoutlimits.stripeconnect.StripeButton;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.stripeconnect.StripeSession;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefOrderActivity extends Activity implements OnClickListener,Animation.AnimationListener {

	ImageView imageView2;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	ListView listViewNewOrder;
	ImageButton imageView4;
	// Animation
	Animation animRotate;

	UserInformation userInfo;

	//ChefNewOrderAdapter adapter;
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
		setContentView(R.layout.chef_neworder);


		userInfo = Sharepreferences.getSharePreferance(ChefOrderActivity.this);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
				.placeholder(R.mipmap.logo_box)   // optional
				.error(R.mipmap.logo_box)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("NEW ORDERS");

		listViewNewOrder = (ListView)findViewById(R.id.listViewNewOrder);
		// load the animation
		animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate);
		// set animation listener
		animRotate.setAnimationListener(ChefOrderActivity.this);
		imageView4=(ImageButton)findViewById(R.id.imageView4);
		imageView4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView4.startAnimation(animRotate);
				getorderValue();
			}
		});







		/*stripe connect.........*//*
		mApp = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
				ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);

		tvSummary = (TextView) findViewById(R.id.tvSummary);
		*//*if (mApp.isConnected()) {
			//tvSummary.setText("Connected as " + mApp.getAccessToken());
			tvSummary.setText("Connected as " + mApp.getSUserId());
			System.out.println("Access Token : "+mApp.getAccessToken());
			connect=true;
			Sharepreferences.setRememberStripe(ChefOrderActivity.this, true, "chef", 1);
		}*//*

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
				Sharepreferences.setRememberStripe(ChefOrderActivity.this, true, "chef", 1);


				updateStripeUserId(mApp.getSUserId());

			}

			@Override
			public void onDisconnected() {
				tvSummary.setText("Disconnected");
				System.out.println("Stripe onDisconnected : =========================>>");
				connect=false;
				Sharepreferences.setRememberStripe(ChefOrderActivity.this, false, "chef", 1);
			}

			@Override
			public void onError(String error) {
				Toast.makeText(ChefOrderActivity.this, error, Toast.LENGTH_SHORT).show();
				connect=false;
				System.out.println("Stripe onError : =========================>>");
				Sharepreferences.setRememberStripe(ChefOrderActivity.this, false, "chef", 1);
			}

		});

		mApp2 = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
				ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);
		mStripeButton2 = (StripeButton) findViewById(R.id.btnConnect2);
		mStripeButton2.setStripeApp(mApp2);
		mStripeButton2.setConnectMode(StripeApp.CONNECT_MODE.ACTIVITY);

		Stripe.apiKey = mApp.getAccessToken();




		if(userInfo.getOffline_stripe_user_id().equalsIgnoreCase("")){
			mStripeButton.setVisibility(View.VISIBLE);
			connect=false;
		}else {
			mStripeButton.setVisibility(View.GONE);
			tvSummary.setText("Connected as " + userInfo.getOffline_stripe_user_id());
			//System.out.println("Access Token : "+mApp.getAccessToken());
			connect=true;
		}*/




		getorderValue();
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {


		case R.id.btnBack:

			if(InternetConnectivity.isConnectedFast(ChefOrderActivity.this)){
				startActivity(new Intent(ChefOrderActivity.this,ChefDashboardActivity.class));
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
		final ProgressDialog progressDialog=new ProgressDialog(ChefOrderActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		RequestQueue queue = Volley.newRequestQueue(this);
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
						Toast.makeText(ChefOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(ChefOrderActivity.this);
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("userid",userInfo.getUserId());				
				//params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		postRequest.setShouldCache(false);
		//AppController.getInstance().addToRequestQueue(postRequest);
		queue.add(postRequest);
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
					if (dataObj.optString("order_status").equalsIgnoreCase("pending")) {
						alllist.add(list);
					}

				}
			}else {
				Toast.makeText(ChefOrderActivity.this, "No Order", Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//adapter.notifyDataSetChanged();
		listViewNewOrder.setAdapter(new ChefOrderListingAdapter(ChefOrderActivity.this,alllist));
	}

	//==================================== Set Adapter =====================================

	public void yourDesiredMethod(String id)
	{


		if(InternetConnectivity.isConnectedFast(ChefOrderActivity.this)){
			Intent intent=new Intent(ChefOrderActivity.this,ChefOrderDetailsActivity.class);
			intent.putExtra("id",id);
			System.out.println("############ id : "+id);
			startActivity(intent);
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}

	}

	public void OrderDetails(final String order_id)
	{
		final ProgressDialog progressDialog=new ProgressDialog(ChefOrderActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		RequestQueue queue = Volley.newRequestQueue(this);
		url = WebServiceURL.CHEF_ORDER_DETAILS;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						OrderDetailsDialog(response);
						System.out.println("\n response @@@@@@@@@@@@@ "+response);
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
						Toast.makeText(ChefOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(ChefOrderActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				params.put("order_id",order_id);
				params.put("format","json");
				System.out.print("order_id *****************    "+order_id);
				return params;
			}
		};
		// Adding request to volley request queue
		postRequest.setShouldCache(false);
		//AppController.getInstance().addToRequestQueue(postRequest);
		queue.add(postRequest);
	}

	public void OrderDetailsDialog(String response){
		final Dialog dialog = new Dialog(ChefOrderActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_order_details);

		TextView tv_date_value=(TextView)dialog.findViewById(R.id.tv_date_value);
		TextView tv_order_type_value=(TextView)dialog.findViewById(R.id.tv_order_type_value);
		TextView tv_transport_type_value=(TextView)dialog.findViewById(R.id.tv_transport_type_value);
		TextView tv_customer_details_value=(TextView)dialog.findViewById(R.id.tv_customer_details_value);
		TextView tv_phone_value=(TextView)dialog.findViewById(R.id.tv_phone_value);
		TextView tv_menu_name=(TextView)dialog.findViewById(R.id.tv_order_details_list_value);

		Button dialogButtonCross = (Button) dialog.findViewById(R.id.btn_cross);

		try {

			JSONObject parentJSON = new JSONObject(response);
			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
            //"1 x Moghlaiii\n1 x test601\n1 x Veg Pizza\n1 x puri\n1 x alur chop"
				System.out.println("RESPONSE : "+response);

				JSONObject other_information=parentJSON.getJSONObject("other_information");
				String datetime_added=other_information.getString("datetime_added");
				String order_type=other_information.getString("order_type");
				String delivery_type=other_information.getString("delivery_type");
				String shipping_first_name=other_information.getString("shipping_first_name");
				String shipping_last_name=other_information.getString("shipping_last_name");
				String shipping_address=other_information.getString("shipping_address");
				String shipping_zip=other_information.getString("shipping_zip");

				String shipping_city=other_information.getString("shipping_city");
				String shipping_state=other_information.getString("shipping_state");
				String shipping_country=other_information.getString("shipping_country");
				String phone=other_information.getString("phone");


				String unit_no=other_information.getString("shipping_aptno");
				String buzzer_no=other_information.getString("shipping_buzzerno");
				String spl_instn=other_information.getString("shipping_sp_instructions");

				String unit_buzzer="";
				if(!unit_no.equalsIgnoreCase("null") && !unit_no.equalsIgnoreCase("")){
					 unit_buzzer=" Unit: "+unit_no;
				}
				if(!buzzer_no.equalsIgnoreCase("null") && !buzzer_no.equalsIgnoreCase("")){
					unit_buzzer=unit_buzzer+" Buzzer: "+buzzer_no;
				}
				//String unit_buzzer="(Unit: "+unit_no+", Buzzer: "+buzzer_no+")";

				String spl_instruction="Special Instructions: "+spl_instn;

				tv_date_value.setText(datetime_added);
				tv_order_type_value.setText(order_type);
				tv_transport_type_value.setText(delivery_type);
				tv_phone_value.setText(phone);

				ArrayList<String> addDetails=new ArrayList<String>();
				//addDetails.add(shipping_first_name);
				//addDetails.add(shipping_last_name);
				if(!unit_buzzer.equalsIgnoreCase("null") && !unit_buzzer.equalsIgnoreCase("") ){
					addDetails.add(unit_buzzer);
				}if(!shipping_address.equalsIgnoreCase("null") && !shipping_address.equalsIgnoreCase("null")){
					addDetails.add(shipping_address);
				}if(!shipping_city.equalsIgnoreCase("null") && !shipping_city.equalsIgnoreCase("null")){
					addDetails.add(shipping_city);
				}if(!shipping_state.equalsIgnoreCase("null") && !shipping_state.equalsIgnoreCase("null")){
					addDetails.add(shipping_state);
				}if(!shipping_country.equalsIgnoreCase("null") && !shipping_country.equalsIgnoreCase("null")){
					addDetails.add(shipping_country);
				}if(!shipping_zip.equalsIgnoreCase("null") && !shipping_zip.equalsIgnoreCase("null")){
					addDetails.add(shipping_zip);
				}if(!spl_instruction.equalsIgnoreCase("null") && !spl_instruction.equalsIgnoreCase("null")){
					addDetails.add(spl_instruction);
				}

				String customerAddress="";
				if(!shipping_first_name.equalsIgnoreCase("null")){
					customerAddress=shipping_first_name;
				}
				if(!shipping_last_name.equalsIgnoreCase("null")){
					customerAddress=customerAddress+" "+shipping_last_name;
				}

				for(int k=0;k<addDetails.size();k++){
					customerAddress=customerAddress+","+addDetails.get(k);
				}
				tv_customer_details_value.setText(customerAddress);

				/*tv_customer_details_value.setText(shipping_first_name+" "+shipping_last_name+"\n"+unit_buzzer
						+","+shipping_address+","+shipping_city+","+shipping_state+","+shipping_country+","+shipping_zip+"\n"+spl_instruction);
*/

				String all_menu="";
				JSONArray data=parentJSON.getJSONArray("data");
				for(int i=0;i<data.length();i++){
					JSONObject menu=data.getJSONObject(i);
					String menu_name=menu.optString("menu_name");
					String quantity=menu.optString("quantity");
					String menu_items=menu.optString("menu_items");
					if(!menu_name.equalsIgnoreCase("null") && !menu_name.equalsIgnoreCase("")){
						if(!menu_items.equalsIgnoreCase("")){
							all_menu = all_menu + "" + quantity + " x " + menu_name + " [ "+menu_items+" ]\n";
						}else {
							all_menu = all_menu + "" + quantity + " x " + menu_name + "\n";
						}
					}
				}
				tv_menu_name.setText(all_menu);

			}else {
				dialog.dismiss();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		dialogButtonCross.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(resultCode) {
			case StripeApp.RESULT_CONNECTED:
				//tvSummary.setText("Connected as " + mApp.getAccessToken());
				System.out.println("Stripe RESULT_CONNECTED : =========================>>");
				//tvSummary.setText("Connected as " + mApp.getSUserId());
				System.out.println("Access Token : "+mApp.getAccessToken());
				break;
			case StripeApp.RESULT_ERROR:
				System.out.println("Stripe RESULT_ERROR : ===========================>>");
				String error_description = data.getStringExtra("error_description");
				//Toast.makeText(ChefOrderActivity.this, error_description, Toast.LENGTH_SHORT).show();
				break;
		}

	}

	public void CheckSecrateCode(final String code){

		if(InternetConnectivity.isConnectedFast(ChefOrderActivity.this)){
			if(userInfo.getOffline_stripe_user_id().contains("acct_")){
				CheckSecrateCode2(code);
			}else {
				CreateDialog.showDialog(ChefOrderActivity.this,"Please connect your Stripe Account");
			}
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}



	}

	public void CheckSecrateCode2(final String code)
	{
		System.out.println("@@@@===========>> code : "+code);
		final ProgressDialog progressDialog=new ProgressDialog(ChefOrderActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.STRIPE_TRANSFER;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {

						parseCheckResponse(response);
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
						Toast.makeText(ChefOrderActivity.this, "Sorry,Have an Error", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(ChefOrderActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				params.put("key",code);

				params.put("private_key", ApplicationData.SECRET_KEY);
				params.put("public_key",ApplicationData.PUBLISHABLE_KEY);

				params.put("stripe_user_id",userInfo.getOffline_stripe_user_id());
				params.put("format","json");

				Log.e("Stripe Transfer","==================================================");
				Log.e("key",code);
				Log.e("stripe_user_id",userInfo.getOffline_stripe_user_id());
				Log.e("format","json");


				return params;
			}
		};
		// Adding request to volley request queue


		postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parseCheckResponse(String response)
	{
		try {

			//{"Status":"true","message":"Payment amount transferred successfully"}
			JSONObject parentJSON = new JSONObject(response);
			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				String message=parentJSON.getString("message");
				Toast.makeText(ChefOrderActivity.this, "Payment amount transferred successfully", Toast.LENGTH_LONG).show();
				getorderValue();
			}else {
				Toast.makeText(ChefOrderActivity.this, "Failed", Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(ChefOrderActivity.this, "Failed", Toast.LENGTH_LONG).show();
		}

	}




	public  void updateStripeUserId(final String offline_stripe_user_id){

			final ProgressDialog progressDialog=new ProgressDialog(ChefOrderActivity.this);
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

							RememberData rememberData = Sharepreferences.getRememberStripe(ChefOrderActivity.this);
							if(rememberData.isLogin()){
								Sharepreferences.setRememberStripe(ChefOrderActivity.this, false, "chef", 0);
								StripeSession.Stripelogout();
							}

							Toast.makeText(ChefOrderActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
				Sharepreferences.updateStripeIdSharePreferance(ChefOrderActivity.this,offline_stripe_user_id);

				mStripeButton.setVisibility(View.GONE);
				//tvSummary.setText("Connected as " + userInfo.getOffline_stripe_user_id());
				//System.out.println("Access Token : "+mApp.getAccessToken());
				//connect=true;

				Toast.makeText(ChefOrderActivity.this, "successfully", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(ChefOrderActivity.this, "Failed", Toast.LENGTH_LONG).show();

				RememberData rememberData = Sharepreferences.getRememberStripe(ChefOrderActivity.this);
				if(rememberData.isLogin()){
					Sharepreferences.setRememberStripe(ChefOrderActivity.this, false, "chef", 0);
					StripeSession.Stripelogout();
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	protected void onResume() {
		super.onResume();
		// clear the notification area when the app is opened
		NotificationUtils.clearNotifications(getApplicationContext());
		ConstantUtils.notificationCount=0;
	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(ChefOrderActivity.this)){
			startActivity(new Intent(ChefOrderActivity.this,ChefDashboardActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}
	}





	@Override
	public void onAnimationEnd(Animation animation) {
		// Take any action after completing the animation

		// check for fade in animation
		if (animation == animRotate) {
			Toast.makeText(getApplicationContext(), "Animation Stopped",
					Toast.LENGTH_SHORT);
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}
}
