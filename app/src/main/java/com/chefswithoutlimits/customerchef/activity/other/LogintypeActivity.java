package com.chefswithoutlimits.customerchef.activity.other;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chefswithoutlimits.R;
import com.chefswithoutlimits.customerchef.activity.home.LoginActivity;
import com.chefswithoutlimits.util.RegisterActivities;

public class LogintypeActivity extends Activity implements OnClickListener{


	Button btnChef;
	Button btnCustomer;
	ImageView btnBack;
	ImageView btnLogout;
	TextView headerTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logintype_activity);
		RegisterActivities.registerActivity(LogintypeActivity.this);

		setWidget();
	}


	public void setWidget()
	{
		btnChef = (Button)findViewById(R.id.btnloginChef);
		btnChef.setOnClickListener(this);
		btnCustomer = (Button)findViewById(R.id.btnloginCustomer);
		btnCustomer.setOnClickListener(this);

		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogout = (ImageView)findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(this);
		headerTxt = (TextView)findViewById(R.id.headerTxt);
		headerTxt.setText("LOGIN");
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnloginChef:
			
			startActivity(new Intent(LogintypeActivity.this,LoginActivity.class));

			break;

		case R.id.btnloginCustomer:

			startActivity(new Intent(LogintypeActivity.this,LoginActivity.class));
			break;

		case R.id.btnBack:


			finish();

			break;

		case R.id.btnLogout:

			break;
		}
	}
}
