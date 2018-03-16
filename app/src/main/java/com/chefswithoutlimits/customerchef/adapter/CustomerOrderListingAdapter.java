package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.chefs.order.ChefOrderActivity;
import com.chefswithoutlimits.customerchef.activity.customer.order.CustomerCompleteOrderActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomerOrderListingAdapter extends BaseAdapter {

	Context context;
	ArrayList<HashMap<String,String>> listitems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	private static LayoutInflater inflater=null;
	public CustomerOrderListingAdapter(Context mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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
		CardView ll_bg;
		TextView invoiceno,secrete_key,total_amont,datetime_added,order_type,schedule_date,tn_check,item_details,item_details_bill;
		EditText etn_code;
		//NetworkImageView img_item;
		//Button btn_delete,btn_edit;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Holder holder=new Holder();
		View rowView;
		rowView = inflater.inflate(R.layout.chef_order_listing_item, null);
		holder.ll_bg=(CardView)rowView.findViewById(R.id.ll_bg);
		holder.invoiceno=(TextView) rowView.findViewById(R.id.invoiceno);
		holder.secrete_key=(TextView) rowView.findViewById(R.id.secrete_key);

		holder.total_amont=(TextView) rowView.findViewById(R.id.total_amont);

		holder.datetime_added=(TextView) rowView.findViewById(R.id.datetime_added);
		holder.order_type=(TextView) rowView.findViewById(R.id.order_type);
		holder.schedule_date=(TextView) rowView.findViewById(R.id.schedule_date);

		holder.tn_check=(TextView)rowView.findViewById(R.id.tn_check);
		holder.item_details=(TextView)rowView.findViewById(R.id.item_details);
		holder.item_details_bill=(TextView)rowView.findViewById(R.id.item_details_bill);
		holder.etn_code=(EditText) rowView.findViewById(R.id.etn_code);

		String invoiceno=listitems.get(position).get("invoiceno");
		String secrete_key=listitems.get(position).get("secrete_key");
		String total_amont=listitems.get(position).get("total_amont");
		System.out.print(invoiceno+"  $$$$$$$$$  "+total_amont);
		holder.invoiceno.setText("Order No : "+invoiceno);
		holder.secrete_key.setVisibility(View.VISIBLE);
		holder.secrete_key.setText("Secret Key : "+secrete_key);
		holder.total_amont.setText("Order Price : "+listitems.get(position).get("order_currency")+" "+total_amont);
        holder.datetime_added.setText("DATE : "+listitems.get(position).get("datetime_added"));
		String order_type=listitems.get(position).get("order_type");
		if(order_type.equalsIgnoreCase("SCPC")){
			holder.order_type.setText("Order Type : Schedule Pickup");
			holder.schedule_date.setVisibility(View.VISIBLE);
		}else if(order_type.equalsIgnoreCase("SCDL")){
			holder.order_type.setText("Order Type : Schedule Delivery");
			holder.schedule_date.setVisibility(View.VISIBLE);
		}else if(order_type.equalsIgnoreCase("IM")){
			holder.order_type.setText("Order Type : Immediate");
			holder.schedule_date.setVisibility(View.GONE);
		}else {
			holder.order_type.setText("Order Type : ");
		}

		String order_status=listitems.get(position).get("order_status");
		if(order_status.equalsIgnoreCase("draft")){
			holder.ll_bg.setBackgroundColor(Color.parseColor("#747474"));
			holder.etn_code.setVisibility(View.GONE);
			holder.tn_check.setVisibility(View.GONE);
		}else if(order_status.equalsIgnoreCase("delivered")){
			//holder.ll_bg.setBackgroundColor(Color.parseColor("#1dc499"));
			holder.ll_bg.setBackgroundColor(Color.parseColor("#EEE1D5"));//gray
			//holder.schedule_date.setVisibility(View.GONE);
			holder.etn_code.setVisibility(View.GONE);
			holder.tn_check.setVisibility(View.GONE);
		}else if(order_status.equalsIgnoreCase("pending")){
			holder.ll_bg.setBackgroundColor(Color.parseColor("#a71e22"));
			holder.etn_code.setVisibility(View.GONE);
			holder.tn_check.setVisibility(View.GONE);
		}else {

		}

		holder.schedule_date.setText("Delivery Schedule Details : "+listitems.get(position).get("schedule_date")+" "+
				listitems.get(position).get("schedule_time"));
		// String sale=listitems.get(position).get("sale_price");

		//holder.menu_item_amd.setText(Html.fromHtml("<p><strike>not yet available!</strike></p>"));

		// holder.menu_item_amd.setText(" AMD : "+listitems.get(position).get("normal_price"));

		/*if(!listitems.get(position).get("item_image").equalsIgnoreCase("")) {
			holder.img_item.setImageUrl(listitems.get(position).get("item_image"), imageLoader);
		}
*/


		// holder.img.setImageResource(imageId[position]);
		holder.item_details_bill.setVisibility(View.GONE);
		holder.item_details.setText("Invoice");
		holder.item_details.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String id=listitems.get(position).get("id");
				System.out.println("&&&&&&&&&&&& id : "+id);
				if(context instanceof CustomerCompleteOrderActivity){
					((CustomerCompleteOrderActivity)context).yourDesiredMethod(id);
				}
			}
		});

		holder.tn_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String code=holder.etn_code.getText().toString();
				if(!code.equalsIgnoreCase("")) {
					System.out.println("&&&&&&&&&&&& code : " + code);
					if (context instanceof ChefOrderActivity) {
						((ChefOrderActivity) context).CheckSecrateCode(code);
					}
				}else {
					Toast.makeText(context,"Please enter Secrete Code", Toast.LENGTH_LONG).show();
				}
				//Toast.makeText(context, listitems.get(position).get("item_id")+"You Clicked "+listitems.get(position).get("item_name"), Toast.LENGTH_LONG).show();
				/*String item_id=listitems.get(position).get("item_id");
				String item_name=listitems.get(position).get("item_name");
				Intent i = new Intent(context,ChefEditMenuOptionItem.class);
				i.putExtra("item_id",item_id);
				i.putExtra("item_name",item_name);
				context.startActivity(i);*/

			}
		});
		return rowView;
	}

}