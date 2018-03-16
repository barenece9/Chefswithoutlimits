package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.option.ChefEditMenuOption;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.option.ChefMenuOptionActivity;
import com.chefswithoutlimits.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ChefMenuOptionAdapter extends BaseAdapter {

	Context context;
	ArrayList<HashMap<String,String>> listitems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	private static LayoutInflater inflater=null;
	public ChefMenuOptionAdapter(ChefMenuOptionActivity mainActivity, ArrayList<HashMap<String,String>> listitems1) {
		// TODO Auto-generated constructor stub
		listitems=listitems1;
		context=mainActivity;
		inflater = ( LayoutInflater )context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listitems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder
	{
		TextView item_name,menu_name,item_status,item_regular_price,item_sale_price;
		NetworkImageView img_item;
		Button btn_delete,btn_edit;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
		View rowView;
		rowView = inflater.inflate(R.layout.chef_menu_option_list_item, null);
		holder.item_name=(TextView) rowView.findViewById(R.id.item_name);
		holder.menu_name=(TextView) rowView.findViewById(R.id.menu_name);

		holder.item_status=(TextView) rowView.findViewById(R.id.item_status);
		holder.item_regular_price=(TextView) rowView.findViewById(R.id.item_regular_price);
		holder.item_sale_price=(TextView) rowView.findViewById(R.id.item_sale_price);

		holder.btn_delete=(Button)rowView.findViewById(R.id.btn_list_delete);
		holder.btn_edit=(Button)rowView.findViewById(R.id.btn_list_edit);
		holder.img_item=(NetworkImageView) rowView.findViewById(R.id.img_item);

		holder.item_name.setText(""+listitems.get(position).get("item_name"));
		holder.menu_name.setText("Menu : "+listitems.get(position).get("menu_name"));
		String item_status=listitems.get(position).get("item_status");
		if(item_status.equalsIgnoreCase("A")){
			holder.item_status.setText("Availability : Yes");
		}else if(item_status.equalsIgnoreCase("IA")){
			holder.item_status.setText("Availability : No");
		}


		holder.item_regular_price.setText("Price : "+listitems.get(position).get("item_regular_price"));
		holder.item_regular_price.setPaintFlags(holder.item_regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		holder.item_sale_price.setText("Sale : "+listitems.get(position).get("item_sale_price"));

		/*if(listitems.get(position).get("item_regular_price").equalsIgnoreCase("0")
				|| listitems.get(position).get("item_regular_price").equalsIgnoreCase("")){
			holder.item_regular_price.setVisibility(View.GONE);
		}else {
			holder.item_regular_price.setVisibility(View.VISIBLE);
		}*/

		if(listitems.get(position).get("item_sale_price").equalsIgnoreCase("0")
				|| listitems.get(position).get("item_sale_price").equalsIgnoreCase("")){
			holder.item_sale_price.setVisibility(View.GONE);
			holder.item_regular_price.setPaintFlags(holder.item_regular_price.getPaintFlags()  & (~ Paint.STRIKE_THRU_TEXT_FLAG));

			if(listitems.get(position).get("item_regular_price").equalsIgnoreCase("0")
					|| listitems.get(position).get("item_regular_price").equalsIgnoreCase("")){
				holder.item_regular_price.setVisibility(View.VISIBLE);
				holder.item_regular_price.setText("No Price Available");
			}else {
				holder.item_regular_price.setVisibility(View.VISIBLE);
			}

		}else {
			holder.item_sale_price.setVisibility(View.VISIBLE);
			holder.item_regular_price.setPaintFlags(holder.item_regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}


		// holder.img.setImageResource(imageId[position]);
		holder.btn_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String item_id=listitems.get(position).get("item_id");
				//Toast.makeText(context, "You Clicked "+slno, Toast.LENGTH_LONG).show();
				if(context instanceof ChefMenuOptionActivity){
					((ChefMenuOptionActivity)context).yourDesiredMethod(item_id);
				}
			}
		});

		holder.btn_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, listitems.get(position).get("item_id")+"You Clicked "+listitems.get(position).get("item_name"), Toast.LENGTH_LONG).show();
				String item_id=listitems.get(position).get("item_id");
				String item_name=listitems.get(position).get("item_name");
				Intent i = new Intent(context,ChefEditMenuOption.class);
				i.putExtra("item_id",item_id);
				i.putExtra("item_name",item_name);
				context.startActivity(i);
				((ChefMenuOptionActivity)context).finish();
				//context.f

			}
		});
		return rowView;
	}

}