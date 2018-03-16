package com.chefswithoutlimits.customerchef.activity.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.customer.cart.CartListActivity;
import com.chefswithoutlimits.customerchef.adapter.ItemListAdapter;
import com.chefswithoutlimits.customerchef.dataVO.ItemListData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemListActivity extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	ImageView imgCart;
	TextView headerTxt;
	ListView itemlistView;
	TextView txtCusName;
	ItemListAdapter adapter;
	ArrayList<ItemListData> itemData;
	UserInformation userInfo;
	private ProgressDialog progressDialog;
	Button btnAddCart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_list);

		itemData = new ArrayList<ItemListData>();

		/*ItemListData obj1 = new ItemListData();		
		itemData.add(obj1);			
		itemData.add(obj1);
		itemData.add(obj1);
		itemData.add(obj1);*/

		userInfo = Sharepreferences.getSharePreferance(this);

		setWidget();
		setAdapter();
		doItemList();
	}


	public void setWidget()
	{	

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		imgCart = (ImageView)findViewById(R.id.imgCart);
		imgCart.setVisibility(View.VISIBLE);
		imgCart.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("SEARCH RESULT");

		itemlistView = (ListView)findViewById(R.id.itemlistView);
		txtCusName = (TextView)findViewById(R.id.txtCusName);
		txtCusName.setText(userInfo.getFirstName()+" "+userInfo.getLastName());
		
		btnAddCart = (Button)findViewById(R.id.btnAddCart);
		btnAddCart.setOnClickListener(this);
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
			
		case R.id.btnAddCart:
			//JSONObject cartItem =
			 createJsonData();
		//	doItemOrder(cartItem.toString());
			
			break;
			
		case R.id.imgCart:
			
			startActivity(new Intent(this,CartListActivity.class));
			
			break;

		}
	}

	public void setAdapter()
	{
		adapter = new ItemListAdapter(ItemListActivity.this, itemData);
		itemlistView.setAdapter(adapter);
	}

	//===============Register Customer Function===================
	void doItemList()
	{
		progressDialog=new ProgressDialog(ItemListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		//url = WebServiceURL.ITEM_URL;
		url="https://chefswithoutlimits.com/webservices/items_service.php";

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
						Toast.makeText(ItemListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   						
				Map<String, String>  params = new HashMap<String, String>(); 										
				params.put("format","json");
				return params;
			}
		};
		// Adding request to volley request queue
		AppController.getInstance().addToRequestQueue(postRequest);
	}

	void parseResponse(String response){	
		
		if(itemData.size()>0)
			itemData.clear();

		try {

			JSONObject parentObject = new JSONObject(response);
			JSONArray parentArray = parentObject.getJSONArray("output");				


			if(parentArray.length()>1){


				for(int i=0;i<parentArray.length();i++){

					ItemListData itemListObj = new ItemListData();	

					JSONObject  itemObj = parentArray.optJSONObject(i);

					JSONObject reocrdObj = itemObj.optJSONObject("records");

					itemListObj.setItemId(reocrdObj.optString("item_id"));
					itemListObj.setImageUrl(reocrdObj.optString("item_image"));
					itemListObj.setItemName(reocrdObj.optString("item_name"));

					itemData.add(itemListObj);
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		adapter.notifyDataSetChanged();

	}
	
	//===============================  Create JSON ==================================================
	
	String obj = "";
	public void createJsonData()
	{
		JSONObject parentObject = new JSONObject();
		
		JSONArray parentArray = new JSONArray();
		
		if(adapter == null)
		{
			Toast.makeText(ItemListActivity.this, "Item list is not available...", Toast.LENGTH_LONG).show();
			
		}else{
			
			ArrayList<ItemListData> tempItem = adapter.getTempList();
			
						
			for(int size = 0;size<tempItem.size();size++){
				
				ItemListData product = tempItem.get(size);
				JSONObject childObject = new JSONObject();
				JSONObject innerObject = new JSONObject();
				
				try {
					childObject.put("item_id", product.getItemId());
					childObject.put("item_quantity", String.valueOf(product.getQuantity()));
					//childObject.put("user_id", userInfo.getUserId());
					//childObject.put("item_name", product.getItemName());
					//childObject.put("item_image", product.getImageUrl());
					
					
					innerObject.put("item", childObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				parentArray.put(innerObject);
			}
			
			try {
				parentObject.put("itemlist", parentArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*if(tempItem.size()>0)
				doItemOrder(parentObject.toString());
			else
				Toast.makeText(ItemListActivity.this, "Item quantity is not set...", Toast.LENGTH_LONG).show();*/
		}
		obj = parentObject.toString();
		new RequestTask().execute("");
		//return parentObject; 
	}
	
	//==================================== Order submit ===========================================

	
	public void parse(String parse)
	{
		try {
			
			JSONObject parentObject = new JSONObject(parse);
			
			JSONArray parentArray = parentObject.getJSONArray("output");
			JSONObject  obj = parentArray.optJSONObject(0);
			String message = obj.getString("message");		
			Toast.makeText(ItemListActivity.this, message, Toast.LENGTH_LONG).show();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
class RequestTask extends AsyncTask<String, String, String>{
		
		StringBuilder stringBuilder;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			progressDialog=new ProgressDialog(ItemListActivity.this);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

	    @Override
	    protected String doInBackground(String... uri) {
	    	
	    	// Create a new HttpClient and Post Header
	    	
	    	
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(WebServiceURL.ORDER_URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair("user_id",userInfo.getUserId()));
		        nameValuePairs.add(new BasicNameValuePair("Itemlist",obj));
		        nameValuePairs.add(new BasicNameValuePair("format","json"));
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
		        InputStream inputStream = response.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }

                

		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
	       
	       // return responseString;
		    return stringBuilder.toString();
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        //Do anything with response..
	        
	        if(progressDialog!=null)
				progressDialog.dismiss();		
	        
	        parse(result);
	    }
	}
}
