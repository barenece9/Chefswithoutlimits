package com.chefswithoutlimits.customerchef.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.customer.CustomerDashboardActivity;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.util.InternetConnectivity;

public class SplashActivity extends Activity {
	
	private static final int SPLASH_TIME_OUT = 2000;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

     	mRunnable = new Runnable() {
            @Override
            public void run() {
                openLandingActivity();
            }
        };
        mHandler.postDelayed(mRunnable, SPLASH_TIME_OUT);
    }

    protected void openLandingActivity() {
		// TODO Auto-generated method stub

            RememberData rememberData = Sharepreferences.getRememberLogin(this);
            if(rememberData.isLogin()) {

                if(InternetConnectivity.isConnectedFast(SplashActivity.this)){
                    if(rememberData.getUserType().equalsIgnoreCase("chef")){
                        startActivity(new Intent(SplashActivity.this, ChefDashboardActivity.class));
                        finish();
                    }else if(rememberData.getUserType().equalsIgnoreCase("customer")){
                        startActivity(new Intent(SplashActivity.this, CustomerDashboardActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                    finish();
                }


            }else {

                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
		}
	@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        // TODO Auto-generated method stub
	        if(keyCode == KeyEvent.KEYCODE_BACK) {
	            mHandler.removeCallbacks(mRunnable);
	            finish();
	        }
	        return super.onKeyDown(keyCode, event);
	    }
}
