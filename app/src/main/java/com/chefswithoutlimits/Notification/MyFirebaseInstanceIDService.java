package com.chefswithoutlimits.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        System.out.println("Token @@@@@@@@@@@@@@@@@@@ : "+refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        UserInformation userInfo = Sharepreferences.getSharePreferance(MyFirebaseInstanceIDService.this);
        RememberData rememberData = Sharepreferences.getRememberLogin(this);
        if(rememberData.isLogin() && rememberData.getUserType().equalsIgnoreCase("chef")) {
            INSERT_FIREBASE_REG_ID(token);
        }

        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }



    void INSERT_FIREBASE_REG_ID(final String token)
    {
        String url;

        url = WebServiceURL.INSERT_FIREBASE_REG_ID;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parseData(response);
                        System.out.println("@@@@@@@@@@@@@ "+response);
                       /* if(progressDialog!=null)
                            progressDialog.dismiss();*/
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* if(progressDialog!=null)
                            progressDialog.dismiss();*/
                        System.out.println("Error=========="+error);
                       // Toast.makeText(this, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                UserInformation userInfo = Sharepreferences.getSharePreferance(MyFirebaseInstanceIDService.this);
                Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",userInfo.getUserId());
                params.put("regId",token);
                params.put("format","json");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(postRequest);
    }


    public void parseData(String response)
    {
        try {
            JSONObject parentJSON = new JSONObject(response);

            String Status=parentJSON.getString("Status");
            if(Status.equalsIgnoreCase("true")) {
                String message=parentJSON.getString("message");
                System.out.println("+++++++++++++++++++++++= "+message);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

