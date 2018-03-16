package com.chefswithoutlimits.customerchef.activity.customer.checkout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.circleimageview.CircleImageView;
import com.chefswithoutlimits.customerchef.activity.customer.cart.CartListActivity;
import com.chefswithoutlimits.customerchef.activity.customer.cart.OfflineCartListActivity;
import com.chefswithoutlimits.customerchef.dataVO.KitchenData;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.CreateDialog;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.util.ConstantUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScheduledDeliveryActivity extends Activity {

    Calendar myCalendar;
    Button btn_set_date;
    TextView txt_date;
    TextView headerTxt;
    TextView txt_customer_name;
    ImageView btnBack;
    ImageView btnLogout;
    TextView tv_time;
    UserInformation userInfo;
    String selectcategory="";//kitchen_id="147";
    CircleImageView imageView2;

    Button btn_continue;


    String Mondaystart="00:00";
    String Mondayend="00:00";
    String Tuesdaystart="00:00";
    String Tuesdayend="00:00";
    String Wednesdaystart="00:00";
    String Wednesdayend="00:00";
    String Thursdaystart="00:00";
    String Thursdayend="00:00";
    String Fridaystart="00:00";
    String Fridayend="00:00";
    String Saturdaystart="00:00";
    String Saturdayend="00:00";
    String Sundaystart="00:00";
    String Sundayend="00:00";

    Spinner spinner_time;
    ArrayAdapter<String> adaptertime;
    TextView tv_selectcategory;

    ArrayList<String> times;
    Date datestart = null;
    Date dateend=null;

    String selecttime="";
    String select_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delivery_scheduled);

        Intent intent = getIntent();
        //kitchen_id=intent.getStringExtra("kitchen_id");
        selectcategory=intent.getStringExtra("selectcategory");
        ConstantUtils.selectcategory="";
        times = new ArrayList<String>();
        setwedgets();
        getTimedata();
    }

    public void setwedgets(){

        userInfo = Sharepreferences.getSharePreferance(this);
        imageView2=(CircleImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getUser_log())
                .placeholder(R.mipmap.user_image)   // optional
                .error(R.mipmap.user_image)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        tv_time=(TextView)findViewById(R.id.tv_time);
        spinner_time=(Spinner)findViewById(R.id.spinner_time);
        tv_selectcategory=(TextView)findViewById(R.id.tv_selectcategory);
        tv_selectcategory.setText("Service Type : "+selectcategory);
        System.out.println("Service Type  ###  "+selectcategory);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Schedule Order");

        txt_customer_name=(TextView)findViewById(R.id.txt_customer_name);
        txt_customer_name.setText(userInfo.getFirstName()+" "+userInfo.getLastName());

        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ScheduledDeliveryActivity.this)){

                    RememberData rememberData = Sharepreferences.getRememberLogin(ScheduledDeliveryActivity.this);
                    if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                        startActivity(new Intent(ScheduledDeliveryActivity.this,CartListActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(ScheduledDeliveryActivity.this,OfflineCartListActivity.class));
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
                Logout.logOut(ScheduledDeliveryActivity.this);
                finish();
            }
        });

        txt_date=(TextView)findViewById(R.id.txt_date);
        btn_set_date=(Button)findViewById(R.id.btn_set_date);
        btn_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                int mYear = myCalendar.get(Calendar.YEAR);
                int mMonth = myCalendar.get(Calendar.MONTH);
                int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(ScheduledDeliveryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        selecttime="";
                        updateLabel();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePicker.show();
            }
        });

       /* btn_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ScheduledDeliveryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_date.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ScheduledDeliveryActivity.this,"Please select schedule Date");
                }else if(selecttime.equalsIgnoreCase("")){
                    CreateDialog.showDialog(ScheduledDeliveryActivity.this,"Please select schedule Time");
                }else if(checkcurrentdate()){

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                  //  SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    try {
                        Date SelectTime = dateFormat.parse(selecttime);
                        int Select_hr=SelectTime.getHours();
                        Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));
                        Calendar c = Calendar.getInstance();
                        int Current_hr=c.get(Calendar.HOUR_OF_DAY);
                       // int Current_hr=CurrentTime.getHours();
                        System.out.println(Current_hr+".....@@@@@@@@@@@......"+Select_hr);
                        if (Select_hr>Current_hr) {

                            Checkout.schedule_time=selecttime;
                            Checkout.schedule_date=select_date;
                            System.out.println(selecttime+"....$$$$$$$$$$$$$$$$$$$$$$...."+select_date);

                            if(InternetConnectivity.isConnectedFast(ScheduledDeliveryActivity.this)){
                                Intent intent2 = new Intent(ScheduledDeliveryActivity.this, ShippingScreen.class);
                                startActivity(intent2);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                            }



                        }else {
                            CreateDialog.showDialog(ScheduledDeliveryActivity.this,"Schedule Time can not be before CurrentTime");
                        }
                    }catch (Exception e){

                        System.out.println("Time Exception.............");
                    }

                }else {



                    Checkout.schedule_time=selecttime;
                    Checkout.schedule_date=select_date;
                    System.out.println(selecttime+"....$$$$$$$$$$$$$$$$$$$$$$...."+select_date);
                    Intent intent2 = new Intent(ScheduledDeliveryActivity.this, ShippingScreen.class);
                    startActivity(intent2);
                    finish();

                }
            }
        });

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecttime=times.get(position);
               // tv_time.setText(selecttime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean checkcurrentdate(){
        boolean check=false;

        String current_date = new SimpleDateFormat("MM/dd/yy").format(new Date());
        System.out.println(current_date+"....check............."+select_date);
        if(select_date.equalsIgnoreCase(current_date)){
            System.out.println("today............."+select_date);
            check=true;
        }
        return check;
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        select_date=sdf.format(myCalendar.getTime());
        txt_date.setText("Date : "+sdf.format(myCalendar.getTime()));


        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf2.format(myCalendar.getTime());


        times.clear();
        SimpleDateFormat sdf5 = new SimpleDateFormat("hh:mm");
        datestart = null;
        dateend=null;


        if(dayOfTheWeek.equalsIgnoreCase("Monday")){
            if(!(Mondaystart.equalsIgnoreCase("") && Mondayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Mondaystart);
                    dateend = sdf5.parse(Mondayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Tuesday")){
            if(!(Tuesdaystart.equalsIgnoreCase("") && Tuesdayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Tuesdaystart);
                    dateend = sdf5.parse(Tuesdayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Wednesday")){
            if(!(Wednesdaystart.equalsIgnoreCase("") && Wednesdayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Wednesdaystart);
                    dateend = sdf5.parse(Wednesdayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Thursday")){
            if(!(Thursdaystart.equalsIgnoreCase("") && Thursdayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Thursdaystart);
                    dateend = sdf5.parse(Thursdayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Friday")){
            if(!(Fridaystart.equalsIgnoreCase("") && Fridayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Fridaystart);
                    dateend = sdf5.parse(Fridayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Saturday")){
            if(!(Saturdaystart.equalsIgnoreCase("") && Saturdayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Saturdaystart);
                    dateend = sdf5.parse(Saturdayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else if(dayOfTheWeek.equalsIgnoreCase("Sunday")){
            if(!(Sundaystart.equalsIgnoreCase("") && Sundayend.equalsIgnoreCase(""))) {
                try {
                    datestart = sdf5.parse(Sundaystart);
                    dateend = sdf5.parse(Sundayend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setadaptertimelist();
            }
        }else {

        }



        Toast.makeText(getApplicationContext(),""+dayOfTheWeek,Toast.LENGTH_SHORT).show();
        adaptertime = new ArrayAdapter<String>(ScheduledDeliveryActivity.this,R.layout.spinner_rows, times);
        adaptertime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(adaptertime);


    }

    public void setadaptertimelist(){
        //if(!datestart.toString().equalsIgnoreCase("")||dateend.toString().equalsIgnoreCase("")) {
            SimpleDateFormat sdfm = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datestart);
            times.add(sdfm.format(datestart));////////////////////
            while (calendar.getTime().before(dateend)) {
                calendar.add(Calendar.MINUTE, 30);
                times.add(sdfm.format(calendar.getTime()));
            }
        //}

    }


    public void getTimedata(){
        final ProgressDialog progressDialog=new ProgressDialog(ScheduledDeliveryActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url = WebServiceURL.CHEF_SCHEDULER_TIME_LIST;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseTimeData(response);
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
                        Toast.makeText(ScheduledDeliveryActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ScheduledDeliveryActivity.this);
                KitchenData kitchenData = Sharepreferences.getSharePreferanceKitchenData(ScheduledDeliveryActivity.this);
                String kitchen_Id=kitchenData.getKitchen_id();
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id",kitchen_Id);
                System.out.println("kitchen_Id   @@@@@@@@ "+kitchen_Id);
                // params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseTimeData(final String response)
    {
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");


            if(Status.equalsIgnoreCase("success")) {
                JSONObject Immediate=null;

                if(selectcategory.equalsIgnoreCase("Pickup")) {
                    Immediate = parentJSON.getJSONObject("Pickup");
                }else if(selectcategory.equalsIgnoreCase("Delivery")){
                    Immediate=parentJSON.getJSONObject("Delivery");
                }else{
                    Immediate=parentJSON.getJSONObject("Immediate");
                }

                JSONObject Monday=Immediate.getJSONObject("Monday");
                Mondaystart=Monday.optString("from");
                Mondayend=Monday.optString("to");

                JSONObject Tuesday=Immediate.getJSONObject("Tuesday");
                Tuesdaystart=Tuesday.optString("from");
                Tuesdayend=Tuesday.optString("to");

                JSONObject Wednesday=Immediate.getJSONObject("Wednesday");
                Wednesdaystart=Wednesday.optString("from");
                Wednesdayend=Wednesday.optString("to");

                JSONObject Thursday=Immediate.getJSONObject("Thursday");
                Thursdaystart=Thursday.optString("from");
                Thursdayend=Thursday.optString("to");


                JSONObject Friday=Immediate.getJSONObject("Friday");
                Fridaystart=Friday.optString("from");
                Fridayend=Friday.optString("to");


                JSONObject Saturday=Immediate.getJSONObject("Saturday");
                Saturdaystart=Saturday.optString("from");
                Saturdayend=Saturday.optString("to");


                JSONObject Sunday=Immediate.getJSONObject("Sunday");
                Sundaystart=Sunday.optString("from");
                Sundayend=Sunday.optString("to");

                btn_continue.setVisibility(View.VISIBLE);

            }else {
                Toast.makeText(ScheduledDeliveryActivity.this, "No data found", Toast.LENGTH_LONG).show();
                btn_continue.setVisibility(View.GONE);
                CreateDialog.showDialog(ScheduledDeliveryActivity.this,"This Kitchen have no set their schedule time ");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ScheduledDeliveryActivity.this)){

            RememberData rememberData = Sharepreferences.getRememberLogin(ScheduledDeliveryActivity.this);
            if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("customer")){
                startActivity(new Intent(ScheduledDeliveryActivity.this,CartListActivity.class));
                finish();
            }else {
                startActivity(new Intent(ScheduledDeliveryActivity.this,OfflineCartListActivity.class));
                finish();
            }

        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
}
