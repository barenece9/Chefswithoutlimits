package com.chefswithoutlimits.customerchef.adapter;

import java.util.ArrayList;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chefswithoutlimits.customerchef.activity.other.ChefCustomerDetail;
import com.chefswithoutlimits.customerchef.activity.other.ChefOrderItem;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.dataVO.ChefNewOrderItem;

public class ChefNewOrderAdapter extends BaseAdapter{

	ArrayList<ChefNewOrderItem> item;
	Context _context;

	public ChefNewOrderAdapter(Context context,ArrayList<ChefNewOrderItem> item)
	{
		this._context = context;
		this.item = item;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item.get(position);
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

		ChefNewOrderItem itemObj = item.get(position);

		if (convertView==null) {

			@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.chef_neworder_item, parent, false);
			holder = new ViewHolder();

			holder.txtVwOrderDetail = (TextView)convertView.findViewById(R.id.txtVwOrderDetail);
			holder.txtVwDate = (TextView)convertView.findViewById(R.id.txtVwDate);
			holder.txtVwAmount = (TextView)convertView.findViewById(R.id.txtVwAmount);
			holder.btnOrderDetail = (Button)convertView.findViewById(R.id.btnOrderDetail);
			holder.btnCustomerDetail = (Button)convertView.findViewById(R.id.btnCustomerDetail);

			holder.txtVwOrderDetail.setText("Order No "+itemObj.getInvoiceNo());
			holder.txtVwDate.setText(itemObj.getOrderDate());
			holder.txtVwAmount.setText("$ "+itemObj.getItemPrice());
			holder.btnOrderDetail.setTag(itemObj);
			holder.btnCustomerDetail.setTag(itemObj);

			holder.btnOrderDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub

					ChefNewOrderItem object = (ChefNewOrderItem)view.getTag();

					Intent intent = new Intent(_context,ChefOrderItem.class);
					intent.putExtra("itemId", object.getOrderId());
					_context.startActivity(intent);
				}
			});

			holder.btnCustomerDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub

					ChefNewOrderItem object = (ChefNewOrderItem)view.getTag();

					Intent intent = new Intent(_context,ChefCustomerDetail.class);
					intent.putExtra("customerId", object.getCustomerId());
					_context.startActivity(intent);
				}
			});

			convertView.setTag(holder);	
		}
		else{
			holder= (ViewHolder) convertView.getTag();	
		}

		return convertView;
	}


	public class ViewHolder
	{
		TextView txtVwOrderDetail;
		TextView txtVwDate;
		TextView txtVwAmount;
		Button btnOrderDetail;
		Button btnCustomerDetail;
	}

}
