package com.chefswithoutlimits.customerchef.activity.customer.cart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.customer.search.SearchKitchen;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.adapter.ExpandableListAdapter;
import com.chefswithoutlimits.customerchef.adapter.OptionArrayAdapter;
import com.chefswithoutlimits.customerchef.dataVO.Oplist;
import com.chefswithoutlimits.customerchef.dataVO.OptionItemValue;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitchenDetailsActivity extends Activity implements View.OnClickListener {

    ImageView btnBack,imgCart;
    ImageView btnLogout;
    TextView headerTxt,txtCusName,Cart_counter;
    ImageView imageView2;
    ImageView imageView1;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;

    public  static List<String> rat;
    public  static List<String> rat2;
    public  static List<String> rat3;


    String kitchen_id="";
    String kitchenName="";
    ArrayList<HashMap<String,String>> menuidlist;
    ArrayList<HashMap<String,String>> optionlist;
    UserInformation userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kitchen_details);
        Intent intent = getIntent();
        kitchen_id=intent.getStringExtra("kitchen_id");
        kitchenName=intent.getStringExtra("kitchenName");
        System.out.println("kitchen_id +++ "+kitchen_id);
        setwidgets();

    }

    void setwidgets() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnLogout = (ImageView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        headerTxt = (TextView) findViewById(R.id.headerTxt);
        headerTxt.setText("KITCHEN MENU");
        imgCart=(ImageView)findViewById(R.id.imgCart);
        imgCart.setVisibility(View.VISIBLE);
        imgCart.setOnClickListener(this);

        txtCusName = (TextView) findViewById(R.id.txtCusName);
        txtCusName.setText(kitchenName);

        Cart_counter=(TextView)findViewById(R.id.Cart_counter);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        KitchenData kitchenData = Sharepreferences.getSharePreferanceKitchenData(this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ kitchenData.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

      /*  imageView1=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ kitchenData.getInsurance_file())
                .placeholder(R.mipmap.dashboard_list)   // optional
                .error(R.mipmap.dashboard_list)      // optional
                .resize(400,400)                        // optional
                .into(imageView1);*/

        getmenutable();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.btnBack:

                if(InternetConnectivity.isConnectedFast(KitchenDetailsActivity.this)){
                    if(!ConstantUtils.onlineCartStatus) {
                        startActivity(new Intent(KitchenDetailsActivity.this, SearchKitchen.class));
                        finish();
                    }else {
                        startActivity(new Intent(KitchenDetailsActivity.this, CustomerDashboardActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.imgCart:

                if(InternetConnectivity.isConnectedFast(KitchenDetailsActivity.this)){
                    if(ConstantUtils.onlineCartStatus) {
                        startActivity(new Intent(KitchenDetailsActivity.this, CartListActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"cart is empty",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnLogout:
                Logout.logOut(KitchenDetailsActivity.this);
                finish();
                break;
        }
    }


    /*
      * Preparing the list data
      */


    public void getmenutable(){
        final ProgressDialog progressDialog=new ProgressDialog(KitchenDetailsActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.KITCHEN_MENU_TABLE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseKitchenMenuTable(response);
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(KitchenDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kitchen_id",kitchen_id);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseKitchenMenuTable(String response)
    {
        try {

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {

                JSONArray jArray = parentObj.getJSONArray("Records");

                int i=0;

                for ( i = 0; i < jArray.length(); i++) {

                    JSONObject innerObj = jArray.optJSONObject(i);
                    HashMap<String,String> list=new HashMap<>();

                    list.put("menu_catid",innerObj.optString("menu_catid"));
                    list.put("cat_name",innerObj.optString("cat_name"));
                    listDataHeader.add(innerObj.optString("cat_name"));

                    JSONArray menu = innerObj.getJSONArray("menu");
                    List<String> item = new ArrayList<String>();
                    for (int j = 0; j < menu.length(); j++){
                        JSONObject inner2Obj = menu.optJSONObject(j);



                        if(inner2Obj.optString("menu_description").equalsIgnoreCase("")){
                            item.add(inner2Obj.optString("menu_name")+"chef@app2017"
                                    +inner2Obj.optString("regular_price")+"chef@app2017"+
                                    inner2Obj.optString("sale_price")+"chef@app2017"
                                    +inner2Obj.optString("id")+"chef@app2017"+
                                    inner2Obj.optString("currency")+"chef@app2017"
                                    +inner2Obj.optString("menu_image")+"chef@app2017"+
                                    inner2Obj.optString("multiple_option")+"chef@app2017"
                                    +"NA");
                        }else {
                            item.add(inner2Obj.optString("menu_name")+"chef@app2017"
                                    +inner2Obj.optString("regular_price")+"chef@app2017"+
                                    inner2Obj.optString("sale_price")+"chef@app2017"
                                    +inner2Obj.optString("id")+"chef@app2017"+
                                    inner2Obj.optString("currency")+"chef@app2017"
                                    +inner2Obj.optString("menu_image")+"chef@app2017"+
                                    inner2Obj.optString("multiple_option")+"chef@app2017"
                                    +inner2Obj.optString("menu_description"));
                        }
                    }
                    listDataChild.put(listDataHeader.get(i), item);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        getcartcounter();
    }


    public void callOption(final String menu_id){
        final ProgressDialog progressDialog2=new ProgressDialog(KitchenDetailsActivity.this);
        progressDialog2.setMessage("loading...");
        progressDialog2.setCancelable(false);
        progressDialog2.setCanceledOnTouchOutside(false);
        progressDialog2.show();
        String url;

        url = WebServiceURL.KITCHEN_MENU_OPTION_TABLE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseOptinalMenu(response,menu_id);
                        if(progressDialog2!=null)
                            progressDialog2.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog2!=null)
                            progressDialog2.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(KitchenDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("menu_id",menu_id);
                params.put("format","json");
                System.out.println("MENU ID  ++++++++   "+menu_id);
                return params;
            }
        };
        // Adding request to volley request queue

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseOptinalMenu(String response,String menu_id)
    {
        ArrayList<OptionItemValue> optionItemValues=new ArrayList<>();
        try {
            /*optionlist=new ArrayList<>();
            optionlist.clear();*/

            optionItemValues.clear();

            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {

                JSONArray jArray = parentObj.getJSONArray("Records");

                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject innerObj = jArray.optJSONObject(i);

                    /*HashMap<String,String> list=new HashMap<>();
                    list.put("item_id",innerObj.optString("item_id"));
                    list.put("item_name",innerObj.optString("item_name"));
                    list.put("item_sale_price",innerObj.optString("item_sale_price"));
                    list.put("item_regular_price",innerObj.optString("item_regular_price"));
                    optionlist.add(list);*/

                    OptionItemValue optionItem=new OptionItemValue();
                    optionItem.setItemId(innerObj.optString("item_id"));
                    optionItem.setItemName(innerObj.optString("item_name"));
                    optionItem.setItemSalePrice(innerObj.optString("item_sale_price"));
                    optionItem.setItemRegularPrice(innerObj.optString("item_regular_price"));
                    optionItem.setItemSelect(false);

                    optionItemValues.add(optionItem);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       showdialog(optionItemValues,menu_id);
    }

    public  void  showdialog(final ArrayList<OptionItemValue> optionItemValues,final String menu_id){

        final Dialog dialog = new Dialog(KitchenDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu_option);
        dialog.setCancelable(true);

        // set values for custom dialog components - text, image and button
        ListView dialog_listView = (ListView) dialog.findViewById(R.id.dialog_listView);
       // dialog_listView.setAdapter(new DialogOptionAdapter(KitchenDetailsActivity.this,optionlist));
        OptionArrayAdapter adapterBarcode = new OptionArrayAdapter(KitchenDetailsActivity.this,optionItemValues);
        dialog_listView.setAdapter(adapterBarcode);

        dialog.show();

        Button btn_close=(Button)dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_done = (Button) dialog.findViewById(R.id.dialog_btn_add_to_care);
        // if decline button is clicked, close the custom dialog
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                String menu_option_item_id="";


                for (int i=0;i<optionItemValues.size();i++){
                    if(optionItemValues.get(i).getItemSelect()) {
                        if(menu_option_item_id.equalsIgnoreCase("")){
                            menu_option_item_id = optionItemValues.get(i).getItemID();
                        }else {
                            menu_option_item_id = menu_option_item_id + "," + optionItemValues.get(i).getItemID();
                        }
                    }
                }


                System.out.println(Oplist.oplist.size()+" selected checkbox***********  "+menu_option_item_id);


                if(menu_option_item_id.equalsIgnoreCase("")){
                    System.out.println(" option items......  "+menu_option_item_id);
                    addtocart(menu_id);
                }else {
                    System.out.println(" option items......  "+menu_option_item_id);
                    addtocartafteroption(menu_id, menu_option_item_id);
                }
                dialog.dismiss();
            }
        });


    }




    public void addtocart(final String menu_id){
        final ProgressDialog progressDialog3=new ProgressDialog(KitchenDetailsActivity.this);
        progressDialog3.setMessage("loading...");
        progressDialog3.setCancelable(false);
        progressDialog3.setCanceledOnTouchOutside(false);
        progressDialog3.show();
        String url;

        url = WebServiceURL.ADD_TO_CART_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseaddtocartresponse(response);
                        if(progressDialog3!=null)
                            progressDialog3.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog3!=null)
                            progressDialog3.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(KitchenDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(KitchenDetailsActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("customer_id",userInfo.getUserId());
                params.put("kitchen_id",kitchen_id);
                params.put("menu_id",menu_id);
                params.put("menu_item","0");
                params.put("quantity","1");
                params.put("format","json");
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void addtocartafteroption(final String menu_id,final String menu_item){
        final ProgressDialog progressDialog4=new ProgressDialog(KitchenDetailsActivity.this);
        progressDialog4.setMessage("loading...");
        progressDialog4.setCancelable(false);
        progressDialog4.setCanceledOnTouchOutside(false);
        progressDialog4.show();
        String url;

        url = WebServiceURL.ADD_TO_CART_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseaddtocartresponse(response);
                        if(progressDialog4!=null)
                            progressDialog4.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog4!=null)
                            progressDialog4.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(KitchenDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(KitchenDetailsActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("customer_id",userInfo.getUserId());
                params.put("kitchen_id",kitchen_id);
                params.put("menu_id",menu_id);
                params.put("menu_item",menu_item);
                params.put("quantity","1");
                params.put("format","json");
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseaddtocartresponse(String response)
    {
        try {

            System.out.println("Response : "+response);
            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {
            String Message=parentObj.optString("Message");
                Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_LONG).show();
                getcartcounter();
            }else {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void getcartcounter(){
        final ProgressDialog progressDialog5=new ProgressDialog(KitchenDetailsActivity.this);
        progressDialog5.setMessage("loading...");
        progressDialog5.setCancelable(false);
        progressDialog5.setCanceledOnTouchOutside(false);
        progressDialog5.show();
        String url;

        url = WebServiceURL.GET_CART_COUNTER;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parsecartcounter(response);
                        if(progressDialog5!=null)
                            progressDialog5.dismiss();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog5!=null)
                            progressDialog5.dismiss();
                        System.out.println("Error=========="+error);
                        Toast.makeText(KitchenDetailsActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(KitchenDetailsActivity.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id",userInfo.getUserId());
                params.put("kitchen_id",kitchen_id);
                params.put("format","json");
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public  void parsecartcounter(String response){
        try {

            System.out.println("Response : "+response);
            JSONObject parentObj = new JSONObject(response);

            String Status=parentObj.optString("Status");
            if(Status.equalsIgnoreCase("true")) {
                ConstantUtils.onlineCartStatus=true;
                String Count=parentObj.optString("Count");
                Cart_counter.setVisibility(View.VISIBLE);
                Cart_counter.setText(Count);
            }else {
                ConstantUtils.onlineCartStatus=false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(KitchenDetailsActivity.this)){
            if(!ConstantUtils.onlineCartStatus) {
                startActivity(new Intent(KitchenDetailsActivity.this, SearchKitchen.class));
                finish();
            }else {
                startActivity(new Intent(KitchenDetailsActivity.this, CustomerDashboardActivity.class));
                finish();
            }
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
