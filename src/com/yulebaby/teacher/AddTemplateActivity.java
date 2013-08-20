package com.yulebaby.teacher;

import java.util.ArrayList;

import com.yulebaby.teacher.db.YuleDBService;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddTemplateActivity extends FragmentActivity implements
		View.OnClickListener {

	private static final String TAG = "AppraTempFragment";

	private static final int ADDTEMPTODB = 110;

	private Button mLeftView, mRightView;
	private TextView tvTitle;

	
	EditText etSmsContent;
	int maxCount = 140;// 限制的最大字数
	TextView tvContentCount;
	String countText;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADDTEMPTODB:
				YuleDBService db = new YuleDBService(AddTemplateActivity.this);
				db.insert(etSmsContent.getText().toString());
				Toast.makeText(AddTemplateActivity.this, "Add to Template", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.fragment_addtemplate);

		countText = getResources().getString(R.string.appraisal_contentcount);

		mLeftView = (Button) findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		mRightView = (Button) findViewById(R.id.title_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);

		tvTitle.setText(R.string.title_bt_appramodel);
		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		mLeftView.setText(R.string.title_bt_cancel);
		mLeftView.setOnClickListener(this);
		mRightView.setVisibility(View.VISIBLE);
		mRightView.setText(R.string.title_bt_confir);
		mRightView.setOnClickListener(this);

		etSmsContent = (EditText) findViewById(R.id.et_apprai_content);
		tvContentCount = (TextView) findViewById(R.id.tv_appraicontent_count);
		etSmsContent.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
				System.out.println("s=" + s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = s.length();
				String len = String.format(countText, number);
				tvContentCount.setText(len);
				selectionStart = etSmsContent.getSelectionStart();
				selectionEnd = etSmsContent.getSelectionEnd();
				// System.out.println("start="+selectionStart+",end="+selectionEnd);
				if (temp.length() > maxCount) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					etSmsContent.setText(s);
					etSmsContent.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			// smWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			// createNewFragment(new
			// TemplateAddFragment(R.layout.fragment_addtemplate));
			if (TextUtils.isEmpty(etSmsContent.getText())) {
				return;
			}
			
			mHandler.sendEmptyMessage(ADDTEMPTODB);
			
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			
			break;

		}

	}

}
