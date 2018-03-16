package com.chefswithoutlimits.customerchef.activity.chefs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.WebServiceURL;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChefSchedular extends Activity {

    ImageView imageView2;
    TextView headerTxt;
    TextView btn_Immediate,btn_Delivery,btn_Pick_up;
    ImageView btnBack;
    Button sudmit;

    UserInformation userInfo;
    private ProgressDialog progressDialog;
    Spinner spinner_monday_start,spinner_monday_end,spinner_tuesday_start,spinner_tuesday_end,spinner_wednesday_start,spinner_wednesday_end,
            spinner_thursday_start,spinner_thursday_end,spinner_friday_start,spinner_friday_end,spinner_saturday_start,spinner_saturday_end,
            spinner_Sunday_start,spinner_Sunday_end;


    String monday_start="",monday_end="",tuesday_start="",tuesday_end="",wednesday_start="",wednesday_end="",
    thursday_start="",thursday_end="",friday_start="",friday_end="",saturday_start="",saturday_end="",
    Sunday_start="",Sunday_end="";

    String buttonstatus="Immediate";
    String Status="";
    String response="";


    //ArrayList<String> timeList = new ArrayList<String>();
    ArrayList<String> timeId = new ArrayList<String>();

    ArrayAdapter<String> adaptertime;

    String selecttimelist="",selecttimeid="";

    String[] timeList = {"","00:00","00:30","01:00","01:30","02:00","02:30","03:00","03:30","04:00",
            "04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00",
            "08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00",
            "12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00",
            "16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00",
            "22:30","23:00","23:30"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chef_schedular);
        setwidget();
        //dotimelisting();
        getTimedata();
    }
    public void setwidget(){

        userInfo = Sharepreferences.getSharePreferance(ChefSchedular.this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Schedulers");
        btn_Immediate=(TextView)findViewById(R.id.btn_Immediate);
        btn_Immediate.setBackgroundColor(Color.parseColor("#e33244"));

        btn_Immediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btn_Immediate.setBackgroundColor(Color.parseColor("#e33244"));//#e33244  #EB4369
                btn_Delivery.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_Pick_up.setBackgroundColor(Color.parseColor("#ffffff"));

                btn_Immediate.setTextColor(Color.parseColor("#ffffff"));
                btn_Delivery.setTextColor(Color.parseColor("#000000"));
                btn_Pick_up.setTextColor(Color.parseColor("#000000"));

                buttonstatus="Immediate";
                if(Status.equalsIgnoreCase("success"))
                parseTimeData();
            }
        });

        btn_Delivery=(TextView)findViewById(R.id.btn_Delivery);
        btn_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Immediate.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_Delivery.setBackgroundColor(Color.parseColor("#e33244"));
                btn_Pick_up.setBackgroundColor(Color.parseColor("#ffffff"));

                btn_Immediate.setTextColor(Color.parseColor("#000000"));
                btn_Delivery.setTextColor(Color.parseColor("#ffffff"));
                btn_Pick_up.setTextColor(Color.parseColor("#000000"));

                buttonstatus="Delivery";
                if(Status.equalsIgnoreCase("success"))
                parseTimeData();
            }
        });

        btn_Pick_up=(TextView)findViewById(R.id.btn_Pick_up);
        btn_Pick_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Immediate.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_Delivery.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_Pick_up.setBackgroundColor(Color.parseColor("#e33244"));

                btn_Immediate.setTextColor(Color.parseColor("#000000"));
                btn_Delivery.setTextColor(Color.parseColor("#000000"));
                btn_Pick_up.setTextColor(Color.parseColor("#ffffff"));

                buttonstatus="Pick_up";
                if(Status.equalsIgnoreCase("success"))
                parseTimeData();
            }
        });

        btn_Immediate.setTextColor(Color.parseColor("#ffffff"));
        btn_Delivery.setTextColor(Color.parseColor("#000000"));
        btn_Pick_up.setTextColor(Color.parseColor("#000000"));

        sudmit=(Button)findViewById(R.id.sudmit);
        sudmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefSchedular.this)){
                    submittimedata();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

               // System.out.println("$$$$$$$$$ "+selecttimelist);
            }
        });
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefSchedular.this)){
                    startActivity(new Intent(ChefSchedular.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageView btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.logOut(ChefSchedular.this);
                finish();
            }
        });

        spinner_monday_start=(Spinner)findViewById(R.id.spinner_monday_start);
        spinner_monday_end=(Spinner)findViewById(R.id.spinner_monday_end);
        spinner_tuesday_start=(Spinner)findViewById(R.id.spinner_tuesday_start);
        spinner_tuesday_end=(Spinner)findViewById(R.id.spinner_tuesday_end);
        spinner_wednesday_start=(Spinner)findViewById(R.id.spinner_wednesday_start);
        spinner_wednesday_end=(Spinner)findViewById(R.id.spinner_wednesday_end);
        spinner_thursday_start=(Spinner)findViewById(R.id.spinner_thursday_start);
        spinner_thursday_end=(Spinner)findViewById(R.id.spinner_thursday_end);
        spinner_friday_start=(Spinner)findViewById(R.id.spinner_friday_start);
        spinner_friday_end=(Spinner)findViewById(R.id.spinner_friday_end);
        spinner_saturday_start=(Spinner)findViewById(R.id.spinner_saturday_start);
        spinner_saturday_end=(Spinner)findViewById(R.id.spinner_saturday_end);
        spinner_Sunday_start=(Spinner)findViewById(R.id.spinner_Sunday_start);
        spinner_Sunday_end=(Spinner)findViewById(R.id.spinner_Sunday_end);


        adaptertime = new ArrayAdapter<String>(ChefSchedular.this,R.layout.spinner_rows, timeList);
        adaptertime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_monday_start.setAdapter(adaptertime);
        spinner_monday_end.setAdapter(adaptertime);
        spinner_tuesday_start.setAdapter(adaptertime);
        spinner_tuesday_end.setAdapter(adaptertime);
        spinner_wednesday_start.setAdapter(adaptertime);
        spinner_wednesday_end.setAdapter(adaptertime);
        spinner_thursday_start.setAdapter(adaptertime);
        spinner_thursday_end.setAdapter(adaptertime);

        spinner_friday_start.setAdapter(adaptertime);
        spinner_friday_end.setAdapter(adaptertime);
        spinner_saturday_start.setAdapter(adaptertime);
        spinner_saturday_end.setAdapter(adaptertime);
        spinner_Sunday_start.setAdapter(adaptertime);
        spinner_Sunday_end.setAdapter(adaptertime);

        spinner_monday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monday_start=timeList[position];
                //selecttimelist=spinner_monday_start.getItemAtPosition(position).toString();
               // Toast.makeText(getApplicationContext()," click: "+monday_start,Toast.LENGTH_SHORT).show();
               // System.out.println("@@@@@@@@@@@"+monday_start);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_monday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_tuesday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tuesday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_tuesday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tuesday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_wednesday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wednesday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_wednesday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                wednesday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_thursday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                thursday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_thursday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                thursday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_friday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                friday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_friday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                friday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_saturday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                saturday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_saturday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                saturday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Sunday_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sunday_start=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Sunday_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Sunday_end=timeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submittimedata(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefSchedular.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url="";
        if(buttonstatus.equalsIgnoreCase("Immediate"))
        url = WebServiceURL.SUBMIT_SCHEDULER_TIME_LIST_IMMEDEATE;
        if(buttonstatus.equalsIgnoreCase("Delivery"))
        url = WebServiceURL.SUBMIT_SCHEDULER_TIME_LIST_DELIVERY;
        if(buttonstatus.equalsIgnoreCase("Pick_up"))
        url = WebServiceURL.SUBMIT_SCHEDULER_TIME_LIST_PICK_UP;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parsesubmitresponse(response);
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefSchedular.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefSchedular.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id",userInfo.getUserId());
                params.put("mon_form",monday_start);
                params.put("mon_to",monday_end);
                params.put("tue_form",tuesday_start);
                params.put("tue_to",tuesday_end);
                params.put("wed_form",wednesday_start);
                params.put("wed_to",wednesday_end);
                params.put("thu_form",thursday_start);
                params.put("thu_to",thursday_end);
                params.put("fri_form",friday_start);
                params.put("fri_to",friday_end);
                params.put("sat_form",saturday_start);
                params.put("sat_to",saturday_end);
                params.put("sun_form",Sunday_start);
                params.put("sun_to",Sunday_end);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parsesubmitresponse(String response){
        try {

            JSONObject parentJSON = new JSONObject(response);
            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                Toast.makeText(ChefSchedular.this, "Submitted successfully", Toast.LENGTH_LONG).show();
                getTimedata();

            }else {
                Toast.makeText(ChefSchedular.this, "Failed to submit", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getTimedata(){
        final ProgressDialog progressDialog=new ProgressDialog(ChefSchedular.this);
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
                    public void onResponse(String response1) {
                        response=response1;
                        parseTimeData();
                        System.out.println("@@@@@@@@@@@@ $$$$$$$  "+response);
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
                        Toast.makeText(ChefSchedular.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                userInfo = Sharepreferences.getSharePreferance(ChefSchedular.this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id",userInfo.getUserId());
               // params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

    public void parseTimeData()
    {
        try {

            spinner_monday_start.setSelection(0);
            spinner_monday_end.setSelection(0);
            spinner_tuesday_start.setSelection(0);
            spinner_tuesday_end.setSelection(0);
            spinner_wednesday_start.setSelection(0);
            spinner_wednesday_end.setSelection(0);
            spinner_thursday_start.setSelection(0);
            spinner_thursday_end.setSelection(0);
            spinner_friday_start.setSelection(0);
            spinner_friday_end.setSelection(0);
            spinner_saturday_start.setSelection(0);
            spinner_saturday_end.setSelection(0);
            spinner_Sunday_start.setSelection(0);
            spinner_saturday_end.setSelection(0);
            JSONObject parentJSON = new JSONObject(response);
            Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("success")) {
                JSONObject Immediate=null;

                if(buttonstatus.equalsIgnoreCase("Pick_up")) {
                    Immediate = parentJSON.getJSONObject("Pickup");
                }else if(buttonstatus.equalsIgnoreCase("Delivery")){
                    Immediate=parentJSON.getJSONObject("Delivery");
                }else if(buttonstatus.equalsIgnoreCase("Immediate")){
                    Immediate=parentJSON.getJSONObject("Immediate");
                }

                JSONObject Monday=Immediate.getJSONObject("Monday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Monday.optString("from"))){
                        spinner_monday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Monday.optString("to"))){
                        spinner_monday_end.setSelection(i);
                    }
                }

                JSONObject Tuesday=Immediate.getJSONObject("Tuesday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Tuesday.optString("from"))){
                        spinner_tuesday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Tuesday.optString("to"))){
                        spinner_tuesday_end.setSelection(i);
                    }
                }

                JSONObject Wednesday=Immediate.getJSONObject("Wednesday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Wednesday.optString("from"))){
                        spinner_wednesday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Wednesday.optString("to"))){
                        spinner_wednesday_end.setSelection(i);
                    }
                }

                JSONObject Thursday=Immediate.getJSONObject("Thursday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Thursday.optString("from"))){
                        spinner_thursday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Thursday.optString("to"))){
                        spinner_thursday_end.setSelection(i);
                    }
                }

                JSONObject Friday=Immediate.getJSONObject("Friday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Friday.optString("from"))){
                        spinner_friday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Friday.optString("to"))){
                        spinner_friday_end.setSelection(i);
                    }
                }

                JSONObject Saturday=Immediate.getJSONObject("Saturday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Saturday.optString("from"))){
                        spinner_saturday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Saturday.optString("to"))){
                        spinner_saturday_end.setSelection(i);
                    }
                }

                JSONObject Sunday=Immediate.getJSONObject("Sunday");
                for(int i=0;i<timeList.length;i++){
                    if(timeList[i].equalsIgnoreCase(Sunday.optString("from"))){
                        spinner_Sunday_start.setSelection(i);
                    }
                    if(timeList[i].equalsIgnoreCase(Sunday.optString("to"))){
                        spinner_Sunday_end.setSelection(i);
                    }
                }

            }else {
                Toast.makeText(ChefSchedular.this, "No data found", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefSchedular.this)){
            startActivity(new Intent(ChefSchedular.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
