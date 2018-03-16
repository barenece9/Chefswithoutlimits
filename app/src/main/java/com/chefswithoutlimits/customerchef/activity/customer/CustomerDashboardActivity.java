package com.chefswithoutlimits.customerchef.activity.customer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.circleimageview.CircleImageView;
import com.chefswithoutlimits.customerchef.activity.customer.cart.CartListActivity;
import com.chefswithoutlimits.customerchef.activity.customer.search.SearchKitchen;
import com.chefswithoutlimits.customerchef.activity.customer.cart.KitchenDetailsActivity;
import com.chefswithoutlimits.customerchef.activity.customer.order.CustomerCompleteOrderActivity;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.GPSService;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.LocationService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidmads.updatehandler.app.UpdateHandler;

public class CustomerDashboardActivity extends AppCompatActivity implements OnClickListener{



	Button btn_Order_List,btn_Account,btn_order_place,btn_delivery_address;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	TextView txtdashUserName;
	ImageView imgCart;
	TextView Cart_counter;
	
	UserInformation userInfo;


	String kitchenID="";
	String kichen_name="";

	CircleImageView imageView2;

	ArrayList<HashMap<String,String>> kitchenlist;

	private static final int REQUEST_READ_PHONE_STATE = 110 ,
			REQUEST_ACCESS_FINE_LOCATION = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboard);
		
		userInfo = Sharepreferences.getSharePreferance(this);

		kitchenlist=new ArrayList<>();

		setWidget();
	}


	public void setWidget()
	{
		btn_Order_List = (Button)findViewById(R.id.btn_Order_List);
		btn_Order_List.setOnClickListener(this);

		btn_Account = (Button)findViewById(R.id.btn_Account);
		btn_Account.setOnClickListener(this);

		btn_order_place=(Button)findViewById(R.id.btn_order_place);
		btn_order_place.setOnClickListener(this);

		btn_delivery_address=(Button)findViewById(R.id.btn_delivery_address);
		btn_delivery_address.setOnClickListener(this);


		imgCart=(ImageView)findViewById(R.id.imgCart);
		imgCart.setOnClickListener(this);
		imgCart.setVisibility(View.VISIBLE);
		Cart_counter=(TextView)findViewById(R.id.Cart_counter);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("CUSTOMER DASHBOARD");

		
		txtdashUserName = (TextView)findViewById(R.id.txtdashUserName);
		txtdashUserName.setText(userInfo.getFirstName()+" "+userInfo.getLastName());
        System.out.println("IMAGE URLLLLLLLLLLLLL  ....    "+ WebServiceURL.IMAGE_PATH+""+userInfo.getUser_log());
		imageView2=(CircleImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+userInfo.getUser_log())
				.placeholder(R.drawable.ic_person_black_24dp)   // optional
				.error(R.drawable.ic_person_black_24dp)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		getcartStatus();

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_Account:

			if(InternetConnectivity.isConnectedFast(CustomerDashboardActivity.this)){
				startActivity(new Intent(CustomerDashboardActivity.this,CustomerProfileSettingActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.imgCart:

			if(InternetConnectivity.isConnectedFast(CustomerDashboardActivity.this)){
				if(ConstantUtils.onlineCartStatus) {
					startActivity(new Intent(CustomerDashboardActivity.this, CartListActivity.class));
					finish();
				}else {
					Toast.makeText(getApplicationContext(),"cart is empty",Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}


			break;

		case R.id.btn_order_place:

			checking();

			break;

		case R.id.btn_Order_List:

			if(InternetConnectivity.isConnectedFast(CustomerDashboardActivity.this)){
				startActivity(new Intent(CustomerDashboardActivity.this, CustomerCompleteOrderActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}


			break;

		case R.id.btn_delivery_address:

			if(InternetConnectivity.isConnectedFast(CustomerDashboardActivity.this)){
				startActivity(new Intent(CustomerDashboardActivity.this, CustomerDeliveryAddressActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}


			break;

		case R.id.btnBack:
			//startActivity(new Intent(CustomerDashboardActivity.this,LoginActivity.class));

			finish();

			break;

		case R.id.btnLogout:	
			
			Logout.logOut(this);
			finish();
			break;
		}
	}


	public void getcartStatus(){
		final ProgressDialog progressDialog=new ProgressDialog(CustomerDashboardActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		String url;

		url = WebServiceURL.CHECK_CART_ITEMS;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parsecartcounter(response);
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
						Toast.makeText(CustomerDashboardActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CustomerDashboardActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				params.put("user_id",userInfo.getUserId());
				params.put("format","json");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public  void parsecartcounter(String response){
		try {

			System.out.println("Response : "+response);
			JSONObject parentObj = new JSONObject(response);

			String Status=parentObj.optString("Status");
			if(Status.equalsIgnoreCase("true")) {

				kitchenlist.clear();

				JSONArray Records=parentObj.getJSONArray("Records");
				JSONObject innerObj=Records.getJSONObject(0);
				ConstantUtils.onlineCartStatus=true;
				kitchenID=innerObj.getString("id");
				kichen_name=innerObj.getString("kichen_name");

				HashMap<String,String> list=new HashMap<>();

				list.put("kichen_name",kichen_name);
				list.put("kitchen_id",innerObj.optString("chef_id"));
				list.put("kichen_Logo",innerObj.optString("kichen_Logo"));
				list.put("kichen_rating",innerObj.optString("kichen_rating"));

				list.put("distance_unit",innerObj.optString("distance_unit"));
				list.put("distance",innerObj.optString("distance"));
				list.put("distance_cover_by_vehicle",innerObj.optString("distance_cover_by_vehicle"));

				list.put("min_order",innerObj.optString("min_order"));

				kitchenlist.add(list);


				KitchenData kitchenData=new KitchenData();
				kitchenData.setKitchen_id(kitchenlist.get(0).get("kitchen_id"));
				kitchenData.setKichen_name(kitchenlist.get(0).get("kichen_name"));
				kitchenData.setKichen_Logo(kitchenlist.get(0).get("kichen_Logo"));
				kitchenData.setDistance(kitchenlist.get(0).get("distance"));
				kitchenData.setMin_order(kitchenlist.get(0).get("min_order"));
				kitchenData.setDistance_cover_by_vehicle(kitchenlist.get(0).get("distance_cover_by_vehicle"));
				Sharepreferences.saveSharePreferanceKitchenData(CustomerDashboardActivity.this, kitchenData);


				imgCart.setVisibility(View.VISIBLE);
				Cart_counter.setVisibility(View.VISIBLE);
				Cart_counter.setText(innerObj.getString("cart_count"));

				// Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_LONG).show();
			}else {
				// Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
				ConstantUtils.onlineCartStatus=false;
				imgCart.setVisibility(View.VISIBLE);
				Cart_counter.setVisibility(View.GONE);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateCheck();

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{
			case REQUEST_ACCESS_FINE_LOCATION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					Toast.makeText(CustomerDashboardActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
					//reload my activity with permission granted or use the features what required the permission
					//finish();
					//startActivity(getIntent());
				} else
				{
					Toast.makeText(CustomerDashboardActivity.this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public void checking(){

		if(InternetConnectivity.isConnectedFast(CustomerDashboardActivity.this)){
			boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
			if(hasPermissionLocation) {
				//GPSTracker gpsTracker = new GPSTracker(this);

				GPSService mGPSService = new GPSService(this);
				mGPSService.getLocation();
				if (mGPSService.isLocationAvailable == false) {
					Toast.makeText(getApplicationContext(),"gps needs some time to fetch your location",Toast.LENGTH_SHORT).show();
					System.out.println("Location not available, Open GPS");
					//gpsTracker.showSettingsAlert();
				} else {
					// Getting location co-ordinates
					double latitude = mGPSService.getLatitude();
					double longitude = mGPSService.getLongitude();


					ConstantUtils.Latitude = Double.toString(latitude);
					ConstantUtils.Longitude = Double.toString(longitude);

					if(ConstantUtils.Latitude.equalsIgnoreCase("0") && ConstantUtils.Longitude.equalsIgnoreCase("0")){
						startService(new Intent(getBaseContext(), LocationService.class));
						Toast.makeText(getApplicationContext(),"gps needs some time to fetch your location",Toast.LENGTH_SHORT).show();
					}else {
						startService(new Intent(getBaseContext(), LocationService.class));
						if(!ConstantUtils.onlineCartStatus){
							startActivity(new Intent(CustomerDashboardActivity.this, SearchKitchen.class));
							finish();
						}else {
							Intent intent=new Intent(CustomerDashboardActivity.this,KitchenDetailsActivity.class);
							KitchenData kitchenData=new KitchenData();
							kitchenData.setKitchen_id(kitchenlist.get(0).get("kitchen_id"));
							kitchenData.setKichen_name(kitchenlist.get(0).get("kichen_name"));
							kitchenData.setKichen_Logo(kitchenlist.get(0).get("kichen_Logo"));
							kitchenData.setDistance(kitchenlist.get(0).get("distance"));
							kitchenData.setDistance_cover_by_vehicle(kitchenlist.get(0).get("distance_cover_by_vehicle"));

							kitchenData.setMin_order(kitchenlist.get(0).get("min_order"));

							Sharepreferences.saveSharePreferanceKitchenData(CustomerDashboardActivity.this, kitchenData);

							intent.putExtra("kitchen_id",kitchenID);
							intent.putExtra("kitchenName",kichen_name);
							startActivity(intent);
							finish();
						}
					}
				}
				/*if (gpsTracker.getIsGPSTrackingEnabled()) {
					if(ConstantUtils.Latitude.equalsIgnoreCase("0") && ConstantUtils.Longitude.equalsIgnoreCase("0")){
						startService(new Intent(getBaseContext(), LocationService.class));
						Toast.makeText(getApplicationContext(),"gps needs some time to fetch your location",Toast.LENGTH_SHORT).show();
					}else {
						startService(new Intent(getBaseContext(), LocationService.class));
						if(!ConstantUtils.onlineCartStatus){
							startActivity(new Intent(CustomerDashboardActivity.this, SearchKitchen.class));
							finish();
						}else {
							Intent intent=new Intent(CustomerDashboardActivity.this,KitchenDetailsActivity.class);
							KitchenData kitchenData=new KitchenData();
							kitchenData.setKitchen_id(kitchenlist.get(0).get("kitchen_id"));
							kitchenData.setKichen_name(kitchenlist.get(0).get("kichen_name"));
							kitchenData.setKichen_Logo(kitchenlist.get(0).get("kichen_Logo"));
							kitchenData.setDistance(kitchenlist.get(0).get("distance"));
							kitchenData.setDistance_cover_by_vehicle(kitchenlist.get(0).get("distance_cover_by_vehicle"));

							kitchenData.setMin_order(kitchenlist.get(0).get("min_order"));

							Sharepreferences.saveSharePreferanceKitchenData(CustomerDashboardActivity.this, kitchenData);

							intent.putExtra("kitchen_id",kitchenID);
							intent.putExtra("kitchenName",kichen_name);
							startActivity(intent);
							finish();
						}
					}

				}else {
					gpsTracker.showSettingsAlert();
				}*/
			}else {
				ActivityCompat.requestPermissions(CustomerDashboardActivity.this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_ACCESS_FINE_LOCATION);
			}
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}
	}


	public void updateCheck(){
		/**
		 * This library works in release mode only with the same JKS key used for
		 * your Previous Version
		 */
        new UpdateHandler.Builder(CustomerDashboardActivity.this)
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
}
