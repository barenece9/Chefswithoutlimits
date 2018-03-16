package com.chefswithoutlimits.customerchef.activity.other;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.MenuCatogoryVO;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.CommonMethodes;
import com.chefswithoutlimits.util.CreateDialog;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChefMenuAdd extends Activity implements OnClickListener{

	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;
	Button btnBrowse;
	ImageView imgItem;
	Spinner menuAddType;
	Spinner menuAddActive;
	Spinner menuAddAvailable;
	EditText chefItemName;
	EditText chefItemDescription;
	EditText chefItemRegularPrice;
	EditText chefItemSalePrice;
	EditText chefEditItemAvailable;
	Button btnMenuAdd;


	int REQUEST_CAMERA = 1,SELECT_FILE = 2;
	//Bitmap bmp;	
	Bitmap bm;
	private ProgressDialog dialog;
	MultipartEntity entity;

	private ProgressDialog progressDialog,progressDialog1;
	ArrayAdapter<String> adapter,adapterActive,adapterAvailable;
	UserInformation userInfo;
	ArrayList<MenuCatogoryVO> menuList = new ArrayList<MenuCatogoryVO>();
	ArrayList<String> menuListStr = new ArrayList<String>();
	ArrayList<String> menuActive = new ArrayList<String>();

	String itemName = "";
	String itemDescription = "";
	String regularPrice = "";
	String salePrice = "";
	String menuType = "";
	String availableItem = "";
	String active = "1";
	String encodedImage = "";
	String available = "1";

	int MY_SOCKET_TIMEOUT_MS = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chef_add_menu);

		userInfo = Sharepreferences.getSharePreferance(this);

		menuActive.add("Yes");
		menuActive.add("No");

		setWidget();
		doCategoryList();
	}

	private void setWidget()
	{
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("ADD MENU");

		btnBrowse = (Button)findViewById(R.id.btnBrowse);
		btnBrowse.setOnClickListener(this);

		imgItem = (ImageView)findViewById(R.id.imgItem);
		btnMenuAdd = (Button)findViewById(R.id.btnMenuAdd);
		btnMenuAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemName = chefItemName.getText().toString();
				itemDescription = chefItemDescription.getText().toString();
				regularPrice = chefItemRegularPrice.getText().toString();
				salePrice = chefItemSalePrice.getText().toString();	
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

					CreateDialog.showDialog(ChefMenuAdd.this, "Enter menu type");

				}else if(itemName.equalsIgnoreCase("")){

					CreateDialog.showDialog(ChefMenuAdd.this, "Enter item name");

				}else if(itemDescription.equalsIgnoreCase("")){

					CreateDialog.showDialog(ChefMenuAdd.this, "Enter item description");

				}else if(regularPrice.equalsIgnoreCase("")){

					CreateDialog.showDialog(ChefMenuAdd.this, "Enter item regular price");

				}else if(salePrice.equalsIgnoreCase("")){

					CreateDialog.showDialog(ChefMenuAdd.this, "Enter item sale price");

				}else if(availableItem.equalsIgnoreCase("")){
					CreateDialog.showDialog(ChefMenuAdd.this, "Enter item available no");
				}else if(encodedImage.equalsIgnoreCase("")){
					//CreateDialog.showDialog(ChefMenuAdd.this, "Select image");
					
					showDialog("Upload menu with out image ?");
				}
				else{
					//new ImageUploadTask().execute();
					uploadMenu();
				}		

			}
		});

		chefItemName = (EditText)findViewById(R.id.chefItemName);
		chefItemDescription = (EditText)findViewById(R.id.chefItemDescription);
		chefItemRegularPrice = (EditText)findViewById(R.id.chefItemRegularPrice);
		chefItemSalePrice = (EditText)findViewById(R.id.chefItemSalePrice);
		chefEditItemAvailable = (EditText)findViewById(R.id.chefEditItemAvailable);

		menuAddType = (Spinner)findViewById(R.id.menuAddType);
		menuAddType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				menuType = menuListStr.get(position);		
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		adapter =new ArrayAdapter<String>(ChefMenuAdd.this,R.layout.spinner_rows, menuListStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuAddType.setAdapter(adapter);

		menuAddActive = (Spinner)findViewById(R.id.menuAddActive);
		menuAddActive.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if(menuActive.get(position).equalsIgnoreCase("Yes"))
					active = "1";	
				else
					active = "0";	
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		adapterActive =new ArrayAdapter<String>(ChefMenuAdd.this,R.layout.spinner_rows, menuActive);
		adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuAddActive.setAdapter(adapterActive);
		
		menuAddAvailable = (Spinner)findViewById(R.id.menuAddAvailable);
		menuAddAvailable.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if(menuActive.get(position).equalsIgnoreCase("Yes"))
					available = "1";	
				else
					available = "0";	
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		adapterAvailable =new ArrayAdapter<String>(ChefMenuAdd.this,R.layout.spinner_rows, menuActive);
		adapterAvailable.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		menuAddAvailable.setAdapter(adapterAvailable);
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

		case R.id.btnBrowse:		
			selectImage();
			break;

		}
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(ChefMenuAdd.this);
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
				//Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				bm = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				//thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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


	//====================================== Category Item ======================================================

	void doCategoryList()
	{
		progressDialog1=new ProgressDialog(ChefMenuAdd.this);
		progressDialog1.setMessage("loading...");
		progressDialog1.show(); 
		progressDialog1.setCancelable(false);
		progressDialog1.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.MENU_CATEGORY;

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
						Toast.makeText(ChefMenuAdd.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
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
	}

	//====================================== Category Item ======================================================

	void uploadMenu()
	{
		progressDialog1=new ProgressDialog(ChefMenuAdd.this);
		progressDialog1.setMessage("loading...");
		progressDialog1.show(); 
		progressDialog1.setCancelable(false);
		progressDialog1.setCanceledOnTouchOutside(false);
		String url;

		url =WebServiceURL.CHEF_MENU_ADD;    	

		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
				new Response.Listener<String>() 
				{
			@Override
			public void onResponse(String response) {
				parseAddMenu(response);
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
						Toast.makeText(ChefMenuAdd.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{   					



				Map<String, String>  params = new HashMap<String, String>(); 				
				params.put("userid",userInfo.getUserId());					
				params.put("menu_name",itemName);
				params.put("menu_category",menuType);
				params.put("menu_description",itemDescription);
				params.put("menu_shortdescription","");
				params.put("regular_price",regularPrice);
				params.put("sale_price",salePrice);
				params.put("available_for_delivery",available);
				params.put("max_quantity_available",availableItem);
				params.put("weight","");
				params.put("active",active);
				params.put("currency","");
				params.put("menu_image",encodedImage);
				params.put("format","json");					


				return params;
			}
		};

		/*postRequest.setRetryPolicy(new DefaultRetryPolicy(
			        0, 
			        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
			        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

		// Adding request to volley request queue
		postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
				0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	public void parseAddMenu(String response)
	{
		try{

			JSONObject parentObj = new JSONObject(response);
			JSONArray jArray = parentObj.getJSONArray("output");
			JSONObject messageObj = jArray.getJSONObject(0);
			String message = messageObj.optString("message");
			Toast.makeText(ChefMenuAdd.this, message, Toast.LENGTH_LONG).show();

		}catch(JSONException e){

		}

	}

	//============================================================================================


	/*class ImageUploadTask extends AsyncTask<Void, Void, Void> {

		String sResponse = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(ChefMenuAdd.this, "Uploading",
					"Please wait...", true);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {				 				

				String url = WebServiceURL.CHEF_MENU_ADD;
				//int i = Integer.parseInt(params[0]);
				//Bitmap bitmap = decodeFile(map.get(i));
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);
				entity = new MultipartEntity();

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				//bitmap.compress(CompressFormat.JPEG, 100, bos);

				bm.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();

				entity.addPart("menu_name", new StringBody(itemName));
				entity.addPart("menu_category", new StringBody(menuType));
				entity.addPart("menu_description", new StringBody(itemDescription));
				entity.addPart("menu_shortdescription", new StringBody(""));
				entity.addPart("regular_price", new StringBody(regularPrice));
				entity.addPart("sale_price", new StringBody(salePrice));
				entity.addPart("available_for_delivery ", new StringBody(availableItem));
				entity.addPart("active ", new StringBody(active));
				entity.addPart("menu_image", new ByteArrayBody(data,"image/jpeg"));

				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				sResponse = EntityUtils.getContentCharSet(response.getEntity());

				System.out.println("sResponse : " + sResponse);
			} catch (Exception e) {
				if (dialog.isShowing())
					dialog.dismiss();
				Log.e(e.getClass().getName(), e.getMessage(), e);

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void Response) {
			try {
				if (dialog.isShowing())
					dialog.dismiss();

				Toast.makeText(getApplicationContext(), sResponse.toString(),
						Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}

		}
	}

	public Bitmap decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
		return bitmap;
	}*/

	public void showDialog(String alertText)
	{
		new AlertDialog.Builder(this)
	    .setTitle("Alert!")
	    .setMessage(alertText)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	uploadMenu();
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
		finish();
	}
}
