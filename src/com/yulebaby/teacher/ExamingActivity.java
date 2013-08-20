package com.yulebaby.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.adapter.AnswerAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.ExamAnswer;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.model.ExamReturnModel;
import com.yulebaby.teacher.model.QuestionModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.net.analysis.adapter.AMemberAdapter;
import com.yulebaby.teacher.net.analysis.adapter.ExamPaperAdapter;
import com.yulebaby.teacher.server.ExamService;
import com.yulebaby.teacher.utils.AsyncTask;

public class ExamingActivity extends Activity implements OnClickListener {
	protected static final String TAG = "ExamingActivity";
	protected static final int UPDATE_TEXTVIEW = 890;
	protected static final int TIME_OVER = 891;
	protected static final int NextQuestion = 892;
	protected static final int PreQuestion = 893;
	private static final int SUBMIT_EXAM = 894;
	private static final int REVIEW_EXAM = 895;
	protected static boolean isRunning = false;
	// Button bt;

	private ExamPaperModel examPaperModel;

	TextView tvQindex, tvQnum;
	TextView tvQuestion;
	TextView tvExamTime;
	ProgressBar pbExaming;
	ListView lvAnswer;
	Button btPre, btNext;
	Button btPreLast, btSubmit;
	Button btExit, btReview;
	Button btExitOverTime, btReviewOverTime;
	Button btExitScroeView, btReviewScoreView;
	RelativeLayout rlTimeOver;
	RelativeLayout rlScoreView;
	View rlExaming;
	TextView tvScore;
	TextView tvState;
	TextView tvExamingTitle;

	// boolean isReview;
	boolean isReview;

	AnswerAdapter answerAdapter;

	ArrayList<QuestionModel> qList;
	ArrayList<ExamAnswer> aList;
	private static Map<String, String> solutionMap = new HashMap<String, String>();

	private static Map<String, String> answerMap = new HashMap<String, String>();
	private int questionIndex = 0;
	private String answerIndex = "";

	String examTitle;

	// boolean isReView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_examing);

		examTitle = getIntent().getExtras().getString("exam_title");
		examPaperModel = (ExamPaperModel) getIntent().getSerializableExtra(
				"exampaper");

		isReview = getIntent().getExtras().getBoolean("review");

		// System.out.println("getIntent get exampaper title:" + examTitle);
		// System.out.println("getIntent get exampapermodel title:"
		// + examPaperModel.getQuestionNum());
		System.out.println("isReview:" + isReview);

		initData();
		initView();

