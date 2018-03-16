package com.chefswithoutlimits.customerchef.activity.customer.checkout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.customerchef.activity.customer.cart.CartListActivity;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineCartListActivity;
import com.chefswithoutlimits.customerchef.dataVO.Address;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.adapter.OrderDetailsAdapter;
import com.chefswithoutlimits.customerchef.dataVO.Address2;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.payment.PaymentActivity;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.ConstantUtils;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShippingScreen extends Activity {


    TextView txt_customer_name;
    TextView headerTxt;
    Button editBtnUSetting,editBtnUPass,chefeditBtndeliveryUpdate;
    ImageView btnBack;
    ImageView btnLogout;
    EditText deliveryFName,deliveryMName,deliveryLName,deliveryEmail,deliveryPhone,
            deliveryAddress,deliveryZip,
            delivery_spl_instn,delivery_buzzer_no,delivery_unit_no;
    Spinner spinnerdeliveryCountry,spinnerdeliveryState,spinnerdeliveryCity;
    TextView spinnerdeliveryCountrytitle,spinnerdeliveryStatetitle,spinnerdeliveryCitytitle,deliveryZiptitle,deliveryAddresstitle;
    UserInformation userInfo;
    KitchenData kitchenData;
    RememberData rememberData;
    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> countryId = new ArrayList<String>();
    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> stateId = new ArrayList<String>();
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> cityId = new ArrayList<String>();
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;
    String countryid = "";
    String stateid = "";
    String cityid = "";
    String countrySelect="";
    String stateSelect="";
    String citySelect="";

    Address addr=new Address();
    Address2 addr2=new Address2();
    Button btn_typt_text;
    String countryname="",statename="",cityname="";


    /*main address...*/
    TextView da_name,da_email,da_address,da_address_title,da_phone,sa_name,sa_email,sa_phone,sa_address,tv_title_deliveryMName,
            delivery_unit_no_title,delivery_buzzer_no_title,delivery_spl_instn_title;


    Button btn_edit_sa,btn_edit_da;
    View edit_layout,main_layout;
    String address_type="";
    Button btn_order_details,btn_continue;
    OrderDetailsAdapter adapter;
    ListView list_dialog;
    ArrayList<HashMap<String,String>> itemData ;

    TextView tv_tax,tv_sub_total,tv_total;

    String countryCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shipping_screen);
        kitchenData = Sharepreferences.getSharePreferanceKitchenData(this);
        userInfo = Sharepreferences.getSharePreferance(ShippingScreen.this);
        da_name=(TextView)findViewById(R.id.da_name);
        da_email=(TextView)findViewById(R.id.da_email);
        da_address=(TextView)findViewById(R.id.da_address);
        da_address_title=(TextView)findViewById(R.id.da_address_title);
        da_phone=(TextView)findViewById(R.id.da_phone);


        delivery_unit_no_title=(TextView)findViewById(R.id.delivery_unit_no_title);
        delivery_buzzer_no_title=(TextView)findViewById(R.id.delivery_buzzer_no_title);
        delivery_spl_instn_title=(TextView)findViewById(R.id.delivery_spl_instn_title);

        delivery_unit_no=(EditText) findViewById(R.id.delivery_unit_no);
        delivery_buzzer_no=(EditText)findViewById(R.id.delivery_buzzer_no);
        delivery_spl_instn=(EditText)findViewById(R.id.delivery_spl_instn);

        sa_name=(TextView)findViewById(R.id.sa_name);
        sa_email=(TextView)findViewById(R.id.sa_email);
        sa_phone=(TextView)findViewById(R.id.sa_phone);
        sa_address=(TextView)findViewById(R.id.sa_address);

        edit_layout= findViewById(R.id.ll_edit_address);
        main_layout= findViewById(R.id.ll_main);

        btn_order_details=(Button)findViewById(R.id.btn_order_details);
        btn_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDialog();
            }
        });
        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Checkout.order_status="pending";  //('draft', 'pending', 'delivered', 'cancelled')
                Checkout.payment_stat="Y";  //('Y', 'N')

                Checkout.shipping_first_name=addr.getFname();
                Checkout.shipping_middle_name=addr.getMname();
                Checkout.shipping_last_name=addr.getLname();
                Checkout.shipping_address=addr.getAddress();
                Checkout.email=addr.getEmail();
                Checkout.phone=addr.getPhone();
                Checkout.shipping_phone_code=addr.getPhoneCode();
                Checkout.shipping_country=addr.getCountry();
                Checkout.shipping_state=addr.getState();
                Checkout.shipping_city=addr.getCity();
                Checkout.shipping_zip=addr.getZip();

                Checkout.shipping_unit_no=addr.getUnit_no();
                Checkout.shipping_buzzer_no=addr.getBuzzer_no();
                Checkout.shipping_spl_instn=addr.getSpl_instn();

                Checkout.billing_first_name=addr2.getFname();
                Checkout.billing_middle_name=addr2.getMname();
                Checkout.billing_last_name=addr2.getLname();
                Checkout.billing_email=addr2.email;
                Checkout.billing_address=addr2.getAddress();
                Checkout.billing_phone=addr2.getPhone();
                Checkout.billing_phone_code=addr2.getPhoneCode();
                Checkout.billing_country=addr2.getCountry();
                Checkout.billing_zip=addr2.getZip();

                Checkout.billing_unit_no=addr2.getUnit_no();
                Checkout.billing_buzzer_no=addr2.getBuzzer_no();
                Checkout.billing_spl_instn=addr2.getSpl_instn();


                if(InternetConnectivity.isConnectedFast(ShippingScreen.this)){

                    String mim_order_amount=kitchenData.getMin_order().trim();
                    if(mim_order_amount.equalsIgnoreCase("") || mim_order_amount.equalsIgnoreCase("0")){
                        mim_order_amount="1";
                    }

                    if(Double.valueOf(Checkout.total_amont)>=Double.valueOf(mim_order_amount)){
                        if(Checkout.delivery_type.equalsIgnoreCase("1")){
                            // for pickup=========================================
                            if(Checkout.shipping_first_name.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your name");
                            }else if(Checkout.phone.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your phone number");
                            }else {


                                checkStripeMode();

                            }
                        }else {
                            if(Checkout.shipping_first_name.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your name");
                            }else if(Checkout.email.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your email");
                            }else if(Checkout.phone.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your phone number");
                            }else if(Checkout.shipping_address.equalsIgnoreCase("")){
                                CreateDialog.showDialog(ShippingScreen.this, "please check your address");
                            }else {


                                checkStripeMode();
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Amount Below Minimum "+mim_order_amount+" Not Accepted",Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }



            }
        });

        btn_edit_da=(Button)findViewById(R.id.btn_edit_da);
        btn_edit_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_layout.setVisibility(View.GONE);
                Animation bottom_up = AnimationUtils.loadAnimation(ShippingScreen.this,
                        R.anim.bottom_up);
                edit_layout.startAnimation(bottom_up);
                edit_layout.setVisibility(View.VISIBLE);

                btn_typt_text.setText("Your Details");
                address_type="DD";


                deliveryFName.setText(addr.getFname());
                deliveryMName.setText(addr.getMname());
                deliveryLName.setText(addr.getLname());
                deliveryEmail.setText(addr.getEmail());
                deliveryPhone.setText(addr.getPhone());
                //deliveryPhoneCode.setText(addr.getPhoneCode());
                deliveryAddress.setText(addr.getAddress());
                deliveryZip.setText(addr.getZip());

                delivery_unit_no.setText(addr.getUnit_no());
                delivery_buzzer_no.setText(addr.getBuzzer_no());
                delivery_spl_instn.setText(addr.getSpl_instn());

                countrySelect=addr.getCountry();
                stateSelect=addr.getState();
                citySelect=addr.getCity();

                if(InternetConnectivity.isConnectedFast(ShippingScreen.this)){
                    if(!Checkout.delivery_type.equalsIgnoreCase("1")){{
                        //not call for pickup
                        doCountryList();
                    }}

                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }




            }
        });
        btn_edit_sa=(Button)findViewById(R.id.btn_edit_sa);
        btn_edit_sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_layout.setVisibility(View.GONE);
                Animation bottom_up = AnimationUtils.loadAnimation(ShippingScreen.this,
                        R.anim.bottom_up);
                edit_layout.startAnimation(bottom_up);
                edit_layout.setVisibility(View.VISIBLE);

                btn_typt_text.setText("SHIPPING DETAILS");
                address_type="SD";


                deliveryFName.setText(addr2.getFname());
                deliveryMName.setText(addr2.getMname());
                deliveryLName.setText(addr2.getLname());
                deliveryEmail.setText(addr2.getEmail());
                deliveryPhone.setText(addr2.getPhone());
                //deliveryPhoneCode.setText(addr2.getPhoneCode());
                deliveryAddress.setText(addr2.getAddress());
                deliveryZip.setText(addr2.getZip());

                delivery_unit_no.setText(addr2.getUnit_no());
                delivery_buzzer_no.setText(addr2.getBuzzer_no());
                delivery_spl_instn.setText(addr2.getSpl_instn());

                countrySelect=addr2.getCountry();
                stateSelect=addr2.getState();
                citySelect=addr2.getCity();


                if(InternetConnectivity.isConnectedFast(ShippingScreen.this)){
                    doCountryList();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }


            }
        });


        setWidget();
        countryCode=GetCountryZipCode();
        System.out.println("countryCode "+countryCode+"\n");
        Log.e("countryCode ",countryCode);
        getalldetails();
    }

    public void setWidget(){

        btn_typt_text=(Button)findViewById(R.id.btn_typt_text);
        Button btn_edit_submit=(Button)findViewById(R.id.btn_edit_submit);
        btn_edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_layout.setVisibility(View.GONE);
               // main_layout.setVisibility(View.GONE);
                Animation bottom_down = AnimationUtils.loadAnimation(ShippingScreen.this,
                        R.anim.bottom_up);
                main_layout.startAnimation(bottom_down);
                main_layout.setVisibility(View.VISIBLE);

                if(address_type.equalsIgnoreCase("DD")) {
                    addr.setFname(deliveryFName.getText().toString());
                    addr.setMname(deliveryMName.getText().toString());
                    addr.setLname(deliveryLName.getText().toString());
                    addr.setEmail(deliveryEmail.getText().toString());
                    addr.setPhone(deliveryPhone.getText().toString());
                    addr.setZip(deliveryZip.getText().toString());
                    addr.setAddress(deliveryAddress.getText().toString());

                    addr.setUnit_no(delivery_unit_no.getText().toString());
                    addr.setBuzzer_no(delivery_buzzer_no.getText().toString());
                    addr.setSpl_instn(delivery_spl_instn.getText().toString());

                    addr.setCity(cityid);
                    addr.setState(stateid);
                    addr.setCountry(countryid);

                    addr.setCityname(cityname);
                    addr.setStatename(statename);
                    addr.setCountryname(countryname);

                    da_name.setText("Name : " + addr.getFname() + " " + addr.getMname() + " " + addr.getLname());
                    da_email.setText("Email Id : " + addr.getEmail());
                    da_phone.setText("Phone : " + addr.getPhone());
                   // da_phone.setText("Phone : "+addr.getPhoneCode()+""+addr.getPhone());

                    String unit_buzzer="(Unit: "+addr.getUnit_no()+", Buzzer: "+addr.getBuzzer_no()+")";
                    String spl_instruction="Special Instructions: "+addr.getSpl_instn();
                    da_address.setText(""+addr.getAddress()+","+unit_buzzer+", "+addr.getCityname()+", "+addr.getStatename()+", "+addr.getCountryname()+", "+addr.getZip()+"\n"+spl_instruction);

                    //da_address.setText("" + addr.getAddress() + ", " + addr.getCityname() + ", " + addr.getStatename() + ", " + addr.getCountryname() + ", " + addr.getZip());
                }else if(address_type.equalsIgnoreCase("SD")) {
                    addr2.setFname(deliveryFName.getText().toString());
                    addr2.setMname(deliveryMName.getText().toString());
                    addr2.setLname(deliveryLName.getText().toString());
                    addr2.setEmail(deliveryEmail.getText().toString());
                    addr2.setPhone(deliveryPhone.getText().toString());
                    addr2.setZip(deliveryZip.getText().toString());
                    addr2.setAddress(deliveryAddress.getText().toString());

                    addr2.setUnit_no(delivery_unit_no.getText().toString());
                    addr2.setBuzzer_no(delivery_buzzer_no.getText().toString());
                    addr2.setSpl_instn(delivery_spl_instn.getText().toString());

                    addr2.setCity(cityid);
                    addr2.setState(stateid);
                    addr2.setCountry(countryid);

                    addr2.setCityname(cityname);
                    addr2.setStatename(statename);
                    addr2.setCountryname(countryname);

                    sa_name.setText("Name : " + addr2.getFname() + " " + addr2.getMname() + " " + addr2.getLname());
                    sa_email.setText("Email Id : " + addr2.getEmail());
                    sa_phone.setText("Phone : " + addr2.getPhone());
                    sa_address.setText("Address : " + addr2.getAddress() + "," + addr2.getCityname() + "," + addr2.getStatename() + "," + addr2.getCountryname() + "," + addr2.getZip());
                }

            }
        });



        userInfo = Sharepreferences.getSharePreferance(this);
        txt_customer_name=(TextView)findViewById(R.id.txtCusName);
        txt_customer_name.setText(userInfo.getFirstName()+" "+userInfo.getLastName());

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Address Details");

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ShippingScreen.this)){


                    RememberData rememberData = Sharepreferences.getRememberLogin(ShippingScreen.this);
                    if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                        startActivity(new Intent(ShippingScreen.this,CartListActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(ShippingScreen.this,OfflineCartListActivity.class));
                        finish();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.logOut(ShippingScreen.this);
                finish();
            }
        });

        deliveryFName = (EditText)findViewById(R.id.deliveryFName);
        deliveryMName = (EditText)findViewById(R.id.deliveryMName);
        deliveryLName = (EditText)findViewById(R.id.deliveryLName);
        deliveryEmail = (EditText)findViewById(R.id.deliveryEmail);
        deliveryPhone = (EditText)findViewById(R.id.deliveryPhone);
       // deliveryPhoneCode = (EditText)findViewById(R.id.deliveryPhoneCode);
        deliveryAddress = (EditText)findViewById(R.id.deliveryAddress);
        deliveryZip = (EditText)findViewById(R.id.deliveryZip);


        /*new element............................................*/
        /*delivery_spl_instn = (EditText)findViewById(R.id.delivery_spl_instn);
        delivery_buzzer_no = (EditText)findViewById(R.id.delivery_buzzer_no);
        delivery_spl_instn = (EditText)findViewById(R.id.delivery_spl_instn);*/


        deliveryAddresstitle = (TextView)findViewById(R.id.deliveryAddresstitle);
        deliveryZiptitle = (TextView)findViewById(R.id.deliveryZiptitle);
        tv_title_deliveryMName=(TextView)findViewById(R.id.tv_title_deliveryMName);

        spinnerdeliveryCountrytitle = (TextView)findViewById(R.id.spinnerdeliveryCountrytitle);
        spinnerdeliveryStatetitle = (TextView)findViewById(R.id.spinnerdeliveryStatetitle);
        spinnerdeliveryCitytitle = (TextView)findViewById(R.id.spinnerdeliveryCitytitle);




        spinnerdeliveryCountry = (Spinner)findViewById(R.id.spinnerdeliveryCountry);
        spinnerdeliveryCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //chefRegCountry = countryList.get(position);
                countryid = countryId.get(position);
                countryname=countryList.get(position);

                if(!countryid.equalsIgnoreCase("-1"))
                    doStateList(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        spinnerdeliveryState = (Spinner)findViewById(R.id.spinnerdeliveryState);
        spinnerdeliveryState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //chefRegState = stateList.get(position);
                stateid = stateId.get(position);
                statename=stateList.get(position);

                if(!stateid.equalsIgnoreCase("-1"))
                    doCityList(stateid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        spinnerdeliveryCity = (Spinner)findViewById(R.id.spinnerdeliveryCity);
        spinnerdeliveryCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                //kitchenCity=cityList.get(position);
                cityid=cityId.get(position);
                cityname=cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        countryAdapter = new ArrayAdapter<String>(ShippingScreen.this,R.layout.spinner_rows, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryCountry.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<String>(ShippingScreen.this,R.layout.spinner_rows, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryState.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<String>(ShippingScreen.this,R.layout.spinner_rows, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdeliveryCity.setAdapter(cityAdapter);




        //Checkout.delivery_type="1"; //Pickup==1/Delivery==2
        if(Checkout.delivery_type.equalsIgnoreCase("1")){
            //for pickup========================
            deliveryFName.setVisibility(View.VISIBLE);
            deliveryMName.setVisibility(View.GONE);
            deliveryLName.setVisibility(View.VISIBLE);
            deliveryEmail.setVisibility(View.VISIBLE);
            deliveryPhone.setVisibility(View.VISIBLE);

            tv_title_deliveryMName.setVisibility(View.GONE);
            deliveryAddress.setVisibility(View.GONE);
            deliveryZip.setVisibility(View.GONE);
            deliveryAddresstitle.setVisibility(View.GONE);
            deliveryZiptitle.setVisibility(View.GONE);
            spinnerdeliveryCountry.setVisibility(View.GONE);
            spinnerdeliveryState.setVisibility(View.GONE);
            spinnerdeliveryCity.setVisibility(View.GONE);
            spinnerdeliveryCountrytitle.setVisibility(View.GONE);
            spinnerdeliveryStatetitle.setVisibility(View.GONE);
            spinnerdeliveryCitytitle.setVisibility(View.GONE);
            da_address.setVisibility(View.GONE);
            da_address_title.setVisibility(View.GONE);

            delivery_spl_instn.setVisibility(View.GONE);
            delivery_spl_instn_title.setVisibility(View.GONE);
            delivery_unit_no_title.setVisibility(View.GONE);
            delivery_unit_no.setVisibility(View.GONE);
            delivery_buzzer_no.setVisibility(View.GONE);
            delivery_buzzer_no_title.setVisibility(View.GONE);



        }else if(Checkout.delivery_type.equalsIgnoreCase("2")) {
            //for delivery======================
            deliveryFName.setVisibility(View.VISIBLE);
            deliveryMName.setVisibility(View.GONE);
            deliveryLName.setVisibility(View.VISIBLE);
            deliveryEmail.setVisibility(View.VISIBLE);
            deliveryPhone.setVisibility(View.VISIBLE);

            tv_title_deliveryMName.setVisibility(View.GONE);
            deliveryAddress.setVisibility(View.VISIBLE);
            deliveryZip.setVisibility(View.VISIBLE);
            deliveryAddresstitle.setVisibility(View.VISIBLE);
            deliveryZiptitle.setVisibility(View.VISIBLE);
            spinnerdeliveryCountry.setVisibility(View.VISIBLE);
            spinnerdeliveryState.setVisibility(View.VISIBLE);
            spinnerdeliveryCity.setVisibility(View.VISIBLE);
            spinnerdeliveryCountrytitle.setVisibility(View.VISIBLE);
            spinnerdeliveryStatetitle.setVisibility(View.VISIBLE);
            spinnerdeliveryCitytitle.setVisibility(View.VISIBLE);
            da_address.setVisibility(View.VISIBLE);
            da_address_title.setVisibility(View.VISIBLE);

            delivery_spl_instn.setVisibility(View.VISIBLE);
            delivery_spl_instn_title.setVisibility(View.VISIBLE);
            delivery_unit_no_title.setVisibility(View.VISIBLE);
            delivery_unit_no.setVisibility(View.VISIBLE);
            delivery_buzzer_no.setVisibility(View.VISIBLE);
            delivery_buzzer_no_title.setVisibility(View.VISIBLE);

        }
    }


    public void getalldetails(){
        final ProgressDialog progressDialog=new ProgressDialog(ShippingScreen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.GET_CUSTOMER_DELIVERY_SETTING;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseALLDetails(response);
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
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();

                RememberData rememberData = Sharepreferences.getRememberLogin(ShippingScreen.this);
                if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                    params.put("userID",userInfo.getUserId());
                }else {
                    params.put("userID","");
                }
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }


    public void parseALLDetails(String parse)
    {
        try {

            JSONObject parentObject = new JSONObject(parse);

            String Status = parentObject.getString("status");
            if(Status.equalsIgnoreCase("true")){

                String first_name=parentObject.getString("first_name");
                if(first_name.equalsIgnoreCase("")){
                    first_name=userInfo.getFirstName();
                }
                String middle_name=parentObject.getString("middle_name");
                String last_name=parentObject.getString("last_name");
                if(last_name.equalsIgnoreCase("")){
                    last_name=userInfo.getLastName();
                }
                String email_address=parentObject.getString("email_address");
                if(email_address.equalsIgnoreCase("")){
                    email_address=userInfo.getEmail();
                }
                String phone_no=parentObject.getString("phone_no");
                if(phone_no.equalsIgnoreCase("")){
                    phone_no=userInfo.getContact();
                }



                da_name.setText("Name : "+first_name+" "+middle_name+" "+last_name);
                da_email.setText("Email Id : "+email_address);
                da_phone.setText("Phone : "+phone_no);
               // da_phone.setText("Phone : "+countryCode+""+phone_no);


                sa_name.setText("Name : "+first_name+" "+middle_name+" "+last_name);
                sa_email.setText("Email Id : "+email_address);
                sa_phone.setText("Phone : "+phone_no);

                String address=parentObject.getString("address");
                String zipcode=parentObject.getString("zipcode");

                String country=parentObject.getString("country");
                String state=parentObject.getString("state");
                String city=parentObject.getString("city");

                String unit_no=parentObject.getString("aptno");
                String buzzer_no=parentObject.getString("buzzerno");
                String spl_instn=parentObject.getString("sp_instructions");

                JSONObject country_details=parentObject.getJSONObject("country_details");
                String country_name=country_details.getString("name");
                JSONObject state_details=parentObject.getJSONObject("state_details");
                String state_name=state_details.getString("name");
                JSONObject city_details=parentObject.getJSONObject("city_details");
                String city_name=city_details.getString("name");
                String unit_buzzer="(Unit: "+unit_no+", Buzzer: "+buzzer_no+")";
                String spl_instruction="Special Instructions: "+spl_instn;
                da_address.setText(""+address+","+unit_buzzer+", "+city_name+", "+state_name+", "+country_name+", "+zipcode+"\n"+spl_instruction);

               /* da_address.setText("Address : "+address+","+city_name+","+state_name+","+country_name+","+zipcode);*/
                sa_address.setText("Address : "+address+","+city_name+","+state_name+","+country_name+","+zipcode);


                /*for delivery address*/
                addr.setFname(first_name);
                addr.setLname(last_name);
                addr.setMname(middle_name);
                addr.setEmail(email_address);
                addr.setPhone(phone_no);
                addr.setPhoneCode(countryCode);
                addr.setAddress(address);
                addr.setZip(zipcode);
                addr.setCity(city);
                addr.setState(state);
                addr.setCountry(country);

                addr.setUnit_no(unit_no);
                addr.setBuzzer_no(buzzer_no);
                addr.setSpl_instn(spl_instn);


                /*for shipping address*/
                addr2.setFname(first_name);
                addr2.setLname(last_name);
                addr2.setMname(middle_name);
                addr2.setEmail(email_address);
                addr2.setPhone(phone_no);
                addr2.setPhoneCode(countryCode);
                addr2.setAddress(address);
                addr2.setZip(zipcode);
                addr2.setCity(city);
                addr2.setState(state);
                addr2.setCountry(country);

                addr2.setUnit_no(unit_no);
                addr2.setBuzzer_no(buzzer_no);
                addr2.setSpl_instn(spl_instn);



            }else {

                String first_name=userInfo.getFirstName();;


                String last_name=userInfo.getLastName();

                String email_address=userInfo.getEmail();

                String phone_no=userInfo.getContact();




                da_name.setText("Name : "+first_name+" "+last_name);
                da_email.setText("Email Id : "+email_address);
                da_phone.setText("Phone : "+phone_no);
                //da_phone.setText("Phone : "+countryCode+""+phone_no);


                sa_name.setText("Name : "+first_name+" "+last_name);
                sa_email.setText("Email Id : "+email_address);
                sa_phone.setText("Phone : "+phone_no);

                da_address.setText("");

               /* da_address.setText("Address : "+address+","+city_name+","+state_name+","+country_name+","+zipcode);*/
                sa_address.setText("Address : ");


                /*for delivery address*/
                addr.setFname(first_name);
                addr.setLname(last_name);
                addr.setMname("");
                addr.setEmail(email_address);
                addr.setPhone(phone_no);
                addr.setPhoneCode(countryCode);
                addr.setAddress("");
                addr.setZip("");
                addr.setCity("");
                addr.setState("");
                addr.setCountry("");

                 addr.setUnit_no("");
                addr.setBuzzer_no("");
                addr.setSpl_instn("");


                /*for shipping address*/
                addr2.setFname(first_name);
                addr2.setLname(last_name);
                addr2.setMname("");
                addr2.setEmail(email_address);
                addr2.setPhone(phone_no);
                addr2.setPhoneCode(countryCode);
                addr2.setAddress("");
                addr2.setZip("");
                addr2.setCity("");
                addr2.setState("");
                addr2.setCountry("");

                addr2.setUnit_no("");
                addr2.setBuzzer_no("");
                addr2.setSpl_instn("");

                Toast.makeText(ShippingScreen.this, "please check your delivery address", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       // doCountryList();
    }



    void doCountryList()
    {
        final ProgressDialog progressDialog2=new ProgressDialog(ShippingScreen.this);
        progressDialog2.setMessage("loading...");
        progressDialog2.show();
        progressDialog2.setCancelable(false);
        progressDialog2.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.COUNTRY_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseCountry(response);
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
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseCountry(String response)
    {
        try {

            if(countryList.size()>0){

                countryList.clear();
                countryId.clear();
            }

            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("Records");

            countryList.add("Select Country");
            countryId.add("-1");

            for(int i=0;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                countryList.add(records.optString("name"));
                countryId.add(records.optString("id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        countryAdapter.notifyDataSetChanged();
        int position = countryId.indexOf(countrySelect);
        spinnerdeliveryCountry.setSelection(position);
        //doStateList(countrySelect);
    }

    //================================== Get state data ====================================

    void doStateList(final String countryId)
    {
        final ProgressDialog progressDialog4=new ProgressDialog(ShippingScreen.this);
        progressDialog4.setMessage("loading...");
        progressDialog4.show();
        progressDialog4.setCancelable(false);
        progressDialog4.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.STATE_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseState(response);
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
                        //doStateList(countryid);
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("country_id",countryId);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseState(String response)
    {
        try {

            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("Records");

            if(stateList.size()>0){

                stateList.clear();
                stateId.clear();
            }

            stateList.add("Select State");
            stateId.add("-1");

            for(int i=0;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                stateList.add(records.optString("name"));
                stateId.add(records.optString("id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        stateAdapter.notifyDataSetChanged();
        int position = stateId.indexOf(stateSelect);
        spinnerdeliveryState.setSelection(position);
        //doCityList(stateSelect);
    }



    public void parseCity(String response)
    {
        try {

            if(cityList.size()>0){

                cityList.clear();
                cityId.clear();
            }


            JSONObject parentObj = new JSONObject(response);

            JSONArray jArray = parentObj.getJSONArray("Records");

            cityList.add("Select City");
            cityId.add("-1");

            for(int i=0;i<jArray.length();i++){

                JSONObject records = jArray.optJSONObject(i);
                //JSONObject records = innerObj.optJSONObject("records");

                cityList.add(records.optString("name"));
                cityId.add(records.optString("id"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cityAdapter.notifyDataSetChanged();
        int position = cityId.indexOf(citySelect);
        spinnerdeliveryCity.setSelection(position);
        //doCurrencyList();
    }


    //================================== Get open close time ====================================

    void doCityList(final String stateId)
    {
        final ProgressDialog progressDialog3=new ProgressDialog(ShippingScreen.this);
        progressDialog3.setMessage("loading...");
        progressDialog3.show();
        progressDialog3.setCancelable(false);
        progressDialog3.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.CITY_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseCity(response);
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
                        //doCityList(stateid);
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("state_id",stateId);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void OrderDialog(){
        final Dialog dialog = new Dialog(ShippingScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.order_dialog);

        list_dialog=(ListView)dialog.findViewById(R.id.list_dialog);
        tv_total=(TextView)dialog.findViewById(R.id.tv_total);
        tv_sub_total=(TextView)dialog.findViewById(R.id.tv_sub_total);
        tv_tax=(TextView)dialog.findViewById(R.id.tv_tax);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        doCartItemList();
    }


    void doCartItemList()
    {
        final ProgressDialog progressDialog=new ProgressDialog(ShippingScreen.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.CART_ITEM_DETAILS;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
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
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ShippingScreen.this);
                Map<String, String>  params = new HashMap<String, String>();
                RememberData rememberData = Sharepreferences.getRememberLogin(ShippingScreen.this);
                if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                    params.put("user_id",userInfo.getUserId());
                }else {
                    params.put("user_id",ConstantUtils.sessionUserId);
                }
                //params.put("user_id",userInfo.getUserId());
                params.put("format","json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    void parseResponse(String response){

        itemData = new ArrayList<>();
        if(itemData.size()>0)
            itemData.clear();

        String currency = "$";

        try {

            JSONObject parentObject = new JSONObject(response);
            String Status=parentObject.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                JSONArray parentArray = parentObject.getJSONArray("Records");
                if (parentArray.length() > 0) {
                    for (int i = 0; i < parentArray.length(); i++) {
                        HashMap<String,String> list=new HashMap<String,String>();
                        JSONObject reocrdObj = parentArray.optJSONObject(i);
                        list.put("cart_id",reocrdObj.optString("cart_id"));
                        list.put("menu_id",reocrdObj.optString("menu_id"));
                        list.put("kitchenID",reocrdObj.optString("kitchenID"));
                      //  kitchenID=reocrdObj.optString("kitchenID");
                        list.put("kichen_name",reocrdObj.optString("kichen_name"));
                      //  kichen_name=reocrdObj.optString("kichen_name");
                        list.put("price",reocrdObj.optString("price"));
                        list.put("menu_name",reocrdObj.optString("menu_name"));
                        list.put("menu_image",reocrdObj.optString("menu_image"));
                        list.put("quantity",reocrdObj.optString("quantity"));
                        list.put("items",reocrdObj.optString("items"));
                        itemData.add(list);
                    }

                }
                JSONObject Kitchen_details=parentObject.getJSONObject("Kitchen_details");
                String service_details=Kitchen_details.optString("service_details");
                String currency_code=Kitchen_details.optString("currency_code");
                String total_price=Kitchen_details.optString("total_price");
                String tax_rate=Kitchen_details.optString("tax_rate");
                String tax_amount=Kitchen_details.optString("tax_amount");
                tv_tax.setText("Tax\n"+tax_amount+" "+currency_code);
                String total_price_after_tax=Kitchen_details.optString("grand_total_price");
                tv_total.setText("TOTAL\n"+total_price_after_tax+" "+currency_code);
                tv_sub_total.setText("SUB-TOTAL\n"+total_price+" "+currency_code);


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //adapter.notifyDataSetChanged();
        adapter = new OrderDetailsAdapter(ShippingScreen.this, itemData);
        list_dialog.setAdapter(adapter);

		/*for(int i=0;i<itemData.size();i++){

			sum = sum + Integer.parseInt(itemData.get(i).getItemPrice());
			currency = itemData.get(i).getItemCurrency();
		}

		texTotal.setText(currency+" "+sum+"");*/

    }




    public void checkStripeMode()
    {
        final ProgressDialog progressDialog2=new ProgressDialog(ShippingScreen.this);
        progressDialog2.setMessage("loading...");
        progressDialog2.show();
        progressDialog2.setCancelable(false);
        progressDialog2.setCanceledOnTouchOutside(false);
        String url;

        url =WebServiceURL.CHECK_STRIPE_MODE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseStripeMode(response);
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
                        Toast.makeText(ShippingScreen.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("type","stripe");
                params.put("status","1");
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseStripeMode(String response)
    {
        try {

            JSONObject parentObj = new JSONObject(response);
            String Status=parentObj.getString("Status");

            if(Status.equalsIgnoreCase("true")){

                JSONObject innerObj=parentObj.getJSONObject("data");

                String id=innerObj.getString("id");
                String type=innerObj.getString("type");
                String url=innerObj.getString("url");
                String client_id=innerObj.getString("client_id");
                String private_key=innerObj.getString("private_key");
                String public_key=innerObj.getString("public_key");
                String status=innerObj.getString("status");
                String mode=innerObj.getString("mode");

                //for stripe connect==============================
                ApplicationData.CLIENT_ID = client_id;
                ApplicationData.SECRET_KEY = private_key;
                ApplicationData.CALLBACK_URL = url;
                //for stripe payment==================================
                ApplicationData.PUBLISHABLE_KEY=public_key;

                ApplicationData.STRIPE_MODE=mode;

                if(mode.equalsIgnoreCase("test")){
                    Toast.makeText(getApplicationContext(),"payment is in test mode",Toast.LENGTH_SHORT).show();
                }else if(mode.equalsIgnoreCase("live")){
                    Toast.makeText(getApplicationContext(),"payment is in live mode",Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(ShippingScreen.this, PaymentActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String GetCountryZipCode(){
        String CountryID="";
        String CountryIDNet="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();
        switch (simState) {

            case (TelephonyManager.SIM_STATE_ABSENT): break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED): break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_UNKNOWN): break;
            case (TelephonyManager.SIM_STATE_READY): {
                //getNetworkCountryIso
                CountryID= manager.getSimCountryIso().toUpperCase();
                CountryIDNet= manager.getNetworkCountryIso().toUpperCase();
                System.out.println("CountryID "+CountryID+"\n");
                System.out.println("CountryIDNet "+CountryIDNet+"\n");
                String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
                for(int i=0;i<rl.length;i++){
                    String[] g=rl[i].split(",");
                    if(g[1].trim().equals(CountryID.trim())){
                        CountryZipCode=g[1];
                        break;
                    }
                }
            }
        }
        return CountryZipCode;
    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ShippingScreen.this)){

            RememberData rememberData = Sharepreferences.getRememberLogin(ShippingScreen.this);
            if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                startActivity(new Intent(ShippingScreen.this,CartListActivity.class));
                finish();
            }else {
                startActivity(new Intent(ShippingScreen.this,OfflineCartListActivity.class));
                finish();
            }


        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
