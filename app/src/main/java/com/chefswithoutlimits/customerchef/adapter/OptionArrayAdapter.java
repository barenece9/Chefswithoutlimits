package com.chefswithoutlimits.customerchef.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.dataVO.OptionItemValue;

import java.util.ArrayList;

public class OptionArrayAdapter extends ArrayAdapter<OptionItemValue> {

    private final ArrayList<OptionItemValue> list;
    private final Activity context;
    Boolean activate=false;
    int count=0;

    public OptionArrayAdapter(Activity context, ArrayList<OptionItemValue> list) {
        super(context, R.layout.dialog_menu_option_list_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView dialog_option_item_name,dialog_option_item_price,dialog_option_item_sale;
        protected CheckBox checkbox_btn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.dialog_menu_option_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.dialog_option_item_name = (TextView) convertView.findViewById(R.id.dialog_option_item_name);

            viewHolder.dialog_option_item_price = (TextView) convertView.findViewById(R.id.dialog_option_item_price);
            viewHolder.dialog_option_item_sale = (TextView) convertView.findViewById(R.id.dialog_option_item_sale);

            viewHolder.checkbox_btn = (CheckBox) convertView.findViewById(R.id.checkbox_btn);
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            viewHolder.checkbox_btn.setButtonDrawable(id);
            viewHolder.checkbox_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setItemSelect(buttonView.isChecked()); // Set the value of checkbox to maintain its state.


                    if(buttonView.isPressed()) {

                       /* if (context instanceof BarcodeActivity) {
                            int count = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getItemSelect()) {
                                    count = count + 1;
                                }
                            }
                            ((BarcodeActivity) context).selectCount(count);
                        }*/
                       // notifyDataSetChanged();
                    }


                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.dialog_option_item_name, viewHolder.dialog_option_item_name);

            convertView.setTag(R.id.dialog_option_item_price, viewHolder.dialog_option_item_price);
            convertView.setTag(R.id.dialog_option_item_sale, viewHolder.dialog_option_item_sale);

            convertView.setTag(R.id.checkbox_btn, viewHolder.checkbox_btn);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox_btn.setTag(position); // This line is important.

        viewHolder.dialog_option_item_name.setText("Name : "+list.get(position).getItemName());
        viewHolder.dialog_option_item_price.setText("Price : "+list.get(position).getItemRegularPrice());
        viewHolder.dialog_option_item_sale.setText("Sale : "+list.get(position).getItemSalePrice());
        //viewHolder.dialog_option_item_price.setText("Price : "+list.get(position).getItemRegularPrice());
        //viewHolder.dialog_option_item_sale.setText("Sale : "+list.get(position).getItemSalePrice());

        /*if(list.get(position).getItemRegularPrice().trim().equalsIgnoreCase("0")){
            viewHolder.dialog_option_item_price.setVisibility(View.GONE);
        }else {
            viewHolder.dialog_option_item_price.setText("Price : "+list.get(position).getItemRegularPrice());
            viewHolder.dialog_option_item_price.setPaintFlags(viewHolder.dialog_option_item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }*/

        if(list.get(position).getItemSalePrice().trim().equalsIgnoreCase("0") || list.get(position).getItemSalePrice().trim().equalsIgnoreCase("")){
            viewHolder.dialog_option_item_sale.setVisibility(View.GONE);
            viewHolder.dialog_option_item_price.setPaintFlags(viewHolder.dialog_option_item_price.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }else {
            viewHolder.dialog_option_item_sale.setVisibility(View.VISIBLE);
            viewHolder.dialog_option_item_price.setPaintFlags(viewHolder.dialog_option_item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.checkbox_btn.setChecked(list.get(position).getItemSelect());
        viewHolder.checkbox_btn.setVisibility(View.VISIBLE);

        return convertView;
    }

    public void CheckBoxVisibility(Boolean activate){
        this.activate = activate;
        notifyDataSetChanged();
    }
}