		if (isReview) {
			mHandler.sendEmptyMessage(REVIEW_EXAM);
		}
	}

	private void initData() {
		qList = new ArrayList<QuestionModel>();
		aList = new ArrayList<ExamAnswer>();

		qList = examPaperModel.getQuestList();
		aList = qList.get(0).getAnswers();

		// String strQ = "Android中ProgressBar加载完成后怎么隐藏?";
		for (int i = 0; i < qList.size(); i++) {
			// ArrayList<String> list = new ArrayList<String>();
			// list.add(i + " java.util.ArrayList");
			// list.add(i + " java.util.HashMap");
			// list.add(i + " java.util.List");
			// list.add(i + " java.util.Map");
			// System.out.println("question id:" + qList.get(i).getId());
			// System.out.println("answer id:" + qList.get(i).getAnswerId());
			solutionMap.put(qList.get(i).getId(), qList.get(i).getAnswerId());
			// /qList.add(new QuestionModel(i + "." + strQ, list));
			if (isReview) {
				// System.out.println("add selectid:"+qList.get(i).getSelectedId());
				answerMap.put(qList.get(i).getId(), qList.get(i)
						.getSelectedId());
			}
		}

		// /aList = qList.get(0).answers;

		answerAdapter = new AnswerAdapter(this, aList);
		count = examTime;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 计时器暂停
		stopTimer();
	}

	private void initView() {

		tvExamingTitle = (TextView) findViewById(R.id.bt_examing_title);
		tvExamingTitle.setText(examTitle);
		tvQindex = (TextView) findViewById(R.id.tv_examing_index);
		String strQNum = getResources().getString(R.string.exam_qnum);
		strQNum = String.format(strQNum, questionIndex + 1);
		// tvQindex.setText("第" + questionIndex + "题");
		tvQindex.setText(strQNum);
		tvQnum = (TextView) findViewById(R.id.tv_examing_qnum);
		tvQnum.setText("共" + (qList.size()) + "题");
		tvQuestion = (TextView) findViewById(R.id.tv_examing_question);
		tvQuestion.setText(qList.get(questionIndex).getStrQuestion());
		pbExaming = (ProgressBar) findViewById(R.id.pb_examing_pb);
		pbExaming.setMax(qList.size());
		pbExaming.setProgress(questionIndex);

		lvAnswer = (ListView) findViewById(R.id.lv_examing_answer);
		btPre = (Button) findViewById(R.id.bt_examing_pre);
		btPre.setOnClickListener(this);
		btNext = (Button) findViewById(R.id.bt_examing_next);
		btNext.setOnClickListener(this);
		btPreLast = (Button) findViewById(R.id.bt_examing_prelast);
		btSubmit = (Button) findViewById(R.id.bt_examing_submit);
		btPreLast.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		btExit = (Button) findViewById(R.id.exit_examing);
		btExit.setOnClickListener(this);
		// btReview = (Button) findViewById(R.id.bt_examing_review);
		// btReview.setOnClickListener(this);

		btExitOverTime = (Button) findViewById(R.id.exit_overtime);
		btExitOverTime.setOnClickListener(this);
		btExitScroeView = (Button) findViewById(R.id.exit_scoreview);
		btExitScroeView.setOnClickListener(this);

		btReviewOverTime = (Button) findViewById(R.id.review_overtime);
		btReviewScoreView = (Button) findViewById(R.id.review_scoreview);
		btReviewOverTime.setOnClickListener(this);
		btReviewScoreView.setOnClickListener(this);

		tvExamTime = (TextView) findViewById(R.id.tv_examing_time);
		if (isReview) {
			tvExamTime.setVisibility(View.GONE);
		}
		rlTimeOver = (RelativeLayout) findViewById(R.id.rl_overtime);
		rlScoreView = (RelativeLayout) findViewById(R.id.rl_scoreview);
		rlExaming = findViewById(R.id.rl_examing);
		tvScore = (TextView) findViewById(R.id.tv_scoreview_score);
		tvState = (TextView) findViewById(R.id.tv_scoreview_state);

		lvAnswer.setAdapter(answerAdapter);
	
		lvAnswer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				answerAdapter.init();
				answerIndex = aList.get(position).getId();
				// map.put(arg2, 100);

				AnswerAdapter.ViewHolder vHollder = (AnswerAdapter.ViewHolder) view
						.getTag();
				// 在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
				vHollder.cb.toggle();
				AnswerAdapter.isSelected.put(answerIndex,
						vHollder.cb.isChecked());
				answerAdapter.notifyDataSetChanged();

			}
		});
		if(isReview){lvAnswer.setEnabled(false);}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!isReview) {
			startTimer();
		}
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

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				// bt.setText(count + "");
				UPDATE_EXAM_TIME();
				break;
			case TIME_OVER:
			case SUBMIT_EXAM:
				// Toast.makeText(ExamingActivity.this,
				// "Timer Over",Toast.LENGTH_SHORT).show();

				// setTimeOverView();
				// mHandler.sendEmptyMessage(SUBMIT_EXAM);
				checkSolution();
				submitExamPaper();
				setScoreView();

				break;

			case NextQuestion:
				if (questionIndex + 1 == qList.size() && !isReview) {
					setLastQuestView();
				}
				if (!TextUtils.isEmpty(answerIndex)) {

					answerMap
							.put(qList.get(questionIndex).getId(), answerIndex);
				} else if (!isReview) {
					Toast.makeText(ExamingActivity.this, "您还没有答题!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				answerIndex = "";
				questionIndex = questionIndex + 1;
				if (questionIndex >= qList.size()) {
					Toast.makeText(ExamingActivity.this, "没有下一题了",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (isReview) {
					setReviewView();
				} else {
					nextQuestion();
				}
				break;
			case PreQuestion:
				if (!TextUtils.isEmpty(answerIndex)) {
					answerMap
							.put(qList.get(questionIndex).getId(), answerIndex);
				}
				answerIndex = "";
				questionIndex = questionIndex - 1;
				if (questionIndex < 0) {
					Toast.makeText(ExamingActivity.this, "没有上一题了",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (isReview) {
					setReviewView();
				} else {

					preQuestion();
				}

				break;

			case REVIEW_EXAM:
				isReview = true;
				questionIndex = 0;
				lastPreView();
				btExit.setVisibility(View.VISIBLE);
				btExit.setText("退出");
				tvExamingTitle.setText(tvExamingTitle.getText() + "回顾");

				setReviewView();
				break;
			default:
				break;
			}
		}
	};
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;

	private static int count = 0;
	private boolean isPause = false;
	private boolean isStop = true;

	private static int delay = 1000; // 1s
	private static int period = 1000; // 1s
	private static int examTime = 15 * 60; // 单位是每秒 15分钟

	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		isRunning = true;
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					if (isRunning) {
						if (count <= 0) {
							mTimerTask.cancel();
							isRunning = false;
							mHandler.sendEmptyMessage(TIME_OVER);
						}

						// Log.i(TAG, "count: " + String.valueOf(count));
						mHandler.sendEmptyMessage(UPDATE_TEXTVIEW);
						do {
							try {
								// Log.i(TAG, "sleep(1000)...");
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}
						} while (isPause);

						count--;
					}
				}
			};
		}

		if (mTimer != null && mTimerTask != null)
			mTimer.schedule(mTimerTask, 0, period);

	}

	/**
	 * 提交考卷答案到服务器
	 * */
	protected void submitExamPaper() {
		// Toast.makeText(this, sbAnswer.toString(), Toast.LENGTH_LONG).show();
		new SubmitExamPaper(this).execute();
	}

	protected void setReviewView() {
		// btExit.
		// adapter.
		// /solutionMap;

		System.out.println("setReviewView");
		rlExaming.setVisibility(View.VISIBLE);
		rlTimeOver.setVisibility(View.GONE);
		rlScoreView.setVisibility(View.GONE);
		pbExaming.setProgress(questionIndex);
		String strQNum = getResources().getString(R.string.exam_qnum);
		strQNum = String.format(strQNum, questionIndex + 1);
		// tvQindex.setText("第" + questionIndex + "题");
		tvQindex.setText(strQNum);
		tvQuestion.setText(qList.get(questionIndex).getStrQuestion());
		aList = qList.get(questionIndex).getAnswers();

		answerAdapter = new AnswerAdapter(this, aList);
		String qId = qList.get(questionIndex).getId();
		System.out.println("question id: " + qId);
		if (solutionMap.containsKey(qId)) {
			System.out.println("Set solution!");
			if (answerMap.containsKey(qId)) {

				answerAdapter.setSolution(solutionMap.get(qId),
						answerMap.get(qId));
			} else {
				answerAdapter.setSolution(solutionMap.get(qId), "-1");
			}
		}
		lvAnswer.setAdapter(answerAdapter);

	}

	protected void setScoreView() {
		rlScoreView.setVisibility(View.VISIBLE);
		rlExaming.setVisibility(View.GONE);
		rlTimeOver.setVisibility(View.GONE);

		btExit.setVisibility(View.GONE);

		tvExamTime.setVisibility(View.GONE);
		// tvScore.setText(correctNum * 2 + "");
		if (correctNum > (qList.size() / 2)) {
			tvState.setText("考试通过");
		} else {
			tvState.setText("考试未通过");
		}

	}

	private void setTimeOverView() {
		rlTimeOver.setVisibility(View.VISIBLE);
		rlScoreView.setVisibility(View.GONE);
		rlExaming.setVisibility(View.GONE);
	}

	protected void setLastQuestView() {
		btPre.setVisibility(View.INVISIBLE);
		btNext.setVisibility(View.INVISIBLE);
		btPreLast.setVisibility(View.VISIBLE);
		btSubmit.setVisibility(View.VISIBLE);
	}

	protected void preQuestion() {
		pbExaming.setProgress(questionIndex);
		tvQindex.setText("第" + (questionIndex + 1) + "题");
		tvQuestion.setText(qList.get(questionIndex).getStrQuestion());
		aList = qList.get(questionIndex).getAnswers();

		answerIndex = answerMap.get(qList.get(questionIndex).getId());

		// adapter = new AnswerAdapter(this, aList);
		// lvAnswer.setAdapter(adapter);
		answerAdapter.setList(aList);
		if (!TextUtils.isEmpty(answerIndex)) {
			// qList.get(questionIndex).getAnswers().get(index)
			answerAdapter.isSelected.put(answerIndex, true);

		}
		answerAdapter.notifyDataSetChanged();
		lvAnswer.invalidate();
	}

	protected void nextQuestion() {

		pbExaming.setProgress(questionIndex);
		tvQindex.setText("第" + (questionIndex + 1) + "题");
		tvQuestion.setText(qList.get(questionIndex).getStrQuestion());
		aList = qList.get(questionIndex).getAnswers();

		answerIndex = answerMap.get(qList.get(questionIndex).getId());

		// adapter = new AnswerAdapter(this, aList);
		// lvAnswer.setAdapter(adapter);
		answerAdapter.setList(aList);
		if (!TextUtils.isEmpty(answerIndex)) {
			// qList.get(questionIndex).getAnswers().get(index)
			answerAdapter.isSelected.put(answerIndex, true);

		}
		answerAdapter.notifyDataSetChanged();
		lvAnswer.invalidate();

		// answerMap
	}

	protected void UPDATE_EXAM_TIME() {
		String strTime = convertSecondTime(count);
		if (count < 30) {
			tvExamTime.setBackgroundColor(Color.RED);
		}
		tvExamTime.setText(strTime);
	}

	private String convertSecondTime(int time) {
		StringBuilder strTemp = new StringBuilder();
		int mills = time % 60;
		int second = time / 60;
		// if (second > 60) {
		strTemp.append(convertHourTime(second));
		strTemp.append(":");
		// }
		if (0 <= mills && mills < 10) {
			strTemp.append("0");
		}
		strTemp.append(mills);
		return strTemp.toString();
	}

	private String convertHourTime(int second) {
		StringBuilder strTemp = new StringBuilder();
		int tempSecond = second % 60;
		int hour = second / 60;
		if (hour >= 60) {
			strTemp.append("59");
		} else {
			if (0 <= hour && hour < 10) {
				strTemp.append("0");
			}
			strTemp.append(hour);
		}
		strTemp.append(":");
		if (0 <= tempSecond && tempSecond < 10) {
			strTemp.append("0");
			strTemp.append(tempSecond);
		} else {
			strTemp.append(tempSecond);
		}
		return strTemp.toString();
	}

	private void stopTimer() {

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		isRunning = false;
		// count = 0;

	}

	int score = 0;
	int correctNum = 0;
	StringBuffer sbAnswer;

	private void checkSolution() {
		// 　Map map = new HashMap();
		correctNum = 0;
		sbAnswer = new StringBuffer();
		System.out.println("检查答案");
		Iterator aIter = answerMap.entrySet().iterator();
		Iterator qIter = solutionMap.entrySet().iterator();
		/*
		 * while (aIter.hasNext() && qIter.hasNext()) { Map.Entry aEntry =
		 * (Map.Entry) aIter.next(); Map.Entry sEntry = (Map.Entry)
		 * qIter.next(); // Object key = aEntry.getKey(); // /Object val =
		 * aEntry.getValue(); if (aEntry.getKey().equals(sEntry.getKey()) ||
		 * aEntry.getKey() == sEntry.getKey()) { if
		 * (aEntry.getValue().equals(sEntry.getValue()) || aEntry.getValue() ==
		 * sEntry.getValue()) { // 回答正确 // score += 5;
		 * System.out.println("回答正确"); correctNum += 1; } } }
		 */

		while (aIter.hasNext()) {
			Map.Entry aEntry = (Map.Entry) aIter.next();

			Object key = aEntry.getKey();

			sbAnswer.append(key.toString());
			sbAnswer.append("-");
			sbAnswer.append(aEntry.getValue());
			sbAnswer.append(",");

			if (solutionMap.containsKey(key)) {
				System.out.println("solutionMap contains key:" + key);
				if (solutionMap.get(key).equals(aEntry.getValue())
						|| solutionMap.get(key) == aEntry.getValue()) {
					// 回答正确
					// score += 5;
					// System.out.println("回答正确");
					correctNum += 1;

				}

			}
		}
	}

	private void lastPreView() {
		btPreLast.setVisibility(View.GONE);
		btSubmit.setVisibility(View.GONE);
		btPre.setVisibility(View.VISIBLE);
		btNext.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.bt_examing_pre:
			// questionIndex = questionIndex - 1;
			mHandler.sendEmptyMessage(PreQuestion);
			break;
		case R.id.bt_examing_next:

			mHandler.sendEmptyMessage(NextQuestion);

			break;
		case R.id.bt_examing_prelast:

			lastPreView();
			mHandler.sendEmptyMessage(PreQuestion);
			break;

		case R.id.bt_examing_submit:
			stopTimer();
			mHandler.sendEmptyMessage(SUBMIT_EXAM);
			break;
		case R.id.exit_overtime:
		case R.id.exit_scoreview:
		case R.id.exit_examing:
			finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		case R.id.review_overtime:
		case R.id.review_scoreview:
			mHandler.sendEmptyMessage(REVIEW_EXAM);
			break;
		}
	}

	private class SubmitExamPaper extends
			AsyncTask<Object, Void, ModelData<ExamReturnModel>> {
		private Context context;
		private ArrayList<ExamPaperModel> list;

		// private BaseAdapter adapter;

		public SubmitExamPaper(Context context) {
			this.context = context;
		}

		@Override
		protected ModelData<ExamReturnModel> doInBackground(Object... params) {
			String strAnswers = sbAnswer.substring(0, sbAnswer.length() - 1);
			ModelData<ExamReturnModel> model = ExamService.submitExam(context,
					examPaperModel.getId(), strAnswers);
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
		protected void onPostExecute(ModelData<ExamReturnModel> result) {
			// if (mWeakActivity.get() == null
			// || mWeakActivity.get().isFinishing()) {
			// return;
			// }
			// if (mWeakFragment.get() == null)
			// return;
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
				// examPaper = result.getModels().get(0);
				// tvQuestionNum.setText(examPaper.getQuestionNum() + "");
				// System.out.println("return result exam questionnum:"+
				// result.getModels().get(0).getQuestionNum());
				tvScore.setText(result.getModels().get(0).getScore() + "");
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

}
