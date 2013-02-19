package edu.neu.madcourse.michaelallen;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;

@SuppressWarnings("deprecation")
public class Team extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);
		
		 View IdButton = findViewById(R.id.phoneId_button);
		 IdButton.setOnClickListener(this);
		 
		 View exitButton = findViewById(R.id.team_exit_button);
		 exitButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 case R.id.phoneId_button:     
				openPhoneIdDialog();
			 break;
		 case R.id.team_exit_button:
			 finish();
			 break;
		 }
		
	}
	
	private void openPhoneIdDialog(){
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		final String ID = tm.getDeviceId();
		
		AlertDialog.Builder IdDialogBuilder = new AlertDialog.Builder(this);
		IdDialogBuilder.create();
		IdDialogBuilder.setMessage("Your Phone ID is " + ID);
		IdDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		}).setNegativeButton("Copy ID", new DialogInterface.OnClickListener(){
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ClipboardManager clipboard = (ClipboardManager)
				        getSystemService(Context.CLIPBOARD_SERVICE);
				clipboard.setText(ID);
			}
			
		});
		IdDialogBuilder.show();
	}

}
