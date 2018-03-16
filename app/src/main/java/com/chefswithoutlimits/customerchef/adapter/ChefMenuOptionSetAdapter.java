package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefEditMenuCategory;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefMenuCategoryActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.optionsets.ChefOptionSetsActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class ChefMenuOptionSetAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> setsName;
    ArrayList<HashMap<String,String>> optionName;
    private static LayoutInflater inflater=null;
    public ChefMenuOptionSetAdapter(ChefOptionSetsActivity mainActivity, ArrayList<HashMap<String,String>> setsName,ArrayList<HashMap<String,String>> optionName) {
        // TODO Auto-generated constructor stub
        this.setsName=setsName;
        this.optionName=optionName;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return setsName.size();
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
        TextView menu_item_name,slno;
        View linearLayout;
        Button btn_delete,btn_edit;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.chef_option_set_list_item, null);
        holder.menu_item_name=(TextView) rowView.findViewById(R.id.menu_item_name);
        holder.slno=(TextView) rowView.findViewById(R.id.slno);

        holder.btn_delete=(Button)rowView.findViewById(R.id.btn_list_delete);
        holder.btn_edit=(Button)rowView.findViewById(R.id.btn_list_edit);
       // holder.list_image=(NetworkImageView) rowView.findViewById(R.id.list_image);

        holder.menu_item_name.setText(setsName.get(position).get("set_name"));
        holder.slno.setText(String.valueOf(position+1)+".");


        holder.linearLayout =  rowView.findViewById(R.id.pr_layout);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.info);

        for(int i=0;i<optionName.size();i++) {
            String availability="";
            if(optionName.get(i).get("item_status").equalsIgnoreCase("A")){
                availability="Available";
            }else if(optionName.get(i).get("item_status").equalsIgnoreCase("IA")){
                availability="Not available";
            }
            TextView valueTV = new TextView(context);
            valueTV.setText(optionName.get(i).get("option_name")+" -"+optionName.get(i).get("opt_reg_price")+" "+availability);
            valueTV.setId(position);
            valueTV.setPadding(2,2,2,2);
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if((setsName.get(position).get("set_id").equals(optionName.get(i).get("set_id")))){
                ((LinearLayout) holder.linearLayout).addView(valueTV);
            }
            
        }

        // holder.img.setImageResource(imageId[position]);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String set_id=setsName.get(position).get("set_id");

                if(context instanceof ChefOptionSetsActivity){
                    ((ChefOptionSetsActivity)context).deleteSet(set_id);
                }
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String set_id=setsName.get(position).get("set_id");
                String set_name=setsName.get(position).get("set_name");

                if(context instanceof ChefOptionSetsActivity){
                    ((ChefOptionSetsActivity)context).editSet(set_id,set_name);
                }

            }
        });
        return rowView;
    }

}