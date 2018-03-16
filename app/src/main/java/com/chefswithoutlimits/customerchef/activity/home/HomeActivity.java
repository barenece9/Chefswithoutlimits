package com.chefswithoutlimits.customerchef.activity.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
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
import com.chefswithoutlimits.customerchef.activity.chefs.order.ChefOrderActivity;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineKitchenDetailsActivity;
import com.chefswithoutlimits.customerchef.activity.customer.search.OfflineSearchKitchen;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.firebase.messaging.FirebaseMessaging;
import com.chefswithoutlimits.Notification.Config;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.GPSService;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.LocationService;
import com.chefswithoutlimits.util.RegisterActivities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends Activity implements OnClickListener{

	Button btnOrderOnline;
	Button btnlogin;
	Button btnRegister;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	private static final int REQUEST_ACCESS_FINE_LOCATION = 111;


	private static final int PERMISSION_CALLBACK_CONSTANT = 100;
	private static final int REQUEST_PERMISSION_SETTING = 101;
	String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.WRITE_EXTERNAL_STORAGE};
	private boolean sentToSettings = false;
	private SharedPreferences permissionStatus;

	private static final String TAG = ChefOrderActivity.class.getSimpleName();
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	ArrayList<HashMap<String,String>> kitchenlist;



	@SuppressWarnings("ALL")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		RegisterActivities.registerActivity(HomeActivity.this);

		btnOrderOnline = (Button)findViewById(R.id.btnOrderOnline);
		btnOrderOnline.setOnClickListener(this);
		btnlogin = (Button)findViewById(R.id.btnlogin);
		btnlogin.setOnClickListener(this);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);


		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("HOME");



		String android_id = Settings.Secure.getString(HomeActivity.this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		Log.e("android_id ===================>> ",android_id);
		ConstantUtils.sessionUserId="guest_"+android_id;

		kitchenlist=new ArrayList<>();


		/// for recive notification......................

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				// checking for type intent filter
				if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
					// gcm successfully registered
					// now subscribe to `global` topic to receive app wide notifications
					FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

					displayFirebaseRegId();

				} else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
					// new push notification is received

					String message = intent.getStringExtra("message");

					Toast.makeText(getApplicationContext(), "New Order : " + message, Toast.LENGTH_LONG).show();

					//txtMessage.setText(message);

					// getorderValue();
				}
			}
		};

		displayFirebaseRegId();

		setPermission();
	}


	public void setPermission(){

		permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

		if(ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED){
			if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[0])
					|| ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[1])
					|| ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[2])
					|| ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[3])){
				//Show Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera,Location and Storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			} else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
				//Previously Permission Request was cancelled with 'Dont Ask Again',
				// Redirect to Settings after showing Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera,Location and Storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						sentToSettings = true;
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", getPackageName(), null);
						intent.setData(uri);
						startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
						Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera,Location and Storage", Toast.LENGTH_LONG).show();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			}  else {
				//just request the permission
				ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
			}

			//txtPermissions.setText("Permissions Required");

			SharedPreferences.Editor editor = permissionStatus.edit();
			editor.putBoolean(permissionsRequired[0],true);
			editor.commit();
		} else {
			//You already have the permission, just go ahead.
			proceedAfterPermission();
		}

	}



	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case R.id.btnOrderOnline:

			if(InternetConnectivity.isConnectedFast(HomeActivity.this)){
					boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
							Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
					if(hasPermissionLocation) {

						/*GPSTracker gpsTracker = new GPSTracker(this);
						if (gpsTracker.getIsGPSTrackingEnabled()) {
							if(ConstantUtils.Latitude.equalsIgnoreCase("0") && ConstantUtils.Longitude.equalsIgnoreCase("0")){
								startService(new Intent(getBaseContext(), LocationService.class));
								Toast.makeText(getApplicationContext(),"gps needs some time to fetch your location",Toast.LENGTH_SHORT).show();
							}else {
								startService(new Intent(getBaseContext(), LocationService.class));
								getCartStatus();
							}

						}else {
							gpsTracker.showSettingsAlert();
						}*/

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


							ConstantUtils.Latitude=Double.toString(latitude);
							ConstantUtils.Longitude=Double.toString(longitude);

							if(ConstantUtils.Latitude.equalsIgnoreCase("0") && ConstantUtils.Longitude.equalsIgnoreCase("0")){
								startService(new Intent(getBaseContext(), LocationService.class));
								Toast.makeText(getApplicationContext(),"gps needs some time to fetch your location",Toast.LENGTH_SHORT).show();
							}else {
								startService(new Intent(getBaseContext(), LocationService.class));
								getCartStatus();
							}

						}
					}else {
						ActivityCompat.requestPermissions(HomeActivity.this,
								new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
								REQUEST_ACCESS_FINE_LOCATION);
					}
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btnlogin:


			Bundle bundle = new Bundle();
			bundle.putString("guestOrder", "0");
			Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
			intent2.putExtras(bundle);
			startActivity(intent2);
			finish();

			break;

		case R.id.btnRegister:

			startActivity(new Intent(HomeActivity.this,RegisterTypeActivity.class));
			finish();
			
			break;

		case R.id.btnBack:
			
			finish();

			break;
			
		case R.id.btnLogout:

			break;
		
		}
	}





	public void getCartStatus(){
		final ProgressDialog progressDialog=new ProgressDialog(HomeActivity.this);
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
						parseCartCounter(response);
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
						Toast.makeText(HomeActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("user_id",ConstantUtils.sessionUserId);
				params.put("format","json");
				return params;
			}
		};

		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public  void parseCartCounter(String response){
		try {

			System.out.println("Response : "+response);
			JSONObject parentObj = new JSONObject(response);

			String Status=parentObj.optString("Status");
			if(Status.equalsIgnoreCase("true")) {

				ConstantUtils.offlineCartStatus=true;

				kitchenlist.clear();

				JSONArray Records=parentObj.getJSONArray("Records");
				JSONObject innerObj=Records.getJSONObject(0);

				ConstantUtils.onlineCartStatus=true;
				ConstantUtils.kitchen_id=innerObj.getString("id");
				ConstantUtils.kitchenName=innerObj.getString("kichen_name");

				HashMap<String,String> list=new HashMap<>();

				list.put("kichen_name",innerObj.getString("kichen_name"));
				list.put("kitchen_id",innerObj.optString("chef_id"));
				list.put("kichen_Logo",innerObj.optString("kichen_Logo"));
				list.put("kichen_rating",innerObj.optString("kichen_rating"));

				list.put("distance_unit",innerObj.optString("distance_unit"));
				list.put("distance",innerObj.optString("distance"));
				list.put("distance_cover_by_vehicle",innerObj.optString("distance_cover_by_vehicle"));



				list.put("latitde",innerObj.optString("latitde"));
				list.put("longitude",innerObj.optString("longitude"));
				list.put("distance_unit",innerObj.optString("distance_unit"));
				list.put("min_order",innerObj.optString("min_order"));


				Double distance=distance(innerObj.optDouble("latitde"),innerObj.optDouble("longitude"),
						Double.valueOf(ConstantUtils.Latitude),Double.valueOf(ConstantUtils.Longitude));

				if(innerObj.optString("distance_unit").equalsIgnoreCase("Km")){
				}else if(innerObj.optString("distance_unit").equalsIgnoreCase("Miles")){
					//1 km = (1/1.609344)
					distance=distance/1.609344;
				}

				System.out.println("Calculate Distance =============>>>    "+distance);


				kitchenlist.add(list);



				if(ConstantUtils.offlineCartStatus){

					KitchenData kitchenData=new KitchenData();
					kitchenData.setKitchen_id(kitchenlist.get(0).get("kitchen_id"));
					kitchenData.setKichen_name(kitchenlist.get(0).get("kichen_name"));
					kitchenData.setKichen_Logo(kitchenlist.get(0).get("kichen_Logo"));
					//kitchenData.setDistance(kitchenlist.get(0).get("distance"));
					kitchenData.setDistance(String.valueOf(distance));
					kitchenData.setDistance_cover_by_vehicle(kitchenlist.get(0).get("distance_cover_by_vehicle"));

					kitchenData.setMin_order(kitchenlist.get(0).get("min_order"));

					Sharepreferences.saveSharePreferanceKitchenData(HomeActivity.this, kitchenData);

					startActivity(new Intent(HomeActivity.this,OfflineKitchenDetailsActivity.class));
					finish();
				}

			}else if(Status.equalsIgnoreCase("false")){

				ConstantUtils.offlineCartStatus=false;

				startActivity(new Intent(HomeActivity.this,OfflineSearchKitchen.class));
				finish();

				//Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == PERMISSION_CALLBACK_CONSTANT){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				proceedAfterPermission();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[0])
					|| ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[1])
					|| ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[2])){
				//txtPermissions.setText("Permissions Required");
				AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera,Location and Storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			} else {
				Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PERMISSION_SETTING) {
			if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission();
			}
		}
	}

	private void proceedAfterPermission() {
		//txtPermissions.setText("We've got all permissions");
		//Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (sentToSettings) {
			if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission();
			}
		}
	}


	// Fetches reg id from shared preferences
	// and displays on the screen
	private void displayFirebaseRegId() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
		String regId = pref.getString("regId", null);

		Log.e("TAG", "Firebase reg id: " + regId);
		//txtRegId.setVisibility(View.GONE);

		if (!TextUtils.isEmpty(regId))
			System.out.println("Firebase Reg Id: " + regId);
		else
			System.out.println("Firebase Reg Id is not received yet!");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// register GCM registration complete receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.REGISTRATION_COMPLETE));

		// register new push message receiver
		// by doing this, the activity will be notified each time a new message arrives
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.PUSH_NOTIFICATION));

		// clear the notification area when the app is opened
		//NotificationUtils.clearNotifications(getApplicationContext());
	}

	@Override
	protected void onPause() {
		// LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}



	//distance always return in km
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1))
				* Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1))
				* Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}
