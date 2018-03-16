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
import com.chefswithoutlimits.customerchef.adapter.ChefMenuItemListAdapter;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.ChefMenuItem;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChefMenuActivity extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;	
	TextView headerTxt;

	Button btnAddMenu;
	Button btnEditMenu;
	ListView listViewMenu;
	private ProgressDialog progressDialog;
	String userId = "";

	UserInformation userInfo;
	ArrayList<ChefMenuItem> menuItem = new ArrayList<ChefMenuItem>();
	ChefMenuItemListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_menu);

		userInfo = Sharepreferences.getSharePreferance(this);

		setWidget();
		getMenuItem();
		setAdapter();
	}

	private void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("MENU");


		btnAddMenu = (Button)findViewById(R.id.btnAddMenu);
		btnAddMenu.setOnClickListener(this);
		btnEditMenu = (Button)findViewById(R.id.btnEditMenu);
		btnEditMenu.setOnClickListener(this);
		listViewMenu = (ListView)findViewById(R.id.listViewMenu);
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

		case R.id.btnAddMenu:		

			startActivity(new Intent(this,ChefMenuAdd.class));

			break;
		case R.id.btnEditMenu:		

			break;

		}
	}

	//========================================= Get Menu Item =========================================

	void getMenuItem()
	{
		progressDialog=new ProgressDialog(ChefMenuActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.LISTING_MENU;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {

				parseData(response);
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
						Toast.makeText(ChefMenuActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 	

				params.put("user_id",userInfo.getUserId());
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}

	public void parseData(String response)
	{
	 if(menuItem.size()>0)
		 menuItem.clear();
		try {

			JSONObject parentJSON = new JSONObject(response);
			String Status=parentJSON.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				JSONArray parentArr = parentJSON.optJSONArray("Records");

				for (int i = 1; i < parentArr.length(); i++) {

					ChefMenuItem object = new ChefMenuItem();

					JSONObject dataObj = parentArr.optJSONObject(i);
					//JSONObject dataObj = childObj.optJSONObject("records");

				/*object.setMenuId(dataObj.optString("id"));
				object.setKitchenId(dataObj.optString("kitchen_id"));
				object.setMenuName(dataObj.optString("menu_name"));
				object.setMenuCategory(dataObj.optString("menu_category"));
				object.setMenuImage(dataObj.optString("menu_image"));
				object.setMenuDescription(dataObj.optString("menu_description"));
				object.setMenuShortDesc(dataObj.optString("menu_shortdescription"));
				object.setMenuSKU(dataObj.optString("sku"));
				object.setCurrency(dataObj.optString("currency"));
				object.setRegularPrice(dataObj.optString("regular_price"));
				object.setSalesPrice(dataObj.optString("sale_price"));
				object.setAvailabledelevery(dataObj.optString("available_for_delivery"));
				object.setMaxQuantPurchase(dataObj.optString("max_quantity_purchasable"));
				object.setMaxQuantAvilable(dataObj.optString("max_quantity_available"));
				object.setWeight(dataObj.optString("weight"));
				object.setActive(dataObj.optString("active"));*/

					object.setMenuId(dataObj.optString("menu_catid"));
					//object.setMenuImage(dataObj.optString("item_image"));
					object.setMenuName(dataObj.optString("cat_name"));
					//object.setSalesPrice(dataObj.optString("sale_price"));
					//object.setCurrency(dataObj.optString("currency"));
					//object.setRegularPrice(dataObj.optString("normal_price"));
					//object.setKitchenId(dataObj.optString("kitchen_id"));

					menuItem.add(object);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	public void setAdapter()
	{
		adapter = new ChefMenuItemListAdapter(this, menuItem);
		listViewMenu.setAdapter(adapter);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		getMenuItem();
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
