package com.chefswithoutlimits.customerchef.activity.chefs.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chefswithoutlimits.R;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.util.WebServiceURL;

public class ChefOrderDetailsActivity extends Activity {

    WebView webView;
    String id="";
    ImageView btnBack;
    TextView headerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order_details);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        System.out.println("@@@@@@@@@@@ id : "+id);
        getwidgets();
    }

    public void getwidgets(){
        webView = (WebView) findViewById(R.id.webView1);
        String url= WebServiceURL.CHEF_INVOICE+""+id+"&user_type=chef";
        //String url="https://chefswithoutlimits.com/webservice_android/get_orderdetails.php?order_id="+id;
        startWebView(url);
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefOrderDetailsActivity.this)){
                    startActivity(new Intent(ChefOrderDetailsActivity.this,ChefOrderActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("INVOICE");
    }


    private void startWebView(String url) {
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ChefOrderDetailsActivity.this);
                    progressDialog.setMessage("Loading...");
                    //progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        //float scale = 100 * webView.getScale();

        webView.setInitialScale(75);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(url);


    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }

        if(InternetConnectivity.isConnectedFast(ChefOrderDetailsActivity.this)){
            startActivity(new Intent(ChefOrderDetailsActivity.this,ChefOrderActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
