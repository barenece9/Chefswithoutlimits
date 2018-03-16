package com.chefswithoutlimits.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chefswithoutlimits.customerchef.activity.customer.checkout.ShippingScreen;
import com.chefswithoutlimits.customerchef.dataVO.Payment;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.chefswithoutlimits.util.InternetConnectivity;
import com.stripe.android.view.CardInputWidget;
import com.chefswithoutlimits.R;

public class PaymentActivity extends AppCompatActivity {

    private DependencyHandler mDependencyHandler;
    public static  String token_id="";
    TextView tv_payable_amount,tv_card_last_4;

    RelativeLayout ll_save_card;
    LinearLayout ll_new_card;
    CheckBox used_save_card,check_box_save_card;

    Button saveButton,btn_clear_card;
    RadioButton radio_new_card;
    RadioButton radio_old_card;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);
        TextView headerTxt = (TextView)findViewById(R.id.headerTxt);
        headerTxt.setText("Payment");
        TextView tv_payable_amount = (TextView)findViewById(R.id.tv_payable_amount);
        tv_payable_amount.setText("Amount Payable : "+ Payment.amount+" "+ Checkout.order_currency);
        ImageView btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this,ShippingScreen.class));
                finish();
            }
        });


        saveButton = (Button) findViewById(R.id.save);
        ll_new_card=(LinearLayout)findViewById(R.id.ll_new_card);
        ll_save_card=(RelativeLayout) findViewById(R.id.ll_save_card);
        tv_card_last_4=(TextView)findViewById(R.id.tv_card_last_4);


        final  RadioGroup radio_payment_method = (RadioGroup) findViewById(R.id.radio_payment_method);
        radio_new_card = (RadioButton) findViewById(R.id.radio_new_card);
        radio_old_card = (RadioButton) findViewById(R.id.radio_old_card);
        radio_new_card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                ll_save_card.setVisibility(View.GONE);
                ll_new_card.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                Checkout.usedsaveCard="0";
            }

        });

        radio_old_card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                ll_save_card.setVisibility(View.VISIBLE);
                ll_new_card.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                Checkout.usedsaveCard="1";


            }

        });

        RememberData rememberCard = Sharepreferences.getCardData(PaymentActivity.this);
        Boolean save=rememberCard.isSavecard();

        if(save){
            radio_old_card.setVisibility(View.VISIBLE);
            tv_card_last_4.setText("xxxx xxxx xxxx "+rememberCard.getLast4());
        }else {
            radio_old_card.setVisibility(View.GONE);
        }

        btn_clear_card=(Button)findViewById(R.id.btn_clear_card);
        btn_clear_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sharepreferences.setCardData(PaymentActivity.this, false, "","",0,0, "");
                ll_save_card.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                radio_old_card.setVisibility(View.GONE);
            }
        });




        Checkout.saveCard="0";
        check_box_save_card=(CheckBox)findViewById(R.id.check_box_save_card) ;
        check_box_save_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Checkout.saveCard="1";
                }else {
                    Checkout.saveCard="0";
                }
            }
        });



       /* Checkout.usedsaveCard="0";
        used_save_card=(CheckBox)findViewById(R.id.used_save_card) ;
        used_save_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Checkout.usedsaveCard="1";
                }else {
                    Checkout.usedsaveCard="0";
                }
            }
        });*/




        mDependencyHandler = new DependencyHandler(
                this,
                (CardInputWidget) findViewById(R.id.card_input_widget),
                (ListView) findViewById(R.id.listview));

        if(InternetConnectivity.isConnectedFast(PaymentActivity.this)){
            mDependencyHandler.attachAsyncTaskTokenController(saveButton);
        }else {
            Toast.makeText(getApplicationContext(),"check your internet connection",Toast.LENGTH_SHORT).show();
        }




      /*  Button saveRxButton = (Button) findViewById(R.id.saverx);
        mDependencyHandler.attachRxTokenController(saveRxButton);

        Button saveIntentServiceButton = (Button) findViewById(R.id.saveWithService);
        mDependencyHandler.attachIntentServiceTokenController(this, saveIntentServiceButton);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDependencyHandler.clearReferences();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentActivity.this,ShippingScreen.class));
        finish();
    }
}
