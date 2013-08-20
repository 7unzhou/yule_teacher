package com.yulebaby.teacher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.db.YuleDBService;
import com.yulebaby.teacher.model.ContentViewModel;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.InteractionService;
import com.yulebaby.teacher.utils.AsyncTask;

public class DoAppraisalActivity extends FragmentActivity implements
		View.OnClickListener {

	private static final String TAG = "AppraTempFragment";

	private static final int REQUESTCODE_APPRATEMP = 456;

	private static final int REQUEST_SMS = 890;

	private static final int SEND_APPRAI = 990;

	public static final int SET_CONTENTVIEW = 980;

	private static final int ADDTOTEMPDB = 987;

	private Button mLeftView, mRightView;
	private TextView tvTitle;
	private TextView tvName;
	String name;
	String id;
	private WeakReference<Activity> mWeakActivity;

	EditText etSmsContent;
	EditText etHeight;
	EditText etWeight;

	Button btSend;

	TextView tvContentCount;
	View addTemplate;

	String countText;
	int maxCount = 140;// 限制的最大字数
	boolean isAppraisal;

	private ContentViewModel content;
	private LoadContent mLoadTask;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_SMS:
				// Toast.makeText(DoAppraisalActivity.this, "msg:" +
				// msg.obj,Toast.LENGTH_SHORT).show();
				// etSmsContent.setText("");
				String sms = (String) msg.obj;
				int leng = sms.length() > 140 ? 140 : sms.length();
				String count = String.format(countText, leng);
				tvContentCount.setText(count);
				etSmsContent.setText((String) sms.substring(0, leng));

				break;

			case SEND_APPRAI:
				DoAppraisalActivity.this.sms = etSmsContent.getText()
						.toString();
				weight = etWeight.getText().toString();
				height = etHeight.getText().toString();
				SendSubmitTask task = new SendSubmitTask(
						DoAppraisalActivity.this);
				task.execute();
				break;

			case SET_CONTENTVIEW:
				etHeight.setText(content.getHeight());
				etWeight.setText(content.getWeight());
				etSmsContent.setText(content.getSmsContent());
				break;

			case ADDTOTEMPDB:
				YuleDBService db = new YuleDBService(DoAppraisalActivity.this);
				db.insert(etSmsContent.getText().toString());
				Toast.makeText(DoAppraisalActivity.this, "Add to Template",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	private class LoadContent extends
			AsyncTask<Object, Void, ModelData<ContentViewModel>> {
		private Context context;

		public LoadContent(Context context) {
			this.context = context;
			// this.adapter = adapter;
		}

		@Override
		protected ModelData<ContentViewModel> doInBackground(Object... params) {
			//
			ModelData<ContentViewModel> swims = InteractionService.commentView(
					context, id);

			return swims;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mEmptyView.setVisibility(View.GONE);
		}

		@Override
		protected void onPostExecute(ModelData<ContentViewModel> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			if (result == null) {
				Log.d(TAG, "onPostExecute result == null");
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// mEmptyView.setVisibility(View.VISIBLE);
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				if (result.getModels().size() > 0) {
					// mEmptyView.setVisibility(View.GONE);
				}
				// mList.addAll(result.getModels());
				content = result.getModels().get(0);
				mHandler.sendEmptyMessage(SET_CONTENTVIEW);
				break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_doappraisal);

		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<Activity>(this);
		}

		name = getIntent().getExtras().getString("apprai_name");
		id = getIntent().getExtras().getString("appid");
		isAppraisal = getIntent().getExtras().getBoolean("isAppraisal");

		btSend = (Button) findViewById(R.id.bt_apprai_send);
		btSend.setOnClickListener(this);

		mLeftView = (Button) findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		mRightView = (Button) findViewById(R.id.title_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);

		tvTitle.setText(R.string.title_bt_sendappra);
		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		// mLeftView.setText(R.string.title_bt_cancel);
		mLeftView.setOnClickListener(this);
		mRightView.setVisibility(View.GONE);
		mRightView.setText(R.string.title_bt_quotetemp);
		mRightView.setOnClickListener(this);

		tvName = (TextView) findViewById(R.id.tv_apprai_name);

		String snameFormat = getResources().getString(R.string.appraisal_name);
		String toAppraiName = String.format(snameFormat, name);
		countText = getResources().getString(R.string.appraisal_contentcount);

		tvName.setText(toAppraiName);

		etSmsContent = (EditText) findViewById(R.id.et_apprai_content);
		etHeight = (EditText) findViewById(R.id.et_apprai_height);
		etWeight = (EditText) findViewById(R.id.et_apprai_weight);
		tvContentCount = (TextView) findViewById(R.id.tv_appraicontent_count);
		addTemplate = findViewById(R.id.tv_addtotemplate);
		addTemplate.setOnClickListener(this);

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
				if (number > maxCount) {
					return;
				}

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

		if (isAppraisal) {
			mRightView.setVisibility(View.GONE);
			btSend.setClickable(false);
			mLoadTask = new LoadContent(this);
			mLoadTask.execute();
		}
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
			Intent intent = new Intent(mWeakActivity.get(),
					AppraisalActivity.class);
			intent.putExtra("quote", true);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			mWeakActivity.get().startActivityForResult(intent,
					REQUESTCODE_APPRATEMP);
			break;
		case R.id.bt_apprai_send:

			if (!isAppraisal) {
				mHandler.sendEmptyMessage(SEND_APPRAI);
			}

			break;

		case R.id.tv_addtotemplate:
			// Toast.makeText(DoAppraisalActivity.this, "msg: tv_addtotemplate",
			// Toast.LENGTH_SHORT).show();

			if (TextUtils.isEmpty(etSmsContent.getText())) {
				return;
			}

			mHandler.sendEmptyMessage(ADDTOTEMPDB);

			break;

		}

	}

	private String sms;
	private String height, weight;

	class SendSubmitTask extends AsyncTask<Object, Void, ModelData> {
		private Context context;

		public SendSubmitTask(Context context) {
			this.context = context;
		}

		@Override
		protected ModelData doInBackground(Object... params) {
			ModelData cancel = InteractionService.submitComment(context, id,
					sms, height, weight);
			return cancel;
		}

		@Override
		protected void onPostExecute(ModelData result) {
			// if(mWeakActivity.get() == null ||
			// mWeakActivity.get().isFinishing()){
			// return;
			// }
			// if(mWeakFrgment.get() == null){
			// return;
			// }
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			if (result == null) {
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				Log.d(TAG, "onPostExecute result == null");
				return;
			}
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				DoAppraisalActivity.this.finish();

				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode != RESULT_OK) {
		// return;
		// }
		switch (requestCode) {
		case REQUESTCODE_APPRATEMP: {
			// System.out.println("REQUESTCODE_APPRATEMP return");
			Bundle b = data.getExtras(); // data为B中回传的Intent
			String str = b.getString("sms_content");// str即为回传的值
			Message msg = mHandler.obtainMessage(REQUEST_SMS);
			msg.obj = str;
			mHandler.sendMessage(msg);
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
