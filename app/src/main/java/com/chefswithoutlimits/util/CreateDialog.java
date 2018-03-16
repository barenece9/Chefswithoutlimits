package com.chefswithoutlimits.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.chefswithoutlimits.R;

public class CreateDialog {

	
	/*public static void showDialog(Context context,String alertText)
	{
		new AlertDialog.Builder(context)
	    .setTitle("Alert!")
	    .setMessage(alertText)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    *//*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)*//*
	     .show();
	}*/


	public static void showDialog(Context activity, String msg){
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.custom_aleart_dialog);

		TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
		text.setText(msg);

		Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}
}
