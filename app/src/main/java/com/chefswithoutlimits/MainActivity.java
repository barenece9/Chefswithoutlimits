package com.chefswithoutlimits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.payment.PaymentActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText custmerEdtuser;
    EditText custmerEdtpass;

    Button customerLogin;

    String userName = "";
    String password = "";

    //ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setwidgets();
    }

    public void setwidgets(){

        custmerEdtuser = (EditText)findViewById(R.id.custmerEdtuser);
        custmerEdtpass = (EditText)findViewById(R.id.custmerEdtpass);

        customerLogin = (Button)findViewById(R.id.customerLogin);
        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = custmerEdtuser.getText().toString();
                password = custmerEdtpass.getText().toString();

               /* if(userName.equalsIgnoreCase(""))
                {
                    CreateDialog.showDialog(MainActivity.this, "Enter username");

                }else if(password.equalsIgnoreCase(""))
                {
                    CreateDialog.showDialog(MainActivity.this, "Enter password");

                }
                else{
                    doUserLogin();
                }*/
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

    }


    public void doUserLogin()
    {
        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url;

        url ="https://chefswithoutlimits.com/webservice_android/login_service.php";

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
                        Toast.makeText(MainActivity.this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username",userName);
                params.put("password",password);
                params.put("format","json");
                Log.d("Params",userName+"   "+password);
                System.out.println(userName+"    "+password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
       /* AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();*/
    }

    public void parseResponse(String response){
        Toast.makeText(getApplicationContext(),"Response : "+response,Toast.LENGTH_LONG).show();
    }
}
