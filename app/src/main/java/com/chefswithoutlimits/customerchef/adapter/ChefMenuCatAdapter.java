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
import android.widget.TextView;

import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefEditMenuCategory;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefMenuCategoryActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class ChefMenuCatAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listitems;

    private static LayoutInflater inflater=null;
    public ChefMenuCatAdapter(ChefMenuCategoryActivity mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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
        TextView menu_item_name,slno;
        //NetworkImageView list_image;
        Button btn_delete,btn_edit;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.chef_menu_category_list_item, null);
        holder.menu_item_name=(TextView) rowView.findViewById(R.id.menu_item_name);
        holder.slno=(TextView) rowView.findViewById(R.id.slno);

        holder.btn_delete=(Button)rowView.findViewById(R.id.btn_list_delete);
        holder.btn_edit=(Button)rowView.findViewById(R.id.btn_list_edit);
       // holder.list_image=(NetworkImageView) rowView.findViewById(R.id.list_image);

        holder.menu_item_name.setText(listitems.get(position).get("cat_name"));
        holder.slno.setText(String.valueOf(position+1)+".");



        // holder.img.setImageResource(imageId[position]);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String menu_catid=listitems.get(position).get("menu_catid");
                //Toast.makeText(context, "You Clicked "+slno, Toast.LENGTH_LONG).show();
                if(context instanceof ChefMenuCategoryActivity){
                    ((ChefMenuCategoryActivity)context).yourDesiredMethod(menu_catid);
                }
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // Toast.makeText(context, "You Clicked "+listitems.get(position).get("cat_name"), Toast.LENGTH_LONG).show();
                String menu_catid=listitems.get(position).get("menu_catid");
                String cat_name=listitems.get(position).get("cat_name");
                //context.startActivity(new Intent(context,ChefEditMenuCategory.class));
                /*Intent intent=new Intent(context,ChefEditMenuCategory.class);
                intent.putExtra("menu_catid",menu_catid);
                intent.putExtra("cat_name",cat_name);
                context.startActivities(intent);*/

                Intent i = new Intent(context,ChefEditMenuCategory.class);
                i.putExtra("menu_catid",menu_catid);
                i.putExtra("cat_name",cat_name);
                context.startActivity(i);
                ((ChefMenuCategoryActivity)context).finish();

            }
        });
        return rowView;
    }

}