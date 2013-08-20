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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
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
import com.yulebaby.teacher.model.SalaryModel;
import com.yulebaby.teacher.model.SopLesson;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.server.GuideService;
import com.yulebaby.teacher.utils.AsyncTask;

public class ManualFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 212;
	private static final String TAG = "ManualFragment";

	private View mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<ManualFragment> mWeakFragment;

	private FrameLayout mContentView;
	private View mCustomView = null;
	private WebView mWebView;

	private LoadGuide loadTask;
	private String loadType;

	// private ArrayList<SopLesson> list;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		};
	};
	// private String loadType = Url.SOP.loadtype1;

	private ArrayList<SopLesson> sectionList;
	private ArrayList<SopLesson> section2List;

	private class LoadGuide extends
			AsyncTask<Object, Void, ModelData<SopLesson>> {
		private Context context;
		private ArrayList<SopLesson> guides;
		private String loadType;

		public LoadGuide(Fragment fragment, ArrayList<SopLesson> guides) {
			this.context = fragment.getActivity();
			this.guides = guides;
		}

		@Override
		protected ModelData<SopLesson> doInBackground(Object... params) {
			loadType = (String) (params[0]);
			ModelData<SopLesson> guides = GuideService.sopLesson(context,
					loadType, (String) (params[1]));
			return guides;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<SopLesson> result) {
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

				// if (loadType.equals(Url.SOP.loadtype1)) {
				sectionList.clear();
				sectionList.addAll(result.getModels());
				initSectionPop(loadType, sectionList);
				System.out.println("initsectionpop");
				// }
				// if (loadType.equals(Url.SOP.loadtype2)) {
				// section2List.clear();
				// section2List.addAll(result.getModels());
				// //initSectionPop(section2List);
				// //popSection.showAsDropDown(titleView);
				// System.out.println("initsection2pop");
				// }
				// mTitles = new ArrayList<String>();
				// for(SopLesson guide : mGuides){
				// mTitles.add(guide.getTitle());
				// }
				// Toast.makeText(context, result.getMessage(),
				// Toast.LENGTH_SHORT).show();
				// System.out.println("soplesson length:"+
				// result.getModels().size());

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
			mWeakFragment = new WeakReference<ManualFragment>(this);
		}
		// list = new ArrayList<SopLesson>();
		sectionList = new ArrayList<SopLesson>();
		// section2List = new ArrayList<SopLesson>();

		// mLoadTask = new LoadMessage(getActivity(), mMessages, mAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// mAdapter.notifyDataSetChanged();
	}

	private View titleView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_manual, container,
				false);
		mLeftView = view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = view.findViewById(R.id.title_right);

		titleView = view.findViewById(R.id.ll_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.menu_manual);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		mRightView.setBackgroundResource(R.drawable.icon_section_titlebar);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		// mContentView = (FrameLayout) view.findViewById(R.id.main_content);
		mWebView = (WebView) view.findViewById(R.id.webview_player);
		initWebView();

		mWebView.loadUrl("http://e.yulebaby.com/s/sopLessonView?id=21");

		return view;
	}

	private void initWebView() {
		mWebView.clearHistory();
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		settings.setPluginsEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// mWebView.setWebChromeClient(new MyWebChromeClient());
		// mWebView.setWebViewClient(new MyWebViewClient());
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

	PopupWindow popSection;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			if (null!=popSection &&popSection.isShowing()) {
				popSection.dismiss();
				return;
			}
			// else {
			// popSection.showAsDropDown(titleView);
			// }
			loadType = Url.SOP.loadtype1;
			loadTask = new LoadGuide(this, sectionList);
			loadTask.execute(loadType, "");
//			if (sectionList.size() < 1) {
//				return;
//			}

			break;

		}
	}

	private SectionAdapter mAdapter;

	public void initSectionPop(String loadType, ArrayList<SopLesson> dataList) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.teaching_section, null);
		// popSection = new PopupWindow(v, getActivity().getWindowManager()
		// .getDefaultDisplay().getWidth(), getActivity()
		// .getWindowManager().getDefaultDisplay().getHeight() * 3 / 5);
		if (null != popSection) {
			popSection.dismiss();
		}
		popSection = new PopupWindow(v,  LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popSection.setBackgroundDrawable(new BitmapDrawable());
		popSection.setFocusable(true);
		popSection.setTouchable(true);
		popSection.setOutsideTouchable(true);
		// LinearLayout main = (LinearLayout) v.findViewById(R.id.main);
		ListView lvSection = (ListView) v
				.findViewById(R.id.lv_teaching_section);
		mAdapter = new SectionAdapter(dataList);

		lvSection.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		lvSection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(getActivity(),
				// sectionList.get(position) + " is onclick!",
				// Toast.LENGTH_SHORT).show();
				// showGuide(type, position);
				// popSection.dismiss();
				// sectionList.get(position).getId();

				// if(section2List.size()<1){
				// loadType = Url.SOP.loadtype2;
				loadTask = new LoadGuide(ManualFragment.this, sectionList);
				if (ManualFragment.this.loadType == Url.SOP.loadtype2) {
					// ManualFragment.this.loadType =Url.SOP.loadtype2;
					System.out.println("loadtype2");
					loadTask.execute(Url.SOP.loadtype2,
							sectionList.get(position).getId());
				} else if (ManualFragment.this.loadType == Url.SOP.loadtype3) {
					ManualFragment.this.loadType = Url.SOP.loadtype3;
					System.out.println("loadtype3");
					loadTask.execute(Url.SOP.loadtype3,
							sectionList.get(position).getId());
				} else {
					popSection.dismiss();
					mWebView.loadUrl(sectionList.get(position).getUrl());
					System.out.println("load ur:"+sectionList.get(position).getUrl());
//					Toast.makeText(
//							getActivity(),
//							sectionList.get(position).getTitle() + ":"
//									+ sectionList.get(position).getUrl(),
//							Toast.LENGTH_LONG).show();
				}
				// tvTitle.setText("正在加载。。。");
				// }else{

				// }

			}
		});

		// if (popSection.isShowing()) {
		// popSection.dismiss();
		// } else {
		if (this.loadType == Url.SOP.loadtype1) {
			this.loadType = Url.SOP.loadtype2;
		} else if (this.loadType == Url.SOP.loadtype2) {
			this.loadType = Url.SOP.loadtype3;
		} else {
			this.loadType = "";
		}
		popSection.showAsDropDown(titleView);

		// }

	}

	PopupWindow SepopSection;

	public void initSection2Pop(ArrayList<SopLesson> dataList) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.teaching_section, null);
		// popSection = new PopupWindow(v, getActivity().getWindowManager()
		// .getDefaultDisplay().getWidth(), getActivity()
		// .getWindowManager().getDefaultDisplay().getHeight() * 3 / 5);
		SepopSection = new PopupWindow(v, getActivity().getWindowManager()
				.getDefaultDisplay().getWidth(), LayoutParams.WRAP_CONTENT);
		SepopSection.setBackgroundDrawable(new BitmapDrawable());
		SepopSection.setFocusable(true);
		SepopSection.setTouchable(true);
		SepopSection.setOutsideTouchable(true);
		// LinearLayout main = (LinearLayout) v.findViewById(R.id.main);
		ListView lvSection = (ListView) v
				.findViewById(R.id.lv_teaching_section);
		SectionAdapter mAdapter = new SectionAdapter(dataList);

		lvSection.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		lvSection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// sectionList.get(position).getId();

				// loadType = Url.SOP.loadtype3;
				loadTask = new LoadGuide(ManualFragment.this, sectionList);

				loadTask.execute(Url.SOP.loadtype3, section2List.get(position)
						.getId());
				tvTitle.setText("正在加载。。。");

			}
		});
		SepopSection.showAsDropDown(titleView);

	}

	class SectionAdapter extends BaseAdapter {
		private int size;
		private ArrayList<SopLesson> mList;

		public SectionAdapter(ArrayList<SopLesson> list) {
			size = sectionList.size();
			this.mList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// System.out.println("sectionList lenght:" + mList.size());
			ViewHolder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.sectionlist_item, null);
				holder = new ViewHolder();
				holder.titleText = (TextView) convertView
						.findViewById(R.id.tv_sectionlist);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.titleText.setText(this.mList.get(position).getTitle());
			return convertView;
		}

		class ViewHolder {
			TextView titleText;
			// TextView timeText;
		}

	}

}
