package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineCartListActivity;
import com.chefswithoutlimits.util.WebServiceURL;

import java.util.ArrayList;
import java.util.HashMap;


public class OfflineCartItemDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    int quantity=1;

    private static LayoutInflater inflater=null;
    public OfflineCartItemDetailsAdapter(Context mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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

    public  class Holder
    {
        TextView menu_name,items,price,quantity;
        NetworkImageView img_item;
        Button btn_delete,btn_up,btn_down;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.cart_list_item_details, null);
        holder.menu_name=(TextView) rowView.findViewById(R.id.menu_name);
        holder.items=(TextView) rowView.findViewById(R.id.items);

        holder.quantity=(TextView) rowView.findViewById(R.id.quantity);
        holder.price=(TextView) rowView.findViewById(R.id.price);

        holder.btn_delete=(Button)rowView.findViewById(R.id.btn_list_delete);
        holder.img_item=(NetworkImageView) rowView.findViewById(R.id.img_item);

        holder.menu_name.setText(listitems.get(position).get("menu_name"));
        holder.items.setText(listitems.get(position).get("items"));
        holder.quantity.setText("Quantity : "+listitems.get(position).get("quantity"));
        holder.price.setText("Price : "+listitems.get(position).get("price"));


        if(!listitems.get(position).get("menu_image").equalsIgnoreCase("")) {
            holder.img_item.setImageUrl(WebServiceURL.IMAGE_PATH+"/"+listitems.get(position).get("menu_image"), imageLoader);
        }

        holder.btn_up=(Button)rowView.findViewById(R.id.btn_up);
        holder.btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               quantity=Integer.valueOf(listitems.get(position).get("quantity"));
                quantity++;
                holder.quantity.setText("Quantity : "+quantity);

                String cart_id=listitems.get(position).get("cart_id");
                String type="1";
                //String menu_id=listitems.get(position).get("menu_id");
                if(context instanceof OfflineCartListActivity){
                    ((OfflineCartListActivity)context).updatecartmenu(cart_id);
                }

            }
        });
        holder.btn_down=(Button)rowView.findViewById(R.id.btn_down);
        holder.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=Integer.valueOf(listitems.get(position).get("quantity"));
                quantity--;
                if(quantity<=0) {
                }else {
                    holder.quantity.setText("Quantity : " + quantity);
                    String cart_id=listitems.get(position).get("cart_id");
                    String type="2";
                    //String menu_id=listitems.get(position).get("menu_id");
                    if(context instanceof OfflineCartListActivity){
                        ((OfflineCartListActivity)context).updatecartmenudec(cart_id);
                    }
                }

            }
        });

        // holder.img.setImageResource(imageId[position]);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String cart_id=listitems.get(position).get("cart_id");
                if(context instanceof OfflineCartListActivity){
                    ((OfflineCartListActivity)context).deletecartmenu(cart_id);
                }
            }
        });

        return rowView;
    }

}