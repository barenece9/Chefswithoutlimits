package com.chefswithoutlimits.customerchef.activity.other;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefMenuCategoryActivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.MenuCatogoryVO;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CommonMethodes;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChefEditMenuActivity extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	Button btnBrowse;
	Button btnMenuEdit;
	NetworkImageView imgItem;
	Spinner menuEditType;
	Spinner menuEditActive;
	Spinner menuEditAvailable;
	ArrayList<MenuCatogoryVO> menuList = new ArrayList<MenuCatogoryVO>();
	ArrayList<String> menuListStr = new ArrayList<String>();
	ArrayList<String> menuActive = new ArrayList<String>();
	ArrayAdapter<String> adapter,adapterActive,adapterAvailable;
	int REQUEST_CAMERA = 1,SELECT_FILE = 2;
	Bitmap bmp;
	String menuId = "";
	String menuImage = "";
	UserInformation userInfo;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private ProgressDialog progressDialog,progressDialog1,progressDialog2;

	EditText chefEditItemName;
	EditText chefEditItemRegularPrice;
	EditText chefEditItemSalePrice;
	EditText chefEditItemDescription;
	EditText chefEditItemAvailable;
	String categoryNo = "";
	String imageUrl = "";
	String active = "1";
	String kitchenId = "";
	String available = "1";
	
	String itemName = "";
	String itemDescription = "";
	String regularPrice = "";
	String salePrice = "";
	String menuType = "";
	String availableItem = "";
	
	String encodedImage = "";
	
	Bitmap bm;
	int MY_SOCKET_TIMEOUT_MS = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_edit_menu);

		menuId = getIntent().getStringExtra("menuId");
		menuImage = getIntent().getStringExtra("meueImage");
		//kitchenId = getIntent().getStringExtra("kitchenId");
		userInfo = Sharepreferences.getSharePreferance(this);
		
		menuActive.add("Yes");
		menuActive.add("No");

		setWidget();
		
		getMenuItem();
		
	}

	private void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(InternetConnectivity.isConnectedFast(ChefEditMenuActivity.this)){
					startActivity(new Intent(ChefEditMenuActivity.this,ChefMenuCategoryActivity.class));
					finish();
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}

			}
		});
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("EDIT MENU");

		btnBrowse = (Button)findViewById(R.id.btnBrowse);
		btnBrowse.setOnClickListener(this);
		
		btnMenuEdit = (Button)findViewById(R.id.btnMenuEdit);
		btnMenuEdit.setOnClickListener(this);

		imgItem = (NetworkImageView)findViewById(R.id.imgItem);

		chefEditItemName = (EditText)findViewById(R.id.chefEditItemName);
		chefEditItemRegularPrice = (EditText)findViewById(R.id.chefEditItemRegularPrice);
		chefEditItemSalePrice = (EditText)findViewById(R.id.chefEditItemSalePrice);
		chefEditItemDescription = (EditText)findViewById(R.id.chefEditItemDescription);
		chefEditItemAvailable = (EditText)findViewById(R.id.chefEditItemAvailable);

		menuEditType = (Spinner)findViewById(R.id.menuEditType);
		menuEditType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//menuType = menuListStr.get(position);	
				
				if(position !=0){
					
					menuType = String.valueOf(position - 1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		adapter =new ArrayAdapter<String>(ChefEditMenuActivity.this,R.layout.spinner_rows, menuListStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuEditType.setAdapter(adapter);
		
		menuEditActive = (Spinner)findViewById(R.id.menuEditActive);
		menuEditActive.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//menuType = menuListStr.get(position);	
				
				if(position == 0)
					active = "1";
				else
					active = "0";
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		adapterActive =new ArrayAdapter<String>(ChefEditMenuActivity.this,R.layout.spinner_rows, menuActive);
		adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuEditActive.setAdapter(adapterActive);
		
		menuEditAvailable = (Spinner)findViewById(R.id.menuEditAvailable);
		menuEditAvailable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				if(position == 0)
					available = "1";
				else
					available = "0";
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		adapterAvailable =new ArrayAdapter<String>(ChefEditMenuActivity.this,R.layout.spinner_rows, menuActive);
		adapterAvailable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuEditAvailable.setAdapter(adapterAvailable);
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {


		case R.id.btnLogout:

			Logout.logOut(this);
			finish();
			break;	

		case R.id.btnBrowse:		
			selectImage();
			break;
		case R.id.btnMenuEdit:
			
			itemName = chefEditItemName.getText().toString();
			itemDescription = chefEditItemDescription.getText().toString();
			regularPrice = chefEditItemRegularPrice.getText().toString();
			salePrice = chefEditItemSalePrice.getText().toString();	
			availableItem = chefEditItemAvailable.getText().toString();	

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Bitmap bitmap = ((BitmapDrawable)imgItem.getDrawable()).getBitmap();				

			if(bitmap !=null){

				/*bitmap.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				encodedImage = Base64.encodeToString(data, Base64.DEFAULT);*/
				encodedImage= CommonMethodes.getStringImage(bitmap);
			}

			System.out.print("Base64 String =====:"+encodedImage);

			if(menuType.equalsIgnoreCase("Select Menu Type")){

				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter menu type");

			}else if(itemName.equalsIgnoreCase("")){

				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter item name");

			}else if(itemDescription.equalsIgnoreCase("")){

				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter item description");

			}else if(regularPrice.equalsIgnoreCase("")){

				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter item regular price");

			}else if(salePrice.equalsIgnoreCase("")){

				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter item sale price");

			}else if(availableItem.equalsIgnoreCase("")){
				CreateDialog.showDialog(ChefEditMenuActivity.this, "Enter item available no");
			}else if(encodedImage.equalsIgnoreCase("")){
				//CreateDialog.showDialog(ChefMenuAdd.this, "Select image");
				
				showDialog("Upload menu with out image ?");
			}
			else{
				//new ImageUploadTask().execute();
				if(InternetConnectivity.isConnectedFast(ChefEditMenuActivity.this)){
					editMenu();
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}

			}
			
			break;

		}
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(ChefEditMenuActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				bm = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 90, bytes);

				File destination = new File(Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");

				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				imgItem.setImageBitmap(bm);

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaColumns.DATA };
				CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null,
						null);
				Cursor cursor =cursorLoader.loadInBackground();
				int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();

				String selectedImagePath = cursor.getString(column_index);
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);

				imgItem.setImageBitmap(bm);

			}
		}


	}


	//========================================= Get Menu Detail =========================================

	void getMenuItem()
	{
		progressDialog=new ProgressDialog(ChefEditMenuActivity.this);
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
						Toast.makeText(ChefEditMenuActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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

			active = dataObj.optString("active");
			categoryNo = dataObj.optString("menu_category");
			available = dataObj.optString("available_for_delivery");
			//imageUrl = "http://chefswithoutlimits.com/"+dataObj.optString("menu_image");
		//	imgItem.setImageUrl(imageUrl, imageLoader);

			chefEditItemName.setText(dataObj.optString("menu_name"));
			chefEditItemDescription.setText(dataObj.optString("menu_description"));
			chefEditItemRegularPrice.setText(dataObj.optString("regular_price"));
			chefEditItemSalePrice.setText(dataObj.optString("sale_price"));
			chefEditItemAvailable.setText(dataObj.optString("max_quantity_available"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//new DownloadImageTask(imgItem).execute(menuImage);
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		imgItem.setImageUrl(menuImage, imageLoader);
		
		if(active.equalsIgnoreCase("1")){
			menuEditActive.setSelection(0);
		}else{
			menuEditActive.setSelection(1);
		}
		
		if(available.equalsIgnoreCase("1")){
			menuEditAvailable.setSelection(0);
		}else{
			menuEditAvailable.setSelection(1);
		}
		
		doCategoryList();

	}
	
	//====================================== Category Item ======================================================
	
	void doCategoryList()
	{
		progressDialog1=new ProgressDialog(ChefEditMenuActivity.this);
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
						Toast.makeText(ChefEditMenuActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
		  menuEditType.setSelection(Integer.parseInt(categoryNo)+1);
		menuType = menuListStr.get(Integer.parseInt(categoryNo)+1);	
	}

	//======================== Image Download =====================================
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	    private static final int IO_BUFFER_SIZE = 4 * 1024;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0]; 
	        try {
	        URL url = new URL(urldisplay);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        
	        return myBitmap;
	        }catch (IOException e) {
	            Log.e("Log", "Could not load Bitmap from: " + urldisplay);
	        }
	        return null;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
	//====================================== Category Item ======================================================
	
	void editMenu()
	{
		progressDialog2=new ProgressDialog(ChefEditMenuActivity.this);
		progressDialog2.setMessage("loading...");
		progressDialog2.show(); 
		progressDialog2.setCancelable(false);
		progressDialog2.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_MENU_EDIT;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseEditMenu(response);
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
						Toast.makeText(ChefEditMenuActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   					
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				/*Bitmap bitmap = ((BitmapDrawable)imgItem.getDrawable()).getBitmap();
				
				String encodedImage = "";
				
				if(bitmap !=null){
					
					bitmap.compress(CompressFormat.JPEG, 100, bos);
					byte[] data = bos.toByteArray();
					encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
				}*/
				
				System.out.print("Base64 String =====:"+encodedImage);
				
				Map<String, String>  params = new HashMap<String, String>(); 				
				params.put("userid",userInfo.getUserId());					
				params.put("menu_name",itemName);
				params.put("menu_category",menuType);
				params.put("regular_price",regularPrice);
				params.put("menu_description",itemDescription);
				params.put("sale_price",salePrice);
				params.put("available_for_delivery",available);
				params.put("max_quantity_available",availableItem);
				params.put("active",active);
				params.put("menu_image",encodedImage);
				params.put("menu_id",menuId);
				params.put("format","json");			
				
				//params.put("menu_shortdescription","");					
				//params.put("weight","");				
				return params;
			}
		};

		postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
				0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}
	
	public void parseEditMenu(String response)
	{
		try{
			
			JSONObject parentObj = new JSONObject(response);
			JSONArray jArray = parentObj.getJSONArray("output");
			JSONObject messageObj = jArray.getJSONObject(0);
			String message = messageObj.optString("message");
			Toast.makeText(ChefEditMenuActivity.this, message, Toast.LENGTH_LONG).show();
			
		}catch(JSONException e){
			
			System.out.println("Error============:"+e);
		}
		
	}
	
	public void showDialog(String alertText)
	{
		new AlertDialog.Builder(this)
	    .setTitle("Alert!")
	    .setMessage(alertText)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	editMenu();
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(ChefEditMenuActivity.this)){
			startActivity(new Intent(ChefEditMenuActivity.this,ChefMenuCategoryActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}
	}
}
