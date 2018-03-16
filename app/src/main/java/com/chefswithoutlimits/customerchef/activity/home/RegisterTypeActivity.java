package com.chefswithoutlimits.customerchef.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chefswithoutlimits.customerchef.activity.chefs.registration.ChefRrgisterActivity;
import com.chefswithoutlimits.customerchef.activity.customer.registration.CustomerRrgisterActivity;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.chefswithoutlimits.R;

public class RegisterTypeActivity extends Activity implements OnClickListener{


	Button btnChefReg;
	Button btnCustomerReg;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registertype_activity);

		setWidget();
	}


	public void setWidget()
	{
		btnChefReg = (Button)findViewById(R.id.btnRegChef);
		btnChefReg.setOnClickListener(this);
		btnCustomerReg = (Button)findViewById(R.id.btnRegCustomer);
		btnCustomerReg.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("CHEF REGISTRATION");
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnRegChef:


			if(InternetConnectivity.isConnectedFast(RegisterTypeActivity.this)){
				startActivity(new Intent(RegisterTypeActivity.this,ChefRrgisterActivity.class));
				finish();
			}else {
				Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btnRegCustomer:

			Bundle bundle = new Bundle();
			bundle.putString("guestOrder","0");
			Intent intent2 = new Intent(RegisterTypeActivity.this, CustomerRrgisterActivity.class);
			intent2.putExtras(bundle);
			startActivity(intent2);
			finish();

			/*startActivity(new Intent(RegisterTypeActivity.this,CustomerRrgisterActivity.class));
			finish();*/
			break;

		case R.id.btnBack:

			startActivity(new Intent(RegisterTypeActivity.this, HomeActivity.class));
			finish();

			break;

		case R.id.btnLogout:

			break;
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(RegisterTypeActivity.this, HomeActivity.class));
		finish();
	}
}
