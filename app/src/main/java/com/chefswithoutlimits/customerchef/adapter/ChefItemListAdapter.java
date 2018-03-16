package com.chefswithoutlimits.customerchef.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.ChefOredrItem;
import com.chefswithoutlimits.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ChefItemListAdapter extends BaseAdapter{

	Context _context;	
	ArrayList<ChefOredrItem> itemData;
	
	//private ImageLoader mImageLoader;
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ChefItemListAdapter(Context context,ArrayList<ChefOredrItem> itemData)
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

		ChefOredrItem itemObj = itemData.get(position);

		if (convertView==null) {

			/*mImageLoader = CustomVolleyRequestQueue.getInstance(_context.getApplicationContext())
					.getImageLoader();*/
			//ItemListData object = itemData.get(position);
			
			if (imageLoader == null)
	            imageLoader = AppController.getInstance().getImageLoader();

			LayoutInflater inflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.chef_list_item, parent, false);
			holder = new ViewHolder();

			holder.itemName = (TextView)convertView.findViewById(R.id.txtItemname);
			holder.txtItemPrice = (TextView)convertView.findViewById(R.id.txtItemPrice);
			holder.itemImage = (NetworkImageView)convertView.findViewById(R.id.itemImage);	
			holder.txtItemQuantity = (TextView)convertView.findViewById(R.id.txtItemQuantity);

			/*imageLoader.get(itemObj.getItemImage(), ImageLoader.getImageListener(holder.itemImage,R.drawable.food_item_image, android.R.drawable
					.ic_dialog_alert));*/
			holder.itemImage.setImageUrl(itemObj.getItemImage(), imageLoader);
			
			holder.txtItemQuantity.setText("Quantity :"+itemObj.getItemQuantity());
			holder.itemName.setText(itemObj.getItemName());
			holder.txtItemPrice.setText(itemObj.getItemCurrency()+" "+itemObj.getItemPrice());

			convertView.setTag(holder);	
		}
		else{
			holder= (ViewHolder) convertView.getTag();	
		}

		return convertView;
	}


	public class ViewHolder
	{
		TextView itemName;
		TextView txtItemPrice;
		TextView txtItemQuantity;
		NetworkImageView itemImage;		
	}

	class Tag
	{
		 int itemQuantity = 0; 
		 EditText editTxt;
	}

}
