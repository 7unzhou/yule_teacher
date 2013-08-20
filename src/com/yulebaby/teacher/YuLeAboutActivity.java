package com.yulebaby.teacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;


public class YuLeAboutActivity extends FragmentActivity implements View.OnClickListener{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about_activity);
		View mLeftView = findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		View mRightView = findViewById(R.id.title_right);
		View mAboutWeb = findViewById(R.id.about_web);
		View mAboutPhone = findViewById(R.id.about_phone);
		TextView mAboutView = (TextView)findViewById(R.id.about);
		mAboutView.setText("@"+getString(R.string.about));
		mRightView.setVisibility(View.INVISIBLE);
		//mLeftView.setBackgroundResource(R.drawable.selector_button_titlebar_back);
		
		mLeftView.setOnClickListener(this);
		mAboutWeb.setOnClickListener(this);
		mAboutPhone.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.title_left:
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		case R.id.about_phone:
			showDialog();
			break;
		case R.id.about_web:
			Uri uri = Uri.parse(getString(R.string.about_url));  
		    startActivity(new Intent(Intent.ACTION_VIEW,uri));  
			break;
		}
	}
	
	public void phoneCall(){
		Intent intent = new Intent(Intent.ACTION_CALL,
				//Uri.parse("tel:" + App.getInstance().getLogin().getPhone()));
				Uri.parse("tel:10086" ));
        startActivity(intent);  
	}
	
	void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = PhoneDialogFragment.newInstance(getString(R.string.dialog_phone) + " " + "10086");
        newFragment.show(ft, "dialog");
    }
	
	public static class PhoneDialogFragment extends DialogFragment {

		String mTitle;

        static PhoneDialogFragment newInstance(String title) {
        	PhoneDialogFragment f = new PhoneDialogFragment();
        	 Bundle args = new Bundle();
             args.putString("title", title);
             f.setArguments(args);
            return f;
        }
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mTitle = getArguments().getString("title");
        }
        
        public Dialog onCreateDialog(Bundle savedInstanceState) {
//        	final View v = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog, null, false);
        	AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
//        	alert.setView(v,10,10,10,10);
        	alert.setCanceledOnTouchOutside(true);
        	alert.setTitle(mTitle);
        	alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((YuLeAboutActivity)getActivity()).phoneCall();
					PhoneDialogFragment.this.dismiss();
				}
			});
        	alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PhoneDialogFragment.this.dismiss();
				}
			});
//        	TextView tv = (TextView)v.findViewById(R.id.dialog_title);
//            tv.setText(mTitle);
//            Button confirm = (Button)v.findViewById(R.id.dialog_confirm);
//            confirm.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    // When button is clicked, call up to owning activity.
//                    ((MainActivity)getActivity()).phoneCall();
//                }
//            });
//            Button cancel = (Button)v.findViewById(R.id.dialog_cancel);
//            cancel.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                	PhoneDialogFragment.this.dismiss();
//                }
//            });
        	return alert;
        }
	}
}
