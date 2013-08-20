package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.LessonService;
import com.yulebaby.teacher.utils.AsyncTask;

public class TeachingFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 207;
	private static final String TAG = "MessageFragment";
	private static final int CLICK_CONTENT_INDEX = 567;
	// private ArrayList<Message> mMessages;
	private static final int CLICK_WATER = 568;
	private static final int CLICK_BILINGUAL = 569;
	private static final int CLICK__CHINESE = 570;
	public static final String TYPE__CHINESE = "1";
	public static final String TYPE__WATER = "3";
	public static final String TYPE__BILINGUAL = "2";

	private boolean isEnd = false;
	private View mEmptyView;
	private View mLeftView, mRightView, titleView;
	private TextView tvTitle;
	private TextView lessonTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<TeachingFragment> mWeakFragment;
	private Login loginUser;

	private TextView tvContent, tvLoading;
	// private TextView tvIndex;
	private View btChinese, btBilingual, btWater;
	PopupWindow pop1;

	SharedPreferences sp;

	String type = "1";
	private LoadGuide mLoadTask;

	private ArrayList<Lesson> mGuides;
	private ArrayList<String> sectionList;

	// private ArrayList<Lesson> mCGuides;
	// private ArrayList<String> cSectionList;
	// private ArrayList<Lesson> mDGuides;
	// private ArrayList<String> dSectionList;
	// private ArrayList<Lesson> mWGuides;
	// private ArrayList<String> wSectionList;

	private SectionAdapter mAdapter;
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (CLICK_CONTENT_INDEX == msg.what) {

			}
			if (CLICK__CHINESE == msg.what) {

				type = TYPE__CHINESE;

				// if (null == mCGuides) {
				mLoadTask = new LoadGuide(TeachingFragment.this, mGuides);
				mLoadTask.execute(type);
				// }
				setChinseSelect();
			}
			if (CLICK_BILINGUAL == msg.what) {
				type = TYPE__BILINGUAL;
				mLoadTask = new LoadGuide(TeachingFragment.this, mGuides);
				mLoadTask.execute(type);
				setBilingualSelect();
			}
			if (CLICK_WATER == msg.what) {
				type = TYPE__WATER;
				mLoadTask = new LoadGuide(TeachingFragment.this, mGuides);
				mLoadTask.execute(type);
				setWaterSelect();
			}
		};
	};

	int selection;

	public TeachingFragment(String type, int section) {
		if (null != type) {
			this.type = type;
		}
		this.selection = section;

		// System.out.println("show type :"+type);
		// System.out.println("show selection :"+selection);

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
			mWeakFragment = new WeakReference<TeachingFragment>(this);
		}
		mGuides = new ArrayList<Lesson>();
		sectionList = new ArrayList<String>();

		sp = this.getActivity().getSharedPreferences(Commons.TEACHER_PREFERE,
				Context.MODE_PRIVATE);
		if (selection != 0) {
			sp.edit().putInt("show_guide_type" + type, selection).commit();

		}

		// sectionList.add("第一章 泡沫之夏");
		// sectionList.add("第二章 泡沫之夏");
		// sectionList.add("第三章 泡沫之夏");
		// sectionList.add("第四章 泡沫之夏");
		// sectionList.add("第五章 泡沫之夏");
		// sectionList.add("第六章 泡沫之夏");
		// sectionList.add("第七章 泡沫之夏");
		// sectionList.add("第八章 泡沫之夏");
		// sectionList.add("第九章 泡沫之夏");
		// sectionList.add("第十章 泡沫之夏");
		// sectionList.add("第十一章 泡沫之夏");

		// mLoadTask = new LoadMessage(getActivity(), mMessages, mAdapter);
		loginUser = App.getInstance().getLogin();
		System.out.println("loginUser auth:"
				+ loginUser.getAuth().isBilingualUpdate());
	}

	public void fatchNext() {

	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	private class LoadGuide extends AsyncTask<String, Void, ModelData<Lesson>> {
		private Context context;
		private ArrayList<Lesson> guides;
		private String type;

		public LoadGuide(TeachingFragment fragment, ArrayList<Lesson> guides) {
			this.context = fragment.getActivity();
			this.guides = guides;
		}

		@Override
		protected ModelData<Lesson> doInBackground(String... params) {
			type = params[0];
			ModelData<Lesson> guides = LessonService.guide(context, loginUser,
					params[0]);

			// SimpleDateFormat sDateFormat = new
			// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			// String date = sDateFormat.format(new java.util.Date());

			// ModelData<Lesson> updateLesson
			// =LessonService.checkUpdate(context,params[0], date);

			return guides;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<Lesson> result) {
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
				// if (type.equals(TYPE__BILINGUAL))
				// ;
				guides.clear();
				guides.addAll(result.getModels());
				sectionList = new ArrayList<String>();
				for (Lesson guide : mGuides) {
					System.out.println("add sectionlist:" + guide.getTitle());
					sectionList.add(guide.getTitle());
				}
				// mAdapter
				mAdapter.notifyDataSetChanged();
				// tvIndex.setVisibility(View.VISIBLE);
				initSectionPop();

				int section = sp.getInt("show_guide_type" + type, 0);
				System.out.println("show guide type" + type + " selection "
						+ section);

				showGuide(type, section);
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

	int section = 0;

	public void showGuide(String type, int section) {
		if (null == mGuides || mGuides.size() < 1) {
			tvContent.setVisibility(View.GONE);
			lessonTitle.setVisibility(View.GONE);

			// tvIndex.setVisibility(View.GONE);
			return;
		}

		if (section < 0 || section > mGuides.size()) {
			return;
		}

		if (mAdapter != null) {
			mAdapter.setSelectIndex(section);
			mAdapter.notifyDataSetChanged();
		}

		sp.edit().putInt("show_guide_type" + type, section).commit();
		this.section = section;

		tvContent.setVisibility(View.VISIBLE);
		// tvIndex.setVisibility(View.VISIBLE);
		lessonTitle.setVisibility(View.VISIBLE);
		String index = (section + 1) + "/" + sectionList.size();
		// tvIndex.setText(index);
		tvContent.setText(mGuides.get(section).getContent());
		lessonTitle.setText(mGuides.get(section).getTitle());
		// mAdapter.notifyDataSetChanged();
		if ("1".equals(type)) {
			setChinseSelect();
		}
		if ("2".equals(type)) {
			setBilingualSelect();
		}
		if ("3".equals(type)) {
			setWaterSelect();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_teaching,
				container, false);
		mLeftView = view.findViewById(R.id.title_left);
		mRightView = view.findViewById(R.id.title_right);
		titleView = view.findViewById(R.id.ll_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.title_teaching);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		mRightView.setBackgroundResource(R.drawable.icon_section_titlebar);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		tvContent = (TextView) view.findViewById(R.id.tv_teaching_content);
		lessonTitle = (TextView) view.findViewById(R.id.tv_lesson_title);
		// tvIndex = (TextView) view.findViewById(R.id.tv_teaching_index);
		tvLoading = (TextView) view.findViewById(R.id.tv_teaching_loading);
		// tvIndex.setOnClickListener(this);

		btBilingual = view.findViewById(R.id.bt_teahcing_bilingual);
		btChinese = view.findViewById(R.id.bt_teahcing_chinese);
		btWater = view.findViewById(R.id.bt_teaching_water);
		btBilingual.setOnClickListener(this);
		btChinese.setOnClickListener(this);
		btWater.setOnClickListener(this);

		// setChinseSelect();

		mLoadTask = new LoadGuide(this, mGuides);
		mLoadTask.execute(type);
		initSectionPop();
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
		case R.id.tv_teaching_index:
			if (pop1.isShowing()) {
				pop1.dismiss();
			} else {
				// pop.showAsDropDown(bt);
				// pop1.showAtLocation(getActivity().findViewById(R.id.layout),Gravity.TOP,
				// 0, 0);

				pop1.showAsDropDown(titleView);
			}

			msg.what = CLICK_CONTENT_INDEX;
			mHandler.sendMessage(msg);
			break;
		case R.id.bt_teaching_water:
			msg.what = CLICK_WATER;
			mHandler.sendMessage(msg);
			break;
		case R.id.bt_teahcing_bilingual:
			msg.what = CLICK_BILINGUAL;
			mHandler.sendMessage(msg);
			break;
		case R.id.bt_teahcing_chinese:
			msg.what = CLICK__CHINESE;
			mHandler.sendMessage(msg);
			break;
		}
	}

	private void setChinseSelect() {
		// tvContent.setText("正在加载中");
		// tvIndex.setVisibility(View.GONE);
		// tvLoading
		// sectionList.clear();
		btChinese.setBackgroundResource(R.drawable.bg_tabbar_select);
		btBilingual.setBackgroundResource(R.drawable.bg_tabbar_normal);
		btWater.setBackgroundResource(R.drawable.bg_tabbar_normal);

	}

	private void setBilingualSelect() {
		// tvContent.setText("正在加载中");
		// tvIndex.setVisibility(View.GONE);
		btBilingual.setBackgroundResource(R.drawable.bg_tabbar_select);
		btChinese.setBackgroundResource(R.drawable.bg_tabbar_normal);
		btWater.setBackgroundResource(R.drawable.bg_tabbar_normal);

	}

	private void setWaterSelect() {
		// tvContent.setText("正在加载中");
		// tvIndex.setVisibility(View.GONE);
		btWater.setBackgroundResource(R.drawable.bg_tabbar_select);
		btBilingual.setBackgroundResource(R.drawable.bg_tabbar_normal);
		btChinese.setBackgroundResource(R.drawable.bg_tabbar_normal);

	}

	public void initSectionPop() {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.teaching_section, null);
//		pop1 = new PopupWindow(v, getActivity().getWindowManager()
//				.getDefaultDisplay().getWidth(), getActivity()
//				.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5);
		pop1 = new PopupWindow(v,  LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		pop1.setBackgroundDrawable(new BitmapDrawable());
		pop1.setFocusable(true);
		pop1.setTouchable(true);
		pop1.setOutsideTouchable(true);
		// LinearLayout main = (LinearLayout) v.findViewById(R.id.main);
		ListView lvSection = (ListView) v
				.findViewById(R.id.lv_teaching_section);
		mAdapter = new SectionAdapter();
		lvSection.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		lvSection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(getActivity(),
				// sectionList.get(position) + " is onclick!",
				// Toast.LENGTH_SHORT).show();
				showGuide(type, position);

				pop1.dismiss();

			}
		});
		// Button btPre = (Button) v.findViewById(R.id.bt_section_pre);
		// Button btNext = (Button) v.findViewById(R.id.bt_section_next);

		// main.addView(c);

	}

	class SectionAdapter extends BaseAdapter {
		private int size;

		public SectionAdapter() {
			size = sectionList.size();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return sectionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// System.out.println("sectionList lenght:"+sectionList.size());
			ViewHolder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.sectionlist_item, null);
				holder = new ViewHolder();
				holder.titleText = (TextView) convertView
						.findViewById(R.id.tv_sectionlist);
				holder.ivStar = (ImageView) convertView
						.findViewById(R.id.iv_star);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.titleText.setText(sectionList.get(position));
			if (position == selectIndex) {
				holder.ivStar
						.setBackgroundResource(R.drawable.icon_star_select);
			} else {
				holder.ivStar
						.setBackgroundResource(R.drawable.icon_star_normal);
			}
			return convertView;
		}

		int selectIndex = 0;

		public void setSelectIndex(int index) {
			this.selectIndex = index;
		}

		class ViewHolder {
			TextView titleText;
			ImageView ivStar;
			// TextView timeText;
		}

	}
	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if (getSlidingMenu().isBehindShowing()) {
	// getSlidingMenu().showAbove();
	// return true;
	// } else {
	// this.finish();
	// }
	// }
	// return super.onKeyUp(keyCode, event);
	// }
}
