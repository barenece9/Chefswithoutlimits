package com.chefswithoutlimits.customerchef.activity.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.chefswithoutlimits.stripeconnect.StripeApp;
import com.chefswithoutlimits.stripeconnect.StripeButton;
import com.chefswithoutlimits.stripeconnect.StripeConnectListener;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.R;

import com.stripe.Stripe;





public class ConnectStripeActivity extends AppCompatActivity {

    private StripeApp mApp, mApp2;
    private TextView tvSummary;
    private StripeButton mStripeButton, mStripeButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_stripe);
        mApp = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
                ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);

        tvSummary = (TextView) findViewById(R.id.tvSummary);
        if (mApp.isConnected()) {
            tvSummary.setText("Connected as " + mApp.getAccessToken());
            System.out.println("Access Token : "+mApp.getAccessToken());
        }

        mStripeButton = (StripeButton) findViewById(R.id.btnConnect1);
        mStripeButton.setStripeApp(mApp);
        mStripeButton.addStripeConnectListener(new StripeConnectListener() {

            @Override
            public void onConnected() {
                tvSummary.setText("Connected as " + mApp.getAccessToken());
                System.out.println("Access Token : "+mApp.getAccessToken());
            }

            @Override
            public void onDisconnected() {
                tvSummary.setText("Disconnected");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ConnectStripeActivity.this, error, Toast.LENGTH_SHORT).show();
            }

        });

        mApp2 = new StripeApp(this, "StripeAccount", ApplicationData.CLIENT_ID,
                ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);
        mStripeButton2 = (StripeButton) findViewById(R.id.btnConnect2);
        mStripeButton2.setStripeApp(mApp2);
        mStripeButton2.setConnectMode(StripeApp.CONNECT_MODE.ACTIVITY);

        Stripe.apiKey = mApp.getAccessToken();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode) {
            case StripeApp.RESULT_CONNECTED:
                tvSummary.setText("Connected as " + mApp.getAccessToken());
                System.out.println("Access Token : "+mApp.getAccessToken());
                break;
            case StripeApp.RESULT_ERROR:
                String error_description = data.getStringExtra("error_description");
                Toast.makeText(ConnectStripeActivity.this, error_description, Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
