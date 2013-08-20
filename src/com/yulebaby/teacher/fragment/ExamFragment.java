package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.yulebaby.teacher.ExamInfoActivity;
import com.yulebaby.teacher.ExamingActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.SalaryDetailActivity;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.adapter.ExamAdapter;
import com.yulebaby.teacher.adapter.SalaryGridAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.fragment.OrderFragment.OrderCutsomAdapter;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.model.SalaryModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.ExamService;
import com.yulebaby.teacher.server.OrderCustomService;
import com.yulebaby.teacher.utils.AsyncTask;

public class ExamFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 209;
	private static final String TAG = "ExamFragment";
	public static final int DOING_EXAM = 110;
	public static final int REVIEW_EXAM = 111;

	private View mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<ExamFragment> mWeakFragment;

	private ArrayList<ExamModel> list;
	private ListView lv;
	private LoadExamList mLoadTask;
	private ExamAdapter adapter;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), ExamInfoActivity.class);
			ExamModel model = (ExamModel) msg.obj;
			switch (msg.what) {
			case REVIEW_EXAM:
				intent.putExtra("exam_title", model.getType());
				intent.putExtra("exampaper_id", model.getId());
				intent.putExtra("review", true);
				startActivity(intent);
				break;
			case DOING_EXAM:
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), ExamInfoActivity.class);
				// ExamModel model= (ExamModel) msg.obj;
				intent.putExtra("exam_title", model.getType());
				intent.putExtra("exampaper_id", model.getId());
				intent.putExtra("review", false);
				startActivity(intent);
				break;

			default:
				break;
			}
		};
	};

	private class LoadExamList extends
			AsyncTask<Object, Void, ModelData<ExamModel>> {
		private Context context;
		private ArrayList<ExamModel> list;
		// private BaseAdapter adapter;
		private volatile boolean isFinish = false;

		public LoadExamList(Context context, ArrayList<ExamModel> swims) {
			this.context = context;
			this.list = swims;
		}

		@Override
		protected ModelData<ExamModel> doInBackground(Object... params) {
			ModelData<ExamModel> model = ExamService.examList(context,
					String.valueOf(params[0]), String.valueOf(params[1]));
			// ExamService.viewExam(context, "65");
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

		public synchronized boolean isFinish() {
			return isFinish;
		}

		@Override
		protected void onPostExecute(ModelData<ExamModel> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			if (mWeakFragment.get() == null)
				return;
			if (result == null) {
				isFinish = true;
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
				list.addAll(result.getModels());
				System.out.println("return result listlenght:" + list.size());
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				lv.invalidate();
				// Toast.makeText(context, "Response.OK", Toast.LENGTH_LONG)
				// .show();
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
			isFinish = true;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<YuLeActivity>(
					(YuLeActivity) activity);
		}
		mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mWeakFragment == null) {
			mWeakFragment = new WeakReference<ExamFragment>(this);
		}
		list = new ArrayList<ExamModel>();
		adapter = new ExamAdapter(getActivity(), list,mHandler);
		mLoadTask = new LoadExamList(getActivity(), list);
		// mLoadTask = new LoadMessage(getActivity(), mMessages, mAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// mAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_exam, container,
				false);
		mLeftView = view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.title_exam);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		lv = (ListView) view.findViewById(R.id.lv_exam);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!list.get(position).isExaming()) {
					// Toast.makeText(MainActivity.this, "go Examing ",
					// Toast.LENGTH_SHORT).show();
					Message msg = mHandler.obtainMessage();
					msg.obj = list.get(position);
					msg.what = DOING_EXAM;
					mHandler.sendMessage(msg);

				} else {
					/*
					 * Toast.makeText(getActivity(), "go to Review Exam ",
					 * Toast.LENGTH_SHORT).show();
					 */
					Message msg = mHandler.obtainMessage();
					msg.obj = list.get(position);
					msg.what = REVIEW_EXAM;
					mHandler.sendMessage(msg);

				}

			}
		});

		String[] param = { "", "" };
		mLoadTask.execute(param);

		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			mWeakActivity.get().toggle(SlidingMenu.LEFT);
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
