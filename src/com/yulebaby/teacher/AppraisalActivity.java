package com.yulebaby.teacher;

import java.util.Timer;
import java.util.TimerTask;

import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.db.YuleDBService;
import com.yulebaby.teacher.fragment.AppraTempFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AppraisalActivity extends FragmentActivity implements
		View.OnClickListener {
	protected static final String TAG = "AppraisalActivity";
	protected static final int UPDATE_TEXTVIEW = 890;
	protected static final int TIME_OVER = 891;
	protected static final int GET_MSG_DATE = 892;
	protected static boolean isRunning = false;
	Button bt;
	boolean isQuote;
	private View contentView;

	FragmentManager manager;
	FragmentTransaction fragmentTransaction;

	private Button mLeftView, mRightView;
	private TextView tvTitle;
	


	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				// bt.setText(count + "");
				break;
			case TIME_OVER:
				Toast.makeText(AppraisalActivity.this, "Timer Over",
						Toast.LENGTH_SHORT).show();
				break;
			case GET_MSG_DATE:
				//getDateTask();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_appraisal);
		

		isQuote = getIntent().getExtras().getBoolean("quote");

		mLeftView = (Button) findViewById(R.id.title_left);

		mRightView = (Button) findViewById(R.id.title_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);

		tvTitle.setText(R.string.title_bt_appramodel);
		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		if (isQuote) {

			mRightView.setVisibility(View.INVISIBLE);
		} else {

			mRightView.setVisibility(View.VISIBLE);
			mRightView.setText(R.string.title_bt_addtemplate);
			mRightView.setOnClickListener(this);

		}

		contentView = findViewById(R.id.appraisal_content);

		manager = getSupportFragmentManager();
		fragmentTransaction = manager.beginTransaction();

		Fragment tempFragment = new AppraTempFragment(isQuote);
		fragmentTransaction.replace(contentView.getId(), tempFragment);
		fragmentTransaction.commit();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// 计时器暂停
	}

//	private void getDateList() {
//
//	}

	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			if (isQuote) {
				Intent data = new Intent(AppraisalActivity.this,
						DoAppraisalActivity.class);
				data.putExtra("sms_content", "");
				// 请求代码可以自己设置，这里设置成20
				AppraisalActivity.this.setResult(456, data);
				// System.out.println("return 456:"+list.get(position));
			}
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			// mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			Intent intent = new Intent(this, AddTemplateActivity.class);
			startActivity(intent);
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		}
	}

}
