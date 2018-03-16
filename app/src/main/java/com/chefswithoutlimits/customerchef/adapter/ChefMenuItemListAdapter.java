package com.chefswithoutlimits.customerchef.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.other.ChefEditMenuActivity;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.other.ChefMenuItemDetail;
import com.chefswithoutlimits.customerchef.dataVO.ChefMenuItem;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.WebServiceURL;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChefMenuItemListAdapter extends BaseAdapter{

	Context _context;	
	ArrayList<ChefMenuItem> itemData;
	private ProgressDialog progressDialog;
	//private ImageLoader mImageLoader;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ChefMenuItemListAdapter(Context context,ArrayList<ChefMenuItem> itemData)
	{
		_context = context;
		this.itemData = itemData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemData.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		ChefMenuItem itemObj = itemData.get(position);

		if (convertView==null) {
			
			

			if (imageLoader == null)
				imageLoader = AppController.getInstance().getImageLoader();

			LayoutInflater inflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.chef_menu_list_item, parent, false);
			holder = new ViewHolder();

			holder.itemName = (TextView)convertView.findViewById(R.id.txtItemname);
			holder.txtItemPrice = (TextView)convertView.findViewById(R.id.txtItemPrice);
			holder.itemImage = (NetworkImageView)convertView.findViewById(R.id.itemImage);	
			holder.btndetail = (Button)convertView.findViewById(R.id.btndetail);	
			holder.btnDelete = (Button)convertView.findViewById(R.id.btnDelete);	
			holder.btnEdit = (Button)convertView.findViewById(R.id.btnEdit);	
		
			
			convertView.setTag(holder);	
		}
		else{
			holder= (ViewHolder) convertView.getTag();	
		}
		
		
		RemoveClass object = new RemoveClass();
		object.object = itemObj;
		object.position = position;
		
		holder.btnDelete.setTag(object);
		holder.btndetail.setTag(itemObj);
		holder.btnEdit.setTag(itemObj);

		/*imageLoader.get(itemObj.getMenuImage(), ImageLoader.getImageListener(holder.itemImage,R.drawable.food_item_image, android.R.drawable
				.ic_dialog_alert));*/
		holder.itemImage.setImageUrl(itemObj.getMenuImage(), imageLoader);

		holder.itemName.setText(itemObj.getMenuName());
		holder.txtItemPrice.setText("Regular Price :"+itemObj.getCurrency()+itemObj.getRegularPrice()+" Sales Price :"+itemObj.getCurrency()+itemObj.getSalesPrice());

		holder.btndetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChefMenuItem object = (ChefMenuItem) v.getTag();
				Intent intent = new Intent(_context,ChefMenuItemDetail.class);
				intent.putExtra("menuId", object.getMenuId());
				intent.putExtra("meueImage", object.getMenuImage());
				_context.startActivity(intent);
			}
		});

		holder.btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ChefMenuItem object = (ChefMenuItem) v.getTag();
				Intent intent = new Intent(_context,ChefEditMenuActivity.class);
				intent.putExtra("menuId", object.getMenuId());
				intent.putExtra("meueImage", object.getMenuImage());
				//intent.putExtra("kitchenId", object.getKitchenId());
				_context.startActivity(intent);
			}
		});
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RemoveClass tagObj = (RemoveClass)v.getTag();
				itemData.remove(tagObj.position);
				deleteMenu(tagObj.object.getMenuId());
				notifyDataSetChanged();		
			}
		});


		return convertView;
	}


	public class ViewHolder
	{
		TextView itemName;
		TextView txtItemPrice;
		NetworkImageView itemImage;		
		Button btnEdit,btnDelete,btndetail;
	}
	
	class RemoveClass
	{
		int position = 0;
		ChefMenuItem object;
	}
	
	//========================================= Delete Menu Item =========================================
	
		void deleteMenu(final String menueId)
		{
			progressDialog=new ProgressDialog(_context);
			progressDialog.setMessage("loading...");
			progressDialog.show(); 
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			String url;

			url = WebServiceURL.DELETE_MENU;

			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					new Response.Listener<String>() 
					{
				@Override
				public void onResponse(String response) {

					//parseData(response);
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
							Toast.makeText(_context, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
						}
					}
					){     
				@Override
				protected Map<String, String> getParams()
				{   		
					UserInformation userInfo = Sharepreferences.getSharePreferance(_context);
					
					Map<String, String>  params = new HashMap<String, String>(); 	


					params.put("userid",userInfo.getUserId());
					params.put("menuid",menueId);
					params.put("format","json");
					return params;
				}
			};
			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(postRequest);           
		}
		
	
}
