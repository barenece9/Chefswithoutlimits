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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.dataVO.MenuCatogoryVO;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefMenuItemDetail extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;	
	TextView headerTxt;

	NetworkImageView itemImage;
	TextView textMenuName;
	TextView textMenuDesc;
	TextView textMenuRegPrice;
	TextView textMenuSalesPrice;

	Spinner menuCategory;
	private ProgressDialog progressDialog,progressDialog1;
	ArrayAdapter<String> adapter;
	String menuId = "";
	String menuImage = "";
	UserInformation userInfo;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	ArrayList<MenuCatogoryVO> menuList = new ArrayList<MenuCatogoryVO>();
	ArrayList<String> menuListStr = new ArrayList<String>();
	
	String categoryNo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_menudetail);
		menuId = getIntent().getStringExtra("menuId");
		menuImage = getIntent().getStringExtra("meueImage");
		userInfo = Sharepreferences.getSharePreferance(this);
		setWidget();
		getMenuItem();
		
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		itemImage.setImageUrl(menuImage, imageLoader);
	}

	private void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("MENU DETAIL");
		menuCategory = (Spinner)findViewById(R.id.menuCategory);

		itemImage = (NetworkImageView)findViewById(R.id.itemImage);
		textMenuName = (TextView)findViewById(R.id.textMenuName);
		textMenuDesc = (TextView)findViewById(R.id.textMenuDesc);
		textMenuRegPrice = (TextView)findViewById(R.id.textMenuRegPrice);
		textMenuSalesPrice = (TextView)findViewById(R.id.textMenuSalesPrice);
		
		adapter =new ArrayAdapter<String>(ChefMenuItemDetail.this,R.layout.spinner_rows, menuListStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuCategory.setAdapter(adapter);
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
	
	//========================================= Get Menu Detail =========================================
	
		void getMenuItem()
		{
			progressDialog=new ProgressDialog(ChefMenuItemDetail.this);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			String url;

			url = WebServiceURL.DETAIL_MENU;

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
							Toast.makeText(ChefMenuItemDetail.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
						}
					}
					){     
				@Override
				protected Map<String, String> getParams()
				{   						
					Map<String, String>  params = new HashMap<String, String>(); 	

					params.put("menuid",menuId);				
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

				JSONObject parentJSON = new JSONObject(response);
				JSONArray parentArr = parentJSON.optJSONArray("output");

				JSONObject childObj = parentArr.optJSONObject(1);
				JSONObject dataObj = childObj.optJSONObject("records");
				
				categoryNo = dataObj.optString("menu_category");
				//itemImage.setImageUrl("http://chefswithoutlimits.com/"+dataObj.optString("menu_image"), imageLoader);
				
				textMenuName.setText(dataObj.optString("menu_name"));
				textMenuDesc.setText(dataObj.optString("menu_description"));
				textMenuRegPrice.setText(dataObj.optString("currency")+" "+dataObj.optString("regular_price"));
				textMenuSalesPrice.setText(dataObj.optString("currency")+" "+dataObj.optString("sale_price"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			doCategoryList();
		}
		
		//====================================== Category Item ======================================================
		
		void doCategoryList()
		{
			progressDialog1=new ProgressDialog(ChefMenuItemDetail.this);
			progressDialog1.setMessage("loading...");
			progressDialog1.show(); 
			progressDialog1.setCancelable(false);
			progressDialog1.setCanceledOnTouchOutside(false);
			String url;

			url =WebServiceURL.MENU_CATEGORY;    	

			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					new Response.Listener<String>() 
					{
				@Override
				public void onResponse(String response) {
					parseCategoryList(response);
					if(progressDialog1!=null)
						progressDialog1.dismiss();				
					Log.d("Response", response);                        
				}
					}, 
					new Response.ErrorListener() 
					{
						@Override
						public void onErrorResponse(VolleyError error) {
							if(progressDialog1!=null)
								progressDialog1.dismiss();
							System.out.println("Error=========="+error);
							Toast.makeText(ChefMenuItemDetail.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
						}
					}
					){     
				@Override
				protected Map<String, String> getParams()
				{   						
					Map<String, String>  params = new HashMap<String, String>(); 				
					params.put("userid",userInfo.getUserId());
					params.put("format","json");
					return params;
				}
			};
			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(postRequest);           
		}
		
		public void parseCategoryList(String response)
		{
			try {
				
				JSONObject parentObj = new JSONObject(response);
				
				JSONArray jArray = parentObj.getJSONArray("output");
				
				//JSONObject childObj = jArray.getJSONObject(0);
				
				MenuCatogoryVO menuCategory = new MenuCatogoryVO();
				menuCategory.setCategoryId("-1");
				menuCategory.setCategoryName("Select Menu Type");
				menuList.add(menuCategory);
				menuListStr.add("Select Menu Type");
				
				for(int i=1;i<jArray.length();i++){
					
					MenuCatogoryVO menuType = new MenuCatogoryVO();
					
					JSONObject innerObj = jArray.optJSONObject(i);
					JSONObject records = innerObj.optJSONObject("records");
					
					menuType.setCategoryId(records.optString("menu_catid"));
					menuType.setCategoryName(records.optString("cat_name"));

					
					menuList.add(menuType);
					
					menuListStr.add(records.optString("cat_name"));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			adapter.notifyDataSetChanged();
			
			if(!categoryNo.equalsIgnoreCase(""))			
			   menuCategory.setSelection(Integer.parseInt(categoryNo)+1);
		}


	@Override
	public void onBackPressed() {
		finish();
	}
}
