package com.chefswithoutlimits.customerchef.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.customerchef.dataVO.ItemListData;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class CartItemListAdapter extends BaseAdapter{

	Context _context;	
	ArrayList<ItemListData> itemData;
	//	ArrayList<ItemListData> newOrderList = new ArrayList<ItemListData>();
	ArrayList<ItemListData> tempItem = new ArrayList<ItemListData>();
	//private ImageLoader mImageLoader;
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CartItemListAdapter(Context context,ArrayList<ItemListData> itemData)
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

		

		if (convertView==null) {

			/*mImageLoader = CustomVolleyRequestQueue.getInstance(_context.getApplicationContext())
					.getImageLoader();*/
			//ItemListData object = itemData.get(position);
			
			if (imageLoader == null)
	            imageLoader = AppController.getInstance().getImageLoader();

			LayoutInflater inflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.cart_item_list, parent, false);
			holder = new ViewHolder();

			holder.itemName = (TextView)convertView.findViewById(R.id.txtItemname);
			//holder.itemImage = (ImageView)convertView.findViewById(R.id.itemImage);
			holder.itemImage = (NetworkImageView)convertView.findViewById(R.id.itemImage);
			
			holder.quentity = (EditText)convertView.findViewById(R.id.editQuantity);
			//holder.quentity.addTextChangedListener(new QuentatyTextWatcher(convertView));
			
			holder.texPrice = (TextView)convertView.findViewById(R.id.texPrice);

			/*mImageLoader.get(object.getImageUrl(), ImageLoader.getImageListener(holder.itemImage,R.drawable.food_item_image, android.R.drawable
					.ic_dialog_alert));*/
			//holder.itemImage.setImageUrl("http://122.160.239.42/phpdemo/chef/upload/menu_pics/Hydrangeas2.jpg", imageLoader);
			


			convertView.setTag(holder);	
		}
		else{
			holder= (ViewHolder) convertView.getTag();	
		}

		ItemListData obj = itemData.get(position);
		
		holder.itemName.setText(obj.getItemName());
		holder.itemImage.setImageUrl(obj.getImageUrl(), imageLoader);
		holder.quentity.setText(obj.getItemQuantity());
		holder.texPrice.setText(obj.getItemCurrency()+" "+obj.getItemPrice());

		/*ItemListData obj = itemData.get(position);

		holder.itemName.setText(obj.getItemName());
		holder.itemImage.setImageUrl(obj.getImageUrl(), imageLoader);

		EditText quantity = (EditText) convertView.findViewById(R.id.editQuantity);
		quantity.setTag(obj);

		Tag tag = new Tag();
		tag.editTxt = quantity;

		holder.btnAdd.setTag(tag);

		holder.btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				Tag tag = (Tag) view.getTag();

				EditText editTxt = tag.editTxt;
				int itemQuantity = tag.itemQuantity;

				String strQ = editTxt.getText().toString();

				if(strQ !=null && !strQ.equalsIgnoreCase("")){

					itemQuantity = Integer.parseInt(strQ);

				}

				itemQuantity = itemQuantity+1;
				editTxt.setText(itemQuantity+"");
			}
		});

		if(obj.getQuantity() != 0){

			String qnty = String.valueOf(obj.getQuantity());
			quantity.setText(qnty);

			if(qnty.length()>0)			
				quantity.setSelection(qnty.length());
		}
		else {
			quantity.setText(obj.getItemQuantity());
		}*/

		return convertView;
	}

	/**
	 *   Text watcher
	 */

	/*private class QuentatyTextWatcher implements TextWatcher{

		private View view;
		private QuentatyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		public void onTextChanged(CharSequence s, int start, int before, int count) { 
			//do nothing
		}
		public void afterTextChanged(Editable s) {

			String qtyString = s.toString().trim();
			int quantity = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

			EditText qtyView = (EditText) view.findViewById(R.id.editQuantity);
			//qtyView.setTextDirection(View.TEXT_DIRECTION_LTR);




			qtyView.setFocusable(true);

			ItemListData product = (ItemListData) qtyView.getTag();

			if(product.getQuantity() != quantity){

				product.setQuantity(quantity);

				if(product.getQuantity() != 0){

					String qnty = String.valueOf(product.getQuantity());

					qtyView.setText(String.valueOf(product.getQuantity()));

					if(qnty.length()>0)			
						qtyView.setSelection(qnty.length());

					if (!tempItem.contains(product)) {
						tempItem.add(product);
					}else{

						tempItem.remove(product);
						tempItem.add(product);
					}
				}
				else {
					qtyView.setText("");
				}
			}

			return;
		}
	}*/

	public class ViewHolder
	{
		TextView itemName;
		NetworkImageView itemImage;
		//ImageView itemImage;
		
		EditText quentity; 
		TextView texPrice;
	}

	public ArrayList<ItemListData> getTempList()
	{
		return tempItem; 		
	}
	
	class Tag
	{
		 int itemQuantity = 0; 
		 EditText editTxt;
	}

}
