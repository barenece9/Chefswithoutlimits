package com.chefswithoutlimits.customerchef.activity.chefs.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chefswithoutlimits.customerchef.activity.chefs.ChefDashboardActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.category.ChefMenuCategoryActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.item.ChefMenuItemActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.option.ChefMenuOptionActivity;
import com.chefswithoutlimits.customerchef.activity.chefs.menu.optionsets.ChefOptionSetsActivity;
import com.chefswithoutlimits.util.Logout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.WebServiceURL;
import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.dataVO.UserInformation;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.squareup.picasso.Picasso;

public class ChefMenuActivity extends Activity implements View.OnClickListener {

    TextView headerTxt;
    ImageView btnLogout;
    Button btn_menu_category,btn_menu_item,btn_menu_option_item,btn_menu_option_sets;
    ImageView btnBack;
    ImageView imageView2;
    UserInformation userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_menu_option);
        setwidget();
    }
    public  void setwidget(){

        userInfo = Sharepreferences.getSharePreferance(ChefMenuActivity.this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Picasso.with(this)
                .load(WebServiceURL.IMAGE_PATH+""+ userInfo.getKichen_Logo())
                .placeholder(R.mipmap.logo_box)   // optional
                .error(R.mipmap.logo_box)      // optional
                .resize(400,400)                        // optional
                .into(imageView2);

        headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Menu");
        btnLogout = (ImageView)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btn_menu_option_item=(Button)findViewById(R.id.btn_menu_option_item);
        btn_menu_option_item.setOnClickListener(this);
        btn_menu_option_sets=(Button)findViewById(R.id.btn_menu_option_sets);
        btn_menu_option_sets.setOnClickListener(this);
        btn_menu_item=(Button)findViewById(R.id.btn_menu_item);
        btn_menu_item.setOnClickListener(this);
        btn_menu_category=(Button)findViewById(R.id.btn_menu_category);
        btn_menu_category.setOnClickListener(this);
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
                    startActivity(new Intent(ChefMenuActivity.this,ChefDashboardActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub

        switch (view.getId()) {
            case R.id.btn_menu_category:
                if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
                    startActivity(new Intent(this,ChefMenuCategoryActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_menu_item:

                if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
                    startActivity(new Intent(this,ChefMenuItemActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_menu_option_item:

                if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
                    startActivity(new Intent(this,ChefMenuOptionActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_menu_option_sets:

                if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
                    startActivity(new Intent(this,ChefOptionSetsActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLogout:
                Logout.logOut(this);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(InternetConnectivity.isConnectedFast(ChefMenuActivity.this)){
            startActivity(new Intent(ChefMenuActivity.this,ChefDashboardActivity.class));
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }
}
