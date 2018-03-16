package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchKitchenListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listitems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static LayoutInflater inflater=null;
    public SearchKitchenListAdapter(Context mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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
        TextView kichen_name,status,address,description,distance,type;
        NetworkImageView img_item;
        RatingBar ratting;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.search_kitchen_list_item, null);
        holder.kichen_name=(TextView) rowView.findViewById(R.id.kichen_name);
        holder.status=(TextView) rowView.findViewById(R.id.status);
        holder.type=(TextView) rowView.findViewById(R.id.type);

        holder.address=(TextView) rowView.findViewById(R.id.address);
        holder.description=(TextView) rowView.findViewById(R.id.description);
        holder.distance=(TextView) rowView.findViewById(R.id.distance);
        holder.ratting=(RatingBar)rowView.findViewById(R.id.ratting);

        holder.img_item=(NetworkImageView) rowView.findViewById(R.id.img_item);

        holder.kichen_name.setText(""+listitems.get(position).get("kichen_name"));
        holder.status.setText("Status : "+listitems.get(position).get("status"));
        holder.address.setText("Address : "+listitems.get(position).get("address"));
        holder.description.setText("Description : "+listitems.get(position).get("description"));
        String distance_unit=listitems.get(position).get("distance_unit");
        if (distance_unit.equalsIgnoreCase("Km")) {
            holder.distance.setText("Distance : " + listitems.get(position).get("distance")+" "+listitems.get(position).get("distance_unit"));
        }else {
            holder.distance.setText("Distance : " + listitems.get(position).get("distance")+" "+listitems.get(position).get("distance_unit"));
        }
        if(!listitems.get(position).get("kichen_Logo").equalsIgnoreCase("")) {
            holder.img_item.setImageUrl(WebServiceURL.IMAGE_PATH+""+listitems.get(position).get("kichen_Logo"), imageLoader);
        }
        if(!listitems.get(position).get("ratting").equalsIgnoreCase("null") && !listitems.get(position).get("ratting").equalsIgnoreCase("")){
            holder.ratting.setRating(Float.valueOf(listitems.get(position).get("ratting")));
        }else {
            holder.ratting.setRating(0);
        }

        holder.type.setText("Type : " + listitems.get(position).get("venue_name"));
        return rowView;
    }

}