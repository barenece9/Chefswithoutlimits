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
import com.chefswithoutlimits.customerchef.activity.chefs.menu.item.ChefEditMenuItem;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.item.ChefMenuItemActivity;
import com.chefswithoutlimits.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ChefMenuItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static LayoutInflater inflater=null;
    public ChefMenuItemAdapter(ChefMenuItemActivity mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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
        TextView menu_item_name,menu_item_category,menu_item_description,menu_item_price,menu_item_amd;
        NetworkImageView img_item;
        Button btn_delete,btn_edit;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.chef_menu_item_list_item, null);
        holder.menu_item_name=(TextView) rowView.findViewById(R.id.menu_item_name);
        holder.menu_item_category=(TextView) rowView.findViewById(R.id.menu_item_category);

        holder.menu_item_description=(TextView) rowView.findViewById(R.id.menu_item_description);
        holder.menu_item_price=(TextView) rowView.findViewById(R.id.menu_item_price);
        holder.menu_item_amd=(TextView) rowView.findViewById(R.id.menu_item_amd);

        holder.btn_delete=(Button)rowView.findViewById(R.id.btn_list_delete);
        holder.btn_edit=(Button)rowView.findViewById(R.id.btn_list_edit);
        holder.img_item=(NetworkImageView) rowView.findViewById(R.id.img_item);

       // holder.menu_item_name.setText("Name : "+listitems.get(position).get("item_name"));
        holder.menu_item_name.setText(""+listitems.get(position).get("item_name"));
        holder.menu_item_category.setText("Category : "+listitems.get(position).get("category"));
        holder.menu_item_description.setText("Currency : "+listitems.get(position).get("currency"));

        holder.menu_item_price.setText("Price : "+listitems.get(position).get("normal_price"));
        holder.menu_item_price.setPaintFlags(holder.menu_item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.menu_item_amd.setText("Sale : "+listitems.get(position).get("sale_price"));


        if(listitems.get(position).get("sale_price").equalsIgnoreCase("0")
                || listitems.get(position).get("sale_price").equalsIgnoreCase("")){
            holder.menu_item_amd.setVisibility(View.GONE);
            holder.menu_item_price.setPaintFlags(holder.menu_item_price.getPaintFlags()  & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            if(listitems.get(position).get("normal_price").equalsIgnoreCase("0")
                    || listitems.get(position).get("normal_price").equalsIgnoreCase("")){
                holder.menu_item_price.setVisibility(View.VISIBLE);
                holder.menu_item_price.setText("Depending upon Item Selection");
            }else {
                holder.menu_item_price.setVisibility(View.VISIBLE);
            }

        }else {
            holder.menu_item_amd.setVisibility(View.VISIBLE);
            holder.menu_item_price.setPaintFlags(holder.menu_item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if(!listitems.get(position).get("item_image").equalsIgnoreCase("")) {
            holder.img_item.setImageUrl(listitems.get(position).get("item_image"), imageLoader);
        }



        // holder.img.setImageResource(imageId[position]);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String item_id=listitems.get(position).get("item_id");
                if(context instanceof ChefMenuItemActivity){
                    ((ChefMenuItemActivity)context).yourDesiredMethod(item_id);
                }
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String item_id=listitems.get(position).get("item_id");
                String item_name=listitems.get(position).get("item_name");
                Intent i = new Intent(context,ChefEditMenuItem.class);
                i.putExtra("item_id",item_id);
                i.putExtra("item_name",item_name);
                context.startActivity(i);
                ((ChefMenuItemActivity)context).finish();

            }
        });
        return rowView;
    }

}