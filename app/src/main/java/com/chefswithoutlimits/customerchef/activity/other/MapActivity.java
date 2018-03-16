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

import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.CordinateData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chefswithoutlimits.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class MapActivity extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	private GoogleMap googleMap;
	private ProgressDialog progressDialog;

	ImageView imgSearch;
	EditText editTextSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.browse_location);

		setWidget();
	}


	public void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("LOCATION");

		imgSearch = (ImageView)findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(this);
		editTextSearch = (EditText)findViewById(R.id.editTextSearch);

		if (googleMap == null) {
			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {


		case R.id.btnBack:

			finish();

			break;

		case R.id.btnLogout:
			Logout.logOut(this);
			finish();
			break;

		case R.id.imgSearch:

			String location = editTextSearch.getText().toString().trim();
			getLocation(location);		
			break;
		}
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}


	//================================== Location Cordinate ====================================

	void getLocation(final String location)
	{
		progressDialog=new ProgressDialog(MapActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CUSTOMER_LOCATION;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				//RegisterActivities.removeAllActivities();
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
						Toast.makeText(MapActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 				
				params.put("country_name",location);
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}

	private void parseResponse(String response)
	{
		double lat = 0;
		double lng= 0;

		try {

			JSONObject parentObject = new JSONObject(response);
			JSONArray parentArray = parentObject.getJSONArray("output");		

			for(int i=0;i<parentArray.length();i++){

				CordinateData cordinate = new CordinateData();
				JSONObject innerObj = parentArray.getJSONObject(i);
				JSONObject records = innerObj.getJSONObject("innerObj");
				cordinate.setLatitude(records.optString("latitde"));
				cordinate.setLongitude(records.optString("longitude"));

				if(records.optString("latitde") !=null && !records.optString("latitde").equalsIgnoreCase(""))
					lat = Double.parseDouble(records.optString("latitde"));

				if(records.optString("longitude") !=null && !records.optString("longitude").equalsIgnoreCase(""))
					lng = Double.parseDouble(records.optString("longitude"));

				if(googleMap !=null){

					googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, lng)));
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
