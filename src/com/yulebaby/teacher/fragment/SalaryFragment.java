package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.security.spec.MGF1ParameterSpec;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.NetworkInfo.DetailedState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.TextElementListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.SalaryDetailActivity;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.adapter.SalaryGridAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.fragment.TeachingFragment.SectionAdapter;
import com.yulebaby.teacher.fragment.TeachingFragment.SectionAdapter.ViewHolder;
import com.yulebaby.teacher.model.AttendListModel;
import com.yulebaby.teacher.model.AttendModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.model.SalaryModel;
import com.yulebaby.teacher.model.SalaryMonthModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.AttendService;
import com.yulebaby.teacher.server.SalaryService;
import com.yulebaby.teacher.utils.AsyncTask;

public class SalaryFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 208;
	private static final String TAG = "SalaryFragment";
	// private static final int CLICK_CONTENT_INDEX = 567;
	// // private ArrayList<Message> mMessages;
	// private static final int CLICK_WATER = 568;
	// private static final int CLICK_BILINGUAL = 569;
	// private static final int CLICK__CHINESE = 570;
	public static final int REFRESH_SALARYVIEW = 680;

	// private boolean isEnd = false;
	private View mEmptyView;
	private Button mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<SalaryFragment> mWeakFragment;

	private TextView tvBasic, tvInsure, tvTotal;
	private TextView tvSalaryCommission, tvSalaryAdd, tvSalarySub,
			tvSalaryOther;
	private View comminssionView, addView, subView, otherView;

	private ArrayList<DaySalary> dayList;

	private SalaryMonthModel salaryModel;

	private LinearLayout mContentView;
	private LoadSalerTask mLoadTask;

	private SalaryGridAdapter mAdapter;

	GridView gridView;

	private Date month;
	private String strMonth;
	private String strDay;

	ArrayList<MonthsModel> monthsList;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_SALARYVIEW:
				refreshSalaryView();
				break;

			default:
				break;
			}

		};
	};

	private class LoadSalerTask extends
			AsyncTask<String, Void, ModelData<SalaryMonthModel>> {
		private Context context;
		private ArrayList<DaySalary> attendList;
		private String month;

		public LoadSalerTask(Fragment fragment, ArrayList<DaySalary> guides) {
			this.context = fragment.getActivity();
			this.attendList = guides;
		}

		@Override
		protected ModelData<SalaryMonthModel> doInBackground(String... params) {
			month = params[0];
			ModelData<SalaryMonthModel> guides = SalaryService.getMonthSalary(
					context, month);
			// System.out.println("guide:"+guides.getm);

			if (monthsList.size() <= 0) {
				ModelData<MonthsModel> monthModel = SalaryService
						.getMonths(context);
				monthsList = monthModel.getModels();
				// System.out.println("monthslist lenght:" + monthsList.size());
				// isLoadMonth = false;
			}
			return guides;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<SalaryMonthModel> result) {
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
				dayList.clear();
				if (null == result || result.getModels().size() <= 0) {

					System.out.println("result:getModels().size() < 0)");
					return;
				}
				salaryModel = result.getModels().get(0);
				getDayWeekIndex(salaryModel.getMonth());
				System.out.println("month:" + salaryModel.getMonth());
				String title = getResources().getString(R.string.title_salary);
				tvTitle.setText(salaryModel.getMonth()+title);
				dayList.addAll(salaryModel.getDayList());

				System.out.println("attendLIst lenght:" + dayList.size());

				mHandler.sendEmptyMessage(REFRESH_SALARYVIEW);

				// mAdapter
				// mAdapter.notifyDataSetChanged();
				// //tvIndex.setVisibility(View.VISIBLE);
				// initSectionPop();
				// showGuide(type, 0);
				// Toast.makeText(context, result.getMessage(),
				// Toast.LENGTH_SHORT).show();
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
			mWeakFragment = new WeakReference<SalaryFragment>(this);
		}
		dayList = new ArrayList<DaySalary>();
		monthsList = new ArrayList<MonthsModel>();
		mAdapter = new SalaryGridAdapter(getActivity(), dayList);
		mLoadTask = new LoadSalerTask(this, dayList);
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
		final View view = inflater.inflate(R.layout.fragment_salary, container,
				false);
		mLeftView = (Button) view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = (Button) view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.VISIBLE);
		mRightView.setBackgroundResource(R.drawable.icon_calendar_titlebar);
		// mRightView.setText(R.string.attend_month_select);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		tvBasic = (TextView) view.findViewById(R.id.tv_salary_basic);
		tvInsure = (TextView) view.findViewById(R.id.tv_salary_insured);
		tvTotal = (TextView) view.findViewById(R.id.tv_salary_total);

		tvSalaryAdd = (TextView) view.findViewById(R.id.tv_salary_add);
		tvSalaryCommission = (TextView) view.findViewById(R.id.tv_commission);
		tvSalaryOther = (TextView) view.findViewById(R.id.tv_salary_other);
		tvSalarySub = (TextView) view.findViewById(R.id.tv_salary_sub);

		comminssionView = view.findViewById(R.id.view_salary_commission);
		comminssionView.setOnClickListener(this);
		addView = view.findViewById(R.id.view_salary_add);
		addView.setOnClickListener(this);
		subView = view.findViewById(R.id.view_salary_sub);
		subView.setOnClickListener(this);
		otherView = view.findViewById(R.id.view_salary_other);
		otherView.setOnClickListener(this);

		// mContentView = (LinearLayout) view.findViewById(R.id.attend_content);

		// refreshSalaryView();

		// initData();
		gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				strDay = dayList.get(position).getDay();
				gotoSalaryDetail(Commons.DETAIL);

			}
		});

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
			break;

		case R.id.view_salary_add:

			gotoSalaryDetail(Commons.ADD);

			break;
		case R.id.view_salary_commission:
			gotoSalaryDetail(Commons.COMMISSION);

			break;
		case R.id.view_salary_other:
			gotoSalaryDetail(Commons.OTHER);

			break;
		case R.id.view_salary_sub:
			gotoSalaryDetail(Commons.SUB);

			break;
		}
	}

	private void gotoSalaryDetail(String type) {
		// Calendar calendar = new ();
		// String strMonth = this.month.getYear() + "-" + this.month.getMonth();
		Intent intent = new Intent(mWeakActivity.get(),
				SalaryDetailActivity.class);
		intent.putExtra("detail_tag", type);
		intent.putExtra("month", strMonth);
		intent.putExtra("day", strDay);
		mWeakActivity.get().startActivity(intent);
	}

	private Calendar calendar;

	// 如果是周日则返回一，如果是
	private void getDayWeekIndex(String month) {

		SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月");
		// Date date = null;

		try {
			this.month = sdf.parse(month);
			// getDayWeekIndex(date);

			calendar = Calendar.getInstance();
			calendar.setTime(this.month);
			// calendar.set(Calendar.DAY_OF_MONTH, 1);
			strMonth = calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1);
			int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
			// //获得当前日期是一个星期的第几天?
			// Toast.makeText(MainActivity.this, "DAY_OF_WEEK:" +
			// dayInWeek,Toast.LENGTH_LONG).show();
			addEmptyDay(dayInWeek);

			// this.month = this.month.get
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Log.d(TAG, "day in week:" + dayInWeek);
	}

	private void addEmptyDay(int num) {
		System.out.println(" num:" + num);
		if (1 == num) {
			return;
		}
		for (; num > 1; num--) {
			dayList.add(null);
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
						String titleText = getResources().getString(
								R.string.title_salary);
						tvTitle.setText(selectMonths[which] + titleText);
						mLoadTask = new LoadSalerTask(SalaryFragment.this,
								dayList);
						mLoadTask.execute(monthsList.get(which).getMonth());

					}
				}).create().show();
	}

	// private void initData() {
	//
	// for (int i = 0; i < 31; i++) {
	// SalaryModel salary = new SalaryModel();
	// // salary.date
	// list.add(salary);
	// }
	//
	// }

	private void refreshSalaryView() {
		// String month = salaryModel.getMonth();

//		String tempStr = getResources().getString(R.string.salary_basic);
//		tempStr = String.format(tempStr, salaryModel.getBasicWage());
//		tvBasic.setText(tempStr);
		tvBasic.setText(salaryModel.getBasicWage()+"");

//		tempStr = getResources().getString(R.string.salary_insured);
//		tempStr = String.format(tempStr, salaryModel.getInsured());
		tvInsure.setText(salaryModel.getInsured()+"");

//		tempStr = getResources().getString(R.string.salary_total);
//		tempStr = String.format(tempStr, salaryModel.getTotal());
		tvTotal.setText(salaryModel.getTotal()+"");

		mAdapter.setMaxSalary(salaryModel.getDayMax());
		mAdapter.notifyDataSetChanged();
		gridView.invalidate();

		tvSalaryAdd.setText(salaryModel.getAdd() + "");
		tvSalaryCommission.setText(salaryModel.getCommission() + "");
		tvSalaryOther.setText(salaryModel.getOther() + "");
		tvSalarySub.setText(salaryModel.getSub() + "");

	}

}
