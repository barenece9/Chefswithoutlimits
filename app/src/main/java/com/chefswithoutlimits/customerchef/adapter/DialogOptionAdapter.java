package com.chefswithoutlimits.customerchef.adapter;

/**
 * Created by db on 1/30/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chefswithoutlimits.customerchef.activity.customer.cart.KitchenDetailsActivity;
import com.chefswithoutlimits.customerchef.dataVO.Oplist;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.dataVO.OptionItemValue;

import java.util.ArrayList;
import java.util.HashMap;


public class DialogOptionAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listitems;
    OptionItemValue optionItemValue;

    private static LayoutInflater inflater=null;
    public DialogOptionAdapter(KitchenDetailsActivity mainActivity, ArrayList<HashMap<String,String>> listitems1) {
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
        TextView dialog_option_item_name,dialog_option_item_price,dialog_option_item_sale;
        CheckBox checkbox_btn;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.dialog_menu_option_list_item, null);
        holder.dialog_option_item_name=(TextView) rowView.findViewById(R.id.dialog_option_item_name);
        holder.dialog_option_item_price=(TextView) rowView.findViewById(R.id.dialog_option_item_price);
        holder.dialog_option_item_sale=(TextView) rowView.findViewById(R.id.dialog_option_item_sale);
        holder.checkbox_btn=(CheckBox) rowView.findViewById(R.id.checkbox_btn);

        holder.checkbox_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                String option_id=listitems.get(position).get("item_id");
                if ( isChecked )
                {
                    OptionItemValue optionItemValue=new OptionItemValue();
                    optionItemValue.setItemId(option_id);
                    optionItemValue.setItemStatus("1");
                   // Oplist.oplist.add(position,optionItemValue);
                    Oplist.oplist.set(position,optionItemValue);
                }else {
                    OptionItemValue optionItemValue=new OptionItemValue();
                    optionItemValue.setItemId(option_id);
                    optionItemValue.setItemStatus("0");
                    //Oplist.oplist.remove(position);
                   // Oplist.oplist.add(position,optionItemValue);
                    Oplist.oplist.set(position,optionItemValue);
                }

            }
        });
        String item_name="",item_regular_price="",item_sale_price="";
        item_name=listitems.get(position).get("item_name");
        item_regular_price=listitems.get(position).get("item_regular_price");
        item_sale_price=listitems.get(position).get("item_sale_price");
        holder.dialog_option_item_name.setText(item_name);
        holder.dialog_option_item_price.setText("Price : "+item_regular_price);
        holder.dialog_option_item_sale.setText("Sale : "+item_sale_price);

        return rowView;
    }

}