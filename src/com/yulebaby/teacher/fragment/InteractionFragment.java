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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.GetChars;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.yulebaby.teacher.AppraisalActivity;
import com.yulebaby.teacher.DoAppraisalActivity;
import com.yulebaby.teacher.ExamInfoActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.SalaryDetailActivity;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.adapter.SalaryGridAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.fragment.OrderFragment.OrderCutsomAdapter;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.model.SalaryModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.InteractionService;
import com.yulebaby.teacher.server.OrderCustomService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;

public class InteractionFragment extends Fragment implements
		View.OnClickListener {

	public static final int INDEX = 211;
	private static final String TAG = "InteractionFragment";

	private Button mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<InteractionFragment> mWeakFragment;

	private ArrayList<InteractionModel> list;
	ListView lv;

	private Button btSearch;
	private EditText etSearchKey;
	private ArrayList<InteractionModel> searchList;

	private InteraAdapter adapter;
	private LoadInteraction mLoadTask;
	private ImageFetcher mImageFetcher;
	private static final String IMAGE_CACHE_DIR = "avatars";
	private static final int SEARCH_USER = 890;
	private String type = "0";

	View loadingView;

	boolean isfillingdata;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEARCH_USER:
				getSearchUser(etSearchKey.getText().toString());
				adapter.setKeyString(etSearchKey.getText().toString());
				adapter.list = searchList;
				adapter.notifyDataSetChanged();
				lv.setVisibility(View.VISIBLE);
				lv.invalidate();
				break;

			default:
				break;
			}
		};
	};

	private class LoadInteraction extends
			AsyncTask<Object, Void, ModelData<InteractionModel>> {
		private Context context;
		private ArrayList<InteractionModel> mList;
		// private BaseAdapter adapter;
		private volatile boolean isFinish = false;

		public LoadInteraction(Context context,
				ArrayList<InteractionModel> swims, BaseAdapter adapter) {
			this.context = context;
			this.mList = swims;
			// this.adapter = adapter;
		}

		@Override
		protected ModelData<InteractionModel> doInBackground(Object... params) {
			//
			isfillingdata = true;
			ModelData<InteractionModel> swims = InteractionService.consume(
					getActivity(), String.valueOf(params[0]),
					String.valueOf(params[1]));
			return swims;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingView.setVisibility(View.VISIBLE);
			// mEmptyView.setVisibility(View.GONE);
		}

		public synchronized boolean isFinish() {
			return isFinish;
		}

		@Override
		protected void onPostExecute(ModelData<InteractionModel> result) {
			loadingView.setVisibility(View.GONE);
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
				isfillingdata = false;
				mList.addAll(result.getModels());
				System.out.println("return result listlenght:" + mList.size());
				// if(isEnd == false && result.getModels().size() <
				// App.LIST_PAGE_SUM){
				// if(mListView.getFooterViewsCount() > 0)
				// mListView.removeFootView();
				// isEnd = true;
				// }else{
				// if(mListView.getFooterViewsCount() > 0)
				// mListView.removeFootView();
				// mListView.addFootView();
				// isEnd = false;
				// }
				// adapter.notifyDataSetChanged();
				// adapter = new InteraAdapter(mList);
				adapter.list = mList;
				adapter.notifyDataSetChanged();
				lv.invalidate();
				// lv.setAdapter(adapter);
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
			mWeakFragment = new WeakReference<InteractionFragment>(this);
		}
		list = new ArrayList<InteractionModel>();
		searchList = new ArrayList<InteractionModel>();
		adapter = new InteraAdapter(list);
		mLoadTask = new LoadInteraction(getActivity(), list, adapter);
		params = new String[2];
		params[0] = "";
		params[1] = "";
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				IMAGE_CACHE_DIR);

		cacheParams.setMemCacheSizePercent(getActivity(), 0.25f);
		mImageFetcher = ImageFetchFactory.getInstance().initImageFetcher(
				IMAGE_CACHE_DIR, getActivity(),
				getResources().getDimensionPixelOffset(R.dimen.listavatar));
		// mImageFetcher = new ImageFetcher(getActivity(),
		// getResources().getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);

		// initData();
		// mLoadTask = new LoadMessage(getActivity(), mMessages, mAdapter);
	}

	// private void initData() {
	// list.add(new InteractionModel("张三", "2013-04-04", "吓到了法搜", true));
	// list.add(new InteractionModel("王五", "2013-05-04", "吓到了法搜", false));
	// list.add(new InteractionModel("赵六", "2013-05-07", "吓到了法搜", false));
	// list.add(new InteractionModel("刘思", "2013-06-04", "吓到了法搜", true));
	// list.add(new InteractionModel("钱六", "2013-08-04", "吓到了法搜", true));
	// }

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		adapter.notifyDataSetChanged();
		// mAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_interaction,
				container, false);
		mLeftView = (Button) view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = (Button) view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.menu_interaction);
		// mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		//mRightView.setText(R.string.title_bt_appramodel);
		mRightView.setBackgroundResource(R.drawable.icon_section_titlebar);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		loadingView = view.findViewById(R.id.rl_subject_loading);

		etSearchKey = (EditText) view.findViewById(R.id.intera_search_et);
		etSearchKey.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// System.out.println("onTextChanged");
				// if(s.length()!=0){
				// //adapter.list.clear();
				// }
				//
				// adapter.notifyDataSetChanged();
				// lv.invalidate();
				lv.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// System.out.println("beforeTextChanged");

			}

			@Override
			public void afterTextChanged(Editable s) {
				// System.out.println("afterTextChanged");

			}
		});
		btSearch = (Button) view.findViewById(R.id.intera_search_bt);
		btSearch.setOnClickListener(this);

		lv = (ListView) view.findViewById(R.id.lv_intera);
		lv.setAdapter(adapter);
		// lv.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// // Toast.makeText(MainActivity.this, "go Examing ",
		// // Toast.LENGTH_SHORT).show();
		// // Intent intent = new Intent();
		// // intent.setClass(getActivity(), ExamInfoActivity.class);
		// // intent.putExtra("exam_title", list.get(position).title);
		// // startActivity(intent);
		//
		//
		//
		// }
		// });

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = view.getLastVisiblePosition();
					if ((position + 1) == list.size()) {
						if (isfillingdata) {
							return;
						} else {
							// type = "1";
							mLoadTask = new LoadInteraction(getActivity(),
									list, adapter);
							String[] params = new String[2];
							params[0] = list.get(position).getTitle();
							params[1] = "1";
							mLoadTask.execute(params);
							// fillData();
							// startIndex += maxResult;
						}
					}
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		
		
		mLoadTask.execute(params);

		return view;
	}

	String[] params;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
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
		mImageFetcher.closeCache();
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
			Intent intent = new Intent(mWeakActivity.get(),
					AppraisalActivity.class);
			intent.putExtra("quote", false);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			mWeakActivity.get().startActivity(intent);
			break;

		case R.id.intera_search_bt:

			mHandler.sendEmptyMessage(SEARCH_USER);

			break;

		}
	}

	private void getSearchUser(String condition) {
		searchList.clear();
		if (condition == null || condition.equals(""))
			return;
		for (int i = 0; i < list.size(); i++) {
			// System.out.println("condition:" + condition);
			// System.out.println("list lenght:" + list.get(i).getName());
			if (!TextUtils.isEmpty(list.get(i).getName())) {

				if (list.get(i).getName().contains(condition.replace(" ", ""))) {

					System.out.println("add too search list");
					searchList.add(list.get(i));
				}
			}
		}
	}

	class InteraAdapter extends BaseAdapter {
		// 定义Context
		private int size;
		private ArrayList<InteractionModel> list;

		public InteraAdapter(ArrayList<InteractionModel> list) {
			this.list = list;
			size = this.list.size();

		}

		String keyString;

		public void setKeyString(String key) {
			this.keyString = key;
		}

		// 获取图片的个数
		public int getCount() {
			return size;
		}

		// 获取图片在库中的位置
		public Object getItem(int position) {
			return this.list.get(position);
		}

		// 获取图片ID
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			size = this.list.size();
			System.out.println("notifyDataSetChanged :" + size);
		}

		@Override
		public void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			super.notifyDataSetInvalidated();
			size = this.list.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// ImageView imageView;
			if (null == this.list || this.list.size() == 0)
				return null;
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.interaction_item, null);
				holder = new ViewHolder();
				holder.contentView = convertView
						.findViewById(R.id.rl_interacontent);
				holder.tvNullContent = (TextView) convertView
						.findViewById(R.id.tv_null_intera);
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.intera_header);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.intera_item_content);
				holder.tvState = (TextView) convertView
						.findViewById(R.id.intera_item_appraisal);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.intera_item_title);
				holder.serverText = getActivity().getResources().getString(
						R.string.server_time);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.intera_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final InteractionModel model = this.list.get(position);
			holder.tvTitle.setVisibility(View.VISIBLE);
			// if (position > 0) {
			// if(list.get(position-1).getTitle().equals(model.getTitle())){
			// holder.tvTitle.setVisibility(View.GONE);
			// }
			//
			// }

			holder.tvTitle.setText(model.getTitle());
			if (TextUtils.isEmpty(model.getName())) {
				holder.tvNullContent.setVisibility(View.VISIBLE);
				holder.contentView.setVisibility(View.GONE);
			} else {
				holder.tvNullContent.setVisibility(View.GONE);
				holder.contentView.setVisibility(View.VISIBLE);
				holder.contentView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//if (!model.isAppraisal()) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									DoAppraisalActivity.class);
							intent.putExtra("apprai_name", model.getName());
							intent.putExtra("appid", model.getId());
							intent.putExtra("isAppraisal", model.isAppraisal());
							startActivity(intent);
						//}

					}
				});
				mImageFetcher.loadImage(model.getImg(), holder.ivHeader);

				if (!TextUtils.isEmpty(keyString)
						&& model.getName().contains(keyString)) {

					// System.out.println(" is contain key");
					int index = model.getName().indexOf(keyString);
					int len = keyString.length();
					Spanned temp = Html.fromHtml(model.getName().substring(0,
							index)
							+ "<u><font color=#f9bbbb>"
							+ model.getName().substring(index, index + len)
							+ "</font></u>"
							+ model.getName().substring(index + len,
									model.getName().length()));

					holder.tvName.setText(temp);

				} else {
					holder.tvName.setText(model.getName());
				}
				String content = String.format(holder.serverText,
						model.getConsumeTime());
				holder.tvContent.setText(content);
				// holder.tvTitle.setText(model.get);
				holder.tvState.setText("");
				if (model.isAppraisal()) {

					//holder.tvState.setText(R.string.interation_isappra);
					//holder.tvState.setBackgroundColor(Color.GREEN);
					holder.tvState.setBackgroundResource(R.drawable.isappra);
				} else {
					//holder.tvState.setBackgroundColor(Color.MAGENTA);
					holder.tvState.setBackgroundResource(R.drawable.isnotappra);
					//holder.tvState.setText(R.string.interation_isnotappra);
				}
			}

			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
			TextView tvName;
			TextView tvContent;
			TextView tvState;
			ImageView ivHeader;
			TextView tvNullContent;
			View contentView;
			String serverText;
			// TextView timeText;
		}
	}

}
