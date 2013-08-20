package com.yulebaby.teacher;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.ExamService;
import com.yulebaby.teacher.utils.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExamInfoActivity extends Activity implements View.OnClickListener {
	protected static final String TAG = "ExamInfoActivity";
	protected static final int UPDATE_TEXTVIEW = 890;
	protected static final int TIME_OVER = 891;
	protected static boolean isRunning = false;
	Button bt;

	private View mLeftView, mRightView;
	private TextView tvTitle;
	String examTitle;
	String exampaperId;
	boolean isReview;

	private TextView tvQuestionNum;
	private ExamPaperModel examPaper;
	
	LoadExamPaper mLoadTask;

	private class LoadExamPaper extends
			AsyncTask<Object, Void, ModelData<ExamPaperModel>> {
		private Context context;
		private ArrayList<ExamPaperModel> list;

		// private BaseAdapter adapter;

		public LoadExamPaper(Context context) {
			this.context = context;
		}

		@Override
		protected ModelData<ExamPaperModel> doInBackground(Object... params) {
			ModelData<ExamPaperModel> model = ExamService.viewExam(context,
					exampaperId);
			return model;
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
		protected void onPostExecute(ModelData<ExamPaperModel> result) {
			
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
				examPaper = result.getModels().get(0);
				tvQuestionNum.setText(examPaper.getQuestionNum() + "");
				if(isReview){
					goToExamingActivity();
				}
				System.out.println("return result exam questionnum:"
						+ result.getModels().get(0).getQuestionNum());
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
		setContentView(R.layout.activity_examinfo);

		
		mLeftView = findViewById(R.id.title_left);
		mRightView = findViewById(R.id.title_right);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		tvTitle = (TextView) findViewById(R.id.tv_title);

		examTitle = getIntent().getExtras().getString("exam_title");
		exampaperId = getIntent().getExtras().getString("exampaper_id");
		isReview = getIntent().getExtras().getBoolean("review");
		tvTitle.setText(examTitle);

		tvQuestionNum = (TextView) findViewById(R.id.tv_examinfo_num);

		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);
		mRightView.setVisibility(View.INVISIBLE);
		bt = (Button) findViewById(R.id.bt_examinfo_goexam);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == examPaper) {
					return;
				}
				goToExamingActivity();

				// if (isRunning) {
				// stopTimer();
				//
				// } else {
				// startTimer();
				// }

			}
		});
		// startTimer();
		
		mLoadTask = new LoadExamPaper(this);
		mLoadTask.execute();

	}
	
	
	
	private void goToExamingActivity(){
		Intent intent = new Intent();
		intent.setClass(ExamInfoActivity.this, ExamingActivity.class);
		intent.putExtra("exampaper", examPaper);
		intent.putExtra("exam_title", examTitle);
		intent.putExtra("review", isReview);
		startActivity(intent);
		finish();
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

	protected void onDestroy() {
		super.onDestroy();
		mLoadTask.cancel(true);
	};
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				// bt.setText(count + "");
				break;
			case TIME_OVER:
				//Toast.makeText(ExamInfoActivity.this, "Timer Over",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	// private Timer mTimer = null;
	// private TimerTask mTimerTask = null;
	//
	// private static int count = 0;
	// private boolean isPause = false;
	// private boolean isStop = true;
	//
	// private static int delay = 1000; // 1s
	// private static int period = 1000; // 1s
	//
	// private void startTimer() {
	// if (mTimer == null) {
	// mTimer = new Timer();
	// }
	//
	// isRunning = true;
	// if (mTimerTask == null) {
	// mTimerTask = new TimerTask() {
	// @Override
	// public void run() {
	// if (isRunning) {
	// if (count >= 60) {
	// mTimerTask.cancel();
	// isRunning = false;
	// mHandler.sendEmptyMessage(TIME_OVER);
	// }
	//
	// Log.i(TAG, "count: " + String.valueOf(count));
	// mHandler.sendEmptyMessage(UPDATE_TEXTVIEW);
	// do {
	// try {
	// Log.i(TAG, "sleep(1000)...");
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// }
	// } while (isPause);
	//
	// count++;
	// }
	// }
	// };
	// }
	//
	// if (mTimer != null && mTimerTask != null)
	// mTimer.schedule(mTimerTask, 0, period);
	//
	// }
	//
	// private void stopTimer() {
	//
	// if (mTimer != null) {
	// mTimer.cancel();
	// mTimer = null;
	// }
	//
	// if (mTimerTask != null) {
	// mTimerTask.cancel();
	// mTimerTask = null;
	// }
	// isRunning = false;
	// // count = 0;
	//
	// }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			// mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		}
	}

}
