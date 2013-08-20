package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.SalaryDetailActivity;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.adapter.SalaryGridAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.AttendListModel;
import com.yulebaby.teacher.model.AttendModel;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.model.SalaryModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.net.Url.Attendance;
import com.yulebaby.teacher.server.AttendService;
import com.yulebaby.teacher.server.LessonService;
import com.yulebaby.teacher.utils.AsyncTask;

public class AttendFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 213;
	private static final String TAG = "AttendFragment";

	private Button mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<AttendFragment> mWeakFragment;

	String title;
	ArrayList<AttendModel> attendList;
	ListView lv;
	private AttendAdapter adapter;

	private TextView tvTotalRmb, tvTimes, tvMonth;

	private int totalRmb = 0;

	LoadAttendTask mLoadTask;

	// boolean isLoadMonth = true;
	ArrayList<MonthsModel> monthsList;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		};
	};

	private class LoadAttendTask extends
			AsyncTask<String, Void, ModelData<AttendListModel>> {
		private Context context;
		private ArrayList<AttendModel> attendList;
		private String type;

		public LoadAttendTask(Fragment fragment, ArrayList<AttendModel> guides) {
			this.context = fragment.getActivity();
			this.attendList = guides;
		}

		@Override
		protected ModelData<AttendListModel> doInBackground(String... params) {
			totalRmb = 0;
			type = params[0];
			ModelData<AttendListModel> guides = AttendService.list(context,
					params[0]);
			if (monthsList.size() <= 0) {
				ModelData<MonthsModel> monthModel = AttendService
						.getMonths(context);
				monthsList = monthModel.getModels();
				System.out.println("monthslist lenght:" + monthsList.size());
				// isLoadMonth = false;
			}
			return guides;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<AttendListModel> result) {
			if (mWeakActivity.get() == null
					&& mWeakActivity.get().isFinishing()) {
				return;
			}
			if (mWeakFragment.get() == null) {
				return;
			}
			if (result == null) {
				Log.d(TAG, "onPostExecute result == null");
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				attendList.clear();
				attendList.addAll(result.getModels().get(0).getList());
				title = result.getModels().get(0).getTitle();
				totalRmb = result.getModels().get(0).getTotalWages();
				String month = subTitleMonth(title);
				tvTitle.setText(month
						+ getResources().getString(R.string.menu_attend));
				tvMonth.setText(month);
				System.out.println("attendLIst lenght:" + attendList.size());

				adapter.list = attendList;
				adapter.notifyDataSetChanged();
				lv.invalidate();

				tvTimes.setText(attendList.size() + "");
				tvTotalRmb.setText(totalRmb + "");

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

	private String subTitleMonth(String title) {
		String month = "";
		if (title.contains("-")) {
			int index = title.indexOf("-");
			month = title.substring(index + 1);
			if (month.startsWith("0")) {
				month = month.replace("0", "");
			}
		}
		return month + "月";
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
			mWeakFragment = new WeakReference<AttendFragment>(this);
		}
		attendList = new ArrayList<AttendModel>();
		monthsList = new ArrayList<MonthsModel>();
		adapter = new AttendAdapter(getActivity(), attendList);
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
		final View view = inflater.inflate(R.layout.fragment_attend, container,
				false);
		mLeftView = (Button) view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = (Button) view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.menu_attend);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.VISIBLE);
		// mRightView.setText(R.string.attend_month_select);
		mRightView.setBackgroundResource(R.drawable.icon_calendar_titlebar);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		tvTimes = (TextView) view.findViewById(R.id.tv_absence_times);
		tvTotalRmb = (TextView) view.findViewById(R.id.tv_absence_rmb);
		tvMonth = (TextView) view.findViewById(R.id.tv_attendmonth);

		lv = (ListView) view.findViewById(R.id.lv_attend);
		lv.setAdapter(adapter);

		mLoadTask = new LoadAttendTask(this, attendList);
		mLoadTask.execute("");

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
		mLoadTask.cancel(true);
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
			selectMonthDialog();
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		}
	}

	String[] selectMonths;

	private void selectMonthDialog() {
		if (monthsList.size() < 0) {
			return;
		}
		if (null == selectMonths) {
			selectMonths = new String[monthsList.size()];
			for (int i = 0, len = monthsList.size(); i < len; i++) {
				selectMonths[i] = monthsList.get(i).getName();
			}
		}

		new AlertDialog.Builder(this.getActivity())
		// .setTitle(R.string.select_dialog)
				.setItems(selectMonths, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						System.out.println("month:"
								+ monthsList.get(which).getMonth());
						mLoadTask = new LoadAttendTask(AttendFragment.this,
								attendList);
						mLoadTask.execute(monthsList.get(which).getMonth());

					}
				}).create().show();
	}

	private String getYear() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	private class AttendAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		// private Integer[] mImageIds = {};

		public ArrayList<AttendModel> list;

		public AttendAdapter(Context c, ArrayList<AttendModel> list) {

			this.list = list;
			mContext = c;
			inflater = LayoutInflater.from(mContext);

		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// ImageView imageView;
			if (null == list || list.size() == 0)
				return null;
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.attend_item, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.attend_item_content);
				holder.tvReason = (TextView) convertView
						.findViewById(R.id.attend_item_reson);
				holder.tvRmb = (TextView) convertView
						.findViewById(R.id.attend_item_rmb);
				holder.tvWorkDate = (TextView) convertView
						.findViewById(R.id.attend_item_workdate);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AttendModel model = list.get(position);

			holder.tvContent.setText(model.getComment());
			holder.tvReason.setText(model.getReson());
			holder.tvRmb.setText(model.getWages());
			String workDate = model.getWorkDate();
			// System.out.println("work date :"+workDate);
			if (workDate.contains("-")) {
				workDate = workDate.replaceFirst("-", "\n");
			}
			holder.tvWorkDate.setText(workDate);

			return convertView;
		}

		class ViewHolder {
			TextView tvWorkDate;
			TextView tvReason;
			TextView tvRmb;
			TextView tvContent;
			// TextView timeText;
		}
	}

}
