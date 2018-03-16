package com.chefswithoutlimits.customerchef.activity.customer.cart;

import java.io.UnsupportedEncodingException;
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
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.circleimageview.CircleImageView;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ScheduledDeliveryActivity;
import com.chefswithoutlimits.customerchef.activity.customer.search.SearchKitchen;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.ShippingScreen;
import com.chefswithoutlimits.customerchef.adapter.CartItemDetailsAdapter;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.customerchef.dataVO.Payment;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CartListActivity extends Activity implements OnClickListener{

	CircleImageView imageView2;
	ImageView btnBack;
	ImageView btnLogout;
	ImageView imgCart;
	TextView headerTxt;
	ListView itemlistView;
	TextView txtCusName;
	CartItemDetailsAdapter adapter;
	ArrayList<HashMap<String,String>> itemData;
	UserInformation userInfo;
	private ProgressDialog progressDialog;
	TextView txt_tax,txt_total;
	RadioGroup order_type;
	RadioButton btn_immediate_delivery,btn_scheduled_order;
	Button btn_shoping,btn_clear,btn_next;

	TextView txt_immediate,txt_scheduled;
	String kitchenID="";
	String kichen_name="";
	int sum = 0;
	String ordertype="";

	TextView txt_tip;
	EditText etn_tip;
	Spinner spinner_type;
	Button btn_add_tip;


	ArrayList<String> category=new ArrayList<>();
	ArrayAdapter<String> adaptercategory;
	String selectcategory="";
	EditText dialog_edit_tex;
	String tip_value="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cart_list);
		itemData = new ArrayList<>();
		setWidget();
	}


	public void setWidget()
	{


		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("CART ITEM");

		itemlistView = (ListView)findViewById(R.id.cartitemlistView);
		txtCusName = (TextView)findViewById(R.id.txtCusName);
		userInfo = Sharepreferences.getSharePreferance(this);
		txtCusName.setText(userInfo.getFirstName()+" "+userInfo.getLastName());
		imageView2=(CircleImageView)findViewById(R.id.imageView2);
		Picasso.with(this)
				.load(WebServiceURL.IMAGE_PATH+""+userInfo.getUser_log())
				.placeholder(R.drawable.ic_person_black_24dp)   // optional
				.error(R.drawable.ic_person_black_24dp)      // optional
				.resize(400,400)                        // optional
				.into(imageView2);

		btn_immediate_delivery=(RadioButton)findViewById(R.id.btn_immediate_delivery);
		btn_immediate_delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					ordertype="IM";
				}else {
					//ordertype="";
				}
			}
		});
		btn_scheduled_order=(RadioButton)findViewById(R.id.btn_scheduled_order);
		btn_scheduled_order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					ordertype="SC";
				}else {
					//ordertype="";
				}
			}
		});


		txt_tax = (TextView)findViewById(R.id.txt_tax);
		txt_total = (TextView)findViewById(R.id.txt_total);

		txt_tip=(TextView)findViewById(R.id.txt_tip);
		etn_tip=(EditText)findViewById(R.id.etn_tip);
		spinner_type=(Spinner)findViewById(R.id.spinner_type);


		//category.add("category");
		//category.add("Delivery");
		//category.add("Pickup");

		adaptercategory = new ArrayAdapter<String>(CartListActivity.this,R.layout.spinner_rows, category);
		adaptercategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_type.setAdapter(adaptercategory);

		spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				String checkselectavailablity=category.get(position);
				if(checkselectavailablity.equalsIgnoreCase("Delivery")){
					selectcategory="Delivery";
				}else if(checkselectavailablity.equalsIgnoreCase("Pickup")){
					selectcategory="Pickup";
				}else {
					if(!checkselectavailablity.equalsIgnoreCase("")) {
						selectcategory = checkselectavailablity;
					}
				}

				System.out.println("Service Type Sart  ###  "+selectcategory);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btn_add_tip=(Button)findViewById(R.id.btn_add_tip);
		btn_add_tip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(),"Add Tips",Toast.LENGTH_SHORT).show();
				addTipDilog();
			}
		});

		txt_scheduled=(TextView)findViewById(R.id.txt_scheduled);
		txt_immediate=(TextView)findViewById(R.id.txt_immediate);

		btn_shoping = (Button)findViewById(R.id.btn_shoping);
		btn_shoping.setOnClickListener(this);

		btn_clear = (Button)findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);

		btn_next = (Button)findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);

		doCartItemList();
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {


		case R.id.btnBack:


			if(InternetConnectivity.isConnectedFast(CartListActivity.this)){
				startActivity(new Intent(CartListActivity.this,CustomerDashboardActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btnLogout:
			
			Logout.logOut(this);
			finish();

			break;	
			
		case R.id.btn_shoping:



			if(InternetConnectivity.isConnectedFast(CartListActivity.this)){
				Intent intent=new Intent(CartListActivity.this,KitchenDetailsActivity.class);
				intent.putExtra("kitchen_id",kitchenID);
				intent.putExtra("kitchenName",kichen_name);
				startActivity(intent);
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}
			
			break;

			case R.id.btn_clear:

              clearcart();

				break;
			case R.id.btn_next:


				if(InternetConnectivity.isConnectedFast(CartListActivity.this)){
					Checkout.kitchenID=kitchenID;
				/*Checkout.order_currency="";
				Checkout.total_amont="";
				Checkout.tip_amount=;*/
					//Checkout.delivery_type=""; //Pickup==1/Delivery==2
					//Checkout.order_type="";  //('IM', 'SCPC', 'SCDL')
				/*Checkout.pay_amount;
				Checkout.escrow_amount;*/

				/*Checkout.schedule_time;
				Checkout.schedule_date;*/


					KitchenData kitchenData = Sharepreferences.getSharePreferanceKitchenData(this);
					String distance=kitchenData.getDistance();
					String deliverydistance=kitchenData.getDistance_cover_by_vehicle();
					//Float.valueOf(deliverydistance)>=Float.valueOf(distance)
					if(selectcategory.equalsIgnoreCase("")) {
						Toast.makeText(getApplicationContext(),"Please select service type",Toast.LENGTH_LONG).show();
					}else if(selectcategory.equalsIgnoreCase("Delivery")){
						if(Float.valueOf(deliverydistance.replaceAll(",", ""))>=Float.valueOf(distance.replaceAll(",", ""))){
							if(ordertype.equalsIgnoreCase("IM")){
								Checkout.delivery_type="2"; //Pickup==1/Delivery==2
								Checkout.order_type="IM";  //('IM', 'SCPC', 'SCDL')
								Intent intent2=new Intent(CartListActivity.this,ShippingScreen.class);
								startActivity(intent2);
								finish();
							}else if(ordertype.equalsIgnoreCase("SC")){
								Checkout.delivery_type="2"; //Pickup==1/Delivery==2
								Checkout.order_type="SCDL";  //('IM', 'SCPC', 'SCDL')
								Intent intent1 = new Intent(CartListActivity.this, ScheduledDeliveryActivity.class);
								intent1.putExtra("selectcategory",selectcategory);
								startActivity(intent1);
								finish();
							}else {
								Toast.makeText(getApplicationContext(),"Please select order type",Toast.LENGTH_LONG).show();
							}
						}else {
							Toast.makeText(getApplicationContext(),"Out of delivery zone",Toast.LENGTH_LONG).show();
						}

					}else if(selectcategory.equalsIgnoreCase("Pickup")){
						if(ordertype.equalsIgnoreCase("IM")) {

							Checkout.delivery_type="1"; //Pickup==1/Delivery==2
							Checkout.order_type="IM";  //('IM', 'SCPC', 'SCDL')
							Intent intent2=new Intent(CartListActivity.this,ShippingScreen.class);
							startActivity(intent2);
							finish();
						}else if(ordertype.equalsIgnoreCase("SC")){

							Checkout.delivery_type="1"; //Pickup==1/Delivery==2
							Checkout.order_type="SCPC";  //('IM', 'SCPC', 'SCDL')
							Intent intent1 = new Intent(CartListActivity.this, ScheduledDeliveryActivity.class);
							intent1.putExtra("selectcategory",selectcategory);
							startActivity(intent1);
							finish();
						}else {
							Toast.makeText(getApplicationContext(),"Please select order type",Toast.LENGTH_LONG).show();
						}
					}else {
						Toast.makeText(getApplicationContext(),"Service type not available",Toast.LENGTH_LONG).show();
					}
				}else {
					Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
				}



				break;
		

		}
	}

	public void setAdapter()
	{
		adapter = new CartItemDetailsAdapter(CartListActivity.this, itemData);
		itemlistView.setAdapter(adapter);
	}

	//===============Cart item Function===================
	void doCartItemList()
	{
		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show(); 
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CART_ITEM_DETAILS;

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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
				){     
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
				Map<String, String>  params = new HashMap<String, String>(); 	
				params.put("user_id",userInfo.getUserId());
				params.put("format","json");
				return params;
			}
		};

		/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();
	}

	void parseResponse(String response){	
		
		if(itemData.size()>0)
			itemData.clear();
		
		String currency = "$";

		try {

			JSONObject parentObject = new JSONObject(response);
			String Status=parentObject.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				JSONArray parentArray = parentObject.getJSONArray("Records");
				if (parentArray.length() > 0) {
					for (int i = 0; i < parentArray.length(); i++) {
						HashMap<String,String> list=new HashMap<String,String>();
						JSONObject reocrdObj = parentArray.optJSONObject(i);
						list.put("cart_id",reocrdObj.optString("cart_id"));
						list.put("menu_id",reocrdObj.optString("menu_id"));
						list.put("kitchenID",reocrdObj.optString("kitchenID"));
						kitchenID=reocrdObj.optString("kitchenID");
						list.put("kichen_name",reocrdObj.optString("kichen_name"));
						kichen_name=reocrdObj.optString("kichen_name");
						list.put("price",reocrdObj.optString("price"));
						list.put("menu_name",reocrdObj.optString("menu_name"));
						list.put("menu_image",reocrdObj.optString("menu_image"));
						list.put("quantity",reocrdObj.optString("quantity"));
						list.put("items",reocrdObj.optString("items"));
						tip_value=reocrdObj.optString("tip_value");
						itemData.add(list);
					}

				}
				JSONObject Kitchen_details=parentObject.getJSONObject("Kitchen_details");
				String service_details=Kitchen_details.optString("service_details");

				String[] parts = service_details.split(",");


				category.clear();
				category.add("Select");
				if(parts.length>0) {
					for (int i = 0; i < parts.length; i++) {
						String part = parts[i];
						category.add(part);
					}
				}else {
					category.add(service_details);
				}

				//category.add(service_details);
				adaptercategory.notifyDataSetChanged();

				String currency_code=Kitchen_details.optString("currency_code");
				String total_price=Kitchen_details.optString("total_price");
				String tax_rate=Kitchen_details.optString("tax_rate");

				String tax_amount=Kitchen_details.optString("tax_amount");
				txt_tax.setText("Tax ("+tax_rate+"%) : "+tax_amount+" "+currency_code);

				String grand_total_price=Kitchen_details.optString("grand_total_price");
				txt_total.setText("TOTAL : "+grand_total_price+" "+currency_code);

				String tip_amount=Kitchen_details.optString("tip_amount");
				txt_tip.setText(""+tip_amount+" "+currency_code);
				//etn_tip.setText(tip_value);


				Checkout.order_currency=currency_code;
				Checkout.total_amont=grand_total_price;
				Checkout.tip_amount=tip_amount;

				Double escrowamount=(Double.valueOf(grand_total_price)*6.9)/100;
				Double payamount=Double.valueOf(grand_total_price)-escrowamount;


				Checkout.pay_amount=String.valueOf(payamount);
				Checkout.escrow_amount=String.valueOf(escrowamount);

				System.out.println(Checkout.escrow_amount+"  escrow_amount=====================pay_amount  "+Checkout.pay_amount);



				//set value for payment...
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
				Payment.amount=grand_total_price;
				Payment.currency=currency_code;
				Payment.customer_email=userInfo.getEmail();


				JSONArray Order_type_details=parentObject.getJSONArray("Order_type_details");
				JSONObject ImmediateObj=Order_type_details.getJSONObject(0);
				String immedate=ImmediateObj.optString("order_type_value_1");
				if (immedate.equalsIgnoreCase("IM")){
					btn_immediate_delivery.setVisibility(View.VISIBLE);
					txt_immediate.setVisibility(View.GONE);
				}else {
					btn_immediate_delivery.setVisibility(View.GONE);
					txt_immediate.setVisibility(View.VISIBLE);
				}
				JSONObject ScheduledObj=Order_type_details.getJSONObject(1);
				String scheduled=ScheduledObj.optString("order_type_value_2");
				if (scheduled.equalsIgnoreCase("SC")){
					btn_scheduled_order.setVisibility(View.VISIBLE);
					txt_scheduled.setVisibility(View.GONE);
				}else {
					btn_scheduled_order.setVisibility(View.GONE);
					txt_scheduled.setVisibility(View.VISIBLE);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//adapter.notifyDataSetChanged();
		adapter = new CartItemDetailsAdapter(CartListActivity.this, itemData);
		itemlistView.setAdapter(adapter);
		
		/*for(int i=0;i<itemData.size();i++){
			
			sum = sum + Integer.parseInt(itemData.get(i).getItemPrice());
			currency = itemData.get(i).getItemCurrency();
		}
		
		texTotal.setText(currency+" "+sum+"");*/

	}

	/*clear all cart item..................................................................*/
	public void clearcart(){

		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.CLEAR_CART_ITEM;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseClearResponse(response);
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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
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

	public void parseClearResponse(String response){


		try {

			JSONObject parentObject = new JSONObject(response);
			String Status=parentObject.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				String message=parentObject.getString("message");
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(CartListActivity.this,SearchKitchen.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(), "Not clear", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*update cart item.........................................................................*/
	public void updatecartmenu(final String cart_id){

		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.UPDATE_CART_ITEM;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseUpdateResponse(response);
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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				params.put("cart_id",cart_id);
				params.put("type","1");
				params.put("format","json");
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();

	}

	public void updatecartmenudec(final String cart_id){

		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.UPDATE_CART_ITEM;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseUpdateResponse(response);
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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				params.put("cart_id",cart_id);
				params.put("type","2");
				params.put("format","json");
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();

	}

	public void parseUpdateResponse(String response){

		try {

			JSONObject parentObject = new JSONObject(response);
			String Status=parentObject.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				String message=parentObject.getString("message");
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
				doCartItemList();
			}else {
				Toast.makeText(getApplicationContext(), "Not update", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*delete cart item........................................................................*/
	public void deletecartmenu(final String cart_id){

		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.DELETE_CART_ITEM;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseDeleteResponse(response);
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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				userInfo = Sharepreferences.getSharePreferance(CartListActivity.this);
				Map<String, String>  params = new HashMap<String, String>();
				//params.put("user_id",userInfo.getUserId());
				params.put("cart_id",cart_id);
				params.put("format","json");
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();

	}

	public void parseDeleteResponse(String response){

		try {

			JSONObject parentObject = new JSONObject(response);
			String Status=parentObject.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
                 String message=parentObject.getString("message");
				 Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
				 doCartItemList();
			}else {
				Toast.makeText(getApplicationContext(), "Not delete", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addTipDilog(){

		final Dialog dialog = new Dialog(CartListActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.add_tip_dialog);
		dialog.setCancelable(true);

		dialog_edit_tex=(EditText)dialog.findViewById(R.id.dialog_edit_text);
		dialog_edit_tex.setText(tip_value);
		Button btn_close=(Button)dialog.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button btn_done = (Button) dialog.findViewById(R.id.dialog_btn_add_to_care);
		// if decline button is clicked, close the custom dialog
		btn_done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Close dialog
				String tip_per=dialog_edit_tex.getText().toString();
				addTip(tip_per);
				dialog.dismiss();
			}
		});
		dialog.show();

	}


	public void addTip(final String tip_per){

		final ProgressDialog progressDialog=new ProgressDialog(CartListActivity.this);
		progressDialog.setMessage("loading...");
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		String url;

		url = WebServiceURL.ADD_TIP;

		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response) {
						parseTipResponse(response);
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
						Toast.makeText(CartListActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
					}
				}
		){
			@Override
			protected Map<String, String> getParams()
			{
				String cart_ids="";
				for (int i=0;i<itemData.size();i++){
					if(cart_ids.equalsIgnoreCase("")){
						cart_ids = itemData.get(i).get("cart_id");
					}else {
						cart_ids = cart_ids + "," + itemData.get(i).get("cart_id");
					}
				}

				// String to be encoded with Base64
                // Sending side
				byte[] data = null;
				try {
					data = cart_ids.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				String cart_base64 = Base64.encodeToString(data, Base64.DEFAULT);
				System.out.println("@@@@@@@@@@@@@@ cart_base64 "+cart_base64);

				Map<String, String>  params = new HashMap<String, String>();
				params.put("tip_per",tip_per);
				params.put("cart_ids",cart_base64);
				params.put("format","json");
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(postRequest);
		AppController.getInstance().getRequestQueue().getCache().remove(url);
		AppController.getInstance().getRequestQueue().getCache().clear();

	}

	public void parseTipResponse(String reaponse){

		try {

			JSONObject parentObject = new JSONObject(reaponse);
			String Status=parentObject.getString("Status");
			if(Status.equalsIgnoreCase("true")) {
				String message=parentObject.getString("message");
				Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
				doCartItemList();
			}else {
				Toast.makeText(getApplicationContext(), "Failed to add", Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {

		if(InternetConnectivity.isConnectedFast(CartListActivity.this)){
			startActivity(new Intent(CartListActivity.this,CustomerDashboardActivity.class));
			finish();
		}else {
			Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
		}


	}

}
