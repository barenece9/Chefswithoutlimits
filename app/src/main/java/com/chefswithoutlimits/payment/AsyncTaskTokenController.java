package com.chefswithoutlimits.payment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chefswithoutlimits.appconroller.AppController;
import com.chefswithoutlimits.customerchef.activity.customer.checkout.PaymentResponseActivity;
import com.chefswithoutlimits.customerchef.dataVO.Payment;
import com.chefswithoutlimits.customerchef.dataVO.RememberData;
import com.chefswithoutlimits.util.ApplicationData;
import com.chefswithoutlimits.util.Checkout;
import com.chefswithoutlimits.util.Sharepreferences;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Logic needed to create tokens using the {@link android.os.AsyncTask} methods included in the
 * sdk: {@link Stripe#createToken(Card, String, TokenCallback)}.
 */
public class AsyncTaskTokenController {

    private CardInputWidget mCardInputWidget;
    private Context mContext;
    private ErrorDialogHandler mErrorDialogHandler;
    private ListViewController mOutputListController;
    private ProgressDialogController mProgressDialogController;
    private String mPublishableKey;

    private Context context;

    public AsyncTaskTokenController(
            @NonNull Button button,
            @NonNull CardInputWidget cardInputWidget,
            @NonNull Context context,
            @NonNull ErrorDialogHandler errorDialogHandler,
            @NonNull ListViewController outputListController,
            @NonNull ProgressDialogController progressDialogController,
            @NonNull String publishableKey) {
        mCardInputWidget = cardInputWidget;
        mContext = context;
        mErrorDialogHandler = errorDialogHandler;
        mPublishableKey = publishableKey;
        mProgressDialogController = progressDialogController;
        mOutputListController = outputListController;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });
    }

    public void detach() {
        mCardInputWidget = null;
    }

    private void saveCard() {

        RememberData rememberCard = Sharepreferences.getCardData(mContext);
        Boolean save=rememberCard.isSavecard();
        Card cardToSave;
        if(!Checkout.usedsaveCard.equalsIgnoreCase("1")) {
            cardToSave = mCardInputWidget.getCard();
            if(Checkout.saveCard.equalsIgnoreCase("1"))
            Sharepreferences.setCardData(mContext, true, cardToSave.getNumber(),cardToSave.getCVC(),cardToSave.getExpMonth(),cardToSave.getExpYear(), cardToSave.getLast4());
        }else {
            cardToSave = new Card(rememberCard.getCardnumber(), rememberCard.getExpdate(), rememberCard.getExpyear(), rememberCard.getCvvnumber());
        }
        if (cardToSave == null) {
            mErrorDialogHandler.showError("Invalid Card Data");
            return;
        }
        mProgressDialogController.startProgress();
        new Stripe(mContext).createToken(
                cardToSave,
                mPublishableKey,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        mOutputListController.addToList(token);
                       // mProgressDialogController.finishProgress();
                        String token_id=token.getId();
                        System.out.println("ASYNC TOKEN IS : "+token_id);
                        payment(token_id);

                    }
                    public void onError(Exception error) {
                        mErrorDialogHandler.showError(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });
    }



    public  void payment(final String token_id){

        System.out.println("SECRETE TOKEN IS : "+token_id);
        String url;
        //url= WebServiceURL.STRIPE_PAYMENT;
         url = "https://chefswithoutlimits.com/webservice_android/charge.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        parsePaymentResponse(response);
                        System.out.println("@@@@@@@@@@@ "+response);
                      //  if(progressDialog2!=null)
                          //  progressDialog2.dismiss();
                        mProgressDialogController.finishProgress();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      // if(progressDialog2!=null)
                           // progressDialog2.dismiss();
                        mProgressDialogController.finishProgress();
                        System.out.println("Error=========="+error);
                        mErrorDialogHandler.showError("Failed to Process Payment");
                        //CreateDialog.showDialog();
                       //Toast.makeText(mContext, "Have a Network Error Please check Internet Connection.", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

               // float pay_float=Float.valueOf(Payment.amount)*100;
                float pay_float=Float.valueOf(Payment.amount)*100;
                int amt_int=(int)pay_float;
                String amount=String.valueOf(amt_int);

                params.put("stripeToken",token_id);

                params.put("private_key", ApplicationData.SECRET_KEY);
                params.put("public_key",ApplicationData.PUBLISHABLE_KEY);

                params.put("amount",amount);
                params.put("currency",Checkout.order_currency);
                params.put("cust_email", Checkout.billing_email);
                params.put("kitchenID", Checkout.kitchenID);

                Log.e("Stripe Payment","==================================================");
                Log.e("stripeToken",token_id);
                Log.e("private_key", ApplicationData.SECRET_KEY);
                Log.e("public_key",ApplicationData.PUBLISHABLE_KEY);
                Log.e("amount",amount);
                Log.e("currency",Checkout.order_currency);
                Log.e("cust_email",Checkout.billing_email);
                Log.e("kitchenID",Checkout.kitchenID);

                return params;
            }
        };
        // Adding request to volley request queue
        postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        AppController.getInstance().getRequestQueue().getCache().clear();
    }

   public  void parsePaymentResponse(String response){

        try {
            JSONObject parentObject = new JSONObject(response);
            //JSONArray parentArray = parentObject.getJSONArray("output");

            String response_str=parentObject.optString("response");
            if(response_str.equalsIgnoreCase("Success")){
                JSONObject  charge = parentObject.optJSONObject("charge");
                String status = charge.getString("status");
                if(status.equalsIgnoreCase("succeeded")){

                    String balance_transaction=charge.getString("balance_transaction");
                    String id=charge.getString("id");

                    Checkout.payment_stat="Y";

                    Checkout.stripe_charge_id=id;

                    /*Intent intent=new Intent(mContext,PaymentResponseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);*/

                }else {
                    mErrorDialogHandler.showError("Failed to Process Payment..");
                }
            }else {
                mErrorDialogHandler.showError("Failed to Process Payment..");
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("PAYMENT RESPONSE : "+response);
    }
}
