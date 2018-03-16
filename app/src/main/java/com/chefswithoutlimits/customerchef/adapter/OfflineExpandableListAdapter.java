package com.chefswithoutlimits.customerchef.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineKitchenDetailsActivity;
import com.chefswithoutlimits.customerchef.dataVO.Oplist;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class OfflineExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String id="";

    public OfflineExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        String part1="",part2="",part3="",currency="",menu_image="",multiple_option="";
        try {
            String string = (String) getChild(groupPosition, childPosition);
            String[] parts = string.split("chef@app2017");
            part1 = parts[0]; // 004
            part2 = parts[1];
            part3 = parts[2];

            id = parts[3];
            currency = parts[4];
            menu_image = parts[5];
            multiple_option=parts[6];
            System.out.println("$$$$$$$$$$$$$  "+multiple_option);
        }catch (Exception e){

        }


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        TextView txtListChild2 = (TextView) convertView.findViewById(R.id.lblListItem2);
        TextView txtListChild3 = (TextView) convertView.findViewById(R.id.lblListItem3);

        LinearLayout ll_details=(LinearLayout)convertView.findViewById(R.id.ll_details);
        ll_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string7 = (String) getChild(groupPosition, childPosition);
                String[] parts7 = string7.split("chef@app2017");
                //String menu_description = parts7[7];
                //String menu_name = parts7[0];// 004
                //menu_description_dialog(menu_description,menu_name);
                //CreateDialog.showDialog(_context,menu_description);
               // Toast.makeText(_context,menu_description,Toast.LENGTH_SHORT).show();
                if(parts7.length>7){
                    String menu_description = parts7[7];
                    String menu_name = parts7[0];// 004
                    menu_description_dialog(menu_description,menu_name);
                }else {
                    Toast.makeText(_context,"no description found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_add_to_cart = (Button) convertView.findViewById(R.id.btn_add_to_cart);
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(_context,"add to cart",Toast.LENGTH_SHORT).show();
                String string2 = (String) getChild(groupPosition, childPosition);
                String[] parts2 = string2.split("chef@app2017");
                String menu_id = parts2[3]; // 004
                if(_context instanceof OfflineKitchenDetailsActivity){
                    ((OfflineKitchenDetailsActivity)_context).addtocart(menu_id);
                }
            }
        });
        Button btn_option = (Button) convertView.findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(_context,"option",Toast.LENGTH_SHORT).show();
                Oplist.oplist.clear();
                String string2 = (String) getChild(groupPosition, childPosition);
                String[] parts2 = string2.split("chef@app2017");
                String menu_id = parts2[3]; // 004
                if(_context instanceof OfflineKitchenDetailsActivity){
                    ((OfflineKitchenDetailsActivity)_context).callOption(menu_id);
                }
            }
        });
        if(multiple_option.equalsIgnoreCase("Y")){
            btn_add_to_cart.setVisibility(View.GONE);
            btn_option.setVisibility(View.VISIBLE);
        }else{
            btn_add_to_cart.setVisibility(View.VISIBLE);
            btn_option.setVisibility(View.GONE);
        }

        txtListChild.setText(""+part1);
        txtListChild2.setText("Price : "+part2+" "+currency);
        txtListChild3.setText("Sale : "+part3+" "+currency);
        /*if(part2.trim().equalsIgnoreCase("0")){
            txtListChild2.setVisibility(View.GONE);
        }else {
            txtListChild2.setText("Price : "+part2+" "+currency);
            txtListChild2.setPaintFlags(txtListChild2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }*/
        if(part3.trim().equalsIgnoreCase("0.00") || part3.trim().equalsIgnoreCase("")){
            txtListChild3.setVisibility(View.GONE);
            txtListChild2.setPaintFlags(txtListChild2.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }else {
            txtListChild3.setVisibility(View.VISIBLE);
            txtListChild2.setPaintFlags(txtListChild2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        /*txtListChild2.setText("Price : "+part2+" "+currency);
        txtListChild3.setText("Sale : "+part3+" "+currency);*/

        NetworkImageView item_img=(NetworkImageView)convertView.findViewById(R.id.item_img);
        if(!menu_image.equalsIgnoreCase("")) {
            item_img.setImageUrl(WebServiceURL.IMAGE_PATH+""+menu_image, imageLoader);
        }
        item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string_img = (String) getChild(groupPosition, childPosition);
                String[] parts_img = string_img.split("chef@app2017");
                String menu_img = parts_img[5]; // 004

                if(!menu_img.equalsIgnoreCase("")) {
                    image_zoom_dialog(WebServiceURL.IMAGE_PATH+""+menu_img);
                }

            }
        });
        
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void menu_description_dialog(String menu_description,String menu_name){
        final Dialog dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu_discription);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tv_menu_name);
        text.setText(menu_name);

        TextView tv_menu_description = (TextView) dialog.findViewById(R.id.tv_menu_description);
        tv_menu_description.setText(menu_description);

        Button dialogButtonCross = (Button) dialog.findViewById(R.id.btn_cross);
        // if button is clicked, close the custom dialog
        dialogButtonCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void image_zoom_dialog(String img_url){
// custom dialog
        final Dialog dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
      //  TextView text = (TextView) dialog.findViewById(R.id.text);
     //   text.setText("Android custom dialog example!");
        ImageView image = (ImageView) dialog.findViewById(R.id.iv);


        Picasso.with(_context)
                .load(img_url)
                .placeholder(R.drawable.chef_logo_round)   // optional
                .error(R.drawable.chef_logo_round)      // optional
                .resize(400,400)                        // optional
                .into(image);

        System.out.println("IMAGE DIALOG UMAGE URL $$$$$$$$$$$$$$$$$ "+img_url);
        Button dialogButtonCross = (Button) dialog.findViewById(R.id.btn_cross);
        // if button is clicked, close the custom dialog
        dialogButtonCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